package com.ydd.conference.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.TextView;

import com.ydd.conference.R;
import com.ydd.conference.event.Event;
import com.ydd.conference.event.ShowSecondEvent;
import com.ydd.conference.util.SharedPreferencesUtil;
import com.ydd.conference.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class LogoActivity extends BaseActivity {

    private TextView titleText;

    private List<String> titles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
        initData();
        initView();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initData();
        initView();
    }

    private void initData() {
        titles = getIntent().getStringArrayListExtra("titles");
    }

    ;

    private void initView() {
        titleText = (TextView) findViewById(R.id.titleText);
        if (titles != null) {
            StringBuffer stringBuffer = new StringBuffer("");
            for (int i = 0; i < titles.size(); i++) {
                stringBuffer.append(titles.get(i) + "\n");
            }
            if (stringBuffer.length() > 1) {
                stringBuffer.delete(stringBuffer.length() - 1, stringBuffer.length());
            }
            DisplayMetrics metric = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metric);
            int width = metric.widthPixels;     // 屏幕宽度（像素）
            int height = metric.heightPixels;
            if(width==1024){
                titleText.setTextSize(TypedValue.COMPLEX_UNIT_SP,100);
            }else {
                titleText.setTextSize(TypedValue.COMPLEX_UNIT_SP,120);
            }
            titleText.setText(stringBuffer.toString());
        }
    }

    public void onEventMainThread(Event event) {
        if (event instanceof ShowSecondEvent) {
            if (null == secondaryDisplay) {
                return;
            }
            String content = ((ShowSecondEvent) event).getContent();
            if(StringUtils.isEmpty(content)){
                content = SharedPreferencesUtil.getMemberName();
            }
            secondaryDisplay.showName(content);
        }
    }

    public static void actionStart(Context context, List<String> titles) {
        Intent intent = new Intent(context, LogoActivity.class);
        intent.putStringArrayListExtra("titles", (ArrayList<String>) titles);

        context.startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
