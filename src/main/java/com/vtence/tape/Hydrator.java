package com.vtence.tape;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface Hydrator<T> {
    T hydrate(ResultSet rs) throws SQLException;
}
