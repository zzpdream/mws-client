package com.ydd.conference.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.ydd.conference.R;

public class ShowRegisterActivity extends BaseActivity {

    private String registeredNum;
    private String expectedNum;
    private String absentNum;
    private String leaveNum;

    private TextView registeredText;
    private TextView expectedText;
    private TextView leaveText;
    private TextView absentText;

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
        absentText = (TextView) findViewById(R.id.absentText);
        leaveText = (TextView) findViewById(R.id.leaveText);

        registeredText.setText(registeredNum);
        expectedText.setText(expectedNum);
        absentText.setText(absentNum);
        leaveText.setText(leaveNum);
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
