package com.vtence.tape;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBC {

    public static void close(Statement statement) {
        if (statement == null) return;
        try {
            statement.close();
        } catch (SQLException ignored) {
        }
    }

    public static void close(Connection connection) {
        if (connection == null) return;
        try {
            connection.close();
        } catch (SQLException ignored) {
        }

    }

    public static void setParameter(PreparedStatement st, int index, Object value) throws SQLException {
        int sqlType = st.getParameterMetaData().getParameterType(index);
        st.setObject(index, value, sqlType);
    }

    private JDBC() {}
}
