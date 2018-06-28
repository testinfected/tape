package com.vtence.tape;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface KeyHandler<T> {
    void handleKeys(ResultSet keys, T entity) throws SQLException;
}
