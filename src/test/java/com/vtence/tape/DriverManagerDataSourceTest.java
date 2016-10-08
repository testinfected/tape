package com.vtence.tape;

import com.vtence.tape.support.TestEnvironment;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.junit.Assert.assertThat;

public class DriverManagerDataSourceTest {

    TestEnvironment env = TestEnvironment.memory();
    DriverManagerDataSource connectionSource = new DriverManagerDataSource(env.url, env.username, env.password);
    Connection connection;

    @Before public void
    openConnection() throws Exception {
        connection = connectionSource.getConnection();
    }

    @After public void
    closeConnection() {
        JDBC.close(connection);
    }

    @Test public void
    configuresConnectionFromProperties() throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        assertThat("url", metaData.getURL(), equalTo(env.url));
        assertThat("username", metaData.getUserName(), equalToIgnoringCase(env.username));
    }

    @Test public void
    doesNotReuseConnections() throws SQLException {
        try (Connection other = connectionSource.getConnection()) {
            assertThat("other connection", other, not(sameInstance(connection)));
        }
    }

    @Test public void
    autoCommitsByDefault() throws SQLException {
        assertThat("auto commit", connection.getAutoCommit(), is(equalTo(true)));
    }

    @Test public void
    setsAutoCommit() throws SQLException {
        connectionSource.setAutoCommit(false);

        try (Connection connection = connectionSource.getConnection()) {
            assertThat("auto commit", connection.getAutoCommit(), is(equalTo(false)));
        }
    }
}