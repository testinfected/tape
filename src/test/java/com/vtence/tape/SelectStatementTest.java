package com.vtence.tape;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class SelectStatementTest {

    @Test public void
    selectsSpecifiedColumnsFromTargetTable() {
        SelectStatement select = new SelectStatement("table", "t", "a", "b", "c");
        assertThat("using fully qualified column names", select.toSql(), equalTo("SELECT t.a, t.b, t.c FROM table AS t"));
    }

    @Test public void
    supportsWhereConditions() {
        SelectStatement select = new SelectStatement("table", "t", "a", "b", "c");
        select.where("a = ? AND b = ?");

        assertThat("sql", select.toSql(), equalTo("SELECT t.a, t.b, t.c FROM table AS t WHERE a = ? AND b = ?"));
    }

    @Test public void
    supportsOrderByExpressions() {
        SelectStatement select = new SelectStatement("table", "t", "a", "b", "c");
        select.orderBy("id DESC");

        assertThat("sql", select.toSql(), equalTo("SELECT t.a, t.b, t.c FROM table AS t ORDER BY id DESC"));
    }

    @Test public void
    supportsShorthandForSelectingAllColumnsFromATable() {
        SelectStatement select = new SelectStatement("table", "t", "*");

        assertThat("sql", select.toSql(), equalTo("SELECT t.* FROM table AS t"));
    }

    @Test public void
    supportsJoinConditions() {
        SelectStatement select = new SelectStatement("this", "t", "a", "b", "c");
        select.join("LEFT OUTER", "other", "o", "o.id = t.other_id", "d", "e", "f");

        assertThat("sql", select.toSql(), equalTo(
                "SELECT t.a, t.b, t.c, o.d, o.e, o.f FROM this AS t " +
                "LEFT OUTER JOIN other AS o ON o.id = t.other_id"));
    }

    @Test public void
    supportsLimitsConditions() {
        SelectStatement select = new SelectStatement("table", "t", "a", "b", "c");
        select.limit(1);

        assertThat("sql", select.toSql(), equalTo("SELECT t.a, t.b, t.c FROM table AS t LIMIT 1"));
    }

    @Test public void
    supportsOffsets() {
        SelectStatement select = new SelectStatement("table", "t", "a", "b", "c");
        select.offset(5);

        assertThat("sql", select.toSql(), equalTo("SELECT t.a, t.b, t.c FROM table AS t OFFSET 5"));
    }

    @Test public void
    usingAShorthandToSelectAllColumnsWillWorkWithAJoinToo() {
        SelectStatement select = new SelectStatement("this", "this", "*");
        select.join("INNER", "other", "other", "other.id = this.other_id", "*");

        assertThat("sql", select.toSql(), equalTo(
                "SELECT this.*, other.* FROM this AS this INNER JOIN other AS other ON other.id = this.other_id"));
    }

    @Test public void
    joinTablesCanAppearMultipleTime() {
        SelectStatement select = new SelectStatement("this", "t", "a");
        select.join("INNER", "other", "o1", "o1.id = t.other_id", "b");
        select.join("INNER", "other", "o2", "o2.id = t.other_id", "b");

        assertThat("sql", select.toSql(), equalTo(
                "SELECT t.a, o1.b, o2.b FROM this AS t " +
                "INNER JOIN other AS o1 ON o1.id = t.other_id " +
                "INNER JOIN other AS o2 ON o2.id = t.other_id"));
    }

    @Test public void
    appliesClausesInCorrectOrder() {
        SelectStatement select = new SelectStatement("this", "t", "a");
        select.where("a = ?");
        select.join("INNER", "other", "o", "o.id = t.other_id", "b", "c");
        select.orderBy("a ASC");
        select.limit(1);

        assertThat("sql", select.toSql(), equalTo(
                "SELECT t.a, o.b, o.c FROM this AS t " +
                "INNER JOIN other AS o ON o.id = t.other_id " +
                "WHERE a = ? ORDER BY a ASC LIMIT 1"));
    }

    @Test public void
    supportsDistinctClause() {
        SelectStatement select = new SelectStatement("table", "t", "a", "b", "c");
        select.distinct();

        assertThat("sql", select.toSql(), equalTo("SELECT DISTINCT t.a, t.b, t.c FROM table AS t"));
    }

    @Test public void
    supportsCountingInsteadOfSelecting() {
        SelectStatement select = new SelectStatement("table", "t", "a", "b", "c");
        select.count();

        assertThat("sql", select.toSql(), equalTo("SELECT COUNT(1) FROM table AS t"));
    }

    @Test public void
    combiningDistinctAndCount() {
        SelectStatement select = new SelectStatement("table", "t", "a", "b", "c");
        select.distinct();
        select.count();

        assertThat("sql", select.toSql(), equalTo("SELECT COUNT(DISTINCT (t.a, t.b, t.c)) FROM table AS t"));
    }
}
