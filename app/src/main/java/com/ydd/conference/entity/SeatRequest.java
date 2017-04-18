package com.ydd.conference.entity;

import com.google.gson.annotations.Expose;

/**
 * Created by hehelt on 16/2/29.
 */
public class SeatRequest extends Message {


    @Expose
    private SeatParams params;

    public SeatParams getParams() {
        return params;
    }

    public void setParams(SeatParams params) {
        this.params = params;
    }

    public static class SeatParams {
        /**
         * seatId : 1
         */

        @Expose
        private String seatId;

        public void setSeatId(String seatId) {
            this.seatId = seatId;
        }

        public String getSeatId() {
            return seatId;
        }
    }


}
