package com.ydd.conference.entity;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by hehelt on 16/2/29.
 */
public class VoteRequest extends Message {

    @Expose
    public VoteParams params;

    public VoteParams getParams() {
        return params;
    }

    public void setParams(VoteParams params) {
        this.params = params;
    }

    public static class VoteParams {
        /**
         * seatId : 1
         * type : 0
         * votes : [{"item":"nothing","value":"1"}]
         */

        @Expose
        private String seatId;
        @Expose
        private int type;
        /**
         * item : nothing
         * value : 1
         */

        @Expose
        private List<Vote> votes;

        public void setSeatId(String seatId) {
            this.seatId = seatId;
        }

        public void setType(int type) {
            this.type = type;
        }

        public void setVotes(List<Vote> votes) {
            this.votes = votes;
        }

        public String getSeatId() {
            return seatId;
        }

        public int getType() {
            return type;
        }

        public List<Vote> getVotes() {
            return votes;
        }
    }


    public static class Vote {

        public static final int STATUS_SELECTED = 1;
        public static final int STATUS_UN_SELECTED = 2;
//        public static final int STATUS_VOTED = 3;

        public Vote(String name, int id) {
            item = name;
            this.id = id;
            isSelected = STATUS_UN_SELECTED;
        }

        public Vote() {

        }

        public Vote(String item, int id, int value) {
            this.item = item;
            this.value = value;
            this.id = id;
        }

        @Expose
        public String item;
        @Expose
        public int value;
        @Expose
        public int id;

        public int isSelected;

        public int getIsSelected() {
            return isSelected;
        }

        public void setIsSelected(int isSelected) {
            this.isSelected = isSelected;
        }

        public boolean isVoted = false;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setItem(String item) {
            this.item = item;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getItem() {
            return item;
        }

        public int getValue() {
            return value;
        }
    }
}
