package com.vtence.tape.support;

import com.vtence.tape.JDBCException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseCleaner {

    private static final String[] TABLES = {
            "line_items",
            "orders",
            "payments",
            "items",
            "products",
    };
    private final DataSource dataSource;

    public DatabaseCleaner(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void clean() throws Exception {
        final Connection connection = dataSource.getConnection();
        try {
            connection.setAutoCommit(true);
            for (String table : TABLES) {
                truncate(connection, table);
            }
            connection.commit();
        } catch (SQLException e) {
            throw new JDBCException("Could not commit transaction", e);
        } catch (Exception e) {
            connection.rollback();
            throw e;
        } finally {
            close(connection);
        }
    }

    private void close(Connection connection) throws SQLException {
        try { connection.close(); } catch (SQLException ignored) {}
    }

    private void truncate(Connection connection, String table) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("delete from " + table);
        statement.executeUpdate();
        statement.close();
    }
}