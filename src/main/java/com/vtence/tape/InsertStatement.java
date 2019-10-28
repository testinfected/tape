package com.vtence.tape;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class InsertStatement implements Statement {
    private final String table;
    private final List<String> columns = new ArrayList<>();

    public InsertStatement(String table, String... columns) {
        this(table, Arrays.asList(columns));
    }

    public InsertStatement(String table, List<String> columns) {
        this.table = table;
        this.columns.addAll(columns);
    }

    public String toSql() {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ").append(table);
        sql.append("(").append(asString(columns)).append(")");
        sql.append(" VALUES(").append(asString(parametersFor(columns))).append(")");
        return sql.toString();
    }

    public String asString(Iterable<?> elements) {
        StringBuilder sb = new StringBuilder();
        for (Iterator<?> it = elements.iterator(); it.hasNext(); ) {
            sb.append(it.next());
            if (it.hasNext()) sb.append(", ");
        }
        return sb.toString();
    }

    private List<String> parametersFor(final Iterable<String> columns) {
        List<String> parameters = new ArrayList<>();
        for (String ignored : columns){
            parameters.add("?");
        }
        return parameters;
    }

    public PreparedStatement prepare(Connection connection) throws SQLException {
        return connection.prepareStatement(toSql(), RETURN_GENERATED_KEYS);
    }
}
