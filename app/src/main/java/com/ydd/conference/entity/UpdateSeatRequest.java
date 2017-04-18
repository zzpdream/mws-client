package com.ydd.conference.entity;

import com.google.gson.annotations.Expose;

/**
 * Created by hehelt on 16/2/28.
 */
public class UpdateSeatRequest extends Message {

    @Expose
    public UpdateSeatParams params;

    public UpdateSeatParams getParams() {
        return params;
    }

    public void setParams(UpdateSeatParams params) {
        this.params = params;
    }

    public static class UpdateSeatParams {
        /**
         * seatId : 1
         * personName : jack
         * personId : 1
         * vote : 0
         * cardId1 : 11122333
         * cardId2 : 99999999
         */
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
