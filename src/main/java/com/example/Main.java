package com.example;

import java.sql.*;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    static void main(String[] args) {
        if (isDevMode(args)) {
            DevDatabaseInitializer.start();
        }
        new Main().run();
    }

    /**
     * Runs the application with login and menu.
     */
    public void run() {
        // Resolve DB settings with precedence: System properties -> Environment variables
        String jdbcUrl = resolveConfig("APP_JDBC_URL", "APP_JDBC_URL");
        String dbUser = resolveConfig("APP_DB_USER", "APP_DB_USER");
        String dbPass = resolveConfig("APP_DB_PASS", "APP_DB_PASS");

        if (jdbcUrl == null || dbUser == null || dbPass == null) {
            throw new IllegalStateException(
                    "Missing DB configuration. Provide APP_JDBC_URL, APP_DB_USER, APP_DB_PASS " +
                            "as system properties (-Dkey=value) or environment variables.");
        }

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPass)) {

            Scanner scanner = new Scanner(System.in);
            System.out.print("Username:");
            String username_ = scanner.nextLine().trim();
            System.out.print("Password:");
            String password_ = scanner.nextLine().trim();

            String sql = "select * from account where name = ? and password = ?";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, username_);
                ps.setString(2, password_);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {

                        // Successful login
                        System.out.println("Login successful!");

                        // Menu loop
                        boolean running = true;
                        while (running) {
                            System.out.println("\n=== Menu ===");
                            System.out.println("1) List moon missions");
                            System.out.println("2) Get a moon mission by mission_id");
                            System.out.println("3) Count missions for a given year");
                            System.out.println("4) Create an account");
                            System.out.println("5) Update an account password");
                            System.out.println("6) Delete an account");
                            System.out.println("0) Exit");
                            System.out.print("Choose option: ");

                            String input = scanner.nextLine();
                            int choice;

                            try {
                                choice = Integer.parseInt(input);
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid input – please enter a number.");
                                continue;
                            }

                            switch (choice) {
                                case 1: {
                                    listMoonMissions(connection);
                                    break;
                                }
                                case 2: {
                                    getMoonMissionById(scanner, connection);
                                    break;
                                }
                                case 3: {
                                    countMissionsByYear(scanner, connection);
                                    break;
                                }
                                case 4: {
                                    createAccount(scanner, connection);
                                    break;
                                }
                                case 5: {
                                    updatePassword(scanner, connection);
                                    break;
                                }
                                case 6: {
                                    deleteAccount(scanner, connection);
                                    break;
                                }
                                case 0: {
                                    System.out.println("Goodbye!");
                                    running = false;
                                    break;
                                }
                                default: {
                                    System.out.println("Invalid option, try again.");
                                }
                            }
                        }
                    }
                    else {
                        // Failed login
                        System.out.println("Invalid username or password");
                        System.out.println("0) Exit");
                        System.out.println("Choose option: ");
                        String input = scanner.nextLine();
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Delete an account.
     */
    private static void deleteAccount(Scanner scanner, Connection connection) {
        System.out.print("user_id: ");
        String userIdInput = scanner.nextLine().trim();

        try {
            long userId = Long.parseLong(userIdInput);

            String deleteQuery = "DELETE FROM account WHERE user_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(deleteQuery)) {
                statement.setLong(1, userId);
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Account deleted successfully!");
                } else {
                    System.out.println("No account found with user_id: " + userId);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid user ID format: " + userIdInput);
        } catch (SQLException e) {
            System.out.println("Error deleting account: " + e.getMessage());
        }
    }

    /**
     * Update the password for an existing user.
     */
    private static void updatePassword(Scanner scanner, Connection connection) throws SQLException {

        String user_id = checkNonEmptyInput(scanner, "Enter user_id");
        String newPassword = checkNonEmptyInput(scanner, "Enter a new password");

        try {
            long userId = Long.parseLong(user_id);
            String query = "update account set password = ? where user_id = ?";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, newPassword);
                statement.setLong(2,userId);

                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Password updated successfully!");
                } else {
                    System.out.println("No account found with user_id: " + userId);
                }
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid user ID: " + user_id);
        }
    }

    /**
     * Create a new account.
     */
    private static void createAccount(Scanner scanner, Connection connection) {

        String firstName = checkNonEmptyInput(scanner, "First name");
        String lastName = checkNonEmptyInput(scanner, "Last name");
        String ssn = checkNonEmptyInput(scanner, "SSN");
        String password = checkNonEmptyInput(scanner, "Password");

        String username = firstName.substring(0, Math.min(3, firstName.length())) +
                lastName.substring(0, Math.min(3, lastName.length()));

        String query = "insert into account (name, first_name, last_name, ssn, password) values (?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);
            statement.setString(2, firstName);
            statement.setString(3, lastName);
            statement.setString(4, ssn);
            statement.setString(5, password);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Account created successfully!");
            }
        } catch (SQLException e) {
        System.out.println("Error creating account: " + e.getMessage());
    }
    }

    /**
     * Method checking for non-empty String input
     */
    private static String checkNonEmptyInput(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt + ": ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println(prompt + " cannot be empty.");
            } else {
                return input;
            }
        }
    }


    /**
     * Count moon missions by a given year.
     */
    private static void countMissionsByYear(Scanner scanner, Connection connection) {
        System.out.print("Enter a year: ");
        String yearInput = scanner.nextLine();
        int missionYear;
        try {
            missionYear = Integer.parseInt(yearInput);
        } catch (NumberFormatException e) {
            System.out.println("Invalid year – please enter a number.");
            return;
        }

        String query = "select count(*) as count from moon_mission where year(launch_date) = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, missionYear);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    int count = result.getInt("count");
                    System.out.println("Number of missions in " + missionYear + ": " + count);
                } else {
                    System.out.println("No missions found in " + missionYear);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting mission: " + e.getMessage());
        }
    }

    /**
     * Get moon mission details by id.
     */
    private static void getMoonMissionById(Scanner scanner, Connection connection) {
        System.out.print("mission_id: ");
        String missionIdInput = scanner.nextLine();
        int missionId;
        try {
            missionId = Integer.parseInt(missionIdInput);
        } catch (NumberFormatException e) {
            System.out.println("Invalid mission ID – please enter a number.");
            return;
        }

        String query = "select * from moon_mission where mission_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, missionId);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    System.out.println("\n=== Mission Details ===");
                    System.out.println("Mission ID: " + result.getInt("mission_id"));
                    System.out.println("Spacecraft: " + result.getString("spacecraft"));
                    System.out.println("Launch Date: " + result.getDate("launch_date"));
                    System.out.println("Carrier Rocket: " + result.getString("carrier_rocket"));
                    System.out.println("Operator: " + result.getString("operator"));
                    System.out.println("Mission Type: " + result.getString("mission_type"));
                    System.out.println("Outcome: " + result.getString("outcome"));
                } else {
                    System.out.println("No mission found with ID: " + missionId);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting mission: " + e.getMessage());
        }
    }

    /**
     * List all the moon missions.
     */
    private static void listMoonMissions(Connection connection) {
        String query = "select spacecraft from moon_mission";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet result = statement.executeQuery()) {

            System.out.println("\n=== Moon Missions ===");
            while (result.next()) {
                String spacecraft = result.getString("spacecraft");
                System.out.println(spacecraft);
            }
        } catch (SQLException e) {
            System.out.println("Error listing missions: " + e.getMessage());
        }
    }

    /**
     * Determines if the application is running in development mode based on system properties,
     * environment variables, or command-line arguments.
     *
     * @param args an array of command-line arguments
     * @return {@code true} if the application is in development mode; {@code false} otherwise
     */
    private static boolean isDevMode(String[] args) {
        if (Boolean.getBoolean("devMode"))  //Add VM option -DdevMode=true
            return true;
        if ("true".equalsIgnoreCase(System.getenv("DEV_MODE")))  //Environment variable DEV_MODE=true
            return true;
        return Arrays.asList(args).contains("--dev"); //Argument --dev
    }

    /**
     * Reads configuration with precedence: Java system property first, then environment variable.
     * Returns trimmed value or null if neither source provides a non-empty value.
     */
    private static String resolveConfig(String propertyKey, String envKey) {
        String v = System.getProperty(propertyKey);
        if (v == null || v.trim().isEmpty()) {
            v = System.getenv(envKey);
        }
        return (v == null || v.trim().isEmpty()) ? null : v.trim();
    }
}