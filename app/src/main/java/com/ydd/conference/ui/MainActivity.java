package com.ydd.conference.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ydd.conference.R;
import com.ydd.conference.config.AppApplication;
import com.ydd.conference.config.Command;
import com.ydd.conference.config.Constant;
import com.ydd.conference.entity.Message;
import com.ydd.conference.entity.UploadRegisterRequest;
import com.ydd.conference.event.ClearSeatEvent;
import com.ydd.conference.event.ConnectEvent;
import com.ydd.conference.event.Event;
import com.ydd.conference.event.InitSeatEvent;
import com.ydd.conference.event.RegisterEvent;
import com.ydd.conference.event.ScannerResultEvent;
import com.ydd.conference.event.SecondaryLogoEvent;
import com.ydd.conference.event.ShowSecondEvent;
import com.ydd.conference.event.StartScannerEvent;
import com.ydd.conference.event.StopScannerEvent;
import com.ydd.conference.event.UnRegisterEvent;
import com.ydd.conference.event.UpdateRegisterUiEvent;
import com.ydd.conference.netty.ConferenceClient;
import com.ydd.conference.netty.ConferenceClientHandler;
import com.ydd.conference.scanner.Scanner;
import com.ydd.conference.util.InstallHelper;
import com.ydd.conference.util.NetUtil;
import com.ydd.conference.util.SharedPreferencesUtil;
import com.ydd.conference.util.StringUtils;
import com.ydd.conference.util.SystemUtil;
import com.ydd.conference.util.ViewUtil;

import de.greenrobot.event.EventBus;


public class MainActivity extends BaseActivity {

    private FrameLayout setSeatLayout;
    private FrameLayout setSeatRightLayout;
    private TextView terminalText;
    private TextView seatIdText;
    private TextView statusText;
    private TextView versionText;
    private TextView ipText;
    private Scanner scanner;

    private long leftLastTime;
    private long rightLastTime;
    private static final int TIME = 1 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //启动网络
        NetUtil.initIp(SharedPreferencesUtil.getLocalIp());

        InstallHelper.init();

        SharedPreferencesUtil.setStatus(Command.INITIALIZE.status);
        initView();
        initData();

        //TODO 调试的时候请将下面的启动网络注释掉
//        if (!SharedPreferencesUtil.getNetConfig()) {
//            SystemUtil.start(this);
//            SharedPreferencesUtil.setIsNetConfig(true);
//        } else {
//            SharedPreferencesUtil.setIsNetConfig(false);
//        }
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Constant.list = new ArrayList<>();
//                for (int i = 0; i < 110; i++) {
//                    StartVoteRequest.VoteItem item = new StartVoteRequest.VoteItem();
//                    item.id = i;
//                    item.title = "李涛李涛";
//                    Constant.list.add(item);
//                }
//                VoteNewActivity.actionStart(MainActivity.this);
//                ArrayList<StopVoteRequest.VoteResult> list = new ArrayList<StopVoteRequest.VoteResult>();
//                for (int i = 0; i < 100; i++) {
//                    StopVoteRequest.VoteResult item = new StopVoteRequest.VoteResult();
//                    item.setTitle("李涛" + i);
//                    item.setMiss(i);
//                    item.setAbstain(i);
//                    item.setYes(i);
//                    item.setNo(i);
//                    list.add(item);
//                }
//                VoteResultNewActivity.actionStart(MainActivity.this, list);
//
//                MainActivity.actionStart(MainActivity.this);
//            }
//        }, 10000);

        AppApplication.executor.submit(new Runnable() {
            @Override
            public void run() {
                SystemUtil.hideSystemBar();
                ConferenceClient conferenceClient = ConferenceClient.getInstance();
                conferenceClient.setContext(MainActivity.this);
                conferenceClient.init();
                scanner = new Scanner();
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initView();
        initData();
    }

    private void initView() {
        setSeatLayout = (FrameLayout) findViewById(R.id.setSeatLayout);
        setSeatLayout.setOnClickListener(this);
        setSeatRightLayout = (FrameLayout) findViewById(R.id.setSeatRightLayout);
        setSeatRightLayout.setOnClickListener(this);
        terminalText = (TextView) findViewById(R.id.terminalText);
        seatIdText = (TextView) findViewById(R.id.seatIdText);
        statusText = (TextView) findViewById(R.id.statusText);
        versionText = (TextView) findViewById(R.id.versionText);
        ipText = (TextView) findViewById(R.id.ipText);
        versionText.setText("V" + SystemUtil.getVersionName());
        ipText.setText(SharedPreferencesUtil.getLocalIp());
    }

    private void initData() {
        if (Constant.isConnected) {
            statusText.setText(R.string.has_in);
        } else {
            statusText.setText(R.string.wait_in);
        }
        seatIdText.setText(SharedPreferencesUtil.getSeatId().equals("") ? "未设置" : SharedPreferencesUtil.getSeatId());
        String type = SharedPreferencesUtil.getTerminalType();
        terminalText.setText(type.equals("1") ? "正式终端" : type.equals("2") ? "列席终端" : "未设置");

        if (type.equals(Constant.Terminal_SECOND) && SharedPreferencesUtil.getSeatId().equals(SharedPreferencesUtil.getChairSeatId())) {
            FullscreenActivity.actionStart(MainActivity.this, Constant.VIDEO_URL);
            return;
        }

        //add by lt 座位号132 固定显示视频
//        if (SharedPreferencesUtil.getSeatId().equals("132")) {
//            FullscreenActivity.actionStart(MainActivity.this, Constant.VIDEO_URL2, true);
//            return;
//        }

        if (type.equals(Constant.Terminal_SECOND)) {
            NameActivity.actionStart(MainActivity.this);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
                case R.id.setSeatLayout:
                    leftLastTime = System.currentTimeMillis();
                    if (leftLastTime - rightLastTime < TIME) {
                        SetSeatActivity.actionStart(MainActivity.this);
                        leftLastTime = 0;
                    }
                break;

            case R.id.setSeatRightLayout:
                rightLastTime = System.currentTimeMillis();
                if (rightLastTime - leftLastTime < TIME) {
                    SetSeatActivity.actionStart(MainActivity.this);
                    rightLastTime = 0;
                }
                break;

        }
    }


    public void onEventMainThread(Event event) {
        if (event instanceof InitSeatEvent) {
            initData();
//            statusText.setText(R.string.wait_in);
            if (((InitSeatEvent) event).isServerChange) {
                statusText.setText(R.string.wait_in);
                ConferenceClient.getInstance().restart();
            }
        } else if (event instanceof ConnectEvent) {
            if (((ConnectEvent) event).isConnected) {
                statusText.setText(R.string.has_in);
            } else {
                statusText.setText(R.string.wait_in);
            }
        } else if (event instanceof ClearSeatEvent) {
            initData();
        } else if (event instanceof ShowSecondEvent) {
            if (null == secondaryDisplay) {
                return;
            }
            String content = ((ShowSecondEvent) event).getContent();
            if(StringUtils.isEmpty(content)){
                content = SharedPreferencesUtil.getMemberName();
            }
            secondaryDisplay.showName(content);
        } else if (event instanceof RegisterEvent) {
            if (scanner != null) {
                scanner.cancel();
            }
            Constant.isManualRegister = true;
            SharedPreferencesUtil.setRegisterStatus(Constant.STATUS_REGISTER);
            EventBus.getDefault().post(new UpdateRegisterUiEvent());
            // add by lt 请假情况
            EventBus.getDefault().post(new ShowSecondEvent());
        } else if (event instanceof UnRegisterEvent) {
            if (Constant.isManualRegister) {
                if (scanner != null) {
                    scanner.start();
                }
                SharedPreferencesUtil.setRegisterStatus(Constant.STATUS_UN_REGISTER);
                Constant.isManualRegister = false;
                EventBus.getDefault().post(new UpdateRegisterUiEvent());
                // add by lt 请假情况
                EventBus.getDefault().post(new ShowSecondEvent());
            }
        } else if (event instanceof ScannerResultEvent) {
            if (Constant.isManualRegister)
                return;
            int status = ((ScannerResultEvent) event).status;
            String carderNumber = ((ScannerResultEvent) event).cardNumber;
            if (status == Constant.STATUS_CARD_SUCCESS) {
                Constant.hasCard = true;
                if (SharedPreferencesUtil.getCardNumber().equals("")) {
                    ViewUtil.showToast("没有排座信息,请联系管理人员");
                    return;
                }
                System.out.print("local number:" + SharedPreferencesUtil.getCardNumber());
                //add by lt 两种模式 检查卡号和不检查卡号
                if (SharedPreferencesUtil.getCardNoCheck() == 1 || SharedPreferencesUtil.getCardNumber().equals(carderNumber) || SharedPreferencesUtil.getCard2Number().equals(carderNumber)) {
                    if (SharedPreferencesUtil.getRegisterStatus() == Constant.STATUS_UN_REGISTER) {
                        SharedPreferencesUtil.setRegisterStatus(Constant.STATUS_REGISTER);
                        setRegisterInfo();
                        System.out.print("-------");
                        // add by lt 请假情况 插卡后改变席卡
                        EventBus.getDefault().post(new ShowSecondEvent());
                    }
                    System.out.println("register");
                } else {
                    if (SharedPreferencesUtil.getRegisterStatus() == Constant.STATUS_REGISTER) {
                        SharedPreferencesUtil.setRegisterStatus(Constant.STATUS_UN_REGISTER);
                        setRegisterInfo();
                        // add by lt 请假情况  插卡后改变席卡
                        EventBus.getDefault().post(new ShowSecondEvent());
                    }
                    System.out.println("unregister");
                }
            } else if (status == Constant.STATUS_CARD_NONE) {
                System.out.println("no card");
                Constant.hasCard = false;
                if (SharedPreferencesUtil.getRegisterStatus() == Constant.STATUS_REGISTER) {
                    SharedPreferencesUtil.setRegisterStatus(Constant.STATUS_UN_REGISTER);
                    setRegisterInfo();
                    // add by lt 请假情况
                    EventBus.getDefault().post(new ShowSecondEvent());
                }
            }
        } else if (event instanceof StopScannerEvent) {
            if (scanner != null) {
                scanner.cancel();
            }
        } else if (event instanceof StartScannerEvent) {
            if (scanner != null) {
                scanner.start();
            }
        } else if(event instanceof SecondaryLogoEvent){
            if(null == secondaryLogoDisplay){
                return;
            }
            secondaryLogoDisplay.load(((SecondaryLogoEvent) event).getTitles());
        }
    }

    /**
     * 设置报到状态
     */
    private void setRegisterInfo() {
        AppApplication.executor.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("update register ui");
                EventBus.getDefault().post(new UpdateRegisterUiEvent());
                UploadRegisterRequest registerRequest = new UploadRegisterRequest();
                registerRequest.setType(Message.TYPE_MESSAGE_REQUEST);
                registerRequest.setCmd(Command.UPDATE_REGISTER.value);
                UploadRegisterRequest.UploadRegisterParams params = new UploadRegisterRequest.UploadRegisterParams();
                params.setSeatId(SharedPreferencesUtil.getSeatId());
                if (SharedPreferencesUtil.getRegisterStatus() == Constant.STATUS_UN_REGISTER) {
                    params.setStatus(Constant.STATUS_UN_REGISTER);
                } else {
                    params.setStatus(Constant.STATUS_REGISTER);
                }
                registerRequest.setParams(params);
                ConferenceClientHandler.mCtx.writeAndFlush(registerRequest);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        String ip = SystemUtil.getLocalHostIp();
        ipText.setText(ip);
        SharedPreferencesUtil.saveIp(ip);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }


}
