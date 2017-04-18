package com.ydd.conference.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.ydd.conference.R;
import com.ydd.conference.util.SharedPreferencesUtil;
import com.ydd.conference.util.StringUtils;

import org.w3c.dom.Text;

public class SingleVoteResultActivity extends BaseActivity {

    private TextView agreeText;
    private TextView againstText;
    private TextView abstainText;
    private TextView noOperateText;
    private TextView missText;
    private TextView text1;
    private TextView text2;
    private TextView text3;
    private TextView text4;

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
        noOperateText = (TextView) findViewById(R.id.noOperateText);
        text1= (TextView) findViewById(R.id.text1);
        text2= (TextView) findViewById(R.id.text2);
        text3= (TextView) findViewById(R.id.text3);
        text4= (TextView) findViewById(R.id.text4);
        if(width==1024){
            agreeText.setTextSize(TypedValue.COMPLEX_UNIT_SP,80);
            againstText.setTextSize(TypedValue.COMPLEX_UNIT_SP,80);
            abstainText.setTextSize(TypedValue.COMPLEX_UNIT_SP,80);
            noOperateText.setTextSize(TypedValue.COMPLEX_UNIT_SP,80);
            text1.setTextSize(TypedValue.COMPLEX_UNIT_SP,80);
            text2.setTextSize(TypedValue.COMPLEX_UNIT_SP,80);
            text3.setTextSize(TypedValue.COMPLEX_UNIT_SP,80);
            text4.setTextSize(TypedValue.COMPLEX_UNIT_SP,80);
            missText.setTextSize(TypedValue.COMPLEX_UNIT_SP,80);
        }else {
            agreeText.setTextSize(TypedValue.COMPLEX_UNIT_SP,100);
            againstText.setTextSize(TypedValue.COMPLEX_UNIT_SP,100);
            abstainText.setTextSize(TypedValue.COMPLEX_UNIT_SP,100);
            noOperateText.setTextSize(TypedValue.COMPLEX_UNIT_SP,100);
            text1.setTextSize(TypedValue.COMPLEX_UNIT_SP,100);
            text2.setTextSize(TypedValue.COMPLEX_UNIT_SP,100);
            text3.setTextSize(TypedValue.COMPLEX_UNIT_SP,100);
            text4.setTextSize(TypedValue.COMPLEX_UNIT_SP,100);
            missText.setTextSize(TypedValue.COMPLEX_UNIT_SP,100);
        }
        againstText.setText(againstNum+" 人");
        agreeText.setText(agreeNum+" 人");
        abstainText.setText(abstainNum+" 人");
        noOperateText.setText(missNum+" 人");
        //TODO by ranfi
        if (StringUtils.isNotEmpty(agreeNum) && Integer.parseInt(agreeNum) >= 0) {
            missText.setVisibility(View.VISIBLE);
//            missText.setText("(有 " + missNum + " 人未按表决键)");
            if(SharedPreferencesUtil.getTotal()==0){
                missText.setText(getResources().getString(R.string.have_passed));
                missText.setTextColor(getResources().getColor(R.color.broom));
            }else {
                int agreePerson = 2*Integer.parseInt(agreeNum);
                if(agreePerson>SharedPreferencesUtil.getTotal()){
                    missText.setText(getResources().getString(R.string.have_passed));
                    missText.setTextColor(getResources().getColor(R.color.broom));
                }else {
                    missText.setText(getResources().getString(R.string.have_not_passed));
                    missText.setTextColor(getResources().getColor(R.color.red_text));
                }
            }
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
