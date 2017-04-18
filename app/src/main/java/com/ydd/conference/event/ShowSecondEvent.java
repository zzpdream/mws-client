package com.ydd.conference.event;

import de.greenrobot.event.EventBus;

/**
 * Created by ranfi on 4/5/16.
 */
public class ShowSecondEvent implements Event {

    private String content;


    public ShowSecondEvent(){

    }

    public ShowSecondEvent(String content){
        this.content = content;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
