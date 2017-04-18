package com.ydd.conference.entity;

import com.google.gson.annotations.Expose;

/**
 * Created by hehelt on 16/2/28.
 */
public class ChairmanRequest extends Message {

    @Expose
    public ChairmanParams params;

    public ChairmanParams getParams() {
        return params;
    }

    public void setParams(ChairmanParams params) {
        this.params = params;
    }

    public static class ChairmanParams {

        @Expose
        private String seatId;
        @Expose
        private String videoUrl;

        public String getSeatId() {
            return seatId;
        }

        public void setSeatId(String seatId) {
            this.seatId = seatId;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }
    }


}
