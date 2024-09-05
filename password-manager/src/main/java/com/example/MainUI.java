package com.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.crypto.SecretKey;

public class MainUI {
    
    private String username;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginUI(); // ログイン画面を表示
        });
    }

    public MainUI(String username) {
        this.username = username;

        JFrame frame = new JFrame("Password Manager");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(10, 10, 80, 25);
        frame.add(usernameLabel);

        JTextField usernameField = new JTextField();
        usernameField.setBounds(100, 10, 160, 25);
        frame.add(usernameField);

        JLabel noteLabel = new JLabel("Note:");
        noteLabel.setBounds(10, 40, 80, 25);
        frame.add(noteLabel);

        JTextField noteField = new JTextField();
        noteField.setBounds(100, 70, 160, 25);
        frame.add(noteField);

        JButton generateButton = new JButton("Generate Password");
        generateButton.setBounds(10, 100, 250, 25);
        frame.add(generateButton);

        JButton saveButton = new JButton("Save Password");
        saveButton.setBounds(10, 130, 250, 25);
        frame.add(saveButton);

        JButton searchButton = new JButton("Search Password");
        searchButton.setBounds(10, 160, 250, 25);
        frame.add(searchButton);

        JButton updateButton = new JButton("Update Password");
        updateButton.setBounds(10, 190, 250, 25);
        frame.add(updateButton);

        JButton deleteButton = new JButton("Delete Password");
        deleteButton.setBounds(10, 220, 250, 25);
        frame.add(deleteButton);

        JTextArea passwordArea = new JTextArea();
        passwordArea.setBounds(10, 250, 250, 50);
        frame.add(passwordArea);

        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String note = noteField.getText();
                String password = PasswordGenerator.generatePassword();
                passwordArea.setText(password);
                try {
                    SecretKey key = EncryptionUtils.generateKey();
                    String encryptedPassword = EncryptionUtils.encrypt(password, key);
                    DatabaseUtils.savePassword(username, encryptedPassword, note);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String password = passwordArea.getText();
                String note = noteField.getText();

                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Username and password fields cannot be empty.");
                    return;
                }
                try {
                    SecretKey key = EncryptionUtils.generateKey();
                    String encryptedPassword = EncryptionUtils.encrypt(password, key);
                    DatabaseUtils.savePassword(username, encryptedPassword, note);
                    JOptionPane.showMessageDialog(frame, "Password saved successfully.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Error saving password.");
                }
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                try {
                    String[] result = DatabaseUtils.getPasswordAndNote(username);
                    if (result != null) {
                        SecretKey key = EncryptionUtils.generateKey();
                        String decryptedPassword = EncryptionUtils.decrypt(result[0], key);
                        String note = result[1];
                        passwordArea.setText("Password: " + decryptedPassword + "\nNote: " + note);
                    } else {
                        passwordArea.setText("Password not found.");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    passwordArea.setText("Error retrieving password.");
                }
            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newPassword = PasswordGenerator.generatePassword();
                try {
                    SecretKey key = EncryptionUtils.generateKey();
                    String encryptedPassword = EncryptionUtils.encrypt(newPassword, key);
                    DatabaseUtils.updatePassword(username, encryptedPassword);
                    passwordArea.setText("Password updated.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DatabaseUtils.deletePassword(username);
                passwordArea.setText("Password deleted.");
            }
        });

        frame.setVisible(true);
        DatabaseInitializer.initialize(); // データベースの初期化
    }
}