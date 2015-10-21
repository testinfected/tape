package com.vtence.tape;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Update<T> {

    private final Table<? super T> set;
    private final T entity;
    private final UpdateStatement statement;
    private final List<Object> parameters = new ArrayList<Object>();

    public static <T> Update<T> set(Table<? super T> table, T entity) {
        return new Update<T>(table, entity);
    }

    public Update(Table<? super T> table, T entity) {
        this.set = table;
        this.entity = entity;
        this.statement = new UpdateStatement(table.name(), table.columnNames());
    }

    public Update where(String clause, Object... parameters) {
        statement.where(clause);
        this.parameters.addAll(Arrays.asList(parameters));
        return this;
    }

    public int execute(Connection connection) {
        PreparedStatement update = null;
        try {
            update = connection.prepareStatement(statement.toSql());
            set.dehydrate(update, entity);
            for (int i = 0; i < parameters.size(); i++) {
                JDBC.setParameter(update, set.columnCount() + i + 1, parameters.get(i));
            }
            return update.executeUpdate();
        } catch (SQLException e) {
            throw new JDBCException("Could not update entity " + entity, e);
        } finally {
            JDBC.close(update);
        }
    }
}
