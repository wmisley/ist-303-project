package com.cgu.ist303.project.registrar;

import com.cgu.ist303.project.dao.model.CampSession;
import com.cgu.ist303.project.dao.model.Camper;
import com.cgu.ist303.project.dao.model.RejectedApplication;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Created by will4769 on 12/8/16.
 */
public class LetterGeneratorTester {
    public LetterGeneratorTester() {

    }

    @Test
    public void generateAcceptanceLetterTest() {
        CampSession cs = new CampSession();
        cs.setEndMonth(2);
        cs.setEndDay(1);
        cs.setCampYear(2017);
        cs.setStartDay(1);
        cs.setStartMonth(1);
        cs.setCampSessioId(1);

        Camper camper = new Camper();
        camper.setFirstName("Will");
        camper.setMiddleName("M");
        camper.setLastName("Isley");
        camper.setStreet("123 State St.");
        camper.setState("CA");
        camper.setZipCode("88888");
        camper.setGender(Camper.Gender.Male);
        camper.setAptNumber("456");
        camper.setAge(29);
        camper.setPhoneNumber("5553332222");
        camper.setRpFirstName("Sam");
        camper.setRpMiddleName("N");
        camper.setRpLastName("Theman");

        LetterGenerator lg = new LetterGenerator();

        try {
            File f = new File("test.pdf");
            f.delete();
        } catch (Exception e) {

        }

        try {
            lg.createAcceptanceLetter("test.pdf", camper, cs);

            File f = new File("test.pdf");
            Assert.assertTrue(f.exists());
        } catch (Exception e) {
            Assert.assertTrue(false);
        }
    }

    @Test
    public void generateRejectionLetterTest() {
        CampSession cs = new CampSession();
        cs.setEndMonth(2);
        cs.setEndDay(1);
        cs.setCampYear(2017);
        cs.setStartDay(1);
        cs.setStartMonth(1);
        cs.setCampSessioId(1);

        Camper camper = new Camper();
        camper.setFirstName("Will");
        camper.setMiddleName("M");
        camper.setLastName("Isley");
        camper.setStreet("123 State St.");
        camper.setState("CA");
        camper.setZipCode("88888");
        camper.setGender(Camper.Gender.Male);
        camper.setAptNumber("456");
        camper.setAge(29);
        camper.setPhoneNumber("5553332222");
        camper.setRpFirstName("Sam");
        camper.setRpMiddleName("N");
        camper.setRpLastName("Theman");

        LetterGenerator lg = new LetterGenerator();

        try {
            File f = new File("test.pdf");
            f.delete();
        } catch (Exception e) {

        }

        try {
            lg.createRejectionLetter("test.pdf", camper, cs, RejectedApplication.RejectionReason.GenderLimitReached);

            File f = new File("test.pdf");
            Assert.assertTrue(f.exists());
        } catch (Exception e) {
            Assert.assertTrue(false);
        }
    }
}
