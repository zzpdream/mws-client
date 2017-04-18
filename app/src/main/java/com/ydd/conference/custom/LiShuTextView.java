package com.ydd.conference.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.ydd.conference.config.AppApplication;

/**
 * Created by hehelt on 16/2/29.
 */
public class LiShuTextView extends TextView {

    public static Typeface liShu = Typeface.createFromAsset(AppApplication.mContext.getAssets(), "lishu.ttf");


    public LiShuTextView(Context context) {
        super(context);
    }

    public LiShuTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(liShu);
    }

    public LiShuTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


}
