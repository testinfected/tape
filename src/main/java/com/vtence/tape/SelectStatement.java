package com.vtence.tape;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

public class SelectStatement implements SqlStatement {

    private final String fromTable;
    private final Map<String, List<String>> columns = new HashMap<String, List<String>>();
    private final Map<String, String> aliases = new HashMap<String, String>();
    private final List<String> joinTables = new ArrayList<String>();
    private final StringBuilder joinClause = new StringBuilder();
    private final StringBuilder whereClause = new StringBuilder();
    private final StringBuilder orderByClause = new StringBuilder();

    public SelectStatement(String fromTable, String... columns) {
        this.fromTable = fromTable;
        this.columns.put(fromTable, asList(columns));
    }

    public SelectStatement(String fromTable, List<String> columns) {
        this.fromTable = fromTable;
        this.columns.put(fromTable, columns);
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
        joinClause.append(" ").append(joinType).append(" ").append(tableName);
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

    public String toSql() {
        StringBuilder sql = new StringBuilder();
        sql.append(selectClause());
        sql.append(fromClause());
        sql.append(joinClause);
        sql.append(whereClause);
        sql.append(orderByClause);
        return sql.toString();
    }

    private String selectClause() {
        return "select " + JDBC.asString(listColumns());
    }

    private Collection<String> listColumns() {
        Collection<String> columnNames = new ArrayList<String>();
        columnNames.addAll(qualifiedColumnNamesOf(fromTable));
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
        from.append(fromTable);
        if (aliased(fromTable)) {
            from.append(" ").append(aliasOf(fromTable));
        }
        return from.toString();
    }
}