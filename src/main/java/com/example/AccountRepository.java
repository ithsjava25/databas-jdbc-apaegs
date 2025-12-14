package com.example;

/**
 * Repository interface for Account operations.
 */
public interface AccountRepository {

    /**
     * Validate login credentials.
     * @return true if username and password match, false otherwise
     */
    boolean validateLogin(String username, String password);

    /**
     * Create a new account.
     * @return the generated username
     */
    String createAccount(String firstName, String lastName, String ssn, String password);

    /**
     * Update password for a user.
     * @return true if update was successful, false if user not found
     */
    boolean updatePassword(long userId, String newPassword);

    /**
     * Delete an account.
     * @return true if delete was successful, false if user not found
     */
    boolean deleteAccount(long userId);
}
