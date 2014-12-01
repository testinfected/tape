package com.vtence.tape.support;

import com.vtence.tape.JDBC;
import com.vtence.tape.JDBCException;

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

    public void clean(Connection connection) {
        try {
            for (String table : TABLES) {
                truncate(connection, table);
            }
            connection.commit();
        } catch (SQLException e) {
            JDBC.rollback(connection);
            throw new JDBCException("Could not commit transaction", e);
        }
    }

    private void truncate(Connection connection, String table) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("delete from " + table);
        statement.executeUpdate();
        statement.close();
    }
}