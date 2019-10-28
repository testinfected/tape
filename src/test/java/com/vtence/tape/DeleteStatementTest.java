package com.vtence.tape;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class DeleteStatementTest {

    @Test public void
    deletesFromSpecifiedTable() {
        DeleteStatement delete = new DeleteStatement("table");
        assertThat("sql", delete.toSql(), equalTo("DELETE FROM table"));
    }

    @Test public void
    supportsWhereConditions() {
        DeleteStatement delete = new DeleteStatement("table");
        delete.where("a = ? AND b = ?");

        assertThat("sql", delete.toSql(), equalTo("DELETE FROM table WHERE a = ? AND b = ?"));
    }
}
