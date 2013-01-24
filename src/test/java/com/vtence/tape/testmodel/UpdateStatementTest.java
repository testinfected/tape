package com.vtence.tape.testmodel;

import com.vtence.tape.UpdateStatement;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class UpdateStatementTest {

    @Test public void
    updatesSpecifiedColumnsInTargetTable() {
        UpdateStatement update = new UpdateStatement("table", "a", "b", "c");
        assertThat("sql", update.toSql(), equalTo("update table set a = ?, b = ?, c = ?"));
    }

    @Test public void
    supportsWhereConditions() {
        UpdateStatement update = new UpdateStatement("table", "a", "b", "c");
        update.where("a = ? and b = ?");

        assertThat("sql", update.toSql(), equalTo("update table set a = ?, b = ?, c = ? where a = ? and b = ?"));
    }
}
