package com.vtence.tape;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Delete<T> {

    private final DeleteStatement statement;

    public static <T> Delete<T> from(Table<T> table) {
        return new Delete<>(table);
    }

    public Delete(Table<T> from) {
        this.statement = new DeleteStatement(from.name());
    }

    public Delete<T> where(String clause, Object... parameters) {
        statement.where(clause);
        statement.addParameters(parameters);
        return this;
    }

    public int execute(StatementExecutor executor) {
        return executor.execute(this::execute);
    }

    public int execute(Connection connection) {
        try (PreparedStatement delete = statement.prepare(connection)) {
            return execute(delete);
        } catch (SQLException e) {
            throw new JDBCException("Could not execute delete", e);
        }
    }

    private int execute(PreparedStatement delete) throws SQLException {
        return delete.executeUpdate();
    }

    public String toString() {
        return statement.toSql();
    }
}
