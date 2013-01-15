package com.vtence.tape;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.vtence.tape.JDBC.asString;

public class InsertStatement {
    private final String targetTable;
    private final List<String> columns = new ArrayList<String>();

    public InsertStatement(String targetTable, String... columns) {
        this(targetTable, Arrays.asList(columns));
    }

    public InsertStatement(String targetTable, List<String> columns) {
        this.targetTable = targetTable;
        this.columns.addAll(columns);
    }

    public String toSql() {
        StringBuilder sql = new StringBuilder();
        sql.append("insert into ").append(targetTable);
        sql.append("(").append(asString(columns)).append(")");
        sql.append(" values(").append(asString(parametersFor(columns))).append(")");
        return sql.toString();
    }

    private List<String> parametersFor(final Iterable<String> columns) {
        List<String> parameters = new ArrayList<String>();
        for (String ignored : columns){
            parameters.add("?");
        }
        return parameters;
    }
}
