package com.example;

import javax.swing.*;

// import org.w3c.dom.events.MouseEvent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.crypto.SecretKey;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;




public class MainUI {
    
    private String username;
    private JLabel passwordLabel;
    private SecretKey key; // SecretKeyをクラスのフィールドとして保持
    private JPasswordField passwordField;
    private JButton togglePasswordButton;
    private boolean passwordVisible = false; // パスワードの表示状態を追跡
    private static final long TIMEOUT = 2 * 60 * 1000; // 5分
    private static Timer logoutTimer;
    private static TimerTask logoutTask;



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginUI(); // ログイン画面を表示
        });
    }

        

    public MainUI(String username) {
        this.username = username;
        try {
            key = EncryptionUtils.generateKey(); // アプリ起動時にキーを生成
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("パスワード管理アプリ");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);




        
        JTextField usernameField = new JTextField();

        JLabel noteLabel = new JLabel("メモ:");
        noteLabel.setBounds(110, 40, 80, 25);
        frame.add(noteLabel);

        JTextField noteField = new JTextField();
        noteField.setBounds(140, 40, 160, 25);
        frame.add(noteField);

        JLabel acLabel = new JLabel("アカウント:");
        acLabel.setBounds(70, 70, 80, 25);
        frame.add(acLabel);

        JTextField acField = new JTextField();
        acField.setBounds(140, 70, 160, 25);
        frame.add(acField);

        passwordLabel = new JLabel("パスワード:");
        passwordLabel.setBounds(70, 100, 80, 25);
        frame.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(140, 100, 130, 25);
        frame.add(passwordField);

        JButton generateButton = new JButton("自動");
        generateButton.setBounds(305, 100, 50, 25);
        frame.add(generateButton);

        JButton saveButton = new JButton("保存");
        saveButton.setBounds(305, 130, 50, 25);
        frame.add(saveButton);

        JTextField searchField = new JTextField();
        searchField.setBounds(70, 170, 150, 25);
        frame.add(searchField);

        JButton searchButton = new JButton("検索");
        searchButton.setBounds(220, 170, 50, 25);
        frame.add(searchButton);

        JButton showButton = new JButton("一覧");
        showButton.setBounds(270, 170, 100, 25);
        frame.add(showButton);

        JButton updateButton = new JButton("Update Password");

        JButton deleteButton = new JButton("Delete Password");

        JCheckBox showPasswordCheckBox = new JCheckBox();
        showPasswordCheckBox.setBounds(275, 100, 150, 25);
        frame.add(showPasswordCheckBox);

        JButton showPasswordButton = new JButton();
        showPasswordButton.setBounds(10, 275, 250, 25);
        frame.add(showPasswordButton);

        ImageIcon eyeIcon = new ImageIcon("eye_slash_icon.jpeg"); // アイコンのパスを指定
        ImageIcon eyeSlashIcon = new ImageIcon("eye_slash_icon.jpeg"); // アイコンのパスを指定

        togglePasswordButton = new JButton(eyeSlashIcon); // 初期状態は非表示アイコン
        togglePasswordButton.setBounds(65, 140, 25, 25);
        togglePasswordButton.setContentAreaFilled(false); // ボタンの背景を透明に
        togglePasswordButton.setBorder(BorderFactory.createEmptyBorder()); // ボタンの枠を非表示に
        frame.add(togglePasswordButton);

        togglePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (showPasswordCheckBox.isSelected()) {
                    passwordField.setEchoChar((char) 0); // パスワードを表示
                } else {
                    passwordField.setEchoChar('●'); // パスワードを隠す
                }
                passwordVisible = !passwordVisible; // 表示状態をトグル
            }
        });
        // パスワード表示切り替え機能
        showPasswordCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (showPasswordCheckBox.isSelected()) {
                    passwordField.setEchoChar((char) 0); // パスワードを表示
                } else {
                    passwordField.setEchoChar('●'); // パスワードを隠す
                }
            }
        });

        // JButton showPasswordButton = new JButton("Show Password");
        // showPasswordButton.setBounds(10, 305, 250, 25);
        // frame.add(showPasswordButton);
        
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String note = noteField.getText();
                String password = PasswordGenerator.generatePassword();
                String ac = acField.getText(); // ID を取得

                
                passwordField.setText(password);
                passwordLabel.setText( "パスワード:");
                try {
                    SecretKey key = EncryptionUtils.generateKey();
                    String encryptedPassword = EncryptionUtils.encrypt(password, key);
                    // DatabaseUtils.savePassword(username, encryptedPassword, note, ac);
                    
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // パスワード生成処理

        
       // パスワード表示処理
        showPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                try {
                    SecretKey key = EncryptionUtils.generateKey(); // 適切なキーをここで取得
                    String decryptedPassword = DatabaseUtils.getPassword(username, key);
                    if (decryptedPassword != null) {
                        passwordField.setText(decryptedPassword);
                        passwordLabel.setText("Displayed Password: " + decryptedPassword);
                    } else {
                        passwordField.setText("No password found for " + username);
                        passwordLabel.setText("No password found."); 
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String password = passwordField.getText();
                String note = noteField.getText();
                String ac = acField.getText(); // ID を取得
        
                if (username.isEmpty() || password.isEmpty() || note.isEmpty() || ac.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "空欄があります");
                    return;
                }
                try {
                    SecretKey key = EncryptionUtils.generateKey();
                    String encryptedPassword = EncryptionUtils.encrypt(password, key);
                    DatabaseUtils.savePassword(username, encryptedPassword, note, ac); // ID も保存
                    JOptionPane.showMessageDialog(frame, "保存できました！");
                    passwordField.setText("");   // パスワードフィールドをリセット
                    noteField.setText("");       // メモフィールドをリセット
                    acField.setText("");  
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "エラーが発生しました");
                }
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchQuery = searchField.getText();
        
                // 検索クエリが空でないかを確認
                if (searchQuery.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please enter a memo to search.");
                    return;
                }
        
                try {
                    // 入力されたメモでパスワードを検索
                    String[] result = DatabaseUtils.getPasswordByNote(searchQuery);
                    if (result != null) {
                        SecretKey key = EncryptionUtils.generateKey();
                        String decryptedPassword = EncryptionUtils.decrypt(result[1], key);
                        String username = result[0];
                        passwordField.setText("Password: " + decryptedPassword + "\nUsername: " + username);
                        passwordLabel.setText("Displayed Password: " + decryptedPassword);
                    } else {
                        passwordField.setText("Password not found.");
                        passwordLabel.setText("Password not found.");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    passwordField.setText("Error retrieving password.");
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
                    passwordField.setText("Password updated.");
                    passwordLabel.setText("Updated Password: " + newPassword); // passwordLabel を更新
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DatabaseUtils.deletePassword(username);
                passwordField.setText("Password deleted.");
                passwordLabel.setText("Password deleted."); 
            }
        });
        

        frame.setVisible(true);
        DatabaseInitializer.initialize(); // データベースの初期化
        

        frame.setVisible(true);

        // タイマーの初期化
        startLogoutTimer();

        // キーボードの操作を監視
        frame.getContentPane().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                resetLogoutTimer(); // キーが押されたらタイマーをリセット
            }
        });

        // マウスの操作を監視
        frame.getContentPane().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                resetLogoutTimer(); // マウスがクリックされたらタイマーをリセット
            }
        });

        // フォーカスをコンテンツパネルに設定することでキーボードイベントを受け取る
        frame.getContentPane().setFocusable(true);
    }

    // ログアウトタイマーを開始
    public static void startLogoutTimer() {
        logoutTimer = new Timer();
        logoutTask = new TimerTask() {
            @Override
            public void run() {
                System.out.println("タイムアウト！アプリを終了します。");
                System.exit(0); // アプリを終了
            }
        };
        logoutTimer.schedule(logoutTask, TIMEOUT); // 指定時間後にタスクを実行
    }

    // タイマーをリセット
    public static void resetLogoutTimer() {
        if (logoutTask != null) {
            logoutTask.cancel(); // 現在のタスクをキャンセル
        }
        startLogoutTimer(); // 新しいタスクを開始
        System.out.println("タイマーをリセットしました。");
    }
}