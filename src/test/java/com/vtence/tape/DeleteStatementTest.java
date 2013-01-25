package com.vtence.tape;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class DeleteStatementTest {

    @Test public void
    deletesFromSpecifiedTable() {
        DeleteStatement delete = new DeleteStatement("table");
        assertThat("sql", delete.toSql(), equalTo("delete from table"));
    }

    @Test public void
    supportsWhereConditions() {
        DeleteStatement delete = new DeleteStatement("table");
        delete.where("a = ? and b = ?");

        assertThat("sql", delete.toSql(), equalTo("delete from table where a = ? and b = ?"));
    }
}
