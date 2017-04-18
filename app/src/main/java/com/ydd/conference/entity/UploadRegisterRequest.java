package com.ydd.conference.entity;

import com.google.gson.annotations.Expose;

/**
 * Created by hehelt on 16/2/29.
 */
public class UploadRegisterRequest extends Message {

    @Expose
    private UploadRegisterParams params;

    public UploadRegisterParams getParams() {
        return params;
    }

    public void setParams(UploadRegisterParams params) {
        this.params = params;
    }

    public static class UploadRegisterParams {
        @Expose
        private String seatId;
        @Expose
        private int status;

        public void setSeatId(String seatId) {
            this.seatId = seatId;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getSeatId() {
            return seatId;
        }

        public int getStatus() {
            return status;
        }
    }

}
