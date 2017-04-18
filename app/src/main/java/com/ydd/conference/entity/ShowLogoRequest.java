package com.ydd.conference.entity;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by hehelt on 16/2/28.
 */
public class ShowLogoRequest extends Message {

    @Expose
    private ShowLogoParams params;

    public ShowLogoParams getParams() {
        return params;
    }

    public void setParams(ShowLogoParams params) {
        this.params = params;
    }

    public static class ShowLogoParams {
        /**
         * seatId : 1
         * titles : ["中华人员共和国","成立啦"]
         * picUrl :
         */
        @Expose
        private String seatId;
        @Expose
        private String picUrl;
        @Expose
        private List<String> titles;

        public void setSeatId(String seatId) {
            this.seatId = seatId;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public void setTitles(List<String> titles) {
            this.titles = titles;
        }

        public String getSeatId() {
            return seatId;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public List<String> getTitles() {
            return titles;
        }

    }


}
