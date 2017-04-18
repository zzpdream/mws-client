package com.ydd.conference.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ydd.conference.R;
import com.ydd.conference.config.Constant;
import com.ydd.conference.event.Event;
import com.ydd.conference.event.SecondaryLogoEvent;
import com.ydd.conference.event.ShowSecondEvent;
import com.ydd.conference.util.SharedPreferencesUtil;
import com.ydd.conference.util.StringUtils;

import de.greenrobot.event.EventBus;

public class NameActivity extends BaseActivity {

    private FrameLayout setSeatLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharedPreferencesUtil.getTerminalType().equals(Constant.Terminal_FIRST)) {
            if(SharedPreferencesUtil.hasAnonAccessPermission()){
                setContentView(R.layout.activity_name_anon);
            } else {
                setContentView(R.layout.activity_name);
            }
        } else {
            setContentView(R.layout.activity_name_big);
        }
        initView();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initView();
    }

    private void initView() {
        setSeatLayout = (FrameLayout) findViewById(R.id.setSeatLayout);
        setSeatLayout.setOnClickListener(this);
        String name = SharedPreferencesUtil.getMemberName();
        if (StringUtils.isEmpty(name) && null != Constant.LOGO_TEXT) {
            for (int i = 0; i < Constant.LOGO_TEXT.size(); i++) {
                name += Constant.LOGO_TEXT.get(i);
                if (i < Constant.LOGO_TEXT.size() - 1) {
                    name += "\n";
                }
            }
        }
//        name = "姚海通";
//        name = "市政府府秘秘书长长长";
        TextView nameText = (TextView) findViewById(R.id.nameText);
        if (SharedPreferencesUtil.getTerminalType().equals(Constant.Terminal_FIRST)) {
            if (name.length() < 5) {
                nameText.setTextSize(250);
            } else if (name.length() == 5) {
                nameText.setTextSize(200);
            } else if (name.length() == 6) {
                nameText.setTextSize(160);
            } else if (name.length() == 7) {
                nameText.setTextSize(140);
            } else {
                nameText.setTextSize(70);
            }
        } else {
            if (name.length() < 4) {
                nameText.setTextSize(360);
            } else if (name.length() == 4) {
                nameText.setTextSize(320);
            } else if (name.length() == 5) {
                nameText.setTextSize(260);
            } else if (name.length() == 6) {
                nameText.setTextSize(200);
            } else if (name.length() == 7) {
                nameText.setTextSize(170);
            } else if (name.length() == 8) {
                nameText.setTextSize(160);
            } else if (name.length() == 9) {
                nameText.setTextSize(145);
            } else if (name.length() == 10) {
                nameText.setTextSize(130);
            } else if (name.length() > 10) {
                nameText.setGravity(Gravity.CENTER);
                nameText.setTextSize(100);
            } else {
                nameText.setTextSize(100);
            }
        }
        nameText.setText(name);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.setSeatLayout:
                SetSeatActivity.actionStart(NameActivity.this);
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //如果是匿名访问不要销毁
        if (SharedPreferencesUtil.getTerminalType().equals(Constant.Terminal_FIRST) && !SharedPreferencesUtil.hasAnonAccessPermission()) {
            finish();
        }
    }

    // add by lt 列席终端主屏名字没有及时变化
    public void onEventMainThread(Event event) {
        super.onEventMainThread(event);
        if (event instanceof ShowSecondEvent) {
            initView();
        }else if(event instanceof SecondaryLogoEvent){
            if(null == secondaryLogoDisplay){
                return;
            }
            secondaryLogoDisplay.load(((SecondaryLogoEvent) event).getTitles());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, NameActivity.class);
        context.startActivity(intent);
    }
}
