package com.vtence.tape;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DriverManagerDataSource extends AbstractDataSource implements DataSource {

    private final String url;
    private final String username;
    private final String password;

    private boolean autoCommit = true;

    public DriverManagerDataSource(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public Connection getConnection() throws SQLException {
        return getConnection(username, password);
    }

    public Connection getConnection(String username, String password) throws SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);
        connection.setAutoCommit(autoCommit);
        return connection;
    }

    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }
}
