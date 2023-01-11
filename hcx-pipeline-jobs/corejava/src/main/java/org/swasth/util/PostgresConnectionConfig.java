package org.swasth.util;

public final class PostgresConnectionConfig {
    public final String user;
    public final String password;
    public final String database;
    public final String host;
    public final int port;
    public final int maxConnections;

    public PostgresConnectionConfig(String user, String password, String database, String host, int port, int maxConnections) {
        this.user = user;
        this.password = password;
        this.database = database;
        this.host = host;
        this.port = port;
        this.maxConnections = maxConnections;
    }
}

