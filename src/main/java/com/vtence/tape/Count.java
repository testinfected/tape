package com.vtence.tape;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Count {
    private final CountStatement countStatement;

    public Count(Table<?> table) {
        this.countStatement = new CountStatement(table.name());
    }

    public static Count from(Table<?> table) {
        return new Count(table);
    }

    public int execute(Connection connection) {
        try (PreparedStatement query = connection.prepareStatement(countStatement())) {
            ResultSet rs = query.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new JDBCException("Could not execute query", e);
        }
    }

    private String countStatement() {
        return countStatement.toSql();
    }
}
