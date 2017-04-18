package com.ydd.conference.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import com.ydd.conference.event.Event;
import com.ydd.conference.event.SecondaryLogoEvent;
import com.ydd.conference.event.ShowSecondEvent;
import com.ydd.conference.util.SharedPreferencesUtil;
import com.ydd.conference.util.StringUtils;

import de.greenrobot.event.EventBus;

/**
 * Created by hehelt on 16/2/26.
 */
public class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    protected SecondaryDisplay secondaryDisplay;

    protected SecondaryLogoDisplay secondaryLogoDisplay;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setBrightness(this, 150);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (null == secondaryDisplay) {
            secondaryDisplay = getSecondDisplay(this);
        }

        if (null == secondaryLogoDisplay) {
            secondaryLogoDisplay = getSecondaryLogoDisplay(this);
        }

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        //add by lt 解决onNewIntent的情况,次屏不刷新
        if (secondaryDisplay != null) {
            secondaryDisplay.showName(SharedPreferencesUtil.getMemberName());
        }
    }

    public void onEventMainThread(Event event) {
        if (event instanceof ShowSecondEvent) {
            if (null == secondaryDisplay) {
                return;
            }
            String content = ((ShowSecondEvent) event).getContent();
            if(StringUtils.isEmpty(content)){
                content = SharedPreferencesUtil.getMemberName();
            }
            secondaryDisplay.showName(content);

        } else if(event instanceof SecondaryLogoEvent){
            secondaryLogoDisplay.load(((SecondaryLogoEvent) event).getTitles());
        }
    }


    /**
     * 禁用系统的默认按键事件
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }


    /**
     * 显示双屏显示
     */
    public SecondaryDisplay getSecondDisplay(Activity displayContext) {
        SecondaryDisplay presentation;
        DisplayManager mDisplayManager;//屏幕管理类
        Display[] displays;//屏幕数组
        mDisplayManager = (DisplayManager) displayContext.getSystemService(Context.DISPLAY_SERVICE);
        displays = mDisplayManager.getDisplays();
        if (displays.length <= 1) {
            return null;
        }
        presentation = new SecondaryDisplay(displayContext, displays[1]);//displays[1]是副屏
        presentation.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY);
        presentation.getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        return presentation;
    }

    /**
     * 显示徽标双屏显示
     */
    public SecondaryLogoDisplay getSecondaryLogoDisplay(Activity displayContext) {
        SecondaryLogoDisplay presentation;
        DisplayManager mDisplayManager;//屏幕管理类
        Display[] displays;//屏幕数组
        mDisplayManager = (DisplayManager) displayContext.getSystemService(Context.DISPLAY_SERVICE);
        displays = mDisplayManager.getDisplays();
        if (displays.length <= 1) {
            return null;
        }
        presentation = new SecondaryLogoDisplay(displayContext, displays[1]);//displays[1]是副屏
        presentation.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY);
        presentation.getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        return presentation;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public static void setBrightness(Activity activity, int brightness) {

        // Settings.System.putInt(activity.getContentResolver(),

        // Settings.System.SCREEN_BRIGHTNESS_MODE,

        // Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);

        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();

        lp.screenBrightness = Float.valueOf(brightness) * (1f / 255f);
        Log.d("lxy", "set  lp.screenBrightness == " + lp.screenBrightness);

        activity.getWindow().setAttributes(lp);
    }
}
