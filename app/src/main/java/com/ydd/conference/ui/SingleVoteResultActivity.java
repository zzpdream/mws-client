package com.ydd.conference.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ydd.conference.R;
import com.ydd.conference.util.StringUtils;

public class SingleVoteResultActivity extends BaseActivity {

    private TextView agreeText;
    private TextView againstText;
    private TextView abstainText;
    private TextView missText;

    private String agreeNum;
    private String againstNum;
    private String abstainNum;
    private String missNum;
    private int width;
    private int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_vote);
        width = getWindowManager().getDefaultDisplay().getWidth();
        height = getWindowManager().getDefaultDisplay().getHeight();
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
        agreeNum = getIntent().getStringExtra("agreeNum");
        againstNum = getIntent().getStringExtra("againstNum");
        abstainNum = getIntent().getStringExtra("abstainNum");
        missNum = getIntent().getStringExtra("missNum");
    }

    private void initView() {
        agreeText = (TextView) findViewById(R.id.agreeText);
        againstText = (TextView) findViewById(R.id.againstText);
        abstainText = (TextView) findViewById(R.id.abstainText);
        missText = (TextView) findViewById(R.id.missText);

        againstText.setText(againstNum);
        agreeText.setText(agreeNum);
        abstainText.setText(abstainNum);
        //TODO by ranfi
        if (StringUtils.isNotEmpty(missNum) && Integer.parseInt(missNum) > 0) {
            missText.setVisibility(View.VISIBLE);
            missText.setText("(有 " + missNum + " 人未按表决键)");
        } else {
            missText.setVisibility(View.GONE);
        }
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        finish();
//    }

    public static void actionStart(Context context, String agreeNum, String againstNum, String abstainNum, String missNum) {
        Intent intent = new Intent(context, SingleVoteResultActivity.class);
        intent.putExtra("agreeNum", agreeNum);
        intent.putExtra("againstNum", againstNum);
        intent.putExtra("abstainNum", abstainNum);
        intent.putExtra("missNum", missNum);
        context.startActivity(intent);
    }


}
