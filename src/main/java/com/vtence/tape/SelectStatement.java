package com.vtence.tape;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

public class SelectStatement {

    private final String table;
    private final Map<String, List<String>> columns = new HashMap<String, List<String>>();
    private final Map<String, String> aliases = new HashMap<String, String>();
    private final List<String> joinTables = new ArrayList<String>();
    private final StringBuilder joinClause = new StringBuilder();
    private final StringBuilder whereClause = new StringBuilder();
    private final StringBuilder orderByClause = new StringBuilder();
    private final StringBuilder limitClause = new StringBuilder();

    public SelectStatement(String table, String... columns) {
        this.table = table;
        this.columns.put(table, asList(columns));
    }

    public SelectStatement(String table, List<String> columns) {
        this.table = table;
        this.columns.put(table, columns);
    }

    public void aliasTableName(String tableName, String alias) {
        aliases.put(tableName, alias);
    }

    public void join(String joinType, String joinTable, String joinCondition, String... columnsToSelect) {
        join(joinType, joinTable, joinCondition, asList(columnsToSelect));
    }

    public void join(String joinType, String joinTable, String joinCondition, List<String> columnsToSelect) {
        joinWith(joinTable, columnsToSelect);
        appendJoinClause(joinTable, joinType, joinCondition);
    }

    private void joinWith(String tableName, List<String> columnNames) {
        joinTables.add(tableName);
        this.columns.put(tableName, columnNames);
    }

    private void appendJoinClause(String tableName, String joinType, String joinCondition) {
        joinClause.append(" ").append(joinType).append(" join ").append(tableName);
        if (aliased(tableName)) {
            joinClause.append(" ").append(aliasOf(tableName));
        }
        joinClause.append(" on ").append(joinCondition);
    }

    public void where(String clause) {
        whereClause.append(" where ").append(clause);
    }

    public void orderBy(String clause) {
        orderByClause.append(" order by ").append(clause);
    }

    public void limit(int rowCount) {
        limit(0, rowCount);
    }

    public void limit(int offset, int rowCount) {
        limitClause.append(" limit ");
        if (offset != 0) limitClause.append(offset).append(", ");
        limitClause.append(rowCount);
    }

    public String toSql() {
        StringBuilder sql = new StringBuilder();
        sql.append(selectClause());
        sql.append(fromClause());
        sql.append(joinClause);
        sql.append(whereClause);
        sql.append(orderByClause);
        sql.append(limitClause);
        return sql.toString();
    }

    private String selectClause() {
        StringBuilder clause = new StringBuilder();
        clause.append("select ");
        for (Iterator<String> it = listColumns().iterator(); it.hasNext(); ) {
            clause.append(it.next());
            if (it.hasNext()) clause.append(", ");
        }
        return clause.toString();
    }

    private Collection<String> listColumns() {
        Collection<String> columnNames = new ArrayList<String>();
        columnNames.addAll(qualifiedColumnNamesOf(table));
        for (String joinTable : joinTables) {
            columnNames.addAll(qualifiedColumnNamesOf(joinTable));
        }
        return columnNames;
    }

    private List<String> qualifiedColumnNamesOf(String tableName) {
        List<String> qualifiedColumnNames = new ArrayList<String>();
        for (String column : columnsOf(tableName)) {
            qualifiedColumnNames.add(aliased(tableName) ? aliasOf(tableName) + "." + column : column);
        }
        return qualifiedColumnNames;
    }

    private List<String> columnsOf(String tableName) {
        return columns.get(tableName);
    }

    private boolean aliased(String tableName) {
        return aliases.containsKey(tableName);
    }

    private String aliasOf(String tableName) {
        return aliases.get(tableName);
    }

    private String fromClause() {
        StringBuilder from = new StringBuilder(" from ");
        from.append(table);
        if (aliased(table)) {
            from.append(" ").append(aliasOf(table));
        }
        return from.toString();
    }
}
