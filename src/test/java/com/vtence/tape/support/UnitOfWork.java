package com.vtence.tape.support;

public interface UnitOfWork {

    void execute() throws Exception;
}
