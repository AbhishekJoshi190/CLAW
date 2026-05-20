package com.claw.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utility class to reset the database by executing schema.sql.
 * This runs independently of the web server.
 */
public class DatabaseReset {
    private static final String MYSQL_URL = "jdbc:mysql://127.0.0.1:3306/?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static final String SCHEMA_PATH = "src/main/resources/schema.sql";

    public static void main(String[] args) {
        System.out.println("CLAW System: Starting Database Reset...");

        try (Connection conn = DriverManager.getConnection(MYSQL_URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            System.out.println("Connected to MySQL Server.");

            StringBuilder sqlBuilder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(SCHEMA_PATH))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    // Skip comments
                    if (line.trim().startsWith("--") || line.trim().isEmpty()) {
                        continue;
                    }
                    sqlBuilder.append(line).append(" ");

                    // If line ends with semicolon, execute the statement
                    if (line.trim().endsWith(";")) {
                        String sql = sqlBuilder.toString();
                        System.out.println("Executing: " + (sql.length() > 50 ? sql.substring(0, 50) + "..." : sql));
                        stmt.execute(sql);
                        sqlBuilder.setLength(0);
                    }
                }
            }

            System.out.println("CLAW System: Database Reset Successful!");
            System.out.println("The 'claw_db' has been recreated with a fresh schema.");

        } catch (SQLException e) {
            System.err.println("CLAW System ERROR: SQL Execution failed!");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("CLAW System ERROR: Could not read schema.sql!");
            e.printStackTrace();
        }
    }
}
