package com.ydd.conference.event;

import java.util.List;

/**
 * Created by peakren on 7/17/16.
 */
public class SecondaryLogoEvent implements Event {

    private List<String> titles;

    public SecondaryLogoEvent(){

    }

    public SecondaryLogoEvent(List<String> titles){
        this.titles=titles;
    }

    public List<String> getTitles() {
        return titles;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }
}
