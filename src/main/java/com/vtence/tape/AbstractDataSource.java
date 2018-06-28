package com.vtence.tape;

import javax.sql.DataSource;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

public abstract class AbstractDataSource implements DataSource {

    public PrintWriter getLogWriter() {
        return new PrintWriter(new OutputStream() {
            public void write(int b) {
            }
        });
    }

    public void setLogWriter(PrintWriter out) {
    }

    public void setLoginTimeout(int seconds) {
    }

    public int getLoginTimeout() {
        return 0;
    }

    public <T> T unwrap(Class<T> type) {
        if (!isDataSource(type)) throw new IllegalArgumentException(type.getName());
        return type.cast(this);
    }

    public boolean isWrapperFor(Class<?> iface) {
        return isDataSource(iface);
    }

    private boolean isDataSource(Class<?> iface) {
        return DataSource.class.equals(iface);
    }

    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException("This data source does not use logging");
    }
}