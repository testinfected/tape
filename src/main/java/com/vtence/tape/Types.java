package com.vtence.tape;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class Types {

    public static final Column.Type<Long> LONG = new Column.Type<Long>() {
        public Long get(ResultSet rs, int index) throws SQLException {
            return rs.getLong(index);
        }

        public void set(PreparedStatement statement, int index, Long value) throws SQLException {
            if (value != null)
                statement.setLong(index, value);
            else
                statement.setNull(index, java.sql.Types.BIGINT);
        }
    };

    public static final Column.Type<String> STRING = new Column.Type<String>() {
        public String get(ResultSet rs, int index) throws SQLException {
            return rs.getString(index);
        }

        public void set(PreparedStatement statement, int index, String value) throws SQLException {
            statement.setString(index, value);
        }
    };

    public static final Column.Type<BigDecimal> BIG_DECIMAL = new Column.Type<BigDecimal>() {
        public BigDecimal get(ResultSet rs, int index) throws SQLException {
            return rs.getBigDecimal(index);
        }

        public void set(PreparedStatement statement, int index, BigDecimal value) throws SQLException {
            statement.setBigDecimal(index, value);
        }
    };

    public static final Column.Type<Integer> INT = new Column.Type<Integer>() {
        public Integer get(ResultSet rs, int index) throws SQLException {
            return rs.getInt(index);
        }

        public void set(PreparedStatement statement, int index, Integer value) throws SQLException {
            if (value != null)
                statement.setInt(index, value);
            else
                statement.setNull(index, java.sql.Types.INTEGER);
        }
    };
}
