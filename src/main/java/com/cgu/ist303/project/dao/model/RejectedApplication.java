package com.cgu.ist303.project.dao.model;

/**
 * Created by will4769 on 10/5/16.
 */
public class RejectedApplication {
    public enum RejectionReason {
        NotRejected(0),
        AlreadyRegisterForYear(1),
        GenderLimitReached(2),
        ApplicationIncomplete(3),
        NotReceivedDuringAllowableTimeframe(4);

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
