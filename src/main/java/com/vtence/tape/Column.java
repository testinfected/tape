package com.vtence.tape;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class Column<T> {

    private final TableSchema table;
    private final String name;
    private final Type<T> type;

    private boolean autoGenerated;

    public Column(TableSchema table, String name, Type<T> type) {
        this.table = table;
        this.name = name;
        this.type = type;
    }

    public String name() {
        return name;
    }

    public void set(PreparedStatement statement, T value) throws SQLException {
        if (!autoGenerated) {
            type.set(statement, table.indexOf(this, false), value);
        }
    }

    public T get(ResultSet resultSet) throws SQLException {
        return type.get(resultSet, indexIn(resultSet));
    }

    public Column<T> autoGenerated() {
        this.autoGenerated = true;
        return this;
    }

    public boolean isAutoGenerated() {
        return autoGenerated;
    }

    private int indexIn(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();

        for (int index = 1; index <= metaData.getColumnCount(); index++) {
            if (matchesColumn(metaData, index) && matchesTable(metaData, index))
                return index;
        }

        throw new SQLException("Result set has no column '" + name + "'");
    }

    private boolean matchesTable(ResultSetMetaData metaData, int index) throws SQLException {
        return metaData.getTableName(index).equalsIgnoreCase(table.name());
    }

    private boolean matchesColumn(ResultSetMetaData metaData, int index) throws SQLException {
        return metaData.getColumnName(index).equalsIgnoreCase(name);
    }

    public interface Type<T> {

        T get(ResultSet rs, int index) throws SQLException;

        void set(PreparedStatement statement, int index, T value) throws SQLException;
    }
}
