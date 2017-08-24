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
}
