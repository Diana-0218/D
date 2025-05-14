package com.cafeteria.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 数据库连接工具类
 * 用于创建和管理与数据库的连接
 */
public class DatabaseConnection {
    // 数据库连接URL
    private static final String URL = "jdbc:mysql://localhost:3306/cafeteria_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    // 数据库用户名
    private static final String USER = "root"; // TODO: 请替换为您的 MySQL 用户名
    // 数据库密码 - 请根据实际情况修改
    private static final String PASSWORD = "Djz@djy02180216"; // TODO: 请替换为您的 MySQL 密码
    
    // 静态连接对象
    private static Connection connection = null;
    
    /**
     * 获取数据库连接
     * @return 数据库连接对象
     */
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                // 加载MySQL驱动
                Class.forName("com.mysql.cj.jdbc.Driver");
                // 创建连接
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("找不到数据库驱动类: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("数据库连接失败: " + e.getMessage());
        }
        return connection;
    }
    
    /**
     * 关闭数据库连接
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("关闭数据库连接失败: " + e.getMessage());
        }
    }
}