package com.vtence.tape;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Delete<T> {

    private final DeleteStatement statement;
    private final List<Object> parameters = new ArrayList<>();

    public static <T> Delete<T> from(Table<T> table) {
        return new Delete<>(table);
    }

    public Delete(Table<T> from) {
        this.statement = new DeleteStatement(from.name());
    }

    public Delete<T> where(String clause, Object... parameters) {
        statement.where(clause);
        this.parameters.addAll(Arrays.asList(parameters));
        return this;
    }

    public int execute(StatementExecutor executor) {
        return executor.execute(this::execute);
    }

    public int execute(Connection connection) {
        try (PreparedStatement delete = connection.prepareStatement(statement.toSql())) {
            setParameters(delete);
            return execute(delete);
        } catch (SQLException e) {
            throw new JDBCException("Could not execute delete", e);
        }
    }

    private void setParameters(PreparedStatement delete) throws SQLException {
        for (int i = 0; i < parameters.size(); i++) {
            JDBC.setParameter(delete, i + 1, parameters.get(i));
        }
    }

    private int execute(PreparedStatement delete) throws SQLException {
        return delete.executeUpdate();
    }
}
