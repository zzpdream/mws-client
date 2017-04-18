package com.ydd.conference.entity;

import com.google.gson.annotations.Expose;

/**
 * Created by hehelt on 16/2/26.
 * 心跳请求
 */
public class HeartBeatRequest extends Message{


    @Expose
    public HeartBeatParams params;

    public HeartBeatParams getParams() {
        return params;
    }

    public void setParams(HeartBeatParams params) {
        this.params = params;
    }

    public static class HeartBeatParams{
        @Expose
        private String seatId;
        @Expose
        private int register;
        @Expose
        private String terminalType;
        @Expose
        private int status;
        @Expose
        private int versionCode;
        @Expose
        private String appVersion;
        @Expose
        private String picZipMd5;
        @Expose
        private int votingRights;
        @Expose
        private String memberId;

        public String getMemberId() {
            return memberId;
        }

        public void setMemberId(String memberId) {
            this.memberId = memberId;
        }

        public int getVotingRights() {
            return votingRights;
        }

        public void setVotingRights(int votingRights) {
            this.votingRights = votingRights;
        }

        public String getPicZipMd5() {
            return picZipMd5;
        }

        public void setPicZipMd5(String picZipMd5) {
            this.picZipMd5 = picZipMd5;
        }

        public String getSeatId() {
            return seatId;
        }

        public void setSeatId(String seatId) {
            this.seatId = seatId;
        }

        public int getRegister() {
            return register;
        }

        public void setRegister(int register) {
            this.register = register;
        }

        public String getTerminalType() {
            return terminalType;
        }

        public void setTerminalType(String terminalType) {
            this.terminalType = terminalType;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(int versionCode) {
            this.versionCode = versionCode;
        }

        public String getAppVersion() {
            return appVersion;
        }

        public void setAppVersion(String appVersion) {
            this.appVersion = appVersion;
        }
    }



}
