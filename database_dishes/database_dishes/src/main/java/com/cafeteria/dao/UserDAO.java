package com.cafeteria.dao;

import com.cafeteria.model.User;
import com.cafeteria.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 用户数据访问对象 (DAO)
 * 处理与User表相关的数据库操作
 */
public class UserDAO {

    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return 找到的用户对象，如果未找到则返回null
     */
    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?"; // Change Users to users
        User user = null;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password")); // 注意：实际应用中应处理密码哈希
                user.setPreference(rs.getString("preference"));
            }
        } catch (SQLException e) {
            System.err.println("根据用户名查找用户失败: " + e.getMessage());
        }
        return user;
    }

    /**
     * 添加新用户到数据库
     * @param user 要添加的用户对象 (密码应已处理)
     * @return 添加成功返回true，否则返回false
     */
    public boolean addUser(User user) {
        String sql = "INSERT INTO users (username, password, preference) VALUES (?, ?, ?)"; // Change Users to users
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword()); // 存储处理后的密码
            pstmt.setString(3, user.getPreference());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                // 获取自动生成的主键ID
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setUserId(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("添加用户失败: " + e.getMessage());
        }
        return false;
    }

    // 可以根据需要添加更多方法，例如 updateUser, deleteUser 等

}