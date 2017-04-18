package com.ydd.conference.entity;

import com.google.gson.annotations.Expose;

/**
 * Created by zhushouwen on 2017/3/24.
 */

public class ImageRequest {
    @Expose
    public Params params;
    public static class Params{
        @Expose
        public String seatId;
        @Expose
        private String imageUrl;

        public String getSeatId() {
            return seatId;
        }

        public void setSeatId(String seatId) {
            this.seatId = seatId;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }
}
