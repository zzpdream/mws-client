package com.ydd.conference.entity;

import com.google.gson.annotations.Expose;

/**
 * Created by hehelt on 16/2/29.
 */
public class ShowSubjectRequest extends Message {

    @Expose
    private ShowSubjectParams params;


    public ShowSubjectParams getParams() {
        return params;
    }

    public void setParams(ShowSubjectParams params) {
        this.params = params;
    }

    public static class ShowSubjectParams {
        /**
         * seatId : 1
         * subject : 第一项议程:审议通过<<上海市人大常委会>>
         */

        @Expose
        private String seatId;
        @Expose
        private String subject;
        @Expose
        private String horizontal;  //控制文字的水平方向, left,center,right
        @Expose
        private String vertical;   //控制文字的垂直方向,top,center,bottom


        public void setSeatId(String seatId) {
            this.seatId = seatId;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getSeatId() {
            return seatId;
        }

        public String getSubject() {
            return subject;
        }

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
    }


}
