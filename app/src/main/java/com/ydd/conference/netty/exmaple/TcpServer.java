package com.ydd.conference.netty.exmaple;

import com.ydd.conference.config.Command;
import com.ydd.conference.entity.HeartBeatResponse;
import com.ydd.conference.entity.Message;
import com.ydd.conference.entity.SeatRequest;
import com.ydd.conference.entity.SetSeatInfoRequest;
import com.ydd.conference.entity.ShowLogoRequest;
import com.ydd.conference.entity.ShowSubjectRequest;
import com.ydd.conference.entity.StartRegisterRequest;
import com.ydd.conference.entity.StartVoteRequest;
import com.ydd.conference.entity.StopVoteRequest;
import com.ydd.conference.entity.VoteRequest;
import com.ydd.conference.netty.MessageDecoder;
import com.ydd.conference.netty.MessageEncoder;
import com.ydd.conference.ui.LogoActivity;
import com.ydd.conference.util.JsonMapper;
import com.ydd.conference.util.SystemUtil;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Dozer @ 5/24/15
 * mail@dozer.cc
 * http://www.dozer.cc
 */
public final class TcpServer {
    private volatile EventLoopGroup bossGroup;

    private volatile EventLoopGroup workerGroup;

    private volatile ServerBootstrap bootstrap;

    private volatile boolean closed = false;

    private final int localPort;

    public TcpServer(int localPort) {
        this.localPort = localPort;
    }

    public void close() {
        closed = true;

        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();

        System.out.println("Stopped Tcp Server: " + localPort);
    }

    public void init() {
        closed = false;

        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);

        bootstrap.channel(NioServerSocketChannel.class);

        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new MessageEncoder());
                ch.pipeline().addLast(new MessageDecoder());
                ch.pipeline().addLast(new TimeServerHandler());
            }

//            @Override
//            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//                super.channelRead(ctx, msg);
//            }
        });

        doBind();
    }

    protected void doBind() {
        if (closed) {
            return;
        }

        bootstrap.bind(localPort).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture f) throws Exception {
                if (f.isSuccess()) {
                    System.out.println("Started Tcp Server: " + localPort);
                } else {
                    System.out.println("Started Tcp Server Failed: " + localPort);

                    f.channel().eventLoop().schedule(new Runnable() {
                        @Override
                        public void run() {
                            doBind();
                        }
                    }, 1, TimeUnit.SECONDS);
                }
            }
        });
    }

    public class TimeServerHandler extends ChannelHandlerAdapter {


        //ChannelHandlerContext通道处理上下文
        @Override
        public void channelActive(final ChannelHandlerContext ctx) throws InterruptedException { // (1)
//            ShowLogoRequest showLogoRequest = new ShowLogoRequest();
//            ShowLogoRequest.ShowLogoParams showLogoParams = new ShowLogoRequest.ShowLogoParams();
//            showLogoParams.setSeatId("1");
//            List<String> list = new ArrayList<String>();
//            list.add("上海十四届人大常委会");
//            list.add("第二十次会议");
//            showLogoParams.setTitles(list);
//            showLogoRequest.setParams(showLogoParams);
//            showLogoRequest.setCmd(Command.SHOW_LOGO.value);
//            showLogoRequest.setType(Message.TYPE_MESSAGE_REQUEST);

//            SetSeatInfoRequest request = new SetSeatInfoRequest();
//            request.setCmd(Command.SET_SEAT.value);
//            request.setType(Message.TYPE_MESSAGE_REQUEST);
//            SetSeatInfoRequest.SetSeatInfoParams params = new SetSeatInfoRequest.SetSeatInfoParams();
//            params.setSeatId("1");
//            params.setCardId1("123456");
//            params.setPersonName("heheLT");
//            params.setPersonId("1");
//            request.setParams(params);


//            StartRegisterRequest request = new StartRegisterRequest();
//            request.setCmd(Command.START_REGISTER.value);
//            request.setType(Message.TYPE_MESSAGE_REQUEST);
//            StartRegisterRequest.StartRegisterParams params = new StartRegisterRequest.StartRegisterParams();
//            params.setSeatId("1");
//            params.setTitle("中华人民共和国成立啦");

            //补到
//            SeatRequest request =new SeatRequest();
//            request.setCmd(Command.REGISTER.value);
//            request.setType(Message.TYPE_MESSAGE_REQUEST);
//            SeatRequest.SeatParams params = new SeatRequest.SeatParams();
//            params.setSeatId("1");

            //销到
//            SeatRequest request =new SeatRequest();
//            request.setCmd(Command.UNREGISTER.value);
//            request.setType(Message.TYPE_MESSAGE_REQUEST);
//            SeatRequest.SeatParams params = new SeatRequest.SeatParams();
//            params.setSeatId("1");

//            //显示议程内容
//            ShowSubjectRequest request =new ShowSubjectRequest();
//            request.setCmd(Command.SHOW_SUBJECT.value);
//            request.setType(Message.TYPE_MESSAGE_REQUEST);
//            ShowSubjectRequest.ShowSubjectParams params = new ShowSubjectRequest.ShowSubjectParams();
//            params.setSeatId("1");
//            params.setSubject("第一项议程:审议通过<<上海市人大常委会>>");

            //申请发言
//            SeatRequest request =new SeatRequest();
//            request.setCmd(Command.START_SPEAK.value);
//            request.setType(Message.TYPE_MESSAGE_REQUEST);
//            SeatRequest.SeatParams params = new SeatRequest.SeatParams();
//            params.setSeatId("1");

            //已申请发言
//            SeatRequest request =new SeatRequest();
//            request.setCmd(Command.REQUEST_SPEAK.value);
//            request.setType(Message.TYPE_MESSAGE_REQUEST);
//            SeatRequest.SeatParams params = new SeatRequest.SeatParams();
//            params.setSeatId("1");

            //设置当前发言人
//            SeatRequest request =new SeatRequest();
//            request.setCmd(Command.SET_SPEAKER.value);
//            request.setType(Message.TYPE_MESSAGE_REQUEST);
//            SeatRequest.SeatParams params = new SeatRequest.SeatParams();
//            params.setSeatId("1");


            //取消或结束发言
//            SeatRequest request =new SeatRequest();
//            request.setCmd(Command.CANCEL_SPEAKING.value);
//            request.setType(Message.TYPE_MESSAGE_REQUEST);
//            SeatRequest.SeatParams params = new SeatRequest.SeatParams();
//            params.setSeatId("1");

            //取消或结束发言
//            SeatRequest request =new SeatRequest();
//            request.setCmd(Command.CANCEL_SPEAKING.value);
//            request.setType(Message.TYPE_MESSAGE_REQUEST);
//            SeatRequest.SeatParams params = new SeatRequest.SeatParams();
//            params.setSeatId("1");

            //单项vote
//            VoteRequest request = new VoteRequest();
//            request.setCmd(Command.START_VOTE.value);
//            VoteRequest.VoteParams params = new VoteRequest.VoteParams();
//            params.setSeatId("1");
//            params.setType(Message.TYPE_SINGLE_VOTE+"");
//            List<VoteRequest.Vote> list = new ArrayList<>();
//            params.setVotes(list);

            while (true){
                //多项vote
                StartVoteRequest request = new StartVoteRequest();
                request.setCmd(Command.START_VOTE.value);
                StartVoteRequest.StartVoteParams params = new StartVoteRequest.StartVoteParams();
                params.setSeatId("1");
                params.setType(Message.TYPE_VOTE + "");
                List<String> list = new ArrayList<>();
                for(int i=0;i<3;i++){
                    list.add("李涛");
                }
//                list.add("李四");
//                params.setItems(list);

                request.setParams(params);
                ctx.writeAndFlush(request);
                Thread.sleep(5000);
//                break;
            }

        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("server------" + msg.toString());

            String message = (String) msg;
            if (!"error".equals(msg.toString())) {
                Message messageEntity = JsonMapper.fromJson(message, Message.class);
                String cmd = messageEntity.getCmd();
                Command command = Command.getCommand(cmd);
                if (command == null) {
                    if (messageEntity.getType() == Message.TYPE_MESSAGE_REQUEST) {
                        Message response = new Message();
                        response.setCmd(command.value);
                        response.setType(Message.TYPE_MESSAGE_RESPONSE);
                        response.setStatus(Message.STATUS_COMMAND_ERROR);
                        ctx.write(response);
                    }
                    return;
                }
                switch (command) {
                    case HEARTBEAT:
                        HeartBeatResponse heartBeatResponse = new HeartBeatResponse();
                        heartBeatResponse.setCmd(Command.HEARTBEAT.value);
                        heartBeatResponse.setType(Message.TYPE_MESSAGE_RESPONSE);
                        HeartBeatResponse.HeartBeatValue heartBeatValue = new HeartBeatResponse.HeartBeatValue();
                        heartBeatValue.setVersionCode(2);
                        heartBeatResponse.setValues(heartBeatValue);
                        ctx.writeAndFlush(heartBeatResponse);
                        break;

                }
            } else {

            }


        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();
            ctx.close();
        }
    }
}
