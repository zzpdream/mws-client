package com.ydd.conference.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import com.ydd.conference.R;
import com.ydd.conference.config.Constant;
import com.ydd.conference.custom.CommonAdapter;
import com.ydd.conference.custom.ViewHolder;
import com.ydd.conference.entity.StopVoteRequest;
import com.ydd.conference.netty.ConferenceClientHandler;
import com.ydd.conference.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

public class VoteResultActivity extends BaseActivity {

    private ListView resultList;

    private CommonAdapter<StopVoteRequest.VoteResult> mAdapter;

    private List<StopVoteRequest.VoteResult> mVotes = new ArrayList<>();

    private static final int MIN_MOVE = 10;
    private static final int NUM_COLUMNS = 10;
    private static final int NUM_RAW = 1;

    private float downY;
    private float upY;
    private int position = 0;
    private boolean isDown = false;
    private int maxItem = 99;
    private int minItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_result);
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
        maxItem = mVotes.size();
    }


    private void initView() {
        resultList = (ListView) findViewById(R.id.resultList);
        mAdapter = new CommonAdapter<StopVoteRequest.VoteResult>(this, mVotes, R.layout.item_vote_result) {
            @Override
            public void convertView(ViewHolder viewHolder, StopVoteRequest.VoteResult item, int position) {
                viewHolder.setTextView(R.id.agreeText, item.getYes() + "");
                viewHolder.setTextView(R.id.againstText, item.getNo() + "");
                viewHolder.setTextView(R.id.absentText, item.getAbstain() + "");
                viewHolder.setTextView(R.id.missText, item.getMiss() + "");
                viewHolder.setTextView(R.id.nameText, position + 1 + ". " + item.getTitle());
            }
        };
        resultList.setAdapter(mAdapter);
        resultList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    downY = event.getY();
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    upY = event.getY();
                    if (upY - downY < -MIN_MOVE) {
                        if (!isDown) {
                            position = position + NUM_COLUMNS * NUM_RAW - 1;
                        }
                        if (position + NUM_COLUMNS * NUM_RAW > maxItem)
                            position = maxItem;
                        else
                            position = position + NUM_COLUMNS * NUM_RAW;
                        isDown = true;
                        resultList.smoothScrollToPosition(position);
                    } else if (upY - downY > MIN_MOVE) {
                        if (isDown) {
                            position = position - NUM_COLUMNS * NUM_RAW + 1;
                        }
                        if (position - NUM_COLUMNS * NUM_RAW < minItem)
                            position = minItem;
                        else
                            position = position - NUM_COLUMNS * NUM_RAW;
                        isDown = false;
                        resultList.smoothScrollToPosition(position);
                    }
                    if (Math.abs(upY - downY) < MIN_MOVE) {
                        return true;
                    } else {
                        return true;
                    }
                }
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    return true;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == Constant.KEY_RIGHT) {
            if (!isDown) {
                position = position + NUM_COLUMNS * NUM_RAW - 1;
            }
            if (position + NUM_COLUMNS * NUM_RAW > maxItem)
                position = maxItem;
            else
                position = position + NUM_COLUMNS * NUM_RAW;
            isDown = true;
            resultList.smoothScrollToPosition(position);
            return true;
        } else if (keyCode == Constant.KEY_LEFT) {
            if (isDown) {
                position = position - NUM_COLUMNS * NUM_RAW + 1;
            }
            if (position - NUM_COLUMNS * NUM_RAW < minItem)
                position = minItem;
            else
                position = position - NUM_COLUMNS * NUM_RAW;
            isDown = false;
            resultList.smoothScrollToPosition(position);
            return true;
        } else {
            return true;
        }
    }

    public static void actionStart(Context context, ArrayList<StopVoteRequest.VoteResult> list) {
        Intent intent = new Intent(context, VoteResultActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("votes", list);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

}
