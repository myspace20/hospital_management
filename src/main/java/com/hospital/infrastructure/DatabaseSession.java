package com.hospital.infrastructure;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public final class DatabaseSession implements AutoCloseable {

    private final  static String username = "postgres";
    private final  static String password = "postgres";

    private final Connection connection;

    private static final Logger LOGGER = Logger.getLogger(DatabaseSession.class.getName());




    public DatabaseSession() {
        LOGGER.info("Initializing database connection");
        try {
            this.connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/employees", username, password);
            LOGGER.info("Database connection successful");
        } catch (SQLException e) {
            LOGGER.severe("Database connection not successful" + e.getMessage());
            throw new RuntimeException("Failed to connect to the database", e);
        }
    }

    public Connection getConnection() {
        return this.connection;
    }

    @Override
    public void close() {
        LOGGER.info("Trying to close database connection");
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                LOGGER.info("Database connection closed");
            }
        } catch (SQLException e) {
            LOGGER.severe("Error closing connection" + e.getMessage());
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }
}

