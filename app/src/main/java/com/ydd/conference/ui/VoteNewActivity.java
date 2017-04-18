package com.ydd.conference.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;

import com.ydd.conference.R;
import com.ydd.conference.config.AppApplication;
import com.ydd.conference.config.Command;
import com.ydd.conference.config.Constant;
import com.ydd.conference.custom.CommonAdapter;
import com.ydd.conference.custom.InterceptLinearLayout;
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

public class VoteNewActivity extends BaseActivity {

    private static final int PAGE_SIZE = 16;
    private int mCurrentPage;
    private int mTotalPage;

    private TextView allText;
    private TextView voteText;
    //    private GridView nameGrid;
    private View submitLayout;
    private TextView submitText;
    private TextView cancelText;

    private TextView[] nameTextArray = new TextView[16];
    private int[] nameTextIdArray = {R.id.name1Text, R.id.name2Text, R.id.name3Text, R.id.name4Text, R.id.name5Text, R.id.name6Text, R.id.name7Text, R.id.name8Text, R.id.name9Text, R.id.name10Text, R.id.name11Text, R.id.name12Text, R.id.name13Text, R.id.name14Text, R.id.name15Text, R.id.name16Text};
    private FrameLayout[] nameLayoutArray = new FrameLayout[16];
    private int[] nameLayoutIdArray = {R.id.frameLayout1, R.id.frameLayout2, R.id.frameLayout3, R.id.frameLayout4, R.id.frameLayout5, R.id.frameLayout6, R.id.frameLayout7, R.id.frameLayout8, R.id.frameLayout9, R.id.frameLayout10, R.id.frameLayout11, R.id.frameLayout12, R.id.frameLayout13, R.id.frameLayout14, R.id.frameLayout15, R.id.frameLayout16};


    //    private CommonAdapter<VoteRequest.Vote> mAdapter;
    private List<VoteRequest.Vote> voteInfoEntities = new ArrayList<>();

    private boolean isAllSelected = true;
    private int oldSelectedPosition;

    private int votedNum;

    private List<Integer> stillList = new ArrayList<>();
    private int mResultSize;
    private InterceptLinearLayout resultLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_new);
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


    }

    public void onEventMainThread(Event event) {
        super.onEventMainThread(event);
        if (event instanceof VoteSuccessEvent) {
            if (SharedPreferencesUtil.getStatus() == Command.VOTE.status) {
                ViewUtil.showToast("表决成功");
                Constant.textTitle = "您已表决";
                TextActivity.actionStart(VoteNewActivity.this);
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
                mResultSize = list.size();
                mCurrentPage = 1;
                mTotalPage = mResultSize % PAGE_SIZE == 0 ? mResultSize / PAGE_SIZE : mResultSize / PAGE_SIZE + 1;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        voteText.setText("已表决: " + 0 + "/" + voteInfoEntities.size());
                        bindData();
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

        for (int i = 0; i < PAGE_SIZE; i++) {
            nameLayoutArray[i] = (FrameLayout) findViewById(nameLayoutIdArray[i]);
            nameTextArray[i] = (TextView) findViewById(nameTextIdArray[i]);
        }

        resultLayout = (InterceptLinearLayout) findViewById(R.id.resultLayout);
        resultLayout.setScrollListener(new InterceptLinearLayout.ScrollListener() {
            @Override
            public void scroll(boolean isDown) {
                changePage(isDown);
            }
        });
        resultLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

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

    private void bindData() {
        int start = (mCurrentPage - 1) * PAGE_SIZE;
        start = start < 0 ? 0 : start;
        for (int i = 0; i < PAGE_SIZE; i++) {
            int position = start + i;
            if (position < mResultSize) {
                nameLayoutArray[i].setVisibility(View.VISIBLE);
                VoteRequest.Vote item = voteInfoEntities.get(position);
                Drawable drawable;
                //赞成
//                if (item.getValue() == Constant.AGREE) {
//                    drawable = getResources().getDrawable(R.mipmap.vote_agree);
//                } else if (item.getValue() == Constant.AGAINST) {
//                    drawable = getResources().getDrawable(R.mipmap.vote_against);
//                } else if (item.getValue() == Constant.MISS) {
//                    drawable = getResources().getDrawable(R.mipmap.vote_miss);
//                } else {
//                    drawable = null;
//                }
//                nameTextArray[i].setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);

                if (!item.isVoted) {
                    if (item.isSelected == VoteRequest.Vote.STATUS_SELECTED) {
                        nameLayoutArray[i].setBackground(getResources().getDrawable(R.drawable.bg_rect_stroke_white));
                        nameTextArray[i].setTextColor(getResources().getColor(R.color.black));
                    } else {
                        nameLayoutArray[i].setBackground(getResources().getDrawable(R.drawable.bg_rect_white));
                        nameTextArray[i].setTextColor(getResources().getColor(R.color.black));
                    }
                } else {
                    if (item.isSelected == VoteRequest.Vote.STATUS_SELECTED) {
                        nameLayoutArray[i].setBackground(getResources().getDrawable(R.drawable.bg_rect_stroke_blue));
                        nameTextArray[i].setTextColor(getResources().getColor(R.color.grey));
                    } else {
                        nameLayoutArray[i].setBackground(getResources().getDrawable(R.drawable.bg_rect_blue));
                        nameTextArray[i].setTextColor(getResources().getColor(R.color.grey));
                    }
                }
                nameTextArray[i].setText(position + 1 + "." + item.item);
                nameLayoutArray[i].setTag(Integer.valueOf(position));
                nameLayoutArray[i].setOnClickListener(this);
            } else {
                nameTextArray[i].setText("");
                nameLayoutArray[i].setOnClickListener(null);
                nameLayoutArray[i].setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Object tag = v.getTag();
        if (tag != null && tag instanceof Integer) {
            Integer position = (Integer) v.getTag();
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
                }
            }
            bindData();
            return;
        }
        switch (v.getId()) {
            case R.id.allText:
                if (!isAllSelected) {
                    isAllSelected = true;
                    allText.setBackground(getResources().getDrawable(R.drawable.bg_rect_stroke_white));
                    for (int i = 0; i < voteInfoEntities.size(); i++) {
                        voteInfoEntities.get(i).isSelected = VoteRequest.Vote.STATUS_UN_SELECTED;
                    }
                    bindData();
                }
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
                bindData();
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
                        bindData();
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
            changePage(true);
            return true;
        } else if (keyCode == Constant.KEY_LEFT) {
            changePage(false);
            return true;
        } else {
            return true;
        }
        submitToServer(value);

        return true;
    }

    public static void actionStart(Context context) {

        Intent intent = new Intent(context, VoteNewActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
