package com.ydd.conference.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ydd.conference.ui.MainActivity;


/**
 * Created by hehelt on 16/1/25.
 */
public class BootCompletedReceiver extends BroadcastReceiver {

    private static final String ACTION = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION)) {
            Intent MyIntent = new Intent(context, MainActivity.class);
            MyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(MyIntent);
        }
    }

}
