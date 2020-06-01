package com.vtence.tape;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Update<T> {

    private final Table<? super T> set;
    private final T entity;
    private final UpdateStatement statement;

    public static <T> Update<T> set(Table<? super T> table, T entity) {
        return new Update<>(table, entity);
    }

    public Update(Table<? super T> table, T entity) {
        this.set = table;
        this.entity = entity;
        this.statement = new UpdateStatement(table.name(), table.columnNames(false));
    }

    public Update where(String clause, Object... parameters) {
        statement.where(clause);
        statement.addParameters(parameters);
        return this;
    }

    public int execute(StatementExecutor executor) {
        return executor.execute(this::execute);
    }

    public int execute(Connection connection) {
        try( PreparedStatement update = statement.prepare(connection)) {
            dehydrate(update);
            return execute(update);
        } catch (SQLException e) {
            throw new JDBCException("Could not update entity " + entity, e);
        }
    }

    private void dehydrate(PreparedStatement update) throws SQLException {
        set.dehydrate(update, entity);
    }

    private int execute(PreparedStatement update) throws SQLException {
        return update.executeUpdate();
    }

    public String toString() {
        return statement.toSql();
    }
}
