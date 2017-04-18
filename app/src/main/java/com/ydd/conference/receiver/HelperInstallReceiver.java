package com.ydd.conference.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ydd.conference.config.AppApplication;
import com.ydd.conference.util.InstallHelper;
import com.ydd.conference.util.ViewUtil;

/**
 * Created by hehelt on 16/3/8.
 */
public class HelperInstallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        final String packageName = intent.getData().getSchemeSpecificPart();
        if(packageName.equals(InstallHelper.HELPER_APP_PACKAGE_NAME)){
            ViewUtil.showToast("helper install");
            InstallHelper.startHelperApp(AppApplication.mContext);
        }

    }


}
