package com.cgu.ist303.project.registrar;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class RefundCalculatorTest {
    public RefundCalculatorTest() {
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void calculateNumberOfDaysStartBeforeEndTest() {
        RefundCalculator rc = new RefundCalculator();

        Calendar cal1 = new GregorianCalendar(2016, 11, 31);
        Date dat1 = cal1.getTime();

        Calendar cal2 = new GregorianCalendar(2017, 0, 1);
        Date dat2 = cal2.getTime();

        Assert.assertEquals(rc.calculateNumberOfDays(dat1, dat2), 1);
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void calculateNumberOfDaysStartAfterEndTest() {
        RefundCalculator rc = new RefundCalculator();

        Calendar cal1 = new GregorianCalendar(2016, 11, 31);
        Date dat1 = cal1.getTime();

        Calendar cal2 = new GregorianCalendar(2017, 0, 1);
        Date dat2 = cal2.getTime();

        Assert.assertEquals(rc.calculateNumberOfDays(dat2, dat1), 1);
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void calculateRefund90PercentFirstDay() {
        RefundCalculator rc = new RefundCalculator();

        Calendar cal1 = new GregorianCalendar(2016, 11, 31);
        Date dat1 = cal1.getTime();

        Calendar cal2 = new GregorianCalendar(2017, 0, 1);
        Date dat2 = cal2.getTime();

        double refund = rc.calculteRefund(100.0, dat1, dat2);

        Assert.assertTrue((refund - 90.0) < .001);
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void calculateRefund90PercentLastDay() {
        RefundCalculator rc = new RefundCalculator();

        Calendar cal1 = new GregorianCalendar(2016, 11, 31);
        Date dat1 = cal1.getTime();

        Calendar cal2 = new GregorianCalendar(2017, 0, 21);
        Date dat2 = cal2.getTime();

        double refund = rc.calculteRefund(100.0, dat1, dat2);

        Assert.assertTrue((refund - 90.0) < .001);
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void calculateRefund65PercentFirstDay() {
        RefundCalculator rc = new RefundCalculator();

        Calendar cal1 = new GregorianCalendar(2016, 11, 31);
        Date dat1 = cal1.getTime();

        Calendar cal2 = new GregorianCalendar(2017, 0, 22);
        Date dat2 = cal2.getTime();

        double refund = rc.calculteRefund(100.0, dat1, dat2);

        Assert.assertTrue((refund - 45.0) < .001);
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void calculateRefund65PercentLastDay() {
        RefundCalculator rc = new RefundCalculator();

        Calendar cal1 = new GregorianCalendar(2016, 11, 31);
        Date dat1 = cal1.getTime();

        Calendar cal2 = new GregorianCalendar(2017, 1, 11);
        Date dat2 = cal2.getTime();

        double refund = rc.calculteRefund(100.0, dat1, dat2);

        Assert.assertTrue((refund - 45.0) < .001);
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void calculateRefund0PercentFirstDay() {
        RefundCalculator rc = new RefundCalculator();

        Calendar cal1 = new GregorianCalendar(2016, 11, 31);
        Date dat1 = cal1.getTime();

        Calendar cal2 = new GregorianCalendar(2017, 1, 12);
        Date dat2 = cal2.getTime();

        double refund = rc.calculteRefund(100.0, dat1, dat2);
        Assert.assertTrue(refund < .001);
    }
}
