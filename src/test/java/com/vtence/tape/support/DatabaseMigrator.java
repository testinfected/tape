package com.vtence.tape.support;

import com.googlecode.flyway.core.Flyway;

import javax.sql.DataSource;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseMigrator {

    private final Flyway flyway;

    public DatabaseMigrator(DataSource dataSource) {
        flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.setSqlMigrationPrefix("");
        silenceLogging();
    }

    private void silenceLogging() {
        Logger logger = Logger.getLogger("com.googlecode.flyway");
        logger.setLevel(Level.OFF);
    }

    public void migrate() {
        flyway.migrate();
    }
}
