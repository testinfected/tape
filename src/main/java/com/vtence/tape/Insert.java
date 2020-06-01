package com.vtence.tape;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Insert<T> {
    private final Table<? super T> into;
    private final T entity;
    private final InsertStatement statement;

    public static <T> Insert<T> into(Table<? super T> table, T entity) {
        return new Insert<>(table, entity);
    }

    public Insert(Table<? super T> table, final T entity) {
        this.into = table;
        this.entity = entity;
        this.statement = new InsertStatement(table.name(), table.columnNames(false));
    }

    public T execute(StatementExecutor executor) {
        return executor.execute(this::execute);
    }

    public T execute(Connection connection) {
        try (PreparedStatement insert = statement.prepare(connection)) {
            dehydrate(insert);
            execute(insert);
            return handleKeys(insert);
        } catch (SQLException e) {
            throw new JDBCException("Could not insert entity " + entity, e);
        }
    }

    private void dehydrate(PreparedStatement insert) throws SQLException {
        into.dehydrate(insert, entity);
    }

    private int execute(PreparedStatement insert) throws SQLException {
        return insert.executeUpdate();
    }

    @SuppressWarnings("unchecked")
    private T handleKeys(PreparedStatement insert) throws SQLException {
        return (T) into.handleKeys(insert.getGeneratedKeys(), entity);
    }

    public String toString() {
        return statement.toSql();
    }
}
