package com.example;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public class DatabaseInitializer {

    public static void initialize() {
        try (Connection conn = DatabaseUtils.connect();
             Statement stmt = conn.createStatement()) {
                String sql = "CREATE TABLE IF NOT EXISTS passwords (" +
                             "username TEXT NOT NULL," +
                             "password TEXT NOT NULL," +
                             "note TEXT NOT NULL," +
                             "ac TEXT NOT NULL)";

   
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
