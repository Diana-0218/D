package com.cafeteria.model;

import java.math.BigDecimal;

/**
 * 菜品实体类
 * 对应数据库中的Dishes表
 */
public class Dish {
    private int dishId;             // 菜品ID
    private String dishName;        // 菜品名称
    private boolean isVegetarian;   // 是否素菜
    private String windowLocation;  // 窗口位置
    private BigDecimal price;       // 价格
    private BigDecimal averageRating; // 平均评分
    
    // 默认构造函数
    public Dish() {
    }
    
    // 带参数的构造函数
    public Dish(int dishId, String dishName, boolean isVegetarian, String windowLocation, 
                BigDecimal price, BigDecimal averageRating) {
        this.dishId = dishId;
        this.dishName = dishName;
        this.isVegetarian = isVegetarian;
        this.windowLocation = windowLocation;
        this.price = price;
        this.averageRating = averageRating;
    }
    
    // 不带ID的构造函数，用于创建新菜品
    public Dish(String dishName, boolean isVegetarian, String windowLocation, 
               BigDecimal price, BigDecimal averageRating) {
        this.dishName = dishName;
        this.isVegetarian = isVegetarian;
        this.windowLocation = windowLocation;
        this.price = price;
        this.averageRating = averageRating;
    }
    
    // Getter和Setter方法
    public int getDishId() {
        return dishId;
    }
    
    public void setDishId(int dishId) {
        this.dishId = dishId;
    }
    
    public String getDishName() {
        return dishName;
    }
    
    public void setDishName(String dishName) {
        this.dishName = dishName;
    }
    
    public boolean isVegetarian() {
        return isVegetarian;
    }
    
    public void setVegetarian(boolean vegetarian) {
        isVegetarian = vegetarian;
    }
    
    public String getWindowLocation() {
        return windowLocation;
    }
    
    public void setWindowLocation(String windowLocation) {
        this.windowLocation = windowLocation;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public BigDecimal getAverageRating() {
        return averageRating;
    }
    
    public void setAverageRating(BigDecimal averageRating) {
        this.averageRating = averageRating;
    }
    
    @Override
    public String toString() {
        return "Dish{" +
                "dishId=" + dishId +
                ", dishName='" + dishName + '\'' +
                ", isVegetarian=" + isVegetarian +
                ", windowLocation='" + windowLocation + '\'' +
                ", price=" + price +
                ", averageRating=" + averageRating +
                '}';
    }
}