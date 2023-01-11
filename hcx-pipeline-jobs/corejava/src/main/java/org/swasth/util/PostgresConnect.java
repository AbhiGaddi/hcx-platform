package org.swasth.util;

import org.postgresql.ds.PGPoolingDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PostgresConnect {
    private PGPoolingDataSource source;
    public Connection connection;
    public Statement statement;
    private PostgresConnectionConfig config;


    public PostgresConnect(PostgresConnectionConfig config) throws Exception {
        this.config =config;
        buildPoolConfig(config);
        connection = source.getConnection();
        statement = connection.createStatement();
    }

    public void buildPoolConfig(PostgresConnectionConfig config) throws Exception {
        Class.forName("org.postgresql.Driver");
        source = new PGPoolingDataSource();
        source.setServerName(config.host);
        source.setPortNumber(config.port);
        source.setUser(config.user);
        source.setPassword(config.password);
        source.setDatabaseName(config.database);
        source.setMaxConnections(config.maxConnections);
    }

    public Connection resetConnection() throws Exception {
        closeConnection();
        buildPoolConfig(config);
        connection = source.getConnection();
        statement = connection.createStatement();
        return connection;
    }

    public Connection getConnection() {
        return this.connection;
    }

    public void closeConnection() throws Exception {
        connection.close();
        source.close();
    }

    public boolean execute(String query) throws Exception {
        try {
            return statement.execute(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
            resetConnection();
            return statement.execute(query);
        }
    }

    public ResultSet executeQuery(String query) throws Exception {
        return statement.executeQuery(query);
    }
}
