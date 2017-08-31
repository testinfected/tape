package com.vtence.tape;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class Count {
    private final CountStatement statement;
    private final List<Object> parameters = new ArrayList<>();

    public Count(Table<?> table) {
        this.statement = new CountStatement(table.name());
    }

    public static Count from(Table<?> table) {
        return new Count(table);
    }

    public int execute(Connection connection) {
        try (PreparedStatement query = connection.prepareStatement(statement.toSql())) {
            setParameters(query);
            ResultSet rs = execute(query);
            rs.first();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new JDBCException("Could not execute query", e);
        }
    }

    public Count where(String conditions, Object... parameters) {
        statement.where(conditions);
        this.parameters.addAll(asList(parameters));
        return this;
    }

    private ResultSet execute(PreparedStatement query) throws SQLException {
        return query.executeQuery();
    }

    private void setParameters(PreparedStatement query) throws SQLException {
        for (int index = 0; index < parameters.size(); index++) {
            JDBC.setParameter(query, index + 1, parameters.get(index));
        }
    }
}
