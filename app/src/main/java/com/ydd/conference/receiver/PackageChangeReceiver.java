package com.ydd.conference.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ydd.conference.ui.MainActivity;
import com.ydd.conference.util.ViewUtil;

/**
 * Created by hehelt on 16/3/8.
 */
public class PackageChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        final String action = intent.getStringExtra("action");

        boolean replace = intent.getBooleanExtra("replace", false);
//        ViewUtil.showToast("receiver");
        if (action.equals(Intent.ACTION_PACKAGE_ADDED)) {
            Intent intent1 = new Intent(context, MainActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        }

    }

}
