package com.vtence.tape;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DeleteStatement implements Statement {

    private final String table;
    private final StringBuilder whereClause = new StringBuilder();

    private final List<Object> parameters = new ArrayList<>();

    public DeleteStatement(String table) {
        this.table = table;
    }

    public void where(String clause) {
        this.whereClause.append(" WHERE ").append(clause);
    }

    public void addParameters(Object... parameters) {
        this.parameters.addAll(Arrays.asList(parameters));
    }

    public String toSql() {
        StringBuilder sql = new StringBuilder("DELETE FROM ");
        sql.append(table);
        sql.append(whereClause);
        return sql.toString();
    }

    public PreparedStatement prepare(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(toSql());
        setParameters(statement);
        return statement;
    }

    private void setParameters(PreparedStatement query) throws SQLException {
        for (int index = 0; index < parameters.size(); index++) {
            JDBC.setParameter(query, index + 1, parameters.get(index));
        }
    }
}
