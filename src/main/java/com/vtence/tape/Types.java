package com.vtence.tape;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.TimeZone;

public final class Types {

    public static final Column.Type<Long> LONG = new Column.Type<Long>() {
        public Long get(ResultSet rs, int index) throws SQLException {
            long value = rs.getLong(index);
            return rs.wasNull() ? null : value;
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
            int value = rs.getInt(index);
            return rs.wasNull() ? null : value;
        }

        public void set(PreparedStatement statement, int index, Integer value) throws SQLException {
            if (value != null)
                statement.setInt(index, value);
            else
                statement.setNull(index, java.sql.Types.INTEGER);
        }
    };

    public static final Column.Type<Boolean> BOOLEAN = new Column.Type<Boolean>() {
        public Boolean get(ResultSet rs, int index) throws SQLException {
            boolean value = rs.getBoolean(index);
            return rs.wasNull() ? null : value;
        }

        public void set(PreparedStatement statement, int index, Boolean value) throws SQLException {
            if (value != null) {
                statement.setBoolean(index, value);
            } else {
                statement.setNull(index, java.sql.Types.BOOLEAN);
            }
        }
    };

    public static Column.Type<Date> dateIn(final TimeZone timeZone) {
        return new Column.Type<Date>() {
            public Date get(ResultSet rs, int index) throws SQLException {
                return rs.getDate(index, Calendar.getInstance(timeZone));
            }

            public void set(PreparedStatement statement, int index, Date value) throws SQLException {
                if (value != null)
                    statement.setDate(index, value, Calendar.getInstance(timeZone));
                 else
                    statement.setNull(index, java.sql.Types.DATE);
            }
        };
    }

    public static Column.Type<Time> timeIn(final TimeZone timeZone) {
        return new Column.Type<Time>() {
            public Time get(ResultSet rs, int index) throws SQLException {
                return rs.getTime(index, Calendar.getInstance(timeZone));
            }

            public void set(PreparedStatement statement, int index, Time value) throws SQLException {
                if (value != null)
                    statement.setTime(index, value, Calendar.getInstance(timeZone));
                else
                    statement.setNull(index, java.sql.Types.TIME);
            }
        };
    }

    public static Column.Type<Timestamp> timestampIn(final TimeZone timeZone) {
        return new Column.Type<Timestamp>() {
            public Timestamp get(ResultSet rs, int index) throws SQLException {
                return rs.getTimestamp(index, Calendar.getInstance(timeZone));
            }

            public void set(PreparedStatement statement, int index, Timestamp value) throws SQLException {
                if (value != null)
                    statement.setTimestamp(index, value, Calendar.getInstance(timeZone));
                else
                    statement.setNull(index, java.sql.Types.TIMESTAMP);
            }
        };
    }
}
