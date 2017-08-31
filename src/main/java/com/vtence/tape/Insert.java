package com.vtence.tape;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

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

    public int execute(final Connection connection) {
        try (PreparedStatement insert = connection.prepareStatement(statement.toSql(), RETURN_GENERATED_KEYS)) {
            into.dehydrate(insert, entity);
            int rowsInserted = execute(insert);
            into.handleKeys(insert.getGeneratedKeys(), entity);
            return rowsInserted;
        } catch (SQLException e) {
            throw new JDBCException("Could not insert entity " + entity, e);
        }
    }

    private int execute(PreparedStatement insert) throws SQLException {
        return insert.executeUpdate();
    }
}
