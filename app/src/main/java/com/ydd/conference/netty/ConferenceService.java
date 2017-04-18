package com.ydd.conference.netty;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.ydd.conference.event.Event;

import de.greenrobot.event.EventBus;


/**
 * Created by hehelt on 16/2/29.
 */
public class ConferenceService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        ConferenceClient conferenceClient = ConferenceClient.getInstance();
//        conferenceClient.setContext(this);
        conferenceClient.init();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //EventBus.getDefault().register(this);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

//    public void onEvent(Event event) {
//
//    }

}
