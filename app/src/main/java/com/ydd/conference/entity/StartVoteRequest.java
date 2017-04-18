package com.ydd.conference.entity;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hehelt on 16/2/29.
 */
public class StartVoteRequest extends Message {


    @Expose
    private StartVoteParams params;

    public StartVoteParams getParams() {
        return params;
    }

    public void setParams(StartVoteParams params) {
        this.params = params;
    }

    public static class StartVoteParams {
        /**
         * seatId : 1
         * type : 0
         * title : xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
         * items : ["张三","李四"]
         */

        @Expose
        private String seatId;
        @Expose
        private String type;
        @Expose
        private String title;
        @Expose
        private String subject;
        @Expose
        private List<VoteItem> items;
        @Expose
        private String horizontal;
        @Expose
        private String vertical;

        public String getHorizontal() {
            return horizontal;
        }

        public void setHorizontal(String horizontal) {
            this.horizontal = horizontal;
        }

        public String getVertical() {
            return vertical;
        }

        public void setVertical(String vertical) {
            this.vertical = vertical;
        }

        public void setSeatId(String seatId) {
            this.seatId = seatId;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setItems(List<VoteItem> items) {
            this.items = items;
        }

        public String getSeatId() {
            return seatId;
        }

        public String getType() {
            return type;
        }

        public String getTitle() {
            return title;
        }

        public List<VoteItem> getItems() {
            return items;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }
    }

    public static class VoteItem implements Serializable {
        @Expose
        public int id;
        @Expose
        public String title;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }


}
