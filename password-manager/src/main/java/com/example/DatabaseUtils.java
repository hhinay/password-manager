package com.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.crypto.SecretKey;

import java.sql.ResultSet;
import java.util.logging.Logger;
import java.util.logging.Level;


public class DatabaseUtils {

    private static final String DB_URL = "jdbc:sqlite:passwords.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
    private static final Logger logger = Logger.getLogger(DatabaseUtils.class.getName());

    public static void savePassword(String username, String encryptedPassword, String note, String ac) {
        logger.log(Level.INFO, "savePassword called with: username={0}, ac={1}", new Object[]{username, ac});
        
        String sql = "INSERT INTO passwords (username, password, note, ac) VALUES (?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, encryptedPassword);
            pstmt.setString(3, note);
            pstmt.setString(4, ac);
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
    public static String getPassword(String username, SecretKey key) {
        String sql = "SELECT password FROM passwords WHERE username = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String encryptedPassword = rs.getString("password");
                return EncryptionUtils.decrypt(encryptedPassword, key); // 復号
                // return rs.getString("password");
            }
        } catch (SQLException e) {
        e.printStackTrace();
        } catch (Exception e) {
        e.printStackTrace();
        }
        return null;
    }
    public static String[] getPasswordByNote(String note) {
        String sql = "SELECT username, password FROM passwords WHERE note = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, note);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new String[]{rs.getString("username"), rs.getString("password")};
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



  

   

