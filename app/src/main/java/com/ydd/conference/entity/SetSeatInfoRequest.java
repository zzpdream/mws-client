package com.ydd.conference.entity;

import com.google.gson.annotations.Expose;

/**
 * Created by hehelt on 16/2/28.
 */
public class SetSeatInfoRequest extends Message {

    @Expose
    public SetSeatInfoParams params;

    public SetSeatInfoParams getParams() {
        return params;
    }

    public void setParams(SetSeatInfoParams params) {
        this.params = params;
    }


    public static class SetSeatInfoParams {
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
        private String memberName;
        @Expose
        private String duty;
        @Expose
        private String memberId;
        @Expose
        private int votingRights;
        @Expose
        private String card1;
        @Expose
        private String card2;
        @Expose
        private int registered;//0 不报到  1 报道
        @Expose
        private String title;
        @Expose
        private int askForLeave;//1请假 0正常
        @Expose
        private int cardNoCheck;//1不检查卡号 0检查卡号

        @Expose
        private int isAnonAccess;   //是否匿名访问 1:是  0:否

        public int getCardNoCheck() {
            return cardNoCheck;
        }

        public void setCardNoCheck(int cardNoCheck) {
            this.cardNoCheck = cardNoCheck;
        }

        public int getAskForLeave() {
            return askForLeave;
        }

        public void setAskForLeave(int askForLeave) {
            this.askForLeave = askForLeave;
        }

        public int getRegistered() {
            return registered;
        }

        public void setRegistered(int registered) {
            this.registered = registered;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSeatId() {
            return seatId;
        }

        public void setSeatId(String seatId) {
            this.seatId = seatId;
        }

        public String getMemberName() {
            return memberName;
        }

        public String getDuty() {
            return duty;
        }

        public void setDuty(String duty) {
            this.duty = duty;
        }

        public void setMemberName(String memberName) {
            this.memberName = memberName;
        }

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

        public String getCard1() {
            return card1;
        }

        public void setCard1(String card1) {
            this.card1 = card1;
        }

        public String getCard2() {
            return card2;
        }

        public void setCard2(String card2) {
            this.card2 = card2;
        }

        public int getIsAnonAccess() {
            return isAnonAccess;
        }

        public void setIsAnonAccess(int isAnonAccess) {
            this.isAnonAccess = isAnonAccess;
        }
    }


}
