package com.mcs.modelsearcher.common;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void initializeDatabase() {
        try {
            // Build full DB path inside user's home dir
            String homeDir = System.getProperty("user.home");
            String dbPath = homeDir + "/model-searcher/sqlite.db";
            String jdbcUrl = "jdbc:sqlite:" + dbPath;

            // Ensure parent directory exists
            File dbFile = new File(dbPath);
            File parentDir = dbFile.getParentFile();
            if (!parentDir.exists()) {
                parentDir.mkdirs();
            }

            // Load schema file
            InputStream schemaStream = DatabaseInitializer.class
                .getClassLoader()
                .getResourceAsStream("sql/schema.sql");

            if (schemaStream == null) {
                throw new FileNotFoundException("Could not find sql/schema.sql in classpath.");
            }

            // Read entire schema file
            StringBuilder sqlBuilder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(schemaStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sqlBuilder.append(line).append("\n");
                }
            }

            // Execute entire script at once
            try (Connection conn = DriverManager.getConnection(jdbcUrl);
                 Statement stmt = conn.createStatement()) {

                stmt.executeUpdate(sqlBuilder.toString());

                System.out.println("Database schema initialized at " + dbPath);
            }

        } catch (Exception e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
