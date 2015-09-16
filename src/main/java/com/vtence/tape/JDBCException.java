package com.vtence.tape;

import java.sql.SQLException;

public class JDBCException extends RuntimeException {

    public JDBCException(String message, SQLException cause) {
        super(message, cause);
    }

    public JDBCException(String message) {
        super(message);
    }

    public JDBCException(SQLException cause) {
        super(cause);
    }

    public SQLException getCause() {
        return (SQLException) super.getCause();
    }

    public boolean isCausedBy(Class<? extends SQLException> sqlException) {
        return sqlException != null && sqlException.isInstance(getCause());
    }
}
