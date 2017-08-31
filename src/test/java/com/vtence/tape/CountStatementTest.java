package com.vtence.tape;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class CountStatementTest {

    @Test public void
    countsFromTargetTable() {
        CountStatement count = new CountStatement("table");
        assertThat("sql", count.toSql(), equalTo("select count(*) from table"));
    }

    @Test public void
    supportsWhereConditions() {
        CountStatement count = new CountStatement("table");
        count.where("a = ? and b = ?");

        assertThat("sql", count.toSql(), equalTo("select count(*) from table where a = ? and b = ?"));
    }
}
