package com.ydd.conference.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;

import com.ydd.conference.R;
import com.ydd.conference.config.AppApplication;
import com.ydd.conference.config.Command;
import com.ydd.conference.config.Constant;
import com.ydd.conference.custom.CommonAdapter;
import com.ydd.conference.custom.ViewHolder;
import com.ydd.conference.entity.Message;
import com.ydd.conference.entity.StartVoteRequest;
import com.ydd.conference.entity.StopVoteRequest;
import com.ydd.conference.entity.VoteRequest;
import com.ydd.conference.event.Event;
import com.ydd.conference.event.VoteSuccessEvent;
import com.ydd.conference.netty.ConferenceClientHandler;
import com.ydd.conference.util.SharedPreferencesUtil;
import com.ydd.conference.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class VoteActivity extends BaseActivity {

    private TextView allText;
    private TextView voteText;
    private GridView nameGrid;
    private View submitLayout;
    private TextView submitText;
    private TextView cancelText;

    private CommonAdapter<VoteRequest.Vote> mAdapter;
    private List<VoteRequest.Vote> voteInfoEntities = new ArrayList<>();

    private boolean isAllSelected = true;
    private int oldSelectedPosition;


    private static final int MIN_MOVE = 10;
    private static final int NUM_COLUMNS = 4;
    private static final int NUM_RAW = 4;

    private float downY;
    private float upY;
    private int position = 0;
    private boolean isDown = false;
    private int maxItem = 99;
    private int minItem = 0;

    private int votedNum;

    private List<Integer> stillList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);
        initData();
        initView();
        initList();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initData();
        initView();
        initList();
    }


    private void initData() {
        voteInfoEntities = new ArrayList<>();
        isAllSelected = true;
        oldSelectedPosition = -1;
        position = 0;
        isDown = false;
        minItem = 0;


    }

    public void onEventMainThread(Event event) {
        super.onEventMainThread(event);
        if (event instanceof VoteSuccessEvent) {
            if (SharedPreferencesUtil.getStatus() == Command.VOTE.status) {
                ViewUtil.showToast("表决成功");
                Constant.textTitle = "已表决";
                TextActivity.actionStart(VoteActivity.this);
            }
        }
    }

    private void initList() {
        AppApplication.executor.submit(new Runnable() {
            @Override
            public void run() {
                List<StartVoteRequest.VoteItem> list = Constant.list;
                if (list != null) {
                    voteInfoEntities.clear();
                    for (int i = 0; i < list.size(); i++) {
                        StartVoteRequest.VoteItem item = list.get(i);
                        voteInfoEntities.add(new VoteRequest.Vote(item.getTitle(), item.getId()));
                    }
                }
                maxItem = list.size();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        voteText.setText("已表决: " + 0 + "/" + voteInfoEntities.size());
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    private void initView() {
        submitLayout = findViewById(R.id.submitLayout);
        submitLayout.setVisibility(View.GONE);
        submitLayout.setOnClickListener(this);
        submitText = (TextView) findViewById(R.id.submitText);
        cancelText = (TextView) findViewById(R.id.cancelText);
        submitText.setOnClickListener(this);
        cancelText.setOnClickListener(this);
        voteText = (TextView) findViewById(R.id.voteText);
        voteText.setOnClickListener(this);
        voteText.setText("已表决: " + 0 + "/" + voteInfoEntities.size());
        allText = (TextView) findViewById(R.id.allText);
        allText.setBackground(getResources().getDrawable(R.drawable.bg_rect_stroke_white));
        allText.setOnClickListener(this);
        nameGrid = (GridView) findViewById(R.id.nameGrid);
        mAdapter = new CommonAdapter<VoteRequest.Vote>(this, voteInfoEntities, R.layout.item_vote) {
            @Override
            public void convertView(ViewHolder viewHolder, VoteRequest.Vote item, int position) {
                TextView nameText = viewHolder.getView(R.id.nameText);
                FrameLayout frameLayout = viewHolder.getView(R.id.frameLayout);
                Drawable drawable;
                //赞成
                if (item.getValue() == Constant.AGREE) {
                    drawable = getResources().getDrawable(R.mipmap.vote_agree);
                } else if (item.getValue() == Constant.AGAINST) {
                    drawable = getResources().getDrawable(R.mipmap.vote_against);
                } else if (item.getValue() == Constant.MISS) {
                    drawable = getResources().getDrawable(R.mipmap.vote_miss);
                } else {
                    drawable = null;
                }
                nameText.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);

                if (!item.isVoted) {
                    if (item.isSelected == VoteRequest.Vote.STATUS_SELECTED) {
                        frameLayout.setBackground(getResources().getDrawable(R.drawable.bg_rect_stroke_white));
                        nameText.setTextColor(getResources().getColor(R.color.black));
                    } else {
                        frameLayout.setBackground(getResources().getDrawable(R.drawable.bg_rect_white));
                        nameText.setTextColor(getResources().getColor(R.color.black));
                    }
                } else {
                    if (item.isSelected == VoteRequest.Vote.STATUS_SELECTED) {
                        frameLayout.setBackground(getResources().getDrawable(R.drawable.bg_rect_stroke_blue));
                        nameText.setTextColor(getResources().getColor(R.color.white));
                    } else {
                        frameLayout.setBackground(getResources().getDrawable(R.drawable.bg_rect_blue));
                        nameText.setTextColor(getResources().getColor(R.color.white));
                    }
                }
                viewHolder.setTextView(R.id.nameText, item.item);
            }
        };
        nameGrid.setAdapter(mAdapter);
        nameGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VoteRequest.Vote item = voteInfoEntities.get(position);
                if (isAllSelected) {
                    isAllSelected = false;
                    allText.setBackground(getResources().getDrawable(R.drawable.bg_rect_white));
                    if (oldSelectedPosition >= 0 && oldSelectedPosition < voteInfoEntities.size()) {
                        voteInfoEntities.get(oldSelectedPosition).isSelected = VoteRequest.Vote.STATUS_UN_SELECTED;
                    }
                    voteInfoEntities.get(position).isSelected = VoteRequest.Vote.STATUS_SELECTED;

                    oldSelectedPosition = position;
                } else {
                    if (item.isSelected == VoteRequest.Vote.STATUS_UN_SELECTED) {
                        if (oldSelectedPosition >= 0 && oldSelectedPosition < voteInfoEntities.size()) {
                            voteInfoEntities.get(oldSelectedPosition).isSelected = VoteRequest.Vote.STATUS_UN_SELECTED;
                        }
                        item.isSelected = VoteRequest.Vote.STATUS_SELECTED;
                        oldSelectedPosition = position;
                    } else if (item.value == VoteRequest.Vote.STATUS_SELECTED) {
                        return;
//                        item.value = VoteRequest.Vote.STATUS_UN_SELECTED;
//                        oldSelectedPosition = -1;
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        });
        nameGrid.setOnTouchListener(new View.OnTouchListener() {
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
                        nameGrid.smoothScrollToPosition(position);
                    } else if (upY - downY > MIN_MOVE) {
                        if (isDown) {
                            position = position - NUM_COLUMNS * NUM_RAW + 1;
                        }
                        if (position - NUM_COLUMNS * NUM_RAW < minItem)
                            position = minItem;
                        else
                            position = position - NUM_COLUMNS * NUM_RAW;
                        isDown = false;
                        nameGrid.smoothScrollToPosition(position);
                    }
                    if (Math.abs(upY - downY) < MIN_MOVE) {
                        return false;
                    } else {
                        return true;
                    }
                }
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.allText:
                if (!isAllSelected) {
                    isAllSelected = true;
                    allText.setBackground(getResources().getDrawable(R.drawable.bg_rect_stroke_white));
                    for (int i = 0; i < voteInfoEntities.size(); i++) {
                        voteInfoEntities.get(i).isSelected = VoteRequest.Vote.STATUS_UN_SELECTED;
                    }
                    mAdapter.notifyDataSetChanged();
                }
                break;

            case R.id.voteText:
//                submitToServer(1);
                break;

            case R.id.submitText:
                VoteRequest voteRequest = new VoteRequest();
                voteRequest.setCmd(Command.VOTE.value);
                voteRequest.setType(Message.TYPE_MESSAGE_REQUEST);
                VoteRequest.VoteParams params = new VoteRequest.VoteParams();
                params.setType(Message.TYPE_VOTE);
                params.setVotes(voteInfoEntities);
                params.setSeatId(SharedPreferencesUtil.getSeatId());
                voteRequest.setParams(params);
                ConferenceClientHandler.mCtx.writeAndFlush(voteRequest);
                break;
            case R.id.cancelText:
                for (int i = 0; i < stillList.size(); i++) {
                    VoteRequest.Vote item = voteInfoEntities.get(stillList.get(i));
                    item.value = 0;
                    item.isVoted = false;
                }
                voteText.setText("已表决: " + (votedNum - stillList.size()) + "/" + voteInfoEntities.size());
                mAdapter.notifyDataSetChanged();
                submitLayout.setVisibility(View.GONE);
                break;
        }
    }


    private void submitToServer(final int value) {
        AppApplication.executor.submit(new Runnable() {
            @Override
            public void run() {
                votedNum = 0;
                stillList.clear();
                for (int i = 0; i < voteInfoEntities.size(); i++) {
                    VoteRequest.Vote item = voteInfoEntities.get(i);
                    if (isAllSelected) {
                        if (!item.isVoted) {
                            item.isVoted = true;
                            item.value = value;
                            item.isSelected = VoteRequest.Vote.STATUS_UN_SELECTED;
                            stillList.add(i);
                        }
                    } else {
                        if (item.isSelected == VoteRequest.Vote.STATUS_SELECTED) {
                            item.isVoted = true;
                            item.value = value;
                            item.isSelected = VoteRequest.Vote.STATUS_UN_SELECTED;
                            stillList.add(i);
                        }
                    }

                    if (item.isVoted) {
                        votedNum++;
                    }
                }
                isAllSelected = true;
                final int finalVotedNum = votedNum;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        allText.setBackground(getResources().getDrawable(R.drawable.bg_rect_stroke_white));
                        if (stillList.size() != voteInfoEntities.size()) {
                            allText.setText("余下项");
                        }
                        voteText.setText("已表决: " + finalVotedNum + "/" + voteInfoEntities.size());
                        mAdapter.notifyDataSetChanged();
                        if (finalVotedNum == voteInfoEntities.size()) {
                            submitLayout.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (ConferenceClientHandler.mCtx == null) {
            ViewUtil.showToast("未连接上服务器,请联系管理人员");
            return super.onKeyDown(keyCode, event);
        }
        if (submitLayout.getVisibility() == View.VISIBLE) {
            return true;
        }
        int value;
        if (keyCode == Constant.KEY_AGAINST) {
            value = Constant.AGAINST;
        } else if (keyCode == Constant.KEY_AGREE) {
            value = Constant.AGREE;
        } else if (keyCode == Constant.KEY_MISS) {
            value = Constant.MISS;
        } else if (keyCode == Constant.KEY_RIGHT) {
            if (!isDown) {
                position = position + NUM_COLUMNS * NUM_RAW - 1;
            }
            if (position + NUM_COLUMNS * NUM_RAW > maxItem)
                position = maxItem;
            else
                position = position + NUM_COLUMNS * NUM_RAW;
            isDown = true;
            nameGrid.smoothScrollToPosition(position);
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
            nameGrid.smoothScrollToPosition(position);
            return true;
        } else {
            return true;
        }
        submitToServer(value);

        return true;
    }

    public static void actionStart(Context context) {

        Intent intent = new Intent(context, VoteActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
