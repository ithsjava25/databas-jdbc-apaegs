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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
