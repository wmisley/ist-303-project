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
        Calendar cal1 = new GregorianCalendar(2016, 11, 31);
        Date dat1 = cal1.getTime();

        Calendar cal2 = new GregorianCalendar(2017, 0, 1);
        Date dat2 = cal2.getTime();

        RefundCalculator rc = new RefundCalculator(dat1, dat2);
        Assert.assertEquals(rc.calculateNumberOfDays(), 1);
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void calculateNumberOfDaysStartAfterEndTest() {
        Calendar cal1 = new GregorianCalendar(2016, 11, 31);
        Date dat1 = cal1.getTime();

        Calendar cal2 = new GregorianCalendar(2017, 0, 1);
        Date dat2 = cal2.getTime();

        RefundCalculator rc = new RefundCalculator(dat2, dat1);
        Assert.assertEquals(rc.calculateNumberOfDays(), 1);
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void calculateRefund90PercentFirstDay() {
        Calendar cal1 = new GregorianCalendar(2016, 11, 31);
        Date dat1 = cal1.getTime();

        Calendar cal2 = new GregorianCalendar(2017, 0, 1);
        Date dat2 = cal2.getTime();

        RefundCalculator rc = new RefundCalculator(dat1, dat2);
        double refund = rc.calculteRefund(100.0);

        Assert.assertTrue((refund - 90.0) < .001);
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void calculateRefund90PercentLastDay() {
        Calendar cal1 = new GregorianCalendar(2016, 11, 31);
        Date dat1 = cal1.getTime();

        Calendar cal2 = new GregorianCalendar(2017, 0, 21);
        Date dat2 = cal2.getTime();

        RefundCalculator rc = new RefundCalculator(dat1, dat2);
        double refund = rc.calculteRefund(100.0);

        Assert.assertTrue((refund - 90.0) < .001);
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void calculateRefund65PercentFirstDay() {
        Calendar cal1 = new GregorianCalendar(2016, 11, 31);
        Date dat1 = cal1.getTime();

        Calendar cal2 = new GregorianCalendar(2017, 0, 22);
        Date dat2 = cal2.getTime();

        RefundCalculator rc = new RefundCalculator(dat1, dat2);
        double refund = rc.calculteRefund(100.0);

        Assert.assertTrue((refund - 45.0) < .001);
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void calculateRefund65PercentLastDay() {
        Calendar cal1 = new GregorianCalendar(2016, 11, 31);
        Date dat1 = cal1.getTime();

        Calendar cal2 = new GregorianCalendar(2017, 1, 11);
        Date dat2 = cal2.getTime();

        RefundCalculator rc = new RefundCalculator(dat1, dat2);
        double refund = rc.calculteRefund(100.0);

        Assert.assertTrue((refund - 45.0) < .001);
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void calculateRefund0PercentFirstDay() {
        Calendar cal1 = new GregorianCalendar(2016, 11, 31);
        Date dat1 = cal1.getTime();

        Calendar cal2 = new GregorianCalendar(2017, 1, 12);
        Date dat2 = cal2.getTime();

        RefundCalculator rc = new RefundCalculator(dat1, dat2);
        double refund = rc.calculteRefund(100.0);
        Assert.assertTrue(refund < .001);
    }
}
