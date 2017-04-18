package com.ydd.conference.entity;

import com.google.gson.annotations.Expose;

/**
 * Created by hehelt on 16/2/28.
 */
public class ClearSeatInfoResponse {
    
        @Expose
        private String seatId;

        public void setSeatId(String seatId) {
            this.seatId = seatId;
        }

        public String getSeatId() {
            return seatId;
        }
}
