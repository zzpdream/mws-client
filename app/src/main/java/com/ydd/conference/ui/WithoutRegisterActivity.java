package com.ydd.conference.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.ydd.conference.R;
import com.ydd.conference.util.SharedPreferencesUtil;

/**
 * Created by ranfi on 4/13/16.
 */
public class WithoutRegisterActivity extends BaseActivity {


    private TextView titleText;
    private TextView personNameText;
    private String title;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_register);
        initData();
        initView();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initData();
        initView();
    }


    public void initData() {
        title = getIntent().getStringExtra("title");
    }

    private void initView() {
        titleText = (TextView) findViewById(R.id.titleText);
        personNameText = (TextView)findViewById(R.id.personNameText);
        titleText.setText(title);
        personNameText.setText(SharedPreferencesUtil.getMemberName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    public static void actionStart(Context context, String title) {
        Intent intent = new Intent(context, WithoutRegisterActivity.class);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }
}
