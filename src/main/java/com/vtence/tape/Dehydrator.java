package com.vtence.tape;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface Dehydrator<T> {
    void dehydrate(PreparedStatement st, T entity) throws SQLException;
}
