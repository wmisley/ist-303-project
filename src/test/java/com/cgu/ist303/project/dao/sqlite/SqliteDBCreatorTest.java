package com.cgu.ist303.project.dao.sqlite;

import com.cgu.ist303.project.dao.CamperDAOTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertTrue;

public class SqliteDBCreatorTest {
    private static final Logger log = LogManager.getLogger(SqliteDBCreatorTest.class);

    @Test
    public void testDatabaseCreation() {
        log.info("Testing database creation");
        String dbPath = "test.db";

        SqliteDBCreator dbCreator = new SqliteDBCreator();

        try {
            dbCreator.createDb(dbPath);

            File f = new File(dbPath);
            if(f.exists() && !f.isDirectory()) {
                log.info("The database was created successfully");
            } else {
                assertTrue(false);
            }
        } catch (Exception e) {
            log.error(e);
            assertTrue(false);
        }

        dbCreator.deleteDabaseFile(dbPath);
    }
}
