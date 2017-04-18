package com.ydd.conference.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ydd.conference.R;
import com.ydd.conference.config.AppApplication;
import com.ydd.conference.config.Command;
import com.ydd.conference.config.Constant;
import com.ydd.conference.entity.Message;
import com.ydd.conference.entity.UploadRegisterRequest;
import com.ydd.conference.event.Event;
import com.ydd.conference.event.StartScannerEvent;
import com.ydd.conference.event.UpdateNameEvent;
import com.ydd.conference.event.UpdateRegisterUiEvent;
import com.ydd.conference.netty.ConferenceClientHandler;
import com.ydd.conference.util.DownloadPictureHelper;
import com.ydd.conference.util.SharedPreferencesUtil;

import de.greenrobot.event.EventBus;


public class StartRegisterActivity extends BaseActivity {

    private String title;
    private TextView titleText;
    private TextView registerStatusText;
    private TextView personNameText;
    private ImageView avatarImage;
    private int width;
    private boolean onlyShowName;

//    private Scanner scanner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_register);
        width = getWindowManager().getDefaultDisplay().getWidth();
        initData();
        initView();


    }

    private void initData() {
        EventBus.getDefault().post(new StartScannerEvent());
        title = getIntent().getStringExtra("title");
        onlyShowName=getIntent().getBooleanExtra("onlyShowName",false);
//        AppApplication.executor.submit(new Runnable() {
//            @Override
//            public void run() {
//                scanner = new Scanner();
//            }
//        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initData();
        initView();
    }

    private void initView() {
        titleText = (TextView) findViewById(R.id.titleText);
        titleText.setText(title);
        registerStatusText = (TextView) findViewById(R.id.registerStatusText);
        if (SharedPreferencesUtil.getRegisterStatus() == Constant.STATUS_REGISTER) {
            registerStatusText.setText(R.string.has_register);
        } else {
            registerStatusText.setText(R.string.please_register);
        }
        if(onlyShowName){
            registerStatusText.setVisibility(View.GONE);
        }else{
            registerStatusText.setVisibility(View.VISIBLE);
        }
        personNameText = (TextView) findViewById(R.id.personNameText);
        int length = SharedPreferencesUtil.getMemberName().length();
        if(length<5){
            nameTextsetTextSize(240);
        }else if(length==5){
            nameTextsetTextSize(190);
        }else if(length==6){
            nameTextsetTextSize(150);
        }else if(length==7){
            nameTextsetTextSize(130);
        }else if(length==8){
            nameTextsetTextSize(110);
        }else if(length==9){
            nameTextsetTextSize(100);
        }else if(length==10){
            nameTextsetTextSize(90);
        }else if(length>10){
            nameTextsetTextSize(80);
//                nameText.setTextSize(75);
            personNameText.setGravity(Gravity.CENTER);
        }
        personNameText.setText(SharedPreferencesUtil.getMemberName());
        avatarImage = (ImageView) findViewById(R.id.avatarImage);
        AppApplication.executor.submit(new Runnable() {
            @Override
            public void run() {
                final Drawable drawable = DownloadPictureHelper.getPicture();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (drawable == null) {
//                            System.out.println("setImage");
//                            avatarImage.setImageDrawable(getResources().getDrawable(R.mipmap.icon_user));
                        } else {
                            avatarImage.setImageDrawable(drawable);
                            avatarImage.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onEventMainThread(Event event) {
        super.onEventMainThread(event);
        //手动补到
//        if (event instanceof RegisterEvent) {
////            if (Constant.hasCard)
////                return;
//            Constant.isManualRegister = true;
//            registerStatusText.setText(R.string.has_register);
////            Constant.registerStatus = Constant.STATUS_REGISTER;
//            SharedPreferencesUtil.setRegisterStatus(Constant.STATUS_REGISTER);
//            stopScanner();
//
//        } else if (event instanceof UnRegisterEvent) {
//            if (Constant.isManualRegister) {
//                registerStatusText.setText(R.string.please_register);
////                Constant.registerStatus = Constant.STATUS_UN_REGISTER;
//                SharedPreferencesUtil.setRegisterStatus(Constant.STATUS_UN_REGISTER);
//                Constant.isManualRegister = false;
//                startScanner();
//            }
//        } else if (event instanceof ScannerResultEvent) {
//            if (Constant.isManualRegister)
//                return;
//            int status = ((ScannerResultEvent) event).status;
//            String carderNumber = ((ScannerResultEvent) event).cardNumber;
//            if (status == Constant.STATUS_CARD_SUCCESS) {
//                Constant.hasCard = true;
//                if (SharedPreferencesUtil.getCardNumber().equals("")) {
//                    ViewUtil.showToast("设备没有初始化,请联系管理人员");
//                    return;
//                }
//                if (SharedPreferencesUtil.getRegisterStatus() == Constant.STATUS_UN_REGISTER) {
//                    if (!SharedPreferencesUtil.getCardNumber().equals(carderNumber) && !SharedPreferencesUtil.getCard2Number().equals(carderNumber)) {
//                        return;
//                    }
//                    SharedPreferencesUtil.setRegisterStatus(Constant.STATUS_REGISTER);
//                } else {
//                    if (SharedPreferencesUtil.getCardNumber().equals(carderNumber) || SharedPreferencesUtil.getCard2Number().equals(carderNumber)) {
//                        return;
//                    }
//                    SharedPreferencesUtil.setRegisterStatus(Constant.STATUS_UN_REGISTER);
//                }
//                setRegisterInfo();
//
//            } else if (status == Constant.STATUS_CARD_NONE) {
//                Constant.hasCard = false;
//                if (SharedPreferencesUtil.getRegisterStatus() == Constant.STATUS_REGISTER) {
//                    UploadRegisterRequest registerRequest = new UploadRegisterRequest();
//                    registerRequest.setCmd(Command.UPDATE_REGISTER.value);
//                    registerRequest.setType(Message.TYPE_MESSAGE_REQUEST);
//                    UploadRegisterRequest.UploadRegisterParams params = new UploadRegisterRequest.UploadRegisterParams();
//                    params.setSeatId(SharedPreferencesUtil.getSeatId());
//                    params.setStatus(Constant.STATUS_UN_REGISTER);
//                    registerRequest.setParams(params);
//                    ConferenceClientHandler.mCtx.writeAndFlush(registerRequest);
//                }
//                registerStatusText.setText(R.string.please_register);
////                Constant.registerStatus = Constant.STATUS_UN_REGISTER;
//                SharedPreferencesUtil.setRegisterStatus(Constant.STATUS_UN_REGISTER);
//            }
//        } else if (event instanceof StopScannerEvent) {
//            stopScanner();
//        } else if (event instanceof StartScannerEvent) {
//            startScanner();
//        } else

        if (event instanceof UpdateRegisterUiEvent) {
            if (SharedPreferencesUtil.getRegisterStatus() == Constant.STATUS_REGISTER) {
                registerStatusText.setText(R.string.has_register);
            } else {
                registerStatusText.setText(R.string.please_register);
            }
        } else if (event instanceof UpdateNameEvent) {
            personNameText.setText(SharedPreferencesUtil.getMemberName());
            AppApplication.executor.submit(new Runnable() {
                @Override
                public void run() {
                    final Drawable drawable = DownloadPictureHelper.getPicture();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (drawable == null) {
//                                avatarImage.setImageDrawable(getResources().getDrawable(R.mipmap.icon_user));
                            } else {
                                avatarImage.setImageDrawable(drawable);
                                avatarImage.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }
            });
        }

    }


    private void nameTextsetTextSize(int i) {
        if (width == 1024) {
            personNameText.setTextSize(i - 5);
        } else {
            personNameText.setTextSize(i);
        }
    }

    /**
     * 开始读卡轮询
     */
//    private void startScanner() {
//        AppApplication.executor.submit(new Runnable() {
//            @Override
//            public void run() {
//                scanner = new Scanner();
//            }
//        });
//    }

    /**
     * 结束读卡轮询
     */
//    private void stopScanner() {
//        if (scanner != null) {
//            scanner.cancel();
//            scanner = null;
//        }
//    }


    /**
     * 设置报到状态
     */
    private void setRegisterInfo() {
        UploadRegisterRequest registerRequest = new UploadRegisterRequest();
        registerRequest.setType(Message.TYPE_MESSAGE_REQUEST);
        registerRequest.setCmd(Command.UPDATE_REGISTER.value);
        UploadRegisterRequest.UploadRegisterParams params = new UploadRegisterRequest.UploadRegisterParams();
        params.setSeatId(SharedPreferencesUtil.getSeatId());
        if (SharedPreferencesUtil.getRegisterStatus() == Constant.STATUS_UN_REGISTER) {
            params.setStatus(Constant.STATUS_UN_REGISTER);
            registerStatusText.setText(R.string.please_register);
        } else {
            params.setStatus(Constant.STATUS_REGISTER);
            registerStatusText.setText(R.string.has_register);
        }
        registerRequest.setParams(params);
        ConferenceClientHandler.mCtx.writeAndFlush(registerRequest);
    }

    public static void actionStart(Context context, String title,boolean onlyShowName) {
        Intent intent = new Intent(context, StartRegisterActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("onlyShowName", onlyShowName);
        context.startActivity(intent);
    }


}
