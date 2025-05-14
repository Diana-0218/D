package com.cafeteria;

import com.cafeteria.model.Dish;
import com.cafeteria.model.User;
import com.cafeteria.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 智能食堂推荐系统主类
 * 作为程序入口，提供命令行界面
 */
public class Main {
    private static Connection connection;
    private static Scanner scanner;
    
    public static void main(String[] args) {
        System.out.println("=== 智能食堂推荐系统 ===");
        
        // 初始化数据库连接
        initializeDatabase();
        
        // 初始化扫描器
        scanner = new Scanner(System.in);
        
        // 显示主菜单
        showMainMenu();
        
        // 关闭资源
        scanner.close();
        DatabaseConnection.closeConnection();
        System.out.println("系统已安全退出。");
    }
    
    /**
     * 初始化数据库连接并创建必要的表
     */
    private static void initializeDatabase() {
        try {
            connection = DatabaseConnection.getConnection();
            if (connection != null) {
                System.out.println("数据库连接成功！");
                // 创建必要的表（如果不存在）
                createTablesIfNotExist();
            } else {
                System.err.println("无法连接到数据库，程序将退出。");
                System.exit(1);
            }
        } catch (Exception e) {
            System.err.println("初始化数据库时发生错误: " + e.getMessage());
            System.exit(1);
        }
    }
    
    /**
     * 创建必要的数据库表（如果不存在）
     */
    private static void createTablesIfNotExist() {
        try {
            Statement stmt = connection.createStatement();
            
            // 创建 users 表 (使用小写和下划线)
            String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                    "user_id INT AUTO_INCREMENT PRIMARY KEY, " + // 列名改为 user_id
                    "username VARCHAR(50) NOT NULL UNIQUE, " +
                    "password VARCHAR(100) NOT NULL, " +
                    "preference VARCHAR(255) " +
                    ")";
            stmt.executeUpdate(createUsersTable);
            
            // 创建 dishes 表 (使用小写和下划线)
            String createDishesTable = "CREATE TABLE IF NOT EXISTS dishes (" +
                    "dish_id INT AUTO_INCREMENT PRIMARY KEY, " + // 列名改为 dish_id
                    "dish_name VARCHAR(100) NOT NULL, " +       // 列名改为 dish_name
                    "is_vegetarian BOOLEAN DEFAULT FALSE, " +   // 列名改为 is_vegetarian
                    "window_location VARCHAR(50), " +          // 列名改为 window_location
                    "price DECIMAL(10,2) NOT NULL, " +
                    "average_rating DECIMAL(3,1) DEFAULT 0.0 " + // 列名改为 average_rating
                    ")";
            stmt.executeUpdate(createDishesTable);
            
            // 创建 ratings 表 (使用小写和下划线)
            String createRatingsTable = "CREATE TABLE IF NOT EXISTS ratings (" +
                    "rating_id INT AUTO_INCREMENT PRIMARY KEY, " + // 列名改为 rating_id
                    "user_id INT, " +                             // 列名改为 user_id
                    "dish_id INT, " +                             // 列名改为 dish_id
                    "rating DECIMAL(3,1) NOT NULL, " +
                    "comment TEXT, " +
                    "rating_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " + // 列名改为 rating_date
                    "FOREIGN KEY (user_id) REFERENCES users(user_id), " + // 引用小写的 users(user_id)
                    "FOREIGN KEY (dish_id) REFERENCES dishes(dish_id) " + // 引用小写的 dishes(dish_id)
                    ")";
            stmt.executeUpdate(createRatingsTable);

            stmt.close();
            System.out.println("数据库表结构检查/创建完成。");
        } catch (SQLException e) {
            System.err.println("创建数据库表时发生错误: " + e.getMessage());
            // 可以在这里添加更详细的错误处理或日志记录
            e.printStackTrace(); // 打印完整的堆栈跟踪，有助于调试
        }
    }

    /**
     * 显示主菜单
     */
    private static void showMainMenu() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n请选择操作：");
            System.out.println("1. 用户登录");
            System.out.println("2. 用户注册");
            System.out.println("3. 浏览菜品");
            System.out.println("4. 搜索菜品");
            System.out.println("5. 退出系统");
            System.out.print("请输入选项 (1-5): ");
            
            int choice = getIntInput();
            switch (choice) {
                case 1:
                    loginUser();
                    break;
                case 2:
                    registerUser();
                    break;
                case 3:
                    browseDishes();
                    break;
                case 4:
                    searchDishes();
                    break;
                case 5:
                    exit = true;
                    break;
                default:
                    System.out.println("无效选项，请重新输入。");
            }
        }
    }
    
    /**
     * 用户登录功能
     */
    private static void loginUser() {
        System.out.println("\n=== 用户登录 ===");
        System.out.print("请输入用户名: ");
        String username = scanner.nextLine();
        System.out.print("请输入密码: ");
        String password = scanner.nextLine();
        
        try {
            // 查询语句也要使用小写表名和列名
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // 获取数据时也要使用小写列名
                int userId = rs.getInt("user_id");
                String preference = rs.getString("preference");
                // 假设 User 类的构造函数和方法也已更新或能处理 user_id
                User user = new User(userId, username, password, preference);
                System.out.println("登录成功！欢迎回来，" + username);
                showUserMenu(user);
            } else {
                System.out.println("用户名或密码错误，请重试。");
            }

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.err.println("登录时发生错误: " + e.getMessage());
        }
    }
    
    /**
     * 用户注册功能
     */
    private static void registerUser() {
        System.out.println("\n=== 用户注册 ===");
        System.out.print("请输入用户名: ");
        String username = scanner.nextLine();
        System.out.print("请输入密码: ");
        String password = scanner.nextLine();
        System.out.print("请输入饮食偏好 (例如: 素食,辣,甜): ");
        String preference = scanner.nextLine();
        
        try {
            // 检查用户名是否已存在
            String checkQuery = "SELECT * FROM users WHERE username = ?"; // 使用小写 users
            PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next()) {
                System.out.println("用户名已存在，请选择其他用户名。");
                rs.close();
                checkStmt.close();
                return;
            }
            
            rs.close();
            checkStmt.close();
            
            // 插入新用户
            String insertQuery = "INSERT INTO users (username, password, preference) VALUES (?, ?, ?)"; // 使用小写 users
            PreparedStatement pstmt = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, preference);
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    // 获取生成的键，假设第一列是 user_id
                    int userId = generatedKeys.getInt(1);
                    // 假设 User 类的构造函数和方法也已更新或能处理 user_id
                    User user = new User(userId, username, password, preference);
                    System.out.println("注册成功！欢迎，" + username);
                    showUserMenu(user);
                }
                generatedKeys.close();
            }
            
            pstmt.close();
        } catch (SQLException e) {
            System.err.println("注册时发生错误: " + e.getMessage());
        }
    }
    
    /**
     * 显示用户菜单
     */
    private static void showUserMenu(User user) {
        boolean logout = false;
        while (!logout) {
            System.out.println("\n=== 用户菜单 ===");
            System.out.println("1. 浏览菜品");
            System.out.println("2. 搜索菜品");
            System.out.println("3. 查看推荐菜品");
            System.out.println("4. 修改个人信息");
            System.out.println("5. 注销登录");
            System.out.print("请输入选项 (1-5): ");
            
            int choice = getIntInput();
            switch (choice) {
                case 1:
                    browseDishes();
                    break;
                case 2:
                    searchDishes();
                    break;
                case 3:
                    recommendDishes(user);
                    break;
                case 4:
                    updateUserInfo(user);
                    break;
                case 5:
                    logout = true;
                    break;
                default:
                    System.out.println("无效选项，请重新输入。");
            }
        }
    }
    
    /**
     * 浏览菜品功能
     */
    private static void browseDishes() {
        System.out.println("\n=== 浏览菜品 ===");
        try {
            String query = "SELECT * FROM dishes ORDER BY dish_name"; // 使用小写 dishes 和 dish_name
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            List<Dish> dishes = new ArrayList<>();
            while (rs.next()) {
                // 获取数据时使用小写列名
                Dish dish = new Dish(
                    rs.getInt("dish_id"),
                    rs.getString("dish_name"),
                    rs.getBoolean("is_vegetarian"),
                    rs.getString("window_location"),
                    rs.getBigDecimal("price"),
                    rs.getBigDecimal("average_rating")
                );
                dishes.add(dish);
            }
            
            if (dishes.isEmpty()) {
                System.out.println("暂无菜品信息。");
            } else {
                System.out.println("ID\t菜名\t\t价格\t评分\t窗口位置\t素食");
                System.out.println("--------------------------------------------------");
                for (Dish dish : dishes) {
                    System.out.printf("%d\t%-10s\t%.2f\t%.1f\t%-10s\t%s\n",
                            dish.getDishId(),
                            dish.getDishName(),
                            dish.getPrice(),
                            dish.getAverageRating(),
                            dish.getWindowLocation(),
                            dish.isVegetarian() ? "是" : "否");
                }
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("浏览菜品时发生错误: " + e.getMessage());
        }
    }
    
    /**
     * 搜索菜品功能
     */
    private static void searchDishes() {
        System.out.println("\n=== 搜索菜品 ===");
        System.out.print("请输入搜索关键词: ");
        String keyword = scanner.nextLine();
        
        try {
            // 使用小写表名和下划线列名
            String query = "SELECT * FROM dishes WHERE dish_name LIKE ? OR window_location LIKE ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, "%" + keyword + "%");
            pstmt.setString(2, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();
            
            List<Dish> dishes = new ArrayList<>();
            while (rs.next()) {
                // 获取数据时使用小写下划线列名
                Dish dish = new Dish(
                    rs.getInt("dish_id"),
                    rs.getString("dish_name"),
                    rs.getBoolean("is_vegetarian"),
                    rs.getString("window_location"),
                    rs.getBigDecimal("price"),
                    rs.getBigDecimal("average_rating")
                );
                dishes.add(dish);
            }
            
            if (dishes.isEmpty()) {
                System.out.println("未找到匹配的菜品。");
            } else {
                System.out.println("找到 " + dishes.size() + " 个匹配的菜品：");
                System.out.println("ID\t菜名\t\t价格\t评分\t窗口位置\t素食");
                System.out.println("--------------------------------------------------");
                for (Dish dish : dishes) {
                    System.out.printf("%d\t%-10s\t%.2f\t%.1f\t%-10s\t%s\n",
                            dish.getDishId(),
                            dish.getDishName(),
                            dish.getPrice(),
                            dish.getAverageRating(),
                            dish.getWindowLocation(),
                            dish.isVegetarian() ? "是" : "否");
                }
            }
            
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.err.println("搜索菜品时发生错误: " + e.getMessage());
        }
    }
    
    /**
     * 推荐菜品功能
     */
    private static void recommendDishes(User user) {
        System.out.println("\n=== 推荐菜品 ===");
        String preference = user.getPreference();
        
        if (preference == null || preference.trim().isEmpty()) {
            System.out.println("您尚未设置饮食偏好，将为您推荐评分最高的菜品。");
            try {
                // 使用小写表名和下划线列名
                String query = "SELECT * FROM dishes ORDER BY average_rating DESC LIMIT 5";
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                
                displayRecommendedDishes(rs);
                
                rs.close();
                stmt.close();
            } catch (SQLException e) {
                System.err.println("推荐菜品时发生错误: " + e.getMessage());
            }
        } else {
            System.out.println("根据您的偏好 [" + preference + "] 为您推荐以下菜品：");
            try {
                // 简单的基于关键词的推荐，使用小写表名和下划线列名
                String[] keywords = preference.split(",");
                StringBuilder queryBuilder = new StringBuilder("SELECT * FROM dishes WHERE ");
                
                for (int i = 0; i < keywords.length; i++) {
                    if (i > 0) {
                        queryBuilder.append(" OR ");
                    }
                    // 使用小写下划线列名
                    queryBuilder.append("dish_name LIKE ? OR window_location LIKE ?");
                    
                    // 特殊处理素食偏好
                    if (keywords[i].trim().equalsIgnoreCase("素食")) {
                        // 使用小写下划线列名
                        queryBuilder.append(" OR is_vegetarian = TRUE");
                    }
                }
                
                // 使用小写下划线列名
                queryBuilder.append(" ORDER BY average_rating DESC LIMIT 5");
                
                PreparedStatement pstmt = connection.prepareStatement(queryBuilder.toString());
                
                int paramIndex = 1;
                for (String keyword : keywords) {
                    pstmt.setString(paramIndex++, "%" + keyword.trim() + "%");
                    pstmt.setString(paramIndex++, "%" + keyword.trim() + "%");
                }
                
                ResultSet rs = pstmt.executeQuery();
                
                displayRecommendedDishes(rs);
                
                rs.close();
                pstmt.close();
            } catch (SQLException e) {
                System.err.println("推荐菜品时发生错误: " + e.getMessage());
            }
        }
    }
    
    /**
     * 显示推荐菜品
     */
    private static void displayRecommendedDishes(ResultSet rs) throws SQLException {
        List<Dish> dishes = new ArrayList<>();
        while (rs.next()) {
            Dish dish = new Dish(
                rs.getInt("dish_id"),
                rs.getString("dish_name"),
                rs.getBoolean("is_vegetarian"),
                rs.getString("window_location"),
                rs.getBigDecimal("price"),
                rs.getBigDecimal("average_rating")
            );
            dishes.add(dish);
        }
        
        if (dishes.isEmpty()) {
            System.out.println("抱歉，未找到符合您偏好的菜品。");
        } else {
            System.out.println("ID\t菜名\t\t价格\t评分\t窗口位置\t素食");
            System.out.println("--------------------------------------------------");
            for (Dish dish : dishes) {
                System.out.printf("%d\t%-10s\t%.2f\t%.1f\t%-10s\t%s\n",
                        dish.getDishId(),
                        dish.getDishName(),
                        dish.getPrice(),
                        dish.getAverageRating(),
                        dish.getWindowLocation(),
                        dish.isVegetarian() ? "是" : "否");
            }
        }
    }
    
    /**
     * 更新用户信息
     */
    private static void updateUserInfo(User user) {
        System.out.println("\n=== 修改个人信息 ===");
        System.out.println("1. 修改密码");
        System.out.println("2. 修改饮食偏好");
        System.out.println("3. 返回上级菜单");
        System.out.print("请选择 (1-3): ");
        
        int choice = getIntInput();
        switch (choice) {
            case 1:
                updatePassword(user);
                break;
            case 2:
                updatePreference(user);
                break;
            case 3:
                return;
            default:
                System.out.println("无效选项，返回上级菜单。");
        }
    }
    
    /**
     * 更新用户密码
     */
    private static void updatePassword(User user) {
        System.out.print("请输入当前密码: ");
        String currentPassword = scanner.nextLine();
        
        if (!currentPassword.equals(user.getPassword())) {
            System.out.println("当前密码错误，无法修改。");
            return;
        }
        
        System.out.print("请输入新密码: ");
        String newPassword = scanner.nextLine();
        System.out.print("请再次输入新密码: ");
        String confirmPassword = scanner.nextLine();
        
        if (!newPassword.equals(confirmPassword)) {
            System.out.println("两次输入的密码不一致，修改失败。");
            return;
        }
        
        try {
            String query = "UPDATE Users SET password = ? WHERE userId = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, newPassword);
            pstmt.setInt(2, user.getUserId());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                user.setPassword(newPassword);
                System.out.println("密码修改成功！");
            } else {
                System.out.println("密码修改失败，请稍后重试。");
            }
            
            pstmt.close();
        } catch (SQLException e) {
            System.err.println("修改密码时发生错误: " + e.getMessage());
        }
    }
    
    /**
     * 更新用户饮食偏好
     */
    private static void updatePreference(User user) {
        System.out.print("请输入新的饮食偏好 (例如: 素食,辣,甜): ");
        String newPreference = scanner.nextLine();
        
        try {
            // 使用小写表名和下划线列名
            String query = "UPDATE users SET preference = ? WHERE user_id = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, newPreference);
            pstmt.setInt(2, user.getUserId()); // user.getUserId() 获取的是正确的 ID
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                user.setPreference(newPreference); // 更新内存中的 User 对象
                System.out.println("饮食偏好更新成功！");
            } else {
                System.out.println("更新失败，未找到用户信息。");
            }
            
            pstmt.close();
        } catch (SQLException e) {
            System.err.println("更新饮食偏好时发生错误: " + e.getMessage());
        }
    }
    
    /**
     * 获取整数输入
     */
    private static int getIntInput() {
        int input = 0;
        boolean validInput = false;
        
        while (!validInput) {
            try {
                String line = scanner.nextLine();
                input = Integer.parseInt(line);
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.print("请输入有效的数字: ");
            }
        }
        
        return input;
    }
}