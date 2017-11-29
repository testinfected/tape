package com.vtence.tape;

import java.sql.Connection;
import java.util.function.Function;

public interface StatementExecutor {

    <T> T execute(Function<Connection, T> statement);
}
