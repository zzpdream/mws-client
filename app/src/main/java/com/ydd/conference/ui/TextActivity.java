package com.ydd.conference.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.TextView;

import com.ydd.conference.R;
import com.ydd.conference.config.AppApplication;
import com.ydd.conference.config.Command;
import com.ydd.conference.config.Constant;
import com.ydd.conference.entity.Message;
import com.ydd.conference.entity.VoteRequest;
import com.ydd.conference.event.Event;
import com.ydd.conference.event.VoteSuccessEvent;
import com.ydd.conference.netty.ConferenceClientHandler;
import com.ydd.conference.util.SharedPreferencesUtil;
import com.ydd.conference.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;


public class TextActivity extends BaseActivity {

    private TextView textView;

//    private TextView voteAgreeText;
//    private TextView voteAgainstText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
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
    }


    private void initView() {
        textView = (TextView) findViewById(R.id.textView);
        textView.setText(Constant.textTitle);
        if (Constant.textTitle == null)
            return;
        if (Constant.textTitle.length() > 15) {
            textView.setTextSize(getResources().getDimension(R.dimen.text_size_10));
            textView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        } else {
            textView.setTextSize(getResources().getDimension(R.dimen.text_size_11));
            textView.setGravity(Gravity.CENTER);
        }
//        voteAgreeText = (TextView) findViewById(R.id.voteAgreeText);
//        voteAgainstText = (TextView) findViewById(R.id.voteAgainstText);
//        voteAgreeText.setOnClickListener(this);
//        voteAgainstText.setOnClickListener(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 接受事件
     *
     * @param event
     */
    public void onEventMainThread(Event event) {
        super.onEventMainThread(event);
        if (event instanceof VoteSuccessEvent) {
            if (SharedPreferencesUtil.getStatus() == Command.VOTE.status &&
                    SharedPreferencesUtil.getVotingRights() == 1 &&
                    SharedPreferencesUtil.getRegisterStatus() == 1) {
                textView.setText("您已表决");
                ViewUtil.showToast("表决成功");
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (SharedPreferencesUtil.getStatus() == Command.START_VOTE.status || (Constant.isSingleVote && SharedPreferencesUtil.getStatus() == Command.VOTE.status)) {
            if (keyCode != Constant.KEY_AGAINST && keyCode != Constant.KEY_MISS && keyCode != Constant.KEY_AGREE) {
                return false;
            }

            //没有表决权的终端禁用按键
            //TODO by ranfi
            if (SharedPreferencesUtil.getVotingRights() == 0) {
                return false;
            }
            final int finalKeyCode = keyCode;
            AppApplication.executor.submit(new Runnable() {
                @Override
                public void run() {
                    VoteRequest.Vote vote = new VoteRequest.Vote();
                    if (finalKeyCode == Constant.KEY_AGAINST) {
                        vote.value = Constant.AGAINST;
                    } else if (finalKeyCode == Constant.KEY_AGREE) {
                        vote.value = Constant.AGREE;
                    } else if (finalKeyCode == Constant.KEY_MISS) {
                        vote.value = Constant.MISS;
                    }
//                    vote.item = Constant.textTitle;
                    vote.id = 1;
                    VoteRequest request = new VoteRequest();
                    request.setCmd(Command.VOTE.value);
                    request.setType(Message.TYPE_MESSAGE_REQUEST);
                    VoteRequest.VoteParams params = new VoteRequest.VoteParams();
                    params.setSeatId(SharedPreferencesUtil.getSeatId());
                    params.setType(Message.TYPE_SINGLE_VOTE);
                    List<VoteRequest.Vote> list = new ArrayList<>();
                    list.add(vote);
                    params.setVotes(list);
                    request.setParams(params);
                    if (ConferenceClientHandler.mCtx != null) {
                        ConferenceClientHandler.mCtx.writeAndFlush(request);
                    }
                }
            });
        }
        return super.onKeyDown(keyCode, event);
    }


    public static void actionStart(Context context) {
        Intent intent = new Intent(context, TextActivity.class);
        context.startActivity(intent);
    }

//    @Override
//    public void onClick(View v) {
//        final int id = v.getId();
//        AppApplication.executor.submit(new Runnable() {
//            @Override
//            public void run() {
//                if (SharedPreferencesUtil.getStatus() == Command.START_VOTE.status && !"已表决".equals(textView.getText())) {
//                    VoteRequest request = new VoteRequest();
//                    request.setCmd(Command.VOTE.value);
//                    request.setType(Message.TYPE_MESSAGE_REQUEST);
//                    VoteRequest.VoteParams params = new VoteRequest.VoteParams();
//                    params.setSeatId(SharedPreferencesUtil.getSeatId());
//                    params.setType(Message.TYPE_SINGLE_VOTE);
//                    List<VoteRequest.Vote> list = new ArrayList<>();
//                    VoteRequest.Vote vote = new VoteRequest.Vote();
//                    vote.item = Constant.textTitle;
//                    switch (id) {
//                        case R.id.voteAgreeText:
//                            vote.value = 1;
//                            list.add(vote);
//                            params.setVotes(list);
//                            break;
//
//                        case R.id.voteAgainstText:
//                            vote.value = 2;
//                            list.add(vote);
//                            params.setVotes(list);
//                            break;
//                    }
//                    request.setParams(params);
//                    if (ConferenceClientHandler.mCtx != null) {
//                        ConferenceClientHandler.mCtx.writeAndFlush(request);
//                    }
//                }
//            }
//        });
//    }
}
