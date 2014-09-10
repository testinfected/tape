package com.vtence.tape;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Table<T> implements Record<T> {

    private final TableSchema schema;
    private final Record<T> record;

    public Table(String name, Record<T> record) {
        this(new TableSchema(name), record);
    }

    public Table(TableSchema schema, Record<T> record) {
        this.schema = schema;
        this.record = record;
    }

    public String name() {
        return schema.name();
    }

    public List<String> columnNames() {
        return schema.columnNames();
    }

    public int columnCount() {
        return schema.columnCount();
    }

    public T hydrate(ResultSet resultSet) throws SQLException {
        return record.hydrate(resultSet);
    }

    public void dehydrate(PreparedStatement statement, T entity) throws SQLException {
        record.dehydrate(statement, entity);
    }

    public void handleKeys(ResultSet keys, T entity) throws SQLException {
        record.handleKeys(keys, entity);
    }
}
