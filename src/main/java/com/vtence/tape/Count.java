package com.vtence.tape;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Count {
    private final CountStatement statement;

    public Count(Table<?> table) {
        this.statement = new CountStatement(table.name());
    }

    public static Count from(Table<?> table) {
        return new Count(table);
    }

    public int execute(Connection connection) {
        try (PreparedStatement query = statement.prepare(connection)) {
            ResultSet rs = execute(query);
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new JDBCException("Could not execute query", e);
        }
    }

    public Count where(String conditions, Object... parameters) {
        statement.where(conditions);
        statement.addParameters(parameters);
        return this;
    }

    public int execute(StatementExecutor executor) {
        return executor.execute(this::execute);
    }

    private ResultSet execute(PreparedStatement query) throws SQLException {
        return query.executeQuery();
    }
}
