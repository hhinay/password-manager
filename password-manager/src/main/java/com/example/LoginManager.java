package com.example;

public class LoginManager {
    private static String loggedInUsername;

    // ログイン処理
    public static boolean login(String username, String password) {
        // ここでユーザー認証のロジックを実装（今回は簡略化）
        if ("testuser".equals(username) && "password".equals(password)) {
            loggedInUsername = username;
            return true;
        }
        return false;
    }

    // ログインしたユーザー名を取得
    public static String getLoggedInUsername() {
        return loggedInUsername;
    }
}
