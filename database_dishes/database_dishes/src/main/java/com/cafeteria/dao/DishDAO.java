package com.cafeteria.dao;

import com.cafeteria.model.Dish;
import com.cafeteria.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜品数据访问对象 (DAO)
 * 处理与Dishes表相关的数据库操作
 */
public class DishDAO {

    /**
     * 获取所有菜品
     * @return 包含所有菜品的列表
     */
    public List<Dish> getAllDishes() {
        List<Dish> dishes = new ArrayList<>();
        String sql = "SELECT * FROM dishes"; // Change Dishes to dishes
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Dish dish = mapResultSetToDish(rs);
                dishes.add(dish);
            }
        } catch (SQLException e) {
            System.err.println("获取所有菜品失败: " + e.getMessage());
        }
        return dishes;
    }

    /**
     * 根据ID查找菜品
     * @param dishId 菜品ID
     * @return 找到的菜品对象，如果未找到则返回null
     */
    public Dish findDishById(int dishId) {
        String sql = "SELECT * FROM dishes WHERE dish_id = ?"; // Change Dishes to dishes
        Dish dish = null;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, dishId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                dish = mapResultSetToDish(rs);
            }
        } catch (SQLException e) {
            System.err.println("根据ID查找菜品失败: " + e.getMessage());
        }
        return dish;
    }

    /**
     * 添加新菜品到数据库
     * @param dish 要添加的菜品对象
     * @return 添加成功返回true，否则返回false
     */
    public boolean addDish(Dish dish) {
        String sql = "INSERT INTO dishes (dish_name, is_vegetarian, window_location, price, average_rating) VALUES (?, ?, ?, ?, ?)"; // Change Dishes to dishes
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, dish.getDishName());
            pstmt.setBoolean(2, dish.isVegetarian());
            pstmt.setString(3, dish.getWindowLocation());
            pstmt.setBigDecimal(4, dish.getPrice());
            pstmt.setBigDecimal(5, dish.getAverageRating()); // Assuming initial rating or default

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        dish.setDishId(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("添加菜品失败: " + e.getMessage());
        }
        return false;
    }

    /**
     * 更新菜品信息
     * @param dish 包含更新信息的菜品对象
     * @return 更新成功返回true，否则返回false
     */
    public boolean updateDish(Dish dish) {
        String sql = "UPDATE dishes SET dish_name = ?, is_vegetarian = ?, window_location = ?, price = ?, average_rating = ? WHERE dish_id = ?"; // Change Dishes to dishes
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, dish.getDishName());
            pstmt.setBoolean(2, dish.isVegetarian());
            pstmt.setString(3, dish.getWindowLocation());
            pstmt.setBigDecimal(4, dish.getPrice());
            pstmt.setBigDecimal(5, dish.getAverageRating());
            pstmt.setInt(6, dish.getDishId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("更新菜品失败: " + e.getMessage());
        }
        return false;
    }

    /**
     * 根据ID删除菜品
     * @param dishId 要删除的菜品ID
     * @return 删除成功返回true，否则返回false
     */
    public boolean deleteDish(int dishId) {
        String sql = "DELETE FROM dishes WHERE dish_id = ?"; // Change Dishes to dishes
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, dishId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("删除菜品失败: " + e.getMessage());
        }
        return false;
    }

    /**
     * 将ResultSet映射到Dish对象
     * @param rs ResultSet对象
     * @return 映射后的Dish对象
     * @throws SQLException SQL异常
     */
    private Dish mapResultSetToDish(ResultSet rs) throws SQLException {
        Dish dish = new Dish();
        dish.setDishId(rs.getInt("dish_id"));
        dish.setDishName(rs.getString("dish_name"));
        dish.setVegetarian(rs.getBoolean("is_vegetarian"));
        dish.setWindowLocation(rs.getString("window_location"));
        dish.setPrice(rs.getBigDecimal("price"));
        dish.setAverageRating(rs.getBigDecimal("average_rating"));
        return dish;
    }
    /**
     * 根据关键字搜索菜品
     * @param keyword 搜索关键字
     * @return 符合条件的菜品列表
     */
    public List<Dish> searchDishesByKeyword(String keyword) {
        List<Dish> dishes = new ArrayList<>();
        String sql = "SELECT * FROM dishes WHERE dish_name LIKE ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 修复：正确使用LIKE语法，确保关键字两边都有%进行模糊匹配
            pstmt.setString(1, "%" + keyword + "%");

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Dish dish = mapResultSetToDish(rs);
                dishes.add(dish);
            }
        } catch (SQLException e) {
            System.err.println("搜索菜品失败: " + e.getMessage());
        }

        return dishes;
    }
}