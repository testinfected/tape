package com.vtence.tape;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class UpdateStatement {
    private final String table;
    private final List<String> columns = new ArrayList<>();
    private final StringBuilder whereClause = new StringBuilder();

    public UpdateStatement(String table, String... columns) {
        this(table, Arrays.asList(columns));
    }

    public UpdateStatement(String table, List<String> columns) {
        this.table = table;
        this.columns.addAll(columns);
    }

    public String toSql() {
        StringBuilder sql = new StringBuilder();
        sql.append("update ").append(table).append(" set ");
        for (Iterator<?> it = columns.iterator(); it.hasNext(); ) {
            sql.append(it.next()).append(" = ?");
            if (it.hasNext()) sql.append(", ");
        }
        sql.append(whereClause);
        return sql.toString();
    }

    public void where(String clause) {
        whereClause.append(" where ").append(clause);
    }
}
