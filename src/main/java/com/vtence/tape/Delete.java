package com.vtence.tape;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Delete<T> {

    private final DeleteStatement statement;
    private final List<Object> parameters = new ArrayList<Object>();

    public static <T> Delete<T> from(Table<T> table) {
        return new Delete<T>(table);
    }

    public Delete(Table<T> from) {
        this.statement = new DeleteStatement(from.name());
    }

    public Delete<T> where(String clause, Object... parameters) {
        statement.where(clause);
        this.parameters.addAll(Arrays.asList(parameters));
        return this;
    }

    public int execute(Connection connection) {
        PreparedStatement delete = null;
        try {
            delete = connection.prepareStatement(statement.toSql());
            for (int i = 0; i < parameters.size(); i++) {
                JDBC.setParameter(delete, i + 1, parameters.get(i));
            }
            return delete.executeUpdate();
        } catch (SQLException e) {
            throw new JDBCException("Could not execute delete", e);
        } finally {
            JDBC.close(delete);
        }
    }
}
