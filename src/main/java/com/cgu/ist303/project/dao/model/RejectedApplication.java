package com.cgu.ist303.project.dao.model;

/**
 * Created by will4769 on 10/5/16.
 */
public class RejectedApplication {
    public enum RejectionReason {
        AlreadyRegister(0),
        GenderLimitReached(1),
        ApplicationIncomplete(2);

        private int value ;

        RejectionReason(int value) {
            this.value = value ;
        }

        public int getValue() {
            return this.value;
        }
    }

    private int camperId = 0;
    private int campSessionId = 0;

    public RejectionReason getReason() {
        return reason;
    }

    public void setReason(RejectionReason reason) {
        this.reason = reason;
    }

    public int getCamperId() {
        return camperId;
    }

    public void setCamperId(int camperId) {
        this.camperId = camperId;
    }

    public int getCampSessionId() {
        return campSessionId;
    }

    public void setCampSessionId(int campSessionId) {
        this.campSessionId = campSessionId;
    }

    private RejectionReason reason;

    public RejectedApplication() {
    }
}
