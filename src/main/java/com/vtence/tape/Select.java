package com.vtence.tape;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Select<T> {

    public static <T> Select<T> from(final Table<T> table) {
        return new Select<T>(table);
    }

    public static <T> Select<T> from(final Table<T> table, String alias) {
        return new Select<T>(table, alias);
    }

    private final Table<T> from;
    private final SelectStatement statement;
    private final List<Object> parameters = new ArrayList<Object>();

    public Select(Table<T> from) {
        this.from = from;
        this.statement = new SelectStatement(from.name(), from.columnNames());
    }

    public Select(Table<T> from, String alias) {
        this(from);
        alias(from, alias);
    }

    private Select<T> alias(Table<?> table, String alias) {
        statement.aliasTableName(table.name(), alias);
        return this;
    }

    public Select<T> join(Table<?> table, String alias, String condition) {
        alias(table, alias);
        statement.join("join", table.name(), condition, table.columnNames());
        return this;
    }

    public Select<T> leftJoin(Table<?> table, String alias, String condition) {
        alias(table, alias);
        statement.join("left join", table.name(), condition, table.columnNames());
        return this;
    }

    public Select<T> where(String clause, Object... parameters) {
        statement.where(clause);
        addParameters(parameters);
        return this;
    }

    public Select<T> orderBy(String clause) {
        statement.orderBy(clause);
        return this;
    }

    public T single(Connection connection) {
        List<T> entities = list(connection);
        if (entities.isEmpty()) throw new JDBCException("Result set is empty");
        if (entities.size() > 1) throw new JDBCException("Result set contains " + entities.size() + " rows (expecting 1)");
        return firstOf(entities);
    }

    public T first(final Connection connection) {
        List<T> entities = list(connection);
        return entities.isEmpty() ? null : firstOf(entities);
    }

    private T firstOf(List<T> entities) {
        return entities.get(0);
    }

    public List<T> list(final Connection connection) {
        List<T> entities = new ArrayList<T>();
        PreparedStatement query = null;
        try {
            query = connection.prepareStatement(statement.toSql());
            for (int index = 0; index < parameters.size(); index++) {
                JDBC.setParameter(query, index + 1, parameters.get(index));
            }
            ResultSet resultSet = execute(query);
            while (resultSet.next()) {
                entities.add(from.hydrate(resultSet));
            }
        } catch (SQLException e) {
            throw new JDBCException("Could not execute query", e);
        } finally {
            JDBC.close(query);
        }
        return entities;
    }

    private ResultSet execute(PreparedStatement query) throws SQLException {
        return query.executeQuery();
    }

    private void addParameters(Object... values) {
        for (Object value : values) {
            addParameter(value);
        }
    }

    private void addParameter(Object value) {
        parameters.add(value);
    }
}