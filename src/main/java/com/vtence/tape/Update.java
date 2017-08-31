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
    private final List<Object> parameters = new ArrayList<>();

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
        this.parameters.addAll(Arrays.asList(parameters));
        return this;
    }

    public int execute(Connection connection) {
        try( PreparedStatement update = connection.prepareStatement(statement.toSql())) {
            set.dehydrate(update, entity);
            setParameters(update);
            return execute(update);
        } catch (SQLException e) {
            throw new JDBCException("Could not update entity " + entity, e);
        }
    }

    private int execute(PreparedStatement update) throws SQLException {
        return update.executeUpdate();
    }

    private void setParameters(PreparedStatement update) throws SQLException {
        for (int i = 0; i < parameters.size(); i++) {
            JDBC.setParameter(update, parameterIndex(i), parameters.get(i));
        }
    }

    private int parameterIndex(int i) {
        return set.columnCount(false) + i + 1;
    }
}
