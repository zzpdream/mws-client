package com.ydd.conference.netty;

import android.app.Activity;
import android.util.Log;

import com.ydd.conference.config.Command;
import com.ydd.conference.config.Constant;
import com.ydd.conference.entity.ChairmanRequest;
import com.ydd.conference.entity.HeartBeatRequest;
import com.ydd.conference.entity.HeartBeatResponse;
import com.ydd.conference.entity.ImageRequest;
import com.ydd.conference.entity.Message;
import com.ydd.conference.entity.SeatRequest;
import com.ydd.conference.entity.SetSeatInfoRequest;
import com.ydd.conference.entity.ShowLogoRequest;
import com.ydd.conference.entity.ShowMemRequest;
import com.ydd.conference.entity.ShowRegisterRequest;
import com.ydd.conference.entity.ShowSubjectRequest;
import com.ydd.conference.entity.StartPlayVideoRequest;
import com.ydd.conference.entity.StartRegisterRequest;
import com.ydd.conference.entity.StartVoteRequest;
import com.ydd.conference.entity.StopVoteRequest;
import com.ydd.conference.entity.UpdateSeatRequest;
import com.ydd.conference.event.InitSeatEvent;
import com.ydd.conference.event.RegisterEvent;
import com.ydd.conference.event.ShowSecondEvent;
import com.ydd.conference.event.StartScannerEvent;
import com.ydd.conference.event.StopScannerEvent;
import com.ydd.conference.event.StopVideoEvent;
import com.ydd.conference.event.UnRegisterEvent;
import com.ydd.conference.event.UpdateNameEvent;
import com.ydd.conference.event.VoteSuccessEvent;
import com.ydd.conference.ui.ActivityActionUtil;
import com.ydd.conference.ui.FullscreenActivity;
import com.ydd.conference.ui.ImageActivity;
import com.ydd.conference.ui.LogoActivity;
import com.ydd.conference.ui.MainActivity;
import com.ydd.conference.ui.NameActivity;
import com.ydd.conference.ui.ShowRegisterActivity;
import com.ydd.conference.ui.SingleVoteResultActivity;
import com.ydd.conference.ui.StartRegisterActivity;
import com.ydd.conference.ui.SubjectActivity;
import com.ydd.conference.ui.TextActivity;
import com.ydd.conference.ui.VideoActivity;
import com.ydd.conference.ui.VoteNewActivity;
import com.ydd.conference.ui.VoteResultNewActivity;
import com.ydd.conference.ui.WithoutRegisterActivity;
import com.ydd.conference.util.DownloadPictureHelper;
import com.ydd.conference.util.JsonMapper;
import com.ydd.conference.util.RebootUtil;
import com.ydd.conference.util.SharedPreferencesUtil;
import com.ydd.conference.util.StringUtils;
import com.ydd.conference.util.SystemUtil;
import com.ydd.conference.util.UpdateAppHelper;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * Created by hehelt on 16/2/26.
 * <p/>
 * 业务处理handler
 */
@ChannelHandler.Sharable
public class ConferenceClientHandler extends ChannelHandlerAdapter {

    private final static String TAG = "ConferenceClientHandler";

    private boolean hasRead = false;

    public static ChannelHandlerContext mCtx;
    private Activity mContext;

    public ConferenceClientHandler(Activity context) {
        mContext = context;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        mCtx = ctx;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            Log.d(TAG, "read data-------" + msg.toString());
            String message = (String) msg;
            if (!"error".equals(msg.toString())) {
                Message messageEntity = JsonMapper.fromJson(message, Message.class);
                String cmd = messageEntity.getCmd();
                Command command = Command.getCommand(cmd);
                if (command == null) {
                    Message response = new Message();
                    response.setCmd(command.value);
                    response.setType(Message.TYPE_MESSAGE_RESPONSE);
                    response.setStatus(Message.STATUS_COMMAND_ERROR);
                    ctx.writeAndFlush(response);
                    return;
                }
                if (command.status != Command.HEARTBEAT.status) {
                    SharedPreferencesUtil.setStatus(command.status);
                }

                Message responseMessage = new Message();
                responseMessage.setCmd(command.value);
                responseMessage.setType(Message.TYPE_MESSAGE_RESPONSE);
                responseMessage.setStatus(Message.STATUS_SUCCESS);

                //add by lt 132座位号固定显示视频
//                if (SharedPreferencesUtil.getSeatId().equals("132") && command.status != Command.SHOW_LOGO.status) {
//                    ctx.writeAndFlush(responseMessage);
//                    return;
//                }
                switch (command) {
                    case CLEAR_SEAT:
                        SeatRequest clearSeat = JsonMapper.fromJson(message, SeatRequest.class);
                        SeatRequest.SeatParams clearSeatParams = clearSeat.getParams();
                        if (clearSeatParams == null) {
                            return;
                        }
                        SharedPreferencesUtil.clearSeat();
                        Constant.init();
//                        EventBus.getDefault().post(new ClearSeatEvent());
                        if (SharedPreferencesUtil.getTerminalType().equals(Constant.Terminal_FIRST)) {
                            //有徽标指令的话,则显示徽标
                            if (null != Constant.LOGO_TEXT) {
                                LogoActivity.actionStart(mContext, Constant.LOGO_TEXT);
                            } else {
                                MainActivity.actionStart(mContext);
                            }
                        }
                        EventBus.getDefault().post(new ShowSecondEvent());
                        //如果清楚座位信息,那么关闭轮询 add by lt
                        EventBus.getDefault().post(new StopScannerEvent());
                        ctx.writeAndFlush(responseMessage);
                        break;
                    case SET_SEAT:
                        SetSeatInfoRequest setSeatInfoRequest = JsonMapper.fromJson(message, SetSeatInfoRequest.class);
                        final SetSeatInfoRequest.SetSeatInfoParams setSeatInfoParams = setSeatInfoRequest.getParams();
                        if (null == setSeatInfoParams) {
                            responseMessage.setStatus(Message.STATUS_UNKNOWN);
                            ctx.writeAndFlush(responseMessage);
                            break;
                        }
                        if (SharedPreferencesUtil.seatIdIsRight(setSeatInfoParams.getSeatId())) {
                            SharedPreferencesUtil.saveSeat(setSeatInfoParams);
                            EventBus.getDefault().post(new UpdateNameEvent());
                            EventBus.getDefault().post(new ShowSecondEvent());

                            //modify by ranfi 排座主屏和副屏显示同样的内容
                            if(SharedPreferencesUtil.hasAnonAccessPermission()){
                                NameActivity.actionStart(mContext);
                                return;
                            }

                            if (SharedPreferencesUtil.getTerminalType().equals(Constant.Terminal_FIRST)) {
                                //如果是正式终端有表决权,并且是报道轮询状态,那么开启轮询 add by lt
                                if (setSeatInfoParams.getRegistered() == 1) {
                                    if (setSeatInfoParams.getVotingRights() == 1) {
                                        StartRegisterActivity.actionStart(mContext, setSeatInfoParams.getTitle(),false);
                                    } else {
                                        WithoutRegisterActivity.actionStart(mContext, setSeatInfoParams.getTitle());
                                        EventBus.getDefault().post(new StopScannerEvent());
                                    }
                                    SharedPreferencesUtil.setStatus(Command.START_REGISTER.status);
                                }
                            }
                        } else {
                            responseMessage.setStatus(Message.STATUS_SEAT_UN_CONSISTENT);
                        }
                        ctx.writeAndFlush(responseMessage);
                        break;

                    case UPDATE_SEAT:
                        UpdateSeatRequest updateSeatRequest = JsonMapper.fromJson(message, UpdateSeatRequest.class);
                        UpdateSeatRequest.UpdateSeatParams updateSeatParams = updateSeatRequest.getParams();
                        if (updateSeatParams != null) {
                            SharedPreferencesUtil.setSeatIdAndType(updateSeatParams.getSeatId(), updateSeatParams.getTerminalType());
                            EventBus.getDefault().post(new InitSeatEvent());
                        } else {
                            responseMessage.setStatus(Message.STATUS_UNKNOWN);
                        }
                        ctx.writeAndFlush(responseMessage);
                        break;

                    /**
                     * 设置主席座位
                     * TODO by ranfi
                     */
                    case SET_CHAIRMAN:
                        //如果是正式终端则不处理
                        if (SharedPreferencesUtil.getTerminalType().equals(Constant.Terminal_FIRST)) {
                            return;
                        }
                        ChairmanRequest chairmanRequest = JsonMapper.fromJson(message, ChairmanRequest.class);
                        ChairmanRequest.ChairmanParams chairSeat = chairmanRequest.getParams();
                        //如果与当前座位号一致,则代表是主席座位
                        String chairSeatId = chairSeat.getSeatId();//主席座位号
                        SharedPreferencesUtil.saveChairman(chairSeat);
                        if (StringUtils.isNotEmpty(chairSeatId) && chairSeatId.equals(SharedPreferencesUtil.getSeatId())) {
                            String videoUrl = Constant.VIDEO_URL;
                            if (StringUtils.isNotEmpty(chairSeat.getVideoUrl())) {
                                videoUrl = chairSeat.getVideoUrl();
                            }
                            FullscreenActivity.actionStart(mContext, videoUrl);
                        } else {
                            ActivityActionUtil.jumpNameActivity(mContext);
                        }
                        break;

                    case INITIALIZE:
                        Constant.init();
                        if (SharedPreferencesUtil.getTerminalType().equals(Constant.Terminal_FIRST)) {
                            //有徽标指令的话,则显示徽标
                            if (null != Constant.LOGO_TEXT) {
                                LogoActivity.actionStart(mContext, Constant.LOGO_TEXT);
                            } else {
                                MainActivity.actionStart(mContext);
                            }
                            //add by lt 初始化 2016-04-04
                            EventBus.getDefault().post(new StopScannerEvent());
                        }
                        ctx.writeAndFlush(responseMessage);
                        break;

                    case SHUTDOWN:
                        ctx.writeAndFlush(responseMessage);
                        RebootUtil.shutdown();
                        break;

                    case REBOOT:
                        ctx.writeAndFlush(responseMessage);
                        RebootUtil.reboot();
                        break;

                    case SHOW_LOGO:
                        ShowLogoRequest request = JsonMapper.fromJson(message, ShowLogoRequest.class);
                        ArrayList<String> titles = (ArrayList) request.getParams().getTitles();
                        Constant.LOGO_TEXT = titles;

                        /**
                         * 终端匿名访问的跳转  modify by ranfi 2016-07-17 20:30
                         */
                        if(SharedPreferencesUtil.hasAnonAccessPermission()){
                        ActivityActionUtil.showLogoActivityByAnonAuth(mContext,titles);
                        return;
                    }

                        //add by lt 132座位号固定显示视频
                        if (SharedPreferencesUtil.getSeatId().equals("10000")) {
                            EventBus.getDefault().post(new ShowSecondEvent());
                            ctx.writeAndFlush(responseMessage);
                            return;
                        }

                        if (SharedPreferencesUtil.getTerminalType().equals(Constant.Terminal_FIRST)) {
                            LogoActivity.actionStart(mContext, titles);
                        } else {
                            ActivityActionUtil.jumpNameActivity(mContext);
                        }
                        ctx.writeAndFlush(responseMessage);
                        break;

                    //开始报名
                    case START_REGISTER:
                        if(!SharedPreferencesUtil.hasAdvancedMeetingPermission()){
                            return;
                        }

                        StartRegisterRequest startRegisterRequest = JsonMapper.fromJson(message, StartRegisterRequest.class);
                        StartRegisterRequest.StartRegisterParams startRegisterParams = startRegisterRequest.getParams();

                        if (null == startRegisterParams) {
                            responseMessage.setStatus(Message.STATUS_UNKNOWN);
                            ctx.writeAndFlush(responseMessage);
                            return;
                        }

                        //如果主机发送的座位号和本地不一致
                        if (!SharedPreferencesUtil.seatIdIsRight(startRegisterParams.getSeatId())) {
                            responseMessage.setStatus(Message.STATUS_SEAT_UN_CONSISTENT);
                            ctx.writeAndFlush(responseMessage);
                            return;
                        }
                        Constant.registerTitle = startRegisterParams.getTitle();
                        //modify by ranfi 2016.4.13 2:26
                        if (SharedPreferencesUtil.getVotingRights() == Constant.NO_VOTE_RIGHT) {
                            WithoutRegisterActivity.actionStart(mContext, startRegisterParams.getTitle());
                        } else {
                            StartRegisterActivity.actionStart(mContext, startRegisterParams.getTitle(),false);
                        }
                        ctx.writeAndFlush(responseMessage);
                        break;
                    case SHOW_MEMBER:
                        if(!SharedPreferencesUtil.hasAdvancedMeetingPermission()){
                            return;
                        }
                        ShowMemRequest showMemRequest = JsonMapper.fromJson(message, ShowMemRequest.class);
                        ShowMemRequest.StartRegisterParams showMemParams = showMemRequest.getParams();
                        if (null == showMemRequest) {
                            responseMessage.setStatus(Message.STATUS_UNKNOWN);
                            ctx.writeAndFlush(responseMessage);
                            return;
                        }

                        //如果主机发送的座位号和本地不一致
                        if (!SharedPreferencesUtil.seatIdIsRight(showMemParams.getSeatId())) {
                            responseMessage.setStatus(Message.STATUS_SEAT_UN_CONSISTENT);
                            ctx.writeAndFlush(responseMessage);
                            return;
                        }
                        Constant.registerTitle = showMemParams.getTitle();
                        //modify by ranfi 2016.4.13 2:26
                        if (SharedPreferencesUtil.getVotingRights() == Constant.NO_VOTE_RIGHT) {
                            WithoutRegisterActivity.actionStart(mContext, showMemParams.getTitle());
                        } else {
                            StartRegisterActivity.actionStart(mContext, showMemParams.getTitle(),true);
                        }
                        ctx.writeAndFlush(responseMessage);
                        break;
                    case SHOW_REGISTER:
                        if(!SharedPreferencesUtil.hasAdvancedMeetingPermission()){
                            return;
                        }
                        Constant.isShowDuty = true;  //设置显示职位名称标记
                        ShowRegisterRequest showRegisterRequest = JsonMapper.fromJson(message, ShowRegisterRequest.class);
                        ShowRegisterRequest.ShowRegisterParams showRegisterParams = showRegisterRequest.getParams();
                        if (showRegisterParams != null) {
                            if (SharedPreferencesUtil.seatIdIsRight(showRegisterParams.getSeatId())) {
                                ShowRegisterActivity.actionStart(mContext, showRegisterParams.getRegistered(), showRegisterParams.getExpected(), showRegisterParams.getAbsent(), showRegisterParams.getLeave());
                            } else {
                                responseMessage.setStatus(Message.STATUS_SEAT_UN_CONSISTENT);
                            }
                        } else {
                            responseMessage.setStatus(Message.STATUS_UNKNOWN);
                        }
                        ctx.writeAndFlush(responseMessage);
                        break;

                    case REGISTER:
                        if(!SharedPreferencesUtil.hasAdvancedMeetingPermission()){
                            return;
                        }

                        SeatRequest registerRequest = JsonMapper.fromJson(message, SeatRequest.class);
                        SeatRequest.SeatParams registerParams = registerRequest.getParams();
                        if (registerParams != null) {
                            if (SharedPreferencesUtil.seatIdIsRight(registerParams.getSeatId())) {
                                EventBus.getDefault().post(new RegisterEvent());
                            } else {
                                responseMessage.setStatus(Message.STATUS_SEAT_UN_CONSISTENT);
                            }
                        } else {
                            responseMessage.setStatus(Message.STATUS_UNKNOWN);
                        }
                        ctx.writeAndFlush(responseMessage);
                        break;

                    case UNREGISTER:

                        if(!SharedPreferencesUtil.hasAdvancedMeetingPermission()){
                            return;
                        }


                        SeatRequest unRegisterRequest = JsonMapper.fromJson(message, SeatRequest.class);
                        SeatRequest.SeatParams unRegisterParams = unRegisterRequest.getParams();
                        if (unRegisterParams != null) {
                            if (SharedPreferencesUtil.seatIdIsRight(unRegisterParams.getSeatId())) {
                                EventBus.getDefault().post(new UnRegisterEvent());
                            } else {
                                responseMessage.setStatus(Message.STATUS_SEAT_UN_CONSISTENT);
                            }
                        } else {
                            responseMessage.setStatus(Message.STATUS_UNKNOWN);
                        }
                        ctx.writeAndFlush(responseMessage);
                        break;

                    case STOP_REGISTER:
                        if(!SharedPreferencesUtil.hasAdvancedMeetingPermission()){
                            return;
                        }
                        EventBus.getDefault().post(new StopScannerEvent());
                        ctx.writeAndFlush(responseMessage);
                        break;

                    case SHOW_SUBJECT:
                        if(!SharedPreferencesUtil.hasAdvancedMeetingPermission()){
                            return;
                        }
                        ShowSubjectRequest showSubjectRequest = JsonMapper.fromJson(message, ShowSubjectRequest.class);
                        ShowSubjectRequest.ShowSubjectParams showSubjectParams = showSubjectRequest.getParams();
                        if (showSubjectParams != null) {
                            if (SharedPreferencesUtil.seatIdIsRight(showSubjectParams.getSeatId())) {
                                SubjectActivity.actionStart(mContext, showSubjectParams);
                            } else {
                                responseMessage.setStatus(Message.STATUS_SEAT_UN_CONSISTENT);
                            }
                        } else {
                            responseMessage.setStatus(Message.STATUS_UNKNOWN);
                        }
                        ctx.writeAndFlush(responseMessage);
                        break;

                    case START_VOTE:
                        if(!SharedPreferencesUtil.hasAdvancedMeetingPermission()){
                            return;
                        }
                        StartVoteRequest startVoteRequest = JsonMapper.fromJson(message, StartVoteRequest.class);
                        StartVoteRequest.StartVoteParams startVoteParams = startVoteRequest.getParams();
                        if (startVoteParams != null) {
                            if (SharedPreferencesUtil.seatIdIsRight(startVoteParams.getSeatId())) {
                                if (SharedPreferencesUtil.getVotingRights() == Constant.NO_VOTE_RIGHT || SharedPreferencesUtil.getRegisterStatus() == Constant.STATUS_UN_REGISTER) {
//                                    Constant.textTitle = startVoteParams.getSubject();
//                                    TextActivity.actionStart(mContext);
                                    //add by lt 当会议议程处理
                                    SubjectActivity.actionStart(mContext, startVoteParams.getSubject(), startVoteParams.getHorizontal(), startVoteParams.getVertical());

                                } else {
                                    if (startVoteParams.getType().equals(Message.TYPE_SINGLE_VOTE + "")) {
                                        Constant.isSingleVote = true;
                                        Constant.textTitle = "现在开始表决\n请按表决器";
                                        TextActivity.actionStart(mContext);
                                    } else {
                                        Constant.isSingleVote = false;
                                        Constant.list = startVoteParams.getItems();
//                                        VoteActivity.actionStart(mContext);
                                        VoteNewActivity.actionStart(mContext);
                                    }
                                }
                                //表决开始时,停止轮询 add
                                EventBus.getDefault().post(new StopScannerEvent());
                            } else {
                                responseMessage.setStatus(Message.STATUS_SEAT_UN_CONSISTENT);
                            }
                        } else {
                            responseMessage.setStatus(Message.STATUS_UNKNOWN);
                        }
                        ctx.writeAndFlush(responseMessage);
                        break;

                    case STOP_VOTE:
                        if(!SharedPreferencesUtil.hasAdvancedMeetingPermission()){
                            return;
                        }
                        StopVoteRequest stopVoteRequest = JsonMapper.fromJson(message, StopVoteRequest.class);
                        StopVoteRequest.StopVoteParams stopVoteParams = stopVoteRequest.getParams();
                        if (stopVoteParams != null) {
                            if (SharedPreferencesUtil.seatIdIsRight(stopVoteParams.getSeatId())) {
//                                if (SharedPreferencesUtil.getVotingRights() == Constant.NO_VOTE_RIGHT || SharedPreferencesUtil.getRegisterStatus() == Constant.STATUS_UN_REGISTER) {
//
//                                } else {
                                if (stopVoteParams.getVotes() != null && stopVoteParams.getVotes().size() > 0) {
                                    if (stopVoteParams.getType() == Message.TYPE_SINGLE_VOTE) {
                                        StopVoteRequest.VoteResult vote = stopVoteParams.getVotes().get(0);
                                        SingleVoteResultActivity.actionStart(mContext, vote.getYes() + "", vote.getNo() + "", vote.getAbstain() + "", vote.getMiss() + "");
                                    } else {
                                        VoteResultNewActivity.actionStart(mContext, (ArrayList<StopVoteRequest.VoteResult>) stopVoteParams.getVotes());
                                    }
                                } else {
                                    responseMessage.setStatus(Message.STATUS_UNKNOWN);
                                }
//                                }
                                //结束开始时,重新开始轮询 add
                                EventBus.getDefault().post(new StartScannerEvent());
                            } else {
                                responseMessage.setStatus(Message.STATUS_SEAT_UN_CONSISTENT);
                            }
                        } else {
                            responseMessage.setStatus(Message.STATUS_UNKNOWN);
                        }
                        ctx.writeAndFlush(responseMessage);
                        break;

                    case CANCEL_SPEAKING:
                    case START_SPEAK:
                        if(!SharedPreferencesUtil.hasAdvancedMeetingPermission()){
                            return;
                        }
                        SeatRequest startSpeakRequest = JsonMapper.fromJson(message, SeatRequest.class);
                        SeatRequest.SeatParams seatParams = startSpeakRequest.getParams();
                        if (seatParams != null) {
                            if (SharedPreferencesUtil.seatIdIsRight(seatParams.getSeatId())) {
                                Constant.textTitle = "发表意见\n请按“发言键”申请";
                                TextActivity.actionStart(mContext);
                            } else {
                                responseMessage.setStatus(Message.STATUS_SEAT_UN_CONSISTENT);
                            }
                        } else {
                            responseMessage.setStatus(Message.STATUS_UNKNOWN);
                        }
                        ctx.writeAndFlush(responseMessage);

                        break;

                    case REQUEST_SPEAK:
                        if(!SharedPreferencesUtil.hasAdvancedMeetingPermission()){
                            return;
                        }
                        SeatRequest requestSpeakRequest = JsonMapper.fromJson(message, SeatRequest.class);
                        SeatRequest.SeatParams requestSpeakParams = requestSpeakRequest.getParams();
                        if (requestSpeakParams != null) {
                            if (SharedPreferencesUtil.seatIdIsRight(requestSpeakParams.getSeatId())) {
                                Constant.textTitle = "您已申请发言\n请稍候";
                                TextActivity.actionStart(mContext);
                            } else {
                                responseMessage.setStatus(Message.STATUS_SEAT_UN_CONSISTENT);
                            }
                        } else {
                            responseMessage.setStatus(Message.STATUS_UNKNOWN);
                        }
                        ctx.writeAndFlush(responseMessage);

                        break;

                    case SET_SPEAKING:
                        if(!SharedPreferencesUtil.hasAdvancedMeetingPermission()){
                            return;
                        }
                        SeatRequest setSpeakRequest = JsonMapper.fromJson(message, SeatRequest.class);
                        SeatRequest.SeatParams setSpeakParams = setSpeakRequest.getParams();
                        if (setSpeakParams != null) {
                            if (SharedPreferencesUtil.seatIdIsRight(setSpeakParams.getSeatId())) {
                                Constant.textTitle = "现在开始发言";
                                TextActivity.actionStart(mContext);
                            } else {
                                responseMessage.setStatus(Message.STATUS_SEAT_UN_CONSISTENT);
                            }
                        } else {
                            responseMessage.setStatus(Message.STATUS_UNKNOWN);
                        }
                        ctx.writeAndFlush(responseMessage);
                        break;


                    case HEARTBEAT:

                        HeartBeatResponse heartBeatResponse = JsonMapper.fromJson(message, HeartBeatResponse.class);
                        HeartBeatResponse.HeartBeatValue heartBeatValue = heartBeatResponse.getValues();
//                    if (heartBeatValue.getSeatId() != null && !SharedPreferencesUtil.getSeatId().equals(heartBeatValue.getSeatId()) && !heartBeatValue.getSeatId().equals("-1")) {
//                        SharedPreferencesUtil.setSeatId(heartBeatValue.getSeatId());
//                        EventBus.getDefault().post(new InitSeatEvent());
//                    }
//                    if (heartBeatValue.getTerminalType() != null && !SharedPreferencesUtil.getTerminalType().equals(heartBeatValue.getTerminalType())) {
//                        SharedPreferencesUtil.setTerminalType(heartBeatValue.getTerminalType());
//                        EventBus.getDefault().post(new InitSeatEvent());
//                    }
                        if (heartBeatValue.getVersionCode() > SystemUtil.getVersion()) {
                            UpdateAppHelper.update(heartBeatValue.getDownloadUrl());
                        }
                        String url = heartBeatValue.getPicZipUrl();
                        if (url != null && !"".equals(url)) {
                            DownloadPictureHelper.update(url);
                        }
                        break;

                    case VOTE:
                        if(!SharedPreferencesUtil.hasAdvancedMeetingPermission()){
                            return;
                        }
                        EventBus.getDefault().post(new VoteSuccessEvent());
                        break;

                    //播放视频   add by lt  v2.0
                    case START_PLAY_VIDEO:
                        if(!SharedPreferencesUtil.hasAdvancedMeetingPermission()){
                            return;
                        }
                        StartPlayVideoRequest startPlayVideoRequest = JsonMapper.fromJson(message,StartPlayVideoRequest.class);
                        StartPlayVideoRequest.Params startPlayVideoParams = startPlayVideoRequest.getParams();
                        if(startPlayVideoParams!=null){
                            VideoActivity.start(mContext,startPlayVideoParams.getUrl());
                        }else{
                            responseMessage.setStatus(Message.STATUS_SEAT_UN_CONSISTENT);
                        }
                        ctx.writeAndFlush(responseMessage);
                        break;

                    //停止播放视频   add by lt  v2.0
                    case STOP_PLAY_VIDEO:
                        EventBus.getDefault().post(new StopVideoEvent());
                        ctx.writeAndFlush(responseMessage);
                        break;
                    case SHOW_IMAGE:
                        ImageRequest imageRequest=JsonMapper.fromJson(message,ImageRequest.class);
                        ImageRequest.Params imageRquestParams = imageRequest.params;
                        if(imageRquestParams!=null){
                            ImageActivity.actionStart(mContext,imageRquestParams.getImageUrl());
                        }else {
                            responseMessage.setStatus(Message.STATUS_COMMAND_ERROR);
                        }
                        ctx.writeAndFlush(responseMessage);
                        break;
                }
            } else {
                Message response = new Message();
                response.setType(Message.TYPE_MESSAGE_RESPONSE);
                response.setStatus(Message.STATUS_MAGIC_WORD_ERROR);
                ctx.writeAndFlush(response);
            }
        } catch (Exception e) {
            Log.e(TAG, "接受代表终端数据异常", e);
        }

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (IdleStateEvent.class.isAssignableFrom(evt.getClass())) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.WRITER_IDLE) {
                HeartBeatRequest heartBeatResponse = new HeartBeatRequest();
                heartBeatResponse.setCmd(Command.HEARTBEAT.value);
                HeartBeatRequest.HeartBeatParams heartBeat = new HeartBeatRequest.HeartBeatParams();
//                heartBeat.setRegister(Constant.registerStatus);
                heartBeat.setRegister(SharedPreferencesUtil.getRegisterStatus());
                heartBeat.setAppVersion(SystemUtil.getVersionName());
                heartBeat.setSeatId(SharedPreferencesUtil.getSeatId());
                heartBeat.setStatus(SharedPreferencesUtil.getStatus());
                heartBeat.setVotingRights(SharedPreferencesUtil.getVotingRights());
                heartBeat.setTerminalType(SharedPreferencesUtil.getTerminalType());
                heartBeat.setVersionCode(SystemUtil.getVersion());
                heartBeat.setPicZipMd5(SharedPreferencesUtil.getMd5());
                heartBeat.setMemberId(SharedPreferencesUtil.getMemberId());
                heartBeatResponse.setParams(heartBeat);
                heartBeatResponse.setType(Message.TYPE_MESSAGE_REQUEST);
                ctx.writeAndFlush(heartBeatResponse);
                System.out.println("heart:" + SharedPreferencesUtil.getStatus());
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
