package com.cgu.ist303.project.resources;

import com.cgu.ist303.project.dao.sqlite.SqliteDBCreator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.rules.ExternalResource;

import static org.junit.Assert.assertTrue;

public class TestResources extends ExternalResource {
    private static final Logger log = LogManager.getLogger(TestResources.class);
    public static String dbFile = "test.db";

    protected void before() {
        log.info("Setting up Camper DAO test");

        SqliteDBCreator dbCreator = new SqliteDBCreator();

        try {
            dbCreator.createDb(dbFile);
        } catch (Exception e) {
            log.error(e);
            assertTrue(false);
        }
    }

    protected void after() {
        log.info("Tearing down Camper DAO test");

        SqliteDBCreator dbCreator = new SqliteDBCreator();
        dbCreator.deleteDabaseFile(dbFile);
    }
}