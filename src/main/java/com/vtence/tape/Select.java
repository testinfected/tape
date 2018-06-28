package com.vtence.tape;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class Select<T> {

    private final Table<? extends T> from;
    private final SelectStatement statement;

    public static <T> Select<T> from(final Table<? extends T> table) {
        return new Select<>(table);
    }

    public static <T> Select<T> from(final Table<? extends T> table, String alias) {
        return new Select<>(table, alias);
    }

    public Select(Table<? extends T> from) {
        this(from, from.name());
    }

    public Select(Table<? extends T> from, String alias) {
        this.from = from;
        this.statement = new SelectStatement(from.name(), from.columnNames(true));
        alias(from, alias);
    }

    private Select<T> alias(Table<?> table, String alias) {
        statement.aliasTableName(table.name(), alias);
        return this;
    }

    public Select<T> join(Table<?> other, String condition) {
        return join(other, other.name(), condition);
    }

    public Select<T> join(Table<?> other, String alias, String condition) {
        alias(other, alias);
        statement.join("inner", other.name(), condition, other.columnNames(true));
        return this;
    }

    public Select<T> leftJoin(Table<?> table, String condition) {
        return leftJoin(table, table.name(), condition);
    }

    public Select<T> leftJoin(Table<?> other, String alias, String condition) {
        alias(other, alias);
        statement.join("left", other.name(), condition, other.columnNames(true));
        return this;
    }

    public Select<T> where(String conditions, Object... parameters) {
        statement.where(conditions);
        statement.addParameters(parameters);
        return this;
    }

    public Select<T> orderBy(String expression) {
        statement.orderBy(expression);
        return this;
    }

    public Optional<T> first(StatementExecutor executor) {
        return executor.execute(this::first);
    }

    public Optional<T> first(Connection connection) {
        statement.limit(1);
        return stream(connection).findFirst();
    }

    public Stream<T> stream(StatementExecutor executor) {
        return executor.execute(this::stream);
    }

    public Stream<T> stream(Connection connection) {
        return list(connection).stream();
    }

    public List<T> list(StatementExecutor executor) {
        return executor.execute(this::list);
    }

    public List<T> list(final Connection connection) {
        try (PreparedStatement query = statement.prepare(connection)) {
            ResultSet rs = execute(query);
            List<T> entities = new ArrayList<>();
            while (rs.next()) {
                entities.add(hydrate(rs));
            }
            return entities;
        } catch (SQLException e) {
            throw new JDBCException("Could not execute query", e);
        }
    }

    private T hydrate(ResultSet rs) throws SQLException {
        return from.hydrate(rs);
    }

    private ResultSet execute(PreparedStatement query) throws SQLException {
        return query.executeQuery();
    }
}
