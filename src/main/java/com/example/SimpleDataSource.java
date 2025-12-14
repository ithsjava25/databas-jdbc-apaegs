package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Simple DataSource implementation that manages database connections.
 * Wraps JDBC DriverManager for easy connection management.
 */
public class SimpleDataSource {

    private final String url;
    private final String username;
    private final String password;

    /**
     * Creates a new SimpleDataSource with connection parameters.
     *
     * @param url the JDBC URL
     * @param username the database username
     * @param password the database password
     */
    public SimpleDataSource(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * Get a new database connection.
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}
