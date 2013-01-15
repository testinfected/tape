package com.vtence.tape;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class InsertStatementTest {

    @Test public void
    insertsSpecifiedColumnsInTargetTable() {
        InsertStatement insert = new InsertStatement("table", "a", "b", "c");
        assertThat("sql", insert.toSql(), equalTo("insert into table(a, b, c) values(?, ?, ?)"));
    }
}
