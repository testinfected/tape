package com.vtence.tape;

import com.vtence.tape.support.TestEnvironment;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.junit.Assert.assertThat;

public class DriverManagerDataSourceTest {

    TestEnvironment env = TestEnvironment.inMemory();
    DriverManagerDataSource connectionSource = new DriverManagerDataSource(env.url, env.username, env.password);
    Connection connection;

    @Before public void
    openConnection() throws Exception {
        connection = connectionSource.getConnection();
    }

    @After public void
    closeConnection() throws SQLException {
        connection.close();
    }

    @Test public void
    configuresConnectionFromProperties() throws Exception {
        DatabaseMetaData metaData = connection.getMetaData();
        assertThat("url", metaData.getURL(), equalTo(env.url));
        assertThat("username", metaData.getUserName(), equalToIgnoringCase(env.username));
    }

    @Test public void
    doesNotReuseConnections() throws Exception {
        Connection other = connectionSource.getConnection();
        try {
            assertThat("other connection", other, not(sameInstance(connection)));
        } finally {
            other.close();
        }
    }

    @Test public void
    disablesAutoCommit() throws SQLException {
        assertThat("auto commit", connection.getAutoCommit(), is(equalTo(false)));
    }
}