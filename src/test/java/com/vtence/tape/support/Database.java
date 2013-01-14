package com.vtence.tape.support;

import com.vtence.tape.DriverManagerDataSource;
import com.vtence.tape.JDBCException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class Database {

    private final DatabaseMigrator migrator;
    private final DataSource dataSource;
    private final DatabaseCleaner cleaner;

    public static Database in(TestEnvironment env) {
        return new Database(env.url, env.username, env.password);
    }

    public Database(String url, String username, String password) {
        this.dataSource = new DriverManagerDataSource(url, username, password);
        this.migrator = new DatabaseMigrator(dataSource);
        this.cleaner = new DatabaseCleaner(dataSource);
    }

    public Connection connect() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new JDBCException("Could not connect to database", e);
        }
    }

    public void migrate() {
        migrator.migrate();
    }

    public void clean() throws Exception {
        cleaner.clean();
    }

    public void reset() throws Exception {
        migrate();
        clean();
    }
}

