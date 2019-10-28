package com.vtence.tape;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class UpdateStatementTest {

    @Test public void
    updatesSpecifiedColumnsInTargetTable() {
        UpdateStatement update = new UpdateStatement("table", "a", "b", "c");
        assertThat("sql", update.toSql(), equalTo("UPDATE table SET a = ?, b = ?, c = ?"));
    }

    @Test public void
    supportsWhereConditions() {
        UpdateStatement update = new UpdateStatement("table", "a", "b", "c");
        update.where("a = ? AND b = ?");

        assertThat("sql", update.toSql(), equalTo("UPDATE table SET a = ?, b = ?, c = ? WHERE a = ? AND b = ?"));
    }
}
