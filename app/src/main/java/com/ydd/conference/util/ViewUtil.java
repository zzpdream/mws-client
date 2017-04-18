package com.ydd.conference.util;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.ydd.conference.config.AppApplication;

/**
 * Created by hehelt on 15/12/17.
 *
 * UI工具类
 */
public class ViewUtil {

    /**
     * 弹出框
     * @param message
     */
    public static void showToast(String message) {

        Toast.makeText(AppApplication.mContext, message, Toast.LENGTH_SHORT).show();
    }


    /**
     * 隐藏键盘
     *
     * @param mContext
     */
    public static void hideSystemKeyBoard(Context mContext) {
        if (mContext != null && ((Activity) mContext).getCurrentFocus() != null && ((Activity) mContext).getCurrentFocus().getWindowToken() != null)
            ((InputMethodManager) mContext.getSystemService(mContext.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(((Activity) mContext).getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }





}
