package com.vtence.tape.support;

import com.vtence.tape.DriverManagerDataSource;
import com.vtence.tape.JDBC;
import com.vtence.tape.JDBCException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Function;

public class Database {

    private final DriverManagerDataSource dataSource;
    private final DatabaseMigrator migrator;
    private final DatabaseCleaner cleaner;

    private Connection connection;

    public static Database in(TestEnvironment env) {
        return new Database(env.url, env.username, env.password);
    }

    public Database(String url, String username, String password) {
        this.dataSource = new DriverManagerDataSource(url, username, password);
        this.migrator = new DatabaseMigrator(dataSource);
        this.cleaner = new DatabaseCleaner();
    }

    public Connection start() {
        dataSource.setAutoCommit(false);
        connection = openConnection();
        migrator.migrate();
        cleaner.clean(connection);
        return connection;
    }

    public <T> T execute(Function<Connection, T> statement) {
        return statement.apply(connection);
    }

    public void stop() {
        JDBC.close(connection);
    }

    private Connection openConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new JDBCException("Could not connect to database", e);
        }
    }
}

