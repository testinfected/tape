package com.vtence.tape;

public class CountStatement {
    private final String table;

    public CountStatement(String table) {
        this.table = table;
    }

    public String toSql() {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(*) from ").append(table);
        return sql.toString();
    }
}
