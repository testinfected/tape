package com.vtence.tape;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class Insert<T> {
    private final Table<T> into;
    private final T entity;
    private final InsertStatement statement;

    public static <T> Insert<T> into(Table<T> table, T entity) {
        return new Insert<T>(table, entity);
    }

    public Insert(Table<T> table, final T entity) {
        this.into = table;
        this.entity = entity;
        this.statement = new InsertStatement(table.name(), table.columnNames());
    }

    public int execute(final Connection connection) {
        PreparedStatement insert = null;
        try {
            insert = connection.prepareStatement(statement.toSql(), RETURN_GENERATED_KEYS);
            into.dehydrate(insert, entity);
            int rowsInserted = insert.executeUpdate();
            into.handleKeys(insert.getGeneratedKeys(), entity);
            return rowsInserted;
        } catch (SQLException e) {
            throw new JDBCException("Could not insert entity " + entity, e);
        } finally {
            JDBC.close(insert);
        }
    }
}
