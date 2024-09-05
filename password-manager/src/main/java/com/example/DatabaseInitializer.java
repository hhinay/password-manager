package com.example;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public class DatabaseInitializer {

    public static void initialize() {
        try (Connection conn = DatabaseUtils.connect();
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS passwords (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "username TEXT NOT NULL," +
                         "password TEXT NOT NULL)" +
                         "note TEXT)";
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
