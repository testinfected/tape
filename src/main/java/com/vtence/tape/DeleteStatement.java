package com.vtence.tape;

public class DeleteStatement {

    private final String table;
    private final StringBuilder whereClause = new StringBuilder();

    public DeleteStatement(String table) {
        this.table = table;
    }

    public void where(String clause) {
        this.whereClause.append(" where ").append(clause);
    }

    public String toSql() {
        StringBuilder sql = new StringBuilder("delete from ");
        sql.append(table);
        sql.append(whereClause);
        return sql.toString();
    }
}
