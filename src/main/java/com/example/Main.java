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
            System.out.println("Username:");
            String username = scanner.nextLine().trim();
            System.out.println("Password:");
            String password = scanner.nextLine().trim();

            String sql = "select * from account where name = ? and password = ?";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, username);
                ps.setString(2, password);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        // Successful login
                        System.out.println("Login successful!");


                        // MENU LOOP
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

                            int choice = scanner.nextInt();
                            scanner.nextLine();

                            switch (choice) {
                                case 1: {
                                    System.out.println("List missions - TODO");
                                    break;
                                }
                                case 2: {
                                    System.out.println("Get missions - TODO");
                                    break;
                                }
                                case 3: {
                                    System.out.println("Count missions - TODO");
                                    break;
                                }
                                case 4: {
                                    System.out.println("Create account - TODO");
                                    break;
                                }
                                case 5: {
                                    System.out.println("Update password - TODO");
                                    break;
                                }
                                case 6: {
                                    System.out.println("Delete account - TODO");
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
                        int choice = scanner.nextInt();
                        scanner.close();
                        return;
                    }
                }
            }

            scanner.close();


        } catch (SQLException e) {
            throw new RuntimeException(e);
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
