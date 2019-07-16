package com.vtence.tape.support;

@FunctionalInterface
public interface QueryUnitOfWork<T> {

    T execute();
}