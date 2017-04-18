package com.ydd.conference.entity;

import com.google.gson.annotations.Expose;

/**
 * Created by hehelt on 16/2/26.
 * <p/>
 * Request和Response
 */
public class Message {

    public static final int TYPE_SINGLE_VOTE = 0;//单项表决
    public static final int TYPE_VOTE = 1;//多项表决

    public static final String TYPE_MESSAGE_REQUEST = "req";
    public static final String TYPE_MESSAGE_RESPONSE = "res";

    public static final int STATUS_SUCCESS = 0;
    public static final int STATUS_COMMAND_ERROR = 100;
    public static final int STATUS_MAGIC_WORD_ERROR = 101;
    public static final int STATUS_UNKNOWN = 110;
    public static final int STATUS_SEAT_UN_EXIST = 200;
    public static final int STATUS_SEAT_UN_CONSISTENT = 201;
    public static final int STATUS_VOTE_UN_CONSISTENT = 300;

    public static final String MAGIC_WORD_ERROR = "magicWord error";

    @Expose
    private String type;
    @Expose
    private String cmd;
    //    @Expose
//    public Object params;
//
//    @Expose
//    public Object values;
    @Expose
    private int status;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

//    public Object getParams() {
//        return params;
//    }
//
//    public void setParams(Object object) {
//        this.params = object;
//    }
//
//    public Object getValues() {
//        return values;
//    }
//
//    public void setValues(Object values) {
//        this.values = values;
//    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
