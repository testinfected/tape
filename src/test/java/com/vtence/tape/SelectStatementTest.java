package com.vtence.tape;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class SelectStatementTest {

    @Test public void
    selectsSpecifiedColumnsFromTargetTable() {
        SelectStatement select = new SelectStatement("table", "a", "b", "c");
        assertThat("sql", select.toSql(), equalTo("select a, b, c from table"));
    }

    @Test public void
    columnNamesArePrefixedWithTableAliasWhenGiven() {
        SelectStatement select = new SelectStatement("table", "a", "b", "c");
        select.aliasTableName("table", "t");

        assertThat("sql", select.toSql(), equalTo("select t.a, t.b, t.c from table t"));
    }

    @Test public void
    supportsWhereConditions() {
        SelectStatement select = new SelectStatement("table", "a", "b", "c");
        select.where("a = ? and b = ?");

        assertThat("sql", select.toSql(), equalTo("select a, b, c from table where a = ? and b = ?"));
    }

    @Test public void
    supportsOrderByExpressions() {
        SelectStatement select = new SelectStatement("table", "a", "b", "c");
        select.orderBy("id desc");

        assertThat("sql", select.toSql(), equalTo("select a, b, c from table order by id desc"));
    }

    @Test public void
    supportsShorthandForSelectingAllColumnsFromATable() {
        SelectStatement select = new SelectStatement("table", "*");

        assertThat("sql", select.toSql(), equalTo("select * from table"));
    }

    @Test public void
    supportsJoinConditions() {
        SelectStatement select = new SelectStatement("this", "a", "b", "c");
        select.join("inner join", "other", "other.id = this.other_id", "d", "e", "f");

        assertThat("sql", select.toSql(), equalTo("select a, b, c, d, e, f from this inner join other on other.id = this.other_id"));
    }

    @Test public void
    supportsLimitsConditions() {
        SelectStatement select = new SelectStatement("table", "a", "b", "c");
        select.limit(1);

        assertThat("sql", select.toSql(), equalTo("select a, b, c from table limit 1"));
    }

    @Test public void
    supportsOffsetInLimits() {
        SelectStatement select = new SelectStatement("table", "a", "b", "c");
        select.limit(3, 5);

        assertThat("sql", select.toSql(), equalTo("select a, b, c from table limit 3, 5"));
    }

    @Test public void
    usingAShorthandToSelectAllColumnsWillWorkWithAJoinToo() {
        SelectStatement select = new SelectStatement("this", "*");
        select.join("inner join", "other", "other.id = this.other_id");

        assertThat("sql", select.toSql(), equalTo("select * from this inner join other on other.id = this.other_id"));
    }

    @Test public void
    joinTableNamesCanBeAliasedAsWell() {
        SelectStatement select = new SelectStatement("this", "a");
        select.aliasTableName("this", "t");
        select.aliasTableName("other", "o");
        select.join("inner join", "other", "o.id = t.other_id", "b");

        assertThat("sql", select.toSql(), equalTo("select t.a, o.b from this t inner join other o on o.id = t.other_id"));
    }

    @Test public void
    appliesClausesInCorrectOrder() {
        SelectStatement select = new SelectStatement("this", "a");
        select.where("a = ?");
        select.join("inner join", "other", "other.id = this.other_id", "b", "c");
        select.orderBy("a asc");
        select.limit(1);

        assertThat("sql", select.toSql(), equalTo("select a, b, c from this inner join other on other.id = this.other_id where a = ? order by a asc limit 1"));
    }
}
