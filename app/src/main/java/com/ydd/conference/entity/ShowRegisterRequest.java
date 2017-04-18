package com.ydd.conference.entity;

import com.google.gson.annotations.Expose;

/**
 * Created by hehelt on 16/2/29.
 */
public class ShowRegisterRequest extends Message {


    @Expose
    private ShowRegisterParams params;


    public ShowRegisterParams getParams() {
        return params;
    }

    public void setParams(ShowRegisterParams params) {
        this.params = params;
    }

    /**
     * seatId : 1
     * expected : 100
     * registered : 98
     * leave : 1
     * absent : 1
     */


    public static class ShowRegisterParams {
        @Expose
        private String seatId;
        @Expose
        private String expected;
        @Expose
        private String registered;
        @Expose
        private String leave;
        @Expose
        private String absent;

        public void setSeatId(String seatId) {
            this.seatId = seatId;
        }

        public void setExpected(String expected) {
            this.expected = expected;
        }

        public void setRegistered(String registered) {
            this.registered = registered;
        }

        public void setLeave(String leave) {
            this.leave = leave;
        }

        public void setAbsent(String absent) {
            this.absent = absent;
        }

        public String getSeatId() {
            return seatId;
        }

        public String getExpected() {
            return expected;
        }

        public String getRegistered() {
            return registered;
        }

        public String getLeave() {
            return leave;
        }

        public String getAbsent() {
            return absent;
        }
    }


}
