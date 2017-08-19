package com.vtence.tape;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JDBC {

    public static void close(Connection connection) {
        if (connection == null) return;
        try {
            connection.close();
        } catch (SQLException ignored) {
        }

    }

    public static void rollback(Connection connection) {
        try {
            connection.rollback();
        } catch (SQLException ignored) {
        }
    }

    public static void setParameter(PreparedStatement st, int index, Object value) throws SQLException {
        int sqlType = st.getParameterMetaData().getParameterType(index);
        st.setObject(index, value, sqlType);
    }

    public static Date toSQLDate(java.util.Date date) {
        return date != null ? new Date(date.getTime()) : null;
    }

    public static java.util.Date fromSQLDate(Date date) {
        return date != null ? new java.util.Date(date.getTime()) : null;
    }

    private JDBC() {}
}
