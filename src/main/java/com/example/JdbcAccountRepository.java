package com.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * JDBC implementation of AccountRepository
 * Handles all database operations related to user accounts.
 */
public class JdbcAccountRepository implements AccountRepository {

    private final SimpleDataSource dataSource;

    /**
     * Creates a new JdbcAccountRepository.
     *
     * @param dataSource the data source for database connections
     */
    public JdbcAccountRepository(SimpleDataSource dataSource) {

        this.dataSource = java.util.Objects.requireNonNull(dataSource, "dataSource");
    }


    @Override
    public boolean validateLogin(String username, String password) {
        String sql = "SELECT * FROM account WHERE name = ? AND password = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // Returns true if user found
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error validating login", e);
        }
    }

    @Override
    public String createAccount(String firstName, String lastName, String ssn, String password) {

        // Validate
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be empty!");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be empty!");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty!");
        }
        if (ssn == null || ssn.trim().isEmpty()) {
            throw new IllegalArgumentException("SSN cannot be empty!");
        }

        // Generate username
        String username = firstName.substring(0, Math.min(3, firstName.length())) +
                lastName.substring(0, Math.min(3, lastName.length()));

        String sql = "INSERT INTO account (name, first_name, last_name, ssn, password) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, firstName);
            ps.setString(3, lastName);
            ps.setString(4, ssn);
            ps.setString(5, password);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                return username;
            } else {
                throw new RuntimeException("Failed to create account");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error creating account", e);
        }
    }

    @Override
    public boolean updatePassword(long userId, String newPassword) {
        String sql = "UPDATE account SET password = ? WHERE user_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newPassword);
            ps.setLong(2, userId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error updating password", e);
        }
    }

    @Override
    public boolean deleteAccount(long userId) {
        String sql = "DELETE FROM account WHERE user_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting account", e);
        }
    }
}
