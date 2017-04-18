package com.ydd.conference.event;


import com.ydd.conference.config.Constant;

/**
 * Created by hehelt on 16/3/8.
 */
public class ScannerResultEvent implements Event {

    public ScannerResultEvent() {
    }

    public String cardNumber = "";

    public int status = Constant.STATUS_CARD_NONE;
}
