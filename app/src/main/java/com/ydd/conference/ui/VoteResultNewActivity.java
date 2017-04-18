package com.ydd.conference.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.ydd.conference.R;
import com.ydd.conference.config.Constant;
import com.ydd.conference.entity.StopVoteRequest;

import java.util.ArrayList;
import java.util.List;

public class VoteResultNewActivity extends BaseActivity {

    private static final int MIN_MOVE = 10;

    private final static int PAGE_SIZE = 8;

    private View resultLayout;
    private List<StopVoteRequest.VoteResult> mVotes = new ArrayList<>();
    private TextView pageText;

    private int mCurrentPage;
    private int mTotalPage;
    private int mResultSize;


    private TextView[] nameTextArray = new TextView[PAGE_SIZE];
    private TextView[] numberTextArray = new TextView[PAGE_SIZE];
    private TextView[] agreeTextArray = new TextView[PAGE_SIZE];
    private TextView[] againstTextArray = new TextView[PAGE_SIZE];
    private TextView[] absentTextArray = new TextView[PAGE_SIZE];
    private TextView[] missTextArray = new TextView[PAGE_SIZE];

    private int[] numberTextIdArray = {R.id.number1Text, R.id.number2Text, R.id.number3Text, R.id.number4Text, R.id.number5Text, R.id.number6Text, R.id.number7Text, R.id.number8Text, R.id.number9Text, R.id.number10Text};
    private int[] nameTextIdArray = {R.id.name1Text, R.id.name2Text, R.id.name3Text, R.id.name4Text, R.id.name5Text, R.id.name6Text, R.id.name7Text, R.id.name8Text, R.id.name9Text, R.id.name10Text};
    private int[] agreeTextIdArray = {R.id.agree1Text, R.id.agree2Text, R.id.agree3Text, R.id.agree4Text, R.id.agree5Text, R.id.agree6Text, R.id.agree7Text, R.id.agree8Text, R.id.agree9Text, R.id.agree10Text};
    private int[] againstTextIdArray = {R.id.against1Text, R.id.against2Text, R.id.against3Text, R.id.against4Text, R.id.against5Text, R.id.against6Text, R.id.against7Text, R.id.against8Text, R.id.against9Text, R.id.against10Text};
    private int[] absentTextIdArray = {R.id.absent1Text, R.id.absent2Text, R.id.absent3Text, R.id.absent4Text, R.id.absent5Text, R.id.absent6Text, R.id.absent7Text, R.id.absent8Text, R.id.absent9Text, R.id.absent10Text};
    private int[] missTextIdArray = {R.id.miss1Text, R.id.miss2Text, R.id.miss3Text, R.id.miss4Text, R.id.miss5Text, R.id.miss6Text, R.id.miss7Text, R.id.miss8Text, R.id.miss9Text, R.id.miss10Text,};
    private float downY;
    private float upY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_result_new);
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
        List<StopVoteRequest.VoteResult> list = (List<StopVoteRequest.VoteResult>) getIntent().getExtras().getSerializable("votes");
        if (list != null) {
            mVotes.clear();
            mVotes.addAll(list);
        }
        mResultSize = mVotes.size();
        mCurrentPage = 1;
        mTotalPage = mResultSize % PAGE_SIZE == 0 ? mResultSize / PAGE_SIZE : mResultSize / PAGE_SIZE + 1;
    }

    private void initView() {
        resultLayout = findViewById(R.id.resultLayout);
        resultLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    downY = event.getY();
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    upY = event.getY();
                    if (upY - downY < -MIN_MOVE) {
                        changePage(true);
                    } else if (upY - downY > MIN_MOVE) {
                        changePage(false);
                    }
                }
                return true;
            }
        });
        pageText = (TextView) findViewById(R.id.pageText);
        for (int i = 0; i < PAGE_SIZE; i++) {
            numberTextArray[i] = (TextView) findViewById(numberTextIdArray[i]);
            nameTextArray[i] = (TextView) findViewById(nameTextIdArray[i]);
            agreeTextArray[i] = (TextView) findViewById(agreeTextIdArray[i]);
            againstTextArray[i] = (TextView) findViewById(againstTextIdArray[i]);
            absentTextArray[i] = (TextView) findViewById(absentTextIdArray[i]);
            missTextArray[i] = (TextView) findViewById(missTextIdArray[i]);
        }
        bindData();
    }

    private void bindData() {
        int start = (mCurrentPage - 1) * PAGE_SIZE;
        start = start < 0 ? 0 : start;
        for (int i = 0; i < PAGE_SIZE; i++) {
            int position = start + i;
            if (position < mResultSize) {
                StopVoteRequest.VoteResult item = mVotes.get(position);
                numberTextArray[i].setText(position + 1 + ". ");
                nameTextArray[i].setText(item.getTitle());
                agreeTextArray[i].setText(item.getYes() + "");
                againstTextArray[i].setText(item.getNo() + "");
                absentTextArray[i].setText(item.getAbstain() + "");
                missTextArray[i].setText(item.getMiss() + "");
            } else {
                numberTextArray[i].setText("");
                nameTextArray[i].setText("");
                agreeTextArray[i].setText("");
                againstTextArray[i].setText("");
                absentTextArray[i].setText("");
                missTextArray[i].setText("");
            }
        }
        pageText.setText("[ 第 " + mCurrentPage + " 页 | 共 " + mTotalPage + " 页 ]");

    }


    private void changePage(boolean isNext) {
        if (isNext) {
            if (mCurrentPage < mTotalPage) {
                mCurrentPage++;
                bindData();
            }
        } else {
            if (mCurrentPage > 1) {
                mCurrentPage--;
                bindData();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == Constant.KEY_RIGHT) {
            changePage(true);
            return true;
        } else if (keyCode == Constant.KEY_LEFT) {
            changePage(false);
            return true;
        } else {
            return true;
        }
    }

    public static void actionStart(Context context, ArrayList<StopVoteRequest.VoteResult> list) {
        Intent intent = new Intent(context, VoteResultNewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("votes", list);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
