package com.vtence.tape;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

public class SelectStatement implements Statement {

    private final String table;
    private final String alias;
    private final Map<String, List<String>> columns = new HashMap<>();
    private final List<Object> parameters = new ArrayList<>();

    private final List<String> joinTables = new ArrayList<>();
    private final StringBuilder joinClause = new StringBuilder();
    private final StringBuilder whereClause = new StringBuilder();
    private final StringBuilder orderByClause = new StringBuilder();
    private final StringBuilder limitClause = new StringBuilder();
    private final StringBuilder offsetClause = new StringBuilder();

    private boolean distinct;
    private boolean count;

    public SelectStatement(String table, String alias, String... columns) {
        this(table, alias, asList(columns));
    }

    public SelectStatement(String table, String alias, List<String> columns) {
        this.table = table;
        this.alias = alias;
        this.columns.put(alias, columns);
    }

    public void join(String joinType, String joinTable, String alias, String joinCondition, String... columnsToSelect) {
        join(joinType, joinTable, alias, joinCondition, asList(columnsToSelect));
    }

    public void join(String joinType, String joinTable, String alias, String joinCondition, List<String> columnsToSelect) {
        joinWith(alias, columnsToSelect);
        appendJoinClause(joinTable, alias, joinType, joinCondition);
    }

    private void joinWith(String tableName, List<String> columnNames) {
        joinTables.add(tableName);
        columns.put(tableName, columnNames);
    }

    private void appendJoinClause(String tableName, String alias, String joinType, String joinCondition) {
        joinClause.append(" ")
                  .append(joinType)
                  .append(" JOIN ")
                  .append(tableName)
                  .append(" AS ")
                  .append(alias)
                  .append(" ON ")
                  .append(joinCondition);
    }

    public void where(String clause) {
        whereClause.append(" WHERE ").append(clause);
    }

    public void orderBy(String clause) {
        orderByClause.append(" ORDER BY ").append(clause);
    }

    public void limit(int rowCount) {
        limitClause.append(" LIMIT ").append(rowCount);
    }

    public void offset(int offset) {
        offsetClause.append(" OFFSET ").append(offset);
    }

    public void distinct() {
        this.distinct = true;
    }

    public void count() {
        this.count = true;
    }

    public void addParameters(Object... parameters) {
        this.parameters.addAll(asList(parameters));
    }

    public String toSql() {
        StringBuilder sql = new StringBuilder();
        sql.append(selectClause());
        sql.append(fromClause());
        sql.append(joinClause);
        sql.append(whereClause);
        sql.append(orderByClause);
        sql.append(limitClause);
        sql.append(offsetClause);
        return sql.toString();
    }

    private String selectClause() {
        StringBuilder clause = new StringBuilder();
        clause.append("SELECT ");
        if (count && !distinct) {
            clause.append("COUNT(1)");
            return clause.toString();
        }
        if (count) clause.append("COUNT(");
        if (distinct) clause.append("DISTINCT ");
        clause.append(columnsSelection());
        if (count) clause.append(")");
        return clause.toString();
    }

    private String columnsSelection() {
        StringBuilder selection = new StringBuilder();
        Collection<String> columns = listColumns();
        if (count && columns.size() > 1) selection.append("(");
        for (Iterator<String> it = columns.iterator(); it.hasNext(); ) {
            selection.append(it.next());
            if (it.hasNext()) selection.append(", ");
        }
        if (count && columns.size() > 1) selection.append(")");
        return selection.toString();
    }

    public PreparedStatement prepare(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(toSql());
        setParameters(statement);
        return statement;
    }

    private Collection<String> listColumns() {
        Collection<String> columnNames = new ArrayList<>(qualifiedColumnNamesOf(alias));
        for (String joinTable : joinTables) {
            columnNames.addAll(qualifiedColumnNamesOf(joinTable));
        }
        return columnNames;
    }

    private List<String> qualifiedColumnNamesOf(String tableName) {
        List<String> qualifiedColumnNames = new ArrayList<>();
        for (String column : columnsOf(tableName)) {
            qualifiedColumnNames.add(tableName + "." + column);
        }
        return qualifiedColumnNames;
    }

    private List<String> columnsOf(String tableName) {
        return columns.get(tableName);
    }

    private String fromClause() {
        StringBuilder from = new StringBuilder(" FROM ");
        from.append(table);
        from.append(" AS ").append(alias);
        return from.toString();
    }

    private void setParameters(PreparedStatement query) throws SQLException {
        for (int index = 0; index < parameters.size(); index++) {
            JDBC.setParameter(query, index + 1, parameters.get(index));
        }
    }
}
