package com.vtence.tape;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class InsertStatement {
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
        sql.append("insert into ").append(table);
        sql.append("(").append(asString(columns)).append(")");
        sql.append(" values(").append(asString(parametersFor(columns))).append(")");
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
}
