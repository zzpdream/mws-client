package com.ydd.conference.event;


/**
 * Created by hehelt on 16/3/8.
 */
public class ConnectEvent implements Event {

    public ConnectEvent(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public boolean isConnected = false;
}
