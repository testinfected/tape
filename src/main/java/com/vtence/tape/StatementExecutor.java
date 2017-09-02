package com.vtence.tape;

public interface StatementExecutor {

    <T> T execute(Statement<T> statement);
}
