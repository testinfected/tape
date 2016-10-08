package com.vtence.tape;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

public abstract class AbstractDataSource implements DataSource {

    public PrintWriter getLogWriter() throws SQLException {
        return new PrintWriter(new OutputStream() {
            public void write(int b) throws IOException {
            }
        });
    }

    public void setLogWriter(PrintWriter out) throws SQLException {
    }

    public void setLoginTimeout(int seconds) throws SQLException {
    }

    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @SuppressWarnings("unchecked")
    public <T> T unwrap(Class<T> type) throws SQLException {
        if (!isDataSource(type)) throw new IllegalArgumentException(type.getName());
        return (T) this;
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return isDataSource(iface);
    }

    private boolean isDataSource(Class<?> iface) {
        return DataSource.class.equals(iface);
    }

    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException("This data source does not use logging");
    }
}