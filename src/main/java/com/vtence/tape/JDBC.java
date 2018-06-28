package com.vtence.tape;

import java.sql.*;

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

    public static java.util.Date toJavaDate(Date date) {
        return date != null ? new java.util.Date(date.getTime()) : null;
    }

    public static java.util.Date toJavaDate(Time time) {
        return time != null ? new java.util.Date(time.getTime()) : null;
    }

    public static java.util.Date toJavaDate(Timestamp timestamp)  {
        return timestamp != null ? new java.util.Date(timestamp.getTime()) : null;
    }

    public static Date toSQLDate(java.util.Date date) {
        return date != null ? new Date(date.getTime()) : null;
    }

    public static Time toSQLTime(java.util.Date time) {
        return time != null ? new Time(time.getTime()) : null;
    }

    public static Timestamp toSQLTimestamp(java.util.Date instant) {
        return instant != null ? new Timestamp(instant.getTime()) : null;
    }

    private JDBC() {}
}
