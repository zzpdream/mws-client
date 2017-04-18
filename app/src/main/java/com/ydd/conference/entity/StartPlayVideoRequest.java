package com.ydd.conference.entity;

import com.google.gson.annotations.Expose;

/**
 * Created by hehelt on 16/7/6.
 */
public class StartPlayVideoRequest extends Message{


    /**
     * seatId : 1
     * url : rtsp://x.x.x.x/stream
     * hardware : false
     * x : 0.0
     * y : 0.0
     * w : 1.0
     * h : 0.9
     */

    @Expose
    public Params params;

    public static class Params {
        @Expose
        public String seatId;
        @Expose
        public String url;
        @Expose
        public String hardware;
        @Expose
        public String x;
        @Expose
        public String y;
        @Expose
        public String w;
        @Expose
        public String h;

        public void setSeatId(String seatId) {
            this.seatId = seatId;
        }

        public String getSeatId() {
            return seatId;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getHardware() {
            return hardware;
        }

        public void setHardware(String hardware) {
            this.hardware = hardware;
        }

        public String getX() {
            return x;
        }

        public void setX(String x) {
            this.x = x;
        }

        public String getY() {
            return y;
        }

        public void setY(String y) {
            this.y = y;
        }

        public String getW() {
            return w;
        }

        public void setW(String w) {
            this.w = w;
        }

        public String getH() {
            return h;
        }

        public void setH(String h) {
            this.h = h;
        }
    }

    public Params getParams() {
        return params;
    }

    public void setParams(Params params) {
        this.params = params;
    }
}
