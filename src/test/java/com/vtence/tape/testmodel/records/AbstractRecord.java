package com.vtence.tape.testmodel.records;

import com.vtence.tape.Record;
import com.vtence.tape.testmodel.Access;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractRecord<T> implements Record<T> {

    public void handleKeys(ResultSet keys, T entity) throws SQLException {
        Access.idOf(entity).set(generatedId(keys));
    }

    private long generatedId(ResultSet rs) throws SQLException {
        return rs.getLong(1);
    }
}
