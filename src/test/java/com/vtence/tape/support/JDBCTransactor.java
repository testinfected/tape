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

    public void perform(UnitOfWork unitOfWork) {
        try {
            unitOfWork.execute();
            connection.commit();
        } catch (SQLException e) {
            JDBC.rollback(connection);
            throw new JDBCException("Could not commit transaction", e);
        }
    }
}
