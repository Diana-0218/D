package com.cafeteria.model;

/**
 * 用户实体类
 * 对应数据库中的Users表
 */
public class User {
    private int userId;         // 用户ID
    private String username;    // 用户名
    private String password;    // 密码
    private String preference;  // 用户偏好
    
    // 默认构造函数
    public User() {
    }
    
    // 带参数的构造函数
    public User(int userId, String username, String password, String preference) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.preference = preference;
    }
    
    // 不带ID的构造函数，用于创建新用户
    public User(String username, String password, String preference) {
        this.username = username;
        this.password = password;
        this.preference = preference;
    }
    
    // Getter和Setter方法
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getPreference() {
        return preference;
    }
    
    public void setPreference(String preference) {
        this.preference = preference;
    }
    
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", preference='" + preference + '\'' +
                '}';
    }
}