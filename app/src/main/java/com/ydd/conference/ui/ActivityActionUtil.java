package com.ydd.conference.ui;

import android.content.Context;

import com.ydd.conference.event.SecondaryLogoEvent;
import com.ydd.conference.event.ShowSecondEvent;
import com.ydd.conference.util.SharedPreferencesUtil;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by ranfi on 4/15/16.
 */
public class ActivityActionUtil {


    /**
     * 跳转到name控制器
     *
     * @param context
     */

    public static void jumpNameActivity(Context context) {
        if (!SharedPreferencesUtil.getSeatId().equals(SharedPreferencesUtil.getChairSeatId())) {
            NameActivity.actionStart(context);
        } else {
            EventBus.getDefault().post(new ShowSecondEvent());
        }
    }

    /**
     * 根据终端匿名权限跳转显示徽标控制器,两个屏显示同样内容
     *
     * @param contents 显示内容
     */
    public static void showLogoActivityByAnonAuth(Context context, List<String> contents){
//        EventBus.getDefault().post(new SecondaryLogoEvent(contents));
        LogoActivity.actionStart(context, contents);
    }

}
