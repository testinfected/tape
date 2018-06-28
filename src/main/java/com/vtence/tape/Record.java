package com.vtence.tape;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface Record<T> extends Hydrator<T>, Dehydrator<T>, KeyHandler<T> {

    @Override
    default void handleKeys(ResultSet keys, T entity) throws SQLException {}
}
