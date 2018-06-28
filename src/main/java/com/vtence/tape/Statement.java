package com.vtence.tape;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface Statement {

    PreparedStatement prepare(Connection connection) throws SQLException;
}
