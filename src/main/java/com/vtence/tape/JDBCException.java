package com.vtence.tape;

import java.sql.SQLException;

public class JDBCException extends RuntimeException {

    public JDBCException(String message, SQLException cause) {
        super(message, cause);
    }

    public JDBCException(String message) {
        super(message);
    }

    public SQLException getCause() {
        return (SQLException) super.getCause();
    }

    public boolean causedBy(Class<? extends SQLException> sqlException) {
        if (sqlException == null) return false;
        return sqlException.isInstance(getCause());
    }
}
