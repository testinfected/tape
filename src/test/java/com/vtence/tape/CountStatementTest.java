package com.vtence.tape;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class CountStatementTest {

    @Test public void
    countsFromTargetTable() {
        CountStatement count = new CountStatement("table");
        assertThat("sql", count.toSql(), equalTo("SELECT COUNT(*) FROM table"));
    }

    @Test public void
    supportsWhereConditions() {
        CountStatement count = new CountStatement("table");
        count.where("a = ? AND b = ?");

        assertThat("sql", count.toSql(), equalTo("SELECT COUNT(*) FROM table WHERE a = ? AND b = ?"));
    }
}
