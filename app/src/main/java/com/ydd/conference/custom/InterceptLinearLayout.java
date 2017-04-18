package com.ydd.conference.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by hehelt on 16/4/12.
 */
public class InterceptLinearLayout extends LinearLayout {

    private static final int MIN_MOVE = 10;

    private float downY;
    private float upY;

    private ScrollListener listener;

    public InterceptLinearLayout(Context context) {
        super(context);
    }

    public InterceptLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InterceptLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        System.out.println("action:" + event.getAction());

        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (upY - downY < -MIN_MOVE) {
                return true;
            } else if (upY - downY > MIN_MOVE) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
//        System.out.println("dispatch:" + event.getAction());
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            downY = event.getY();
        }
//        System.out.println("downY:" + downY);
        if (event.getAction() == MotionEvent.ACTION_UP) {
            upY = event.getY();
//            System.out.println("upY:" + upY);
            if (upY - downY < -MIN_MOVE) {
                if (listener != null) {
                    listener.scroll(true);
                }
            } else if (upY - downY > MIN_MOVE) {
                if (listener != null) {
                    listener.scroll(false);
                }
            }
        }

        return super.dispatchTouchEvent(event);
    }


    public void setScrollListener(ScrollListener listener) {
        this.listener = listener;
    }

    public interface ScrollListener {
        void scroll(boolean isDown);
    }

}
