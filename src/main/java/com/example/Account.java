package com.example;

/**
 * Represents a user account in the system.
 * Contains user information including credentials and personal details.
 */
public class Account {
    private Long userId;
    private String name;
    private String firstName;
    private String lastName;
    private String ssn;
    private String password;

    /**
     * Default constructor.
     */
    public Account() {}

    /**
     * Creates a new Account with all fields.
     *
     * @param userId the unique user identifier
     * @param name the username
     * @param firstName the user's first name
     * @param lastName the user's last name
     * @param ssn the social security number
     * @param password the password
     */
    public Account(Long userId, String name, String firstName, String lastName, String ssn, String password) {
        this.userId = userId;
        this.name = name;
        this.firstName = firstName;
        this.lastName = lastName;
        this.ssn = ssn;
        this.password = password;
    }

    /**
     * Returns the unique ID of the user.
     * @return the user ID
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Sets the unique ID of the user.
     * @param userId the user ID to set
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * Returns the full name of the user.
     * @return the user's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the full name of the user.
     * @param name the user's name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the first name of the user.
     * @return the user's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the user.
     * @param firstName the user's first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the last name of the user.
     * @return the user's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the user.
     * @param lastName the user's last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the social security number of the user.
     * @return the user's SSN
     */
    public String getSsn() {
        return ssn;
    }

    /**
     * Sets the social security number of the user.
     * @param ssn the user's SSN to set
     */
    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    /**
     * Returns the user's password.
     * Note: Storing passwords in plain text is unsafe.
     * @return the user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the user's password.
     * Note: Storing passwords in plain text is unsafe.
     * @param password the user's password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

}
