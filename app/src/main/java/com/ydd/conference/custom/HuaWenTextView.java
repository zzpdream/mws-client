package com.ydd.conference.custom;

import android.app.Application;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.ydd.conference.config.AppApplication;

/**
 * Created by hehelt on 16/2/29.
 */
public class HuaWenTextView extends TextView {

    public static Typeface huaWen = Typeface.createFromAsset(AppApplication.mContext.getAssets(), "huawen.ttf");

    public HuaWenTextView(Context context) {
        super(context);
    }

    public HuaWenTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(huaWen);
    }

    public HuaWenTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
