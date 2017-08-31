package com.vtence.tape;

public class CountStatement {
    private final String table;
    private final StringBuilder whereClause = new StringBuilder();

    public CountStatement(String table) {
        this.table = table;
    }

    public void where(String conditions) {
        whereClause.append(" where ").append(conditions);
    }

    public String toSql() {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(*) from ")
           .append(table)
           .append(whereClause);
        return sql.toString();
    }
}
