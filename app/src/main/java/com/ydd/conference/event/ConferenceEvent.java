package com.ydd.conference.event;

import com.ydd.conference.config.EventType;

import java.util.Map;

/**
 * Created by ranfi on 4/11/16.
 */
public class ConferenceEvent implements Event {


    private EventType eventType;
    private Map<String, Object> params;

    public ConferenceEvent(EventType eventType, Map<String, Object> params) {
        this.eventType = eventType;
        this.params = params;
    }


    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
