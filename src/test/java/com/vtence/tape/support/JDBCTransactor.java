package com.vtence.tape.support;

import com.vtence.tape.JDBC;
import com.vtence.tape.JDBCException;

import java.sql.Connection;
import java.sql.SQLException;

public class JDBCTransactor {
    private final Connection connection;

    public JDBCTransactor(Connection connection) {
        this.connection = connection;
    }

    public void perform(UnitOfWork work) {
        try {
            work.execute();
            connection.commit();
        } catch (SQLException e) {
            JDBC.rollback(connection);
            throw new JDBCException("Transaction failed", e);
        } catch (Throwable e) {
            JDBC.rollback(connection);
            throw e;
        }
    }

    public <T> T perform(QueryUnitOfWork<T> work) {
        Query<T> query = new Query<>(work);
        perform(query);
        return query.result;
    }

    private static class Query<T> implements UnitOfWork {
        private final QueryUnitOfWork<T> work;

        private T result;

        private Query(QueryUnitOfWork<T> work) {
            this.work = work;
        }

        public void execute() {
            result = work.execute();
        }
    }
}