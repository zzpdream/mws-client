package com.ydd.conference.entity;

import com.google.gson.annotations.Expose;

/**
 * Created by hehelt on 16/2/26.
 * 心跳返回
 */
public class HeartBeatResponse extends Message {

    @Expose
    private HeartBeatValue values;

    public HeartBeatValue getValues() {
        return values;
    }

    public void setValues(HeartBeatValue values) {
        this.values = values;
    }

    public static class HeartBeatValue {
        @Expose
        private int versionCode;

        @Expose
        private String downloadUrl;
        @Expose
        private String picZipUrl;
        @Expose
        private String seatId;
        @Expose
        private String terminalType;

        public String getTerminalType() {
            return terminalType;
        }

        public void setTerminalType(String terminalType) {
            this.terminalType = terminalType;
        }

        public String getSeatId() {
            return seatId;
        }

        public void setSeatId(String seatId) {
            this.seatId = seatId;
        }

        public String getPicZipUrl() {
            return picZipUrl;
        }

        public void setPicZipUrl(String picZipUrl) {
            this.picZipUrl = picZipUrl;
        }

        public int getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(int versionCode) {
            this.versionCode = versionCode;
        }

        public String getDownloadUrl() {
//            downloadUrl = downloadUrl.replace("image","www");
            return downloadUrl;
        }

        public void setDownloadUrl(String downloadUrl) {
            this.downloadUrl = downloadUrl;
        }
    }


}
