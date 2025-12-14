package com.example;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Main application class for the Moon Mission CLI.
 * Provides a command-line interface for managing moon missions and user accounts.
 */
public class Main {

    /**
     * Application entry point.
     *
     * @param args command-line arguments
     */
    static void main(String[] args) {
        if (isDevMode(args)) {
            DevDatabaseInitializer.start();
        }
        new Main().run();
    }

    /**
     * Runs the application with login and menu system.
     * Initializes database connection, authenticates user, and presents menu options.
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

        // Create DataSource
        SimpleDataSource dataSource = new SimpleDataSource(jdbcUrl, dbUser, dbPass);

        // Create Repositories
        AccountRepository accountRepo = new JdbcAccountRepository(dataSource);
        MoonMissionRepository missionRepo = new JdbcMoonMissionRepository(dataSource);

        Scanner scanner = null;
        try {
            scanner = new Scanner(System.in);

            // Login
            System.out.print("Username: ");
            String username = scanner.nextLine().trim();
            System.out.print("Password: ");
            String password = scanner.nextLine().trim();

            if (accountRepo.validateLogin(username, password)) {
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
                            listMoonMissions(missionRepo);
                            break;
                        }
                        case 2: {
                            getMoonMissionById(scanner, missionRepo);
                            break;
                        }
                        case 3: {
                            countMissionsByYear(scanner, missionRepo);
                            break;
                        }
                        case 4: {
                            createAccount(scanner, accountRepo);
                            break;
                        }
                        case 5: {
                            updatePassword(scanner, accountRepo);
                            break;
                        }
                        case 6: {
                            deleteAccount(scanner, accountRepo);
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
            } else {
                // Failed login
                System.out.println("Invalid username or password");
                System.out.println("0) Exit");
                System.out.print("Choose option: ");

                String input = scanner.nextLine();

                if (input.equals("0")) {
                    return;
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }



    }


    /**
     * Deletes a user account by user ID.
     *
     * @param scanner the Scanner for reading user input
     * @param accountRepo the account repository
     */
    private static void deleteAccount(Scanner scanner, AccountRepository accountRepo) {
        System.out.print("user_id: ");
        String userIdInput = scanner.nextLine().trim();

        try {
            long userId = Long.parseLong(userIdInput);

            boolean deleted = accountRepo.deleteAccount(userId);
            if (deleted) {
                System.out.println("Account deleted successfully!");
            } else {
                System.out.println("No account found with user_id: " + userId);
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid user ID format: " + userIdInput);
        }
    }

    /**
     * Updates the password for an existing user account.
     *
     * @param scanner the Scanner for reading user input
     * @param accountRepo the account repository
     */
    private static void updatePassword(Scanner scanner, AccountRepository accountRepo) {
        System.out.print("Enter user_id: ");
        String userIdInput = scanner.nextLine().trim();
        System.out.print("Enter a new password: ");
        String newPassword = scanner.nextLine().trim();

        if (newPassword.isEmpty()) {
            System.out.println("Password cannot be empty.");
            return;
        }

        try {
            long userId = Long.parseLong(userIdInput);

            boolean updated = accountRepo.updatePassword(userId, newPassword);
            if (updated) {
                System.out.println("Password updated successfully!");
            } else {
                System.out.println("No account found with user_id: " + userId);
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid user ID: " + userIdInput);
        }
    }

    /**
     * Creates a new user account.
     * Prompts for first name, last name, SSN, and password.
     *
     * @param scanner the Scanner for reading user input
     * @param accountRepo the account repository
     */
    private static void createAccount(Scanner scanner, AccountRepository accountRepo) {
        String firstName = checkNonEmptyInput(scanner, "First name");
        String lastName = checkNonEmptyInput(scanner, "Last name");
        String ssn = checkNonEmptyInput(scanner, "SSN");
        String password = checkNonEmptyInput(scanner, "Password");

        try {
            String username = accountRepo.createAccount(firstName, lastName, ssn, password);
            System.out.println("Account created successfully! Username: " + username);
        } catch (RuntimeException e) {
            System.out.println("Error creating account: " + e.getMessage());
        }
    }

    /**
     * Validates and reads non-empty string input from user.
     * Continues prompting until a non-empty value is provided.
     *
     * @param scanner the Scanner for reading user input
     * @param prompt the prompt message to display
     * @return the validated non-empty input string
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
     * Counts and displays the number of moon missions launched in a given year.
     *
     * @param scanner the Scanner for reading user input
     * @param missionRepo the moon mission repository
     */
    private static void countMissionsByYear(Scanner scanner, MoonMissionRepository missionRepo) {
        System.out.print("Enter a year: ");
        String yearInput = scanner.nextLine();

        try {
            int year = Integer.parseInt(yearInput);
            int count = missionRepo.countMissionsByYear(year);
            System.out.println("Number of missions in " + year + ": " + count);

        } catch (NumberFormatException e) {
            System.out.println("Invalid year – please enter a number.");
        }
    }

    /**
     * Retrieves and displays details of a specific moon mission by its ID.
     *
     * @param scanner the Scanner for reading user input
     * @param missionRepo the moon mission repository
     */
    private static void getMoonMissionById(Scanner scanner, MoonMissionRepository missionRepo) {
        System.out.print("mission_id: ");
        String missionIdInput = scanner.nextLine();

        try {
            int missionId = Integer.parseInt(missionIdInput);
            MoonMission mission = missionRepo.getMissionById(missionId);

            if (mission != null) {
                System.out.println("\n=== Mission Details ===");
                System.out.println("Mission ID: " + mission.getMissionId());
                System.out.println("Spacecraft: " + mission.getSpacecraft());
                System.out.println("Launch Date: " + mission.getLaunchDate());
                System.out.println("Carrier Rocket: " + mission.getCarrierRocket());
                System.out.println("Operator: " + mission.getOperator());
                System.out.println("Mission Type: " + mission.getMissionType());
                System.out.println("Outcome: " + mission.getOutcome());
            } else {
                System.out.println("No mission found with ID: " + missionId);
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid mission ID – please enter a number.");
        }
    }

    /**
     * Lists all moon mission spacecraft names.
     *
     * @param missionRepo the moon mission repository
     */
    private static void listMoonMissions(MoonMissionRepository missionRepo) {
        List<String> spacecrafts = missionRepo.listAllSpacecrafts();

        System.out.println("\n=== Moon Missions ===");
        for (String spacecraft : spacecrafts) {
            System.out.println(spacecraft);
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