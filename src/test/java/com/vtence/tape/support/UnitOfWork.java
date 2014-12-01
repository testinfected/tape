package com.vtence.tape.support;

import java.sql.SQLException;

public interface UnitOfWork {

    void execute() throws SQLException;
}
