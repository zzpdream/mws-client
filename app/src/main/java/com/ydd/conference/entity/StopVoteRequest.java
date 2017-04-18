package com.ydd.conference.entity;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hehelt on 16/2/29.
 */
public class StopVoteRequest extends Message {

    @Expose
    private StopVoteParams params;

    public StopVoteParams getParams() {
        return params;
    }

    public void setParams(StopVoteParams params) {
        this.params = params;
    }

    public static class StopVoteParams {
        /**
         * seatId : 1
         * type : 0
         * title : fdfdfdf
         * votes : [{"item":"nothing","agree":"66","against":"0","abstain":"2","miss":"0"}]
         */

        @Expose
        private String seatId;
        @Expose
        private int type;
        @Expose
        private String title;
        /**
         * item : nothing
         * agree : 66
         * against : 0
         * abstain : 2
         * miss : 0
         */

        @Expose
        private List<VoteResult> votes;

        public void setSeatId(String seatId) {
            this.seatId = seatId;
        }

        public void setType(int type) {
            this.type = type;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setVotes(List<VoteResult> votes) {
            this.votes = votes;
        }

        public String getSeatId() {
            return seatId;
        }

        public int getType() {
            return type;
        }

        public String getTitle() {
            return title;
        }

        public List<VoteResult> getVotes() {
            return votes;
        }
    }


    public static class VoteResult implements Serializable{
        @Expose
        private String title;
        @Expose
        private int yes;
        @Expose
        private int no;
        @Expose
        private int abstain;
        @Expose
        private int miss;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getYes() {
            return yes;
        }

        public void setYes(int yes) {
            this.yes = yes;
        }

        public int getNo() {
            return no;
        }

        public void setNo(int no) {
            this.no = no;
        }

        public int getAbstain() {
            return abstain;
        }

        public void setAbstain(int abstain) {
            this.abstain = abstain;
        }

        public int getMiss() {
            return miss;
        }

        public void setMiss(int miss) {
            this.miss = miss;
        }
    }
}
