package com.vtence.tape;

import java.sql.Connection;
import java.util.function.Function;

public interface Statement<T> extends Function<Connection, T> {}
