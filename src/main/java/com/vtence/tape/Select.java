package com.vtence.tape;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;

public class Select<T> {

    private final Table<? extends T> from;
    private final SelectStatement statement;

    public static <T> Select<T> from(Table<? extends T> table) {
        return new Select<>(table);
    }

    public static <T> Select<T> from(Table<? extends T> table, String as) {
        return new Select<>(table, as);
    }

    public static <T> Select<T> distinct(Table<? extends T> from) {
        return distinct(from, from.name());
    }

    public static <T> Select<T> distinct(Table<? extends T> from, String as) {
        Select<T> select = new Select<>(from, as);
        select.distinct();
        return select;
    }

    public Select(Table<? extends T> from) {
        this(from, from.name());
    }

    public Select(Table<? extends T> from, String as) {
        this.from = from;
        this.statement = new SelectStatement(from.name(), as, from.columnNames(true));
    }

    public Select<T> join(Table<?> other, String condition) {
        return join(other, other.name(), condition);
    }

    public Select<T> join(Table<?> other, String condition, boolean select) {
        return join(other, other.name(), condition, select);
    }

    public Select<T> join(Table<?> other, String as, String condition) {
        return join(other, as, condition, false);
    }

    public Select<T> join(Table<?> other, String as, String condition, boolean select) {
        return join(other, as, "INNER", condition, select);
    }

    public Select<T> leftJoin(Table<?> table, String condition) {
        return leftJoin(table, table.name(), condition, false);
    }

    public Select<T> leftJoin(Table<?> table, String condition, boolean select) {
        return leftJoin(table, table.name(), condition, select);
    }

    public Select<T> leftJoin(Table<?> other, String as, String condition) {
        return leftJoin(other, as, condition, false);
    }

    public Select<T> leftJoin(Table<?> other, String as, String condition, boolean select) {
        return join(other, as, "LEFT", condition, select);
    }

    private Select<T> join(Table<?> other, String as, String type, String condition, boolean select) {
        statement.join(type, other.name(), as, condition, select ? other.columnNames(true) : emptyList());
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

    public void distinct() {
        statement.distinct();
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

    public int count(StatementExecutor executor) {
        return executor.execute(this::count);
    }

    public int count(Connection connection) {
        statement.count();

        try (PreparedStatement query = statement.prepare(connection)) {
            ResultSet rs = execute(query);
            rs.next();
            return rs.getInt(1);
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
