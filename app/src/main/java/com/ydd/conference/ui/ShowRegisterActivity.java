package com.ydd.conference.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.TextView;

import com.ydd.conference.R;
import com.ydd.conference.util.SharedPreferencesUtil;
import com.ydd.conference.util.StringUtils;

import static com.ydd.conference.R.id.absentText;

public class ShowRegisterActivity extends BaseActivity {

    private String registeredNum;
    private String expectedNum;
    private String absentNum;
    private String leaveNum;

    private TextView registeredText;
    private TextView expectedText;
    private TextView leaveText;
    private TextView registerStatusText;
    private TextView text1;
    private TextView text2;
    private TextView text3;
    private TextView text4;

//    private TextView absentText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_register);
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
        leaveNum = getIntent().getStringExtra("leaveNum");
        absentNum = getIntent().getStringExtra("absentNum");
        expectedNum = getIntent().getStringExtra("expectedNum");
        registeredNum = getIntent().getStringExtra("registeredNum");
    }

    private void initView() {
        registeredText = (TextView) findViewById(R.id.registeredText);
        expectedText = (TextView) findViewById(R.id.expectedText);
        registerStatusText= (TextView) findViewById(R.id.registerStatusText);
//        absentText = (TextView) findViewById(absentText);
        leaveText = (TextView) findViewById(R.id.leaveText);
        text1 = (TextView) findViewById(R.id.text1);
        text2= (TextView) findViewById(R.id.text2);
        text3 = (TextView) findViewById(R.id.text3);
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = metric.heightPixels;
        if(StringUtils.isNotEmpty(expectedNum)){
            SharedPreferencesUtil.saveTotal(Integer.parseInt(expectedNum));
        }
        registeredText.setText(registeredNum+" 人");
        expectedText.setText(expectedNum+" 人");
//        absentText.setText(absentNum);
        int absentPersonNum=0;
        int currentNum = 0;
        if(StringUtils.isNotEmpty(leaveNum)&&StringUtils.isNotEmpty(absentNum)){
           absentPersonNum = Integer.parseInt(leaveNum)+Integer.parseInt(absentNum);
           currentNum = Integer.parseInt(registeredNum);
        }
        leaveText.setText(absentPersonNum+" 人");

        if(currentNum>absentPersonNum){
            registerStatusText.setText(getResources().getString(R.string.apply_laws_num));
            registerStatusText.setTextColor(getResources().getColor(R.color.white));
        }else {
            registerStatusText.setText(getResources().getString(R.string.not_apply_laws_num));
            registerStatusText.setTextColor(getResources().getColor(R.color.red_text));
        }
        if(width == 1024){
            text2.setTextSize(TypedValue.COMPLEX_UNIT_SP,80);
            text3.setTextSize(TypedValue.COMPLEX_UNIT_SP,80);
            text1.setTextSize(TypedValue.COMPLEX_UNIT_SP,80);
            registeredText.setTextSize(TypedValue.COMPLEX_UNIT_SP,80);
            expectedText.setTextSize(TypedValue.COMPLEX_UNIT_SP,80);
            leaveText.setTextSize(TypedValue.COMPLEX_UNIT_SP,80);
            registerStatusText.setTextSize(TypedValue.COMPLEX_UNIT_SP,80);
        }else {
            text2.setTextSize(TypedValue.COMPLEX_UNIT_SP,120);
            text3.setTextSize(TypedValue.COMPLEX_UNIT_SP,120);
            text1.setTextSize(TypedValue.COMPLEX_UNIT_SP,120);
            registeredText.setTextSize(TypedValue.COMPLEX_UNIT_SP,120);
            expectedText.setTextSize(TypedValue.COMPLEX_UNIT_SP,120);
            leaveText.setTextSize(TypedValue.COMPLEX_UNIT_SP,120);
            registerStatusText.setTextSize(TypedValue.COMPLEX_UNIT_SP,120);
        }

    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        finish();
//    }


    public static void actionStart(Context context, String registeredNum, String expectedNum, String absentNum, String leaveNum) {
        Intent intent = new Intent(context, ShowRegisterActivity.class);
        intent.putExtra("registeredNum", registeredNum);
        intent.putExtra("expectedNum", expectedNum);
        intent.putExtra("absentNum", absentNum);
        intent.putExtra("leaveNum", leaveNum);
        context.startActivity(intent);
    }
}
