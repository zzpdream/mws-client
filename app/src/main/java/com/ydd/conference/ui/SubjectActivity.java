package com.ydd.conference.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.ydd.conference.R;
import com.ydd.conference.custom.HuaWenTextView;
import com.ydd.conference.entity.ShowSubjectRequest;
import com.ydd.conference.event.Event;
import com.ydd.conference.util.StringUtils;

/**
 * 会议议程的UI界面实现
 * <p>
 * Created by ranfi on 4/5/16.
 */
public class SubjectActivity extends BaseActivity {

    private TextView subjectTextView;
    private String subject;
    private String horizontal;
    private String vertical;

    private static Paint textPaint;


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_subject);

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
        subject = getIntent().getStringExtra("subject");
        horizontal = getIntent().getStringExtra("horizontal");
        vertical = getIntent().getStringExtra("vertical");
    }

    /**
     * 设置text view的水平和垂直的位置属性
     * horizontal: left,center,right
     * vertical: top,center,bottom
     */
    private void setTextProperties() {
        if (StringUtils.isNotEmpty(horizontal) && StringUtils.isNotEmpty(vertical)) {
            if (horizontal.equalsIgnoreCase("left") && vertical.equalsIgnoreCase("top")) {
                subjectTextView.setGravity(Gravity.LEFT | Gravity.TOP);
                return;
            }
            if (horizontal.equalsIgnoreCase("left") && vertical.equalsIgnoreCase("center")) {
                subjectTextView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                return;
            }

            if (horizontal.equalsIgnoreCase("left") && vertical.equalsIgnoreCase("bottom")) {
                subjectTextView.setGravity(Gravity.LEFT | Gravity.BOTTOM);
                return;
            }
            if (horizontal.equalsIgnoreCase("center") && vertical.equalsIgnoreCase("top")) {
                subjectTextView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
                return;
            }
            if (horizontal.equalsIgnoreCase("center") && vertical.equalsIgnoreCase("center")) {
                subjectTextView.setGravity(Gravity.CENTER);
                return;
            }
            if (horizontal.equalsIgnoreCase("center") && vertical.equalsIgnoreCase("bottom")) {
                subjectTextView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
                return;
            }

            if (horizontal.equalsIgnoreCase("right") && vertical.equalsIgnoreCase("top")) {
                subjectTextView.setGravity(Gravity.RIGHT | Gravity.TOP);
                return;
            }
            if (horizontal.equalsIgnoreCase("right") && vertical.equalsIgnoreCase("center")) {
                subjectTextView.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
                return;
            }
            if (horizontal.equalsIgnoreCase("right") && vertical.equalsIgnoreCase("bottom")) {
                subjectTextView.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
                return;
            }

        }
    }


    private void initView() {
        subjectTextView = (TextView) findViewById(R.id.subjectTextView);
        subjectTextView.setVisibility(View.INVISIBLE);
        subjectTextView.setText(subject);
        setTextProperties();
        subjectTextView.setTextSize(getResources().getDimension(R.dimen.text_subject));

        WindowManager wm = this.getWindowManager();
        final int height = wm.getDefaultDisplay().getHeight();
//        final int width = wm.getDefaultDisplay().getWidth();
//
        if (textPaint == null) {
            textPaint = new TextPaint();
            textPaint.setTypeface(HuaWenTextView.huaWen);
        }

        subjectTextView.post(new Runnable() {
            @Override
            public void run() {
//                int textHeight = subjectTextView.getLineCount() * subjectTextView.getLineHeight();
//                if (textHeight > height) {
//                    int textSize = 0;
//                    float fact = textHeight * 1.0f / height;
//                    if (fact > 1.0 && fact <= 1.5) {
//                        textSize = 55;
//                    } else if (fact > 1.5 && fact <= 2.0f) {
//                        textSize = 45;
//                    } else if (fact > 2.0 && fact <= 3.0f) {
//                        textSize = 35;
//                    } else {
//                        textSize = 24;
//                    }
//                    subjectTextView.setTextSize(textSize);
//                }
                subjectTextView.setTextSize(getTextSize());
                subjectTextView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onEventMainThread(Event event) {


    }

    /**
     * 启动自己
     *
     * @param context
     */
    public static void actionStart(Context context, ShowSubjectRequest.ShowSubjectParams subjectParams) {
        Intent intent = new Intent(context, SubjectActivity.class);
        intent.putExtra("subject", subjectParams.getSubject());
        intent.putExtra("horizontal", subjectParams.getHorizontal());
        intent.putExtra("vertical", subjectParams.getVertical());
        context.startActivity(intent);
    }

    /**
     * 启动自己
     *
     * @param context
     */
    public static void actionStart(Context context, String subject, String horizontal, String vertical) {
        Intent intent = new Intent(context, SubjectActivity.class);
        intent.putExtra("subject", subject);
        intent.putExtra("horizontal", horizontal);
        intent.putExtra("vertical", vertical);
        context.startActivity(intent);
    }

    public int getTextSize() {
        WindowManager wm = this.getWindowManager();
        final int height = wm.getDefaultDisplay().getHeight();
        final int width = wm.getDefaultDisplay().getWidth();
        int textSize = (int) getResources().getDimension(R.dimen.text_subject);
        textPaint.setTextSize(textSize);
        double lineHeight = 0;
        while (true) {
            if (textSize <= 16) {
                break;
            }

            int lineCount = (int) ((textPaint.measureText(subject) + width - 1) / width);
            System.out.println("height:" + height);
            System.out.println("line:" + lineCount);
            Paint.FontMetrics fm = textPaint.getFontMetrics();
            lineHeight = Math.ceil(fm.descent - fm.top);
            int textHeight = (int) (lineHeight * lineCount);
            System.out.print("lineHeight:" + lineHeight);
            System.out.println("textSize:" + textSize);
            if (textHeight > height - lineHeight / 2) {
                textSize = textSize - 8;
                textPaint.setTextSize(textSize);
            } else {
                break;
            }

        }
        return textSize;
    }

}
