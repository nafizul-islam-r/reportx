package services;

import db.DBConnection;
import utils.PasswordUtil;
import models.User;

import java.sql.*;
import java.util.Scanner;

public class AuthService {
    private Scanner scanner = new Scanner(System.in);

    public User login() {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        try (Connection conn = DBConnection.getConnection()) {
            String hashedPassword = PasswordUtil.hashPassword(password);
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("✅ Login successful!");
                return new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role")
                );
            } else {
                System.out.println("❌ Invalid credentials.\n");
            }
        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage() + "\n");
        }
        return null;
    }

    public void register() {
        System.out.print("Choose a username: ");
        String username = scanner.nextLine();
        System.out.print("Choose a password: ");
        String password = scanner.nextLine();

        try (Connection conn = DBConnection.getConnection()) {
            String hashedPassword = PasswordUtil.hashPassword(password);
            String checkSql = "SELECT * FROM users WHERE username = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                System.out.println("❌ Username already taken.\n");
                return;
            }

            String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, 'user')";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            stmt.executeUpdate();
            System.out.println("✅ Registered successfully. You can now log in.\n");
        } catch (Exception e) {
            System.out.println("Registration error: " + e.getMessage() + "\n");
        }
    }
}
