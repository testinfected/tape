package com.vtence.tape;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class CountStatement implements Statement {
    private final String table;
    private final StringBuilder whereClause = new StringBuilder();

    private final List<Object> parameters = new ArrayList<>();

    public CountStatement(String table) {
        this.table = table;
    }

    public void where(String conditions) {
        whereClause.append(" WHERE ").append(conditions);
    }

    public String toSql() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM ")
           .append(table)
           .append(whereClause);
        return sql.toString();
    }

    public void addParameters(Object... parameters) {
        this.parameters.addAll(asList(parameters));
    }

    @Override
    public PreparedStatement prepare(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(toSql());
        setParameters(statement);
        return statement;
    }

    private void setParameters(PreparedStatement statement) throws SQLException {
        for (int index = 0; index < parameters.size(); index++) {
            JDBC.setParameter(statement, index + 1, parameters.get(index));
        }
    }
}
