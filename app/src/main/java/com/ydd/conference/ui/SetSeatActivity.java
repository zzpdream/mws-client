package com.ydd.conference.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ydd.conference.R;
import com.ydd.conference.config.Constant;
import com.ydd.conference.custom.HuaWenTextView;
import com.ydd.conference.event.InitSeatEvent;
import com.ydd.conference.util.NetUtil;
import com.ydd.conference.util.SharedPreferencesUtil;
import com.ydd.conference.util.SystemUtil;
import com.ydd.conference.util.ViewUtil;

import de.greenrobot.event.EventBus;


public class SetSeatActivity extends BaseActivity implements View.OnClickListener {

    private EditText seatEdit;
    private Spinner typeSpinner;
    private TextView submitText;
    private TextView optionBarText;
    private TextView settingText;
    private TextView setIpText;
    private TextView backText;
    private EditText ipEdit;
    private EditText portEdit;
    private EditText localIpEdit;
    private FrameLayout frameLayout;

    private RadioGroup radioGroup;
    private RadioButton firstRadio;
    private RadioButton secondRadio;

//    private ArrayAdapter<String> typeAdapter;

    private String terminalType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_seat);
        initView();
    }

    private void initView() {

        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        frameLayout.setOnClickListener(this);
        optionBarText = (TextView) findViewById(R.id.optionBarText);
        optionBarText.setOnClickListener(this);
        if (Constant.barIsShow) {
            optionBarText.setText(R.string.hide_bar);
        } else {
            optionBarText.setText(R.string.show_bar);
        }
        settingText = (TextView) findViewById(R.id.settingText);
        settingText.setOnClickListener(this);
        seatEdit = (EditText) findViewById(R.id.seatEdit);
        seatEdit.setTypeface(HuaWenTextView.huaWen);
        typeSpinner = (Spinner) findViewById(R.id.typeSpinner);
        //typeAdapter = new ArrayAdapter<String>(this, R.layout.item_terminal_type);
//        typeAdapter.add("正式终端");
//        typeAdapter.add("列席终端");
        //typeSpinner.setAdapter(typeAdapter);
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                terminalType = position + "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ipEdit = (EditText) findViewById(R.id.ipEdit);
        localIpEdit = (EditText) findViewById(R.id.localIpEdit);
        ipEdit.setTypeface(HuaWenTextView.huaWen);
        portEdit = (EditText) findViewById(R.id.portEdit);
        portEdit.setTypeface(HuaWenTextView.huaWen);
        ipEdit.setText(SharedPreferencesUtil.getServerIp());
        portEdit.setText(SharedPreferencesUtil.getServerPort() + "");
        submitText = (TextView) findViewById(R.id.submitText);
        submitText.setOnClickListener(this);
        backText = (TextView) findViewById(R.id.backText);
        backText.setOnClickListener(this);
        seatEdit.setText(SharedPreferencesUtil.getSeatId());
        localIpEdit.setText(SharedPreferencesUtil.getLocalIp());

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.firstRadio:
                        terminalType = "1";
                        break;
                    case R.id.secondRadio:
                        terminalType = "2";
                        break;
                }
            }
        });
        String type = SharedPreferencesUtil.getTerminalType();
        if (type.equals("2")) {
            radioGroup.check(R.id.secondRadio);
        } else {
            radioGroup.check(R.id.firstRadio);
        }

        setIpText = (TextView) findViewById(R.id.setIpText);
        setIpText.setOnClickListener(this);
        firstRadio = (RadioButton) findViewById(R.id.firstRadio);
        secondRadio = (RadioButton) findViewById(R.id.secondRadio);
        firstRadio.setTypeface(HuaWenTextView.huaWen);
        secondRadio.setTypeface(HuaWenTextView.huaWen);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submitText:
                String seat = seatEdit.getText().toString();
                String ip = ipEdit.getText().toString(); //服务器IP
                String portString = portEdit.getText().toString();
                String localIp = localIpEdit.getText().toString(); //本机IP

                if (seat == null || "".equals(seat)) {
                    ViewUtil.showToast("请输入座位号");
                    return;
                }
                if (ip == null || "".equals(ip)) {
                    ViewUtil.showToast("请输入服务器地址");
                    return;
                }
                if (portString == null || "".equals(portString)) {
                    ViewUtil.showToast("请输入端口号");
                    return;
                }
//                if (!NetUtil.isIP(localIp)) {
//                    ViewUtil.showToast("请输入正确的本机IP");
//                    return;
//                }
                int port;
                try {
                    port = Integer.valueOf(portString);
                } catch (Exception e) {
                    e.printStackTrace();
                    ViewUtil.showToast("请输入正确的端口号");
                    return;
                }

//                if(ConferenceClientHandler.mCtx==null){
//                    ViewUtil.showToast("还没有连接到服务器");
//                    return;
//                }
//                InitSeatRequest initSeatRequest = new InitSeatRequest();dd
//                initSeatRequest.setType(Message.TYPE_MESSAGE_REQUEST);
//                initSeatRequest.setCmd(Command.INIT_SEAT.value);
//                InitSeatRequest.InitSeatParams initSeatParams = new InitSeatRequest.InitSeatParams();
//                initSeatParams.setSeatId(seat);
//                initSeatParams.setTerminalType(terminalType);
//                initSeatRequest.setParams(initSeatParams);
//                ConferenceClientHandler.mCtx.writeAndFlush(initSeatRequest);

                SharedPreferencesUtil.setSeatIdAndType(seat, terminalType);
//                SharedPreferencesUtil.saveIp(localIp);  //保存本机设置的IP
                boolean isChange = SharedPreferencesUtil.setServerIpAndPort(ip, port);
                InitSeatEvent initSeatEvent = new InitSeatEvent();
                initSeatEvent.isServerChange = isChange;
                EventBus.getDefault().post(initSeatEvent);
                ViewUtil.showToast("设置成功");
                finish();
                break;

            case R.id.optionBarText:
                if (Constant.barIsShow) {
                    optionBarText.setText(R.string.show_bar);
                    SystemUtil.hideSystemBar();
                } else {
                    optionBarText.setText(R.string.hide_bar);
                    SystemUtil.showSystemBar();
                }
                break;

            case R.id.settingText:
//                ComponentName componentName = new ComponentName(
//                        //这个是另外一个应用程序的包名
//                        "com.fsl.ethernet",
//                        //这个参数是要启动的Activity
//                        "com.fsl.ethernet.MainActivity");
//                try {
//                    Intent intent = new Intent();
//                    intent.setComponent(componentName);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                    Toast.makeText(this, "启动", Toast.LENGTH_SHORT).show();
//                    System.out.print("启动");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Toast.makeText(this, "失败", Toast.LENGTH_SHORT).show();
//                    System.out.println(e.getMessage());
//
//                }
                SystemUtil.showSystemBar();
                optionBarText.setText(R.string.hide_bar);
                startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 100);
                break;

            case R.id.setIpText:
                SystemUtil.showSystemBar();
                optionBarText.setText(R.string.hide_bar);
                ComponentName componentName = new ComponentName(
                        //这个是另外一个应用程序的包名
                        "com.fsl.ethernet",
                        //这个参数是要启动的Activity
                        "com.fsl.ethernet.MainActivity");
                try {
                    Intent intent = new Intent();
                    intent.setComponent(componentName);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    Toast.makeText(this, "启动成功", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "启动IP设置APK失败", Toast.LENGTH_SHORT).show();
                    System.out.println(e.getMessage());

                }
                break;

            case R.id.backText:
                finish();
                break;

            case R.id.frameLayout:

                ViewUtil.hideSystemKeyBoard(SetSeatActivity.this);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            //SystemUtil.hideSystemBar();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, SetSeatActivity.class);
        context.startActivity(intent);
    }
}
