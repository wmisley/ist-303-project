package com.cgu.ist303.project.registrar;

import com.cgu.ist303.project.dao.model.CampSession;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class RefundCalculator {
    public RefundCalculator() {
    }

    public long calculateNumberOfDays(Date dt1, Date dt2) {
        long timeDiff = dt1.getTime() - dt2.getTime();
        long diffDays = timeDiff / (60 * 60 * 24 * 1000);
        return diffDays;
    }

    public double calculteRefund(double paidAmount, Date datePacketMailed) {
        final long DAYS_IN_THREE_WEEKS = 21;
        final long DAYS_IN_SIX_WEEKS = 42;

        long days = calculateNumberOfDays(new Date(), datePacketMailed);
        double perCentageRefund = 1.0;
        double refund = paidAmount;

        if (days <= DAYS_IN_THREE_WEEKS) {
            perCentageRefund = 0.9;
        } else if (days < DAYS_IN_SIX_WEEKS) {
            perCentageRefund = 0.65;
        } else {
            perCentageRefund = 0.0;
        }

        refund = paidAmount * perCentageRefund;

        return refund;
    }
}
