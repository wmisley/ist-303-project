package com.cgu.ist303.project.registrar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

public class RefundCalculator {
    private static final Logger log = LogManager.getLogger(RefundCalculator.class);
    private Date dateToday;
    private Date datePacketMailed;

    public RefundCalculator(Date dtToday, Date dtPacketMailed) {
        dateToday = dtToday;
        datePacketMailed = dtPacketMailed;
    }

    public double calculatePercentageRefund() {
        final long DAYS_IN_THREE_WEEKS = 21;
        final long DAYS_IN_SIX_WEEKS = 42;
        double perCentageRefund = 1.0;

        long days = calculateNumberOfDays();

        if (days <= DAYS_IN_THREE_WEEKS) {
            perCentageRefund = 0.9;
        } else if (days <= DAYS_IN_SIX_WEEKS) {
            perCentageRefund = 0.45;
        } else {
            perCentageRefund = 0.0;
        }

        return perCentageRefund;
    }

    public long calculateNumberOfDays() {
        long timeDiff = dateToday.getTime() - datePacketMailed.getTime();
        long diffDays = timeDiff / (60 * 60 * 24 * 1000);
        return Math.abs(diffDays);
    }

    public double calculteRefund(double paidAmount) {
        return paidAmount * calculatePercentageRefund();
    }
}
