package com.vtence.tape;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class UpdateStatement implements Statement {
    private final String table;
    private final List<String> columns = new ArrayList<>();
    private final StringBuilder whereClause = new StringBuilder();

    private final List<Object> parameters = new ArrayList<>();

    public UpdateStatement(String table, String... columns) {
        this(table, Arrays.asList(columns));
    }

    public UpdateStatement(String table, List<String> columns) {
        this.table = table;
        this.columns.addAll(columns);
    }

    public void where(String clause) {
        whereClause.append(" WHERE ").append(clause);
    }

    public void addParameters(Object... parameters) {
        this.parameters.addAll(Arrays.asList(parameters));
    }

    public String toSql() {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE ").append(table).append(" SET ");
        for (Iterator<?> it = columns.iterator(); it.hasNext(); ) {
            sql.append(it.next()).append(" = ?");
            if (it.hasNext()) sql.append(", ");
        }
        sql.append(whereClause);
        return sql.toString();
    }

    public PreparedStatement prepare(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(toSql());
        setParameters(statement);
        return statement;
    }

    private void setParameters(PreparedStatement statement) throws SQLException {
        for (int i = 0; i < parameters.size(); i++) {
            JDBC.setParameter(statement, parameterIndex(i), parameters.get(i));
        }
    }

    private int parameterIndex(int i) {
        return columns.size() + i + 1;
    }
}
