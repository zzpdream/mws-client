package com.ydd.conference.entity;

import com.google.gson.annotations.Expose;

/**
 * Created by hehelt on 16/2/26.
 * 心跳请求
 */
public class InitSeatRequest extends Message {


    @Expose
    public InitSeatParams params;

    public InitSeatParams getParams() {
        return params;
    }

    public void setParams(InitSeatParams params) {
        this.params = params;
    }

    public static class InitSeatParams {
        @Expose
        private String seatId;

        @Expose
        private String terminalType;


        public String getSeatId() {
            return seatId;
        }

        public void setSeatId(String seatId) {
            this.seatId = seatId;
        }


        public String getTerminalType() {
            return terminalType;
        }

        public void setTerminalType(String terminalType) {
            this.terminalType = terminalType;
        }

    }


}
