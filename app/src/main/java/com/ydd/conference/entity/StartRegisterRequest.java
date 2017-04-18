package com.ydd.conference.entity;

import com.google.gson.annotations.Expose;

/**
 * Created by hehelt on 16/2/29.
 */
public class StartRegisterRequest extends Message {

    @Expose
    private StartRegisterParams params;


    public StartRegisterParams getParams() {
        return params;
    }

    public void setParams(StartRegisterParams params) {
        this.params = params;
    }

    public static class StartRegisterParams {

        /**
         * seatId : 1
         * title : 上海市第十四届人大常委会第二十四次会议
         */
        @Expose
        private String seatId;
        @Expose
        private String title;

        public void setSeatId(String seatId) {
            this.seatId = seatId;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSeatId() {
            return seatId;
        }

        public String getTitle() {
            return title;
        }
    }

}
