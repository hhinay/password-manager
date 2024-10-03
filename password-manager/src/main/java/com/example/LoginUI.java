package com.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginUI(); // ログイン画面を表示
        });
    }

    public LoginUI() {
        JFrame loginFrame = new JFrame("ログイン");
        loginFrame.setSize(300, 200);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLayout(null);

        JLabel userLabel = new JLabel("ID:");
        userLabel.setBounds(10, 10, 80, 25);
        loginFrame.add(userLabel);

        JTextField userText = new JTextField();
        userText.setBounds(100, 10, 160, 25);
        loginFrame.add(userText);

        JLabel passLabel = new JLabel("パスワード:");
        passLabel.setBounds(10, 40, 80, 25);
        loginFrame.add(passLabel);

        JPasswordField passText = new JPasswordField();
        passText.setBounds(100, 40, 160, 25);
        loginFrame.add(passText);

        JButton loginButton = new JButton("ログイン");
        loginButton.setBounds(10, 80, 250, 25);
        loginFrame.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // ログイン処理をここに追加
                String username = userText.getText();
                new MainUI(username); // ログイン成功後にメイン画面を表示
                loginFrame.dispose(); // ログイン画面を閉じる
            }
        });

        loginFrame.setVisible(true);
    }
}
