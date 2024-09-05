package com.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class DatabaseUtils {

    private static final String DB_URL = "jdbc:sqlite:passwords.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void savePassword(String username, String encryptedPassword, String note) {
        String sql = "INSERT INTO passwords (username, password, note) VALUES (?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, encryptedPassword);
            pstmt.setString(3, note);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static String[] getPasswordAndNote(String username) {
        String sql = "SELECT password, note FROM passwords WHERE username = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new String[]{rs.getString("password"), rs.getString("note")};
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getPassword(String username) {
        String sql = "SELECT password FROM passwords WHERE username = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void updatePassword(String username, String newEncryptedPassword) {
        String sql = "UPDATE passwords SET password = ? WHERE username = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newEncryptedPassword);
            pstmt.setString(2, username);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void deletePassword(String username) {
        String sql = "DELETE FROM passwords WHERE username = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}

    
