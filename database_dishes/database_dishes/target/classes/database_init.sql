-- 创建数据库
CREATE DATABASE IF NOT EXISTS cafeteria_db;
USE cafeteria_db;

-- 用户表
CREATE TABLE IF NOT EXISTS Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL,
    preference VARCHAR(50)
);

-- 菜品表
CREATE TABLE IF NOT EXISTS Dishes (
    dish_id INT AUTO_INCREMENT PRIMARY KEY,
    dish_name VARCHAR(100) UNIQUE NOT NULL,
    is_vegetarian TINYINT(1) DEFAULT 0,
    window_location VARCHAR(50),
    price DECIMAL(6,2) NOT NULL,
    average_rating DECIMAL(3,2) DEFAULT 0.0
);

-- 口味表
CREATE TABLE IF NOT EXISTS Taste (
    taste_id INT AUTO_INCREMENT PRIMARY KEY,
    taste_name VARCHAR(50) UNIQUE NOT NULL
);

-- 类别表
CREATE TABLE IF NOT EXISTS Categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(50) UNIQUE NOT NULL
);

-- 评分记录表
CREATE TABLE IF NOT EXISTS Reviews (
    review_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    dish_id INT NOT NULL,
    rating INT NOT NULL CHECK (rating BETWEEN 0 AND 5),
    comment TEXT,
    review_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (dish_id) REFERENCES Dishes(dish_id)
);

-- 推荐表
CREATE TABLE IF NOT EXISTS Recommendations (
    recommendation_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    dish_id INT NOT NULL,
    recommendation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (dish_id) REFERENCES Dishes(dish_id)
);

-- 菜品口味关联表
CREATE TABLE IF NOT EXISTS Dish_Taste (
    dish_id INT NOT NULL,
    taste_id INT NOT NULL,
    PRIMARY KEY (dish_id, taste_id),
    FOREIGN KEY (dish_id) REFERENCES Dishes(dish_id),
    FOREIGN KEY (taste_id) REFERENCES Taste(taste_id)
);

-- 菜品类别关联表
CREATE TABLE IF NOT EXISTS Dish_Category (
    dish_id INT NOT NULL,
    category_id INT NOT NULL,
    PRIMARY KEY (dish_id, category_id),
    FOREIGN KEY (dish_id) REFERENCES Dishes(dish_id),
    FOREIGN KEY (category_id) REFERENCES Categories(category_id)
);

-- 插入一些初始数据
-- 插入用户数据
INSERT INTO Users (username, password, preference) VALUES
('student1', 'password123', '辣'),
('student2', 'password123', '甜'),
('student3', 'password123', '酸');

-- 插入口味数据
INSERT INTO Taste (taste_name) VALUES
('辣'), ('甜'), ('酸'), ('咸'), ('麻');

-- 插入类别数据
INSERT INTO Categories (category_name) VALUES
('米饭'), ('面食'), ('汤类'), ('小吃'), ('甜点'), ('饮料');

-- 插入菜品数据
INSERT INTO Dishes (dish_name, is_vegetarian, window_location, price, average_rating) VALUES
('麻辣香锅', 0, '一楼窗口1', 15.00, 4.5),
('宫保鸡丁', 0, '一楼窗口2', 12.00, 4.2),
('西红柿鸡蛋面', 0, '二楼窗口1', 8.00, 4.0),
('清炒时蔬', 1, '二楼窗口2', 6.00, 3.8),
('红烧牛肉面', 0, '二楼窗口3', 12.00, 4.7),
('酸辣汤', 0, '三楼窗口1', 5.00, 4.1),
('糖醋里脊', 0, '三楼窗口2', 14.00, 4.3),
('水煮鱼', 0, '一楼窗口3', 20.00, 4.6),
('蛋炒饭', 0, '一楼窗口4', 8.00, 3.9),
('红豆沙', 1, '三楼窗口3', 4.00, 4.0);

-- 关联菜品和口味
INSERT INTO Dish_Taste (dish_id, taste_id) VALUES
(1, 1), -- 麻辣香锅-辣
(1, 5), -- 麻辣香锅-麻
(2, 1), -- 宫保鸡丁-辣
(2, 2), -- 宫保鸡丁-甜
(3, 4), -- 西红柿鸡蛋面-咸
(3, 3), -- 西红柿鸡蛋面-酸
(4, 4), -- 清炒时蔬-咸
(5, 4), -- 红烧牛肉面-咸
(6, 1), -- 酸辣汤-辣
(6, 3), -- 酸辣汤-酸
(7, 2), -- 糖醋里脊-甜
(7, 3), -- 糖醋里脊-酸
(8, 1), -- 水煮鱼-辣
(9, 4), -- 蛋炒饭-咸
(10, 2); -- 红豆沙-甜

-- 关联菜品和类别
INSERT INTO Dish_Category (dish_id, category_id) VALUES
(1, 1), -- 麻辣香锅-米饭
(2, 1), -- 宫保鸡丁-米饭
(3, 2), -- 西红柿鸡蛋面-面食
(4, 1), -- 清炒时蔬-米饭
(5, 2), -- 红烧牛肉面-面食
(6, 3), -- 酸辣汤-汤类
(7, 1), -- 糖醋里脊-米饭
(8, 1), -- 水煮鱼-米饭
(9, 1), -- 蛋炒饭-米饭
(10, 5); -- 红豆沙-甜点

-- 插入一些评分数据
INSERT INTO Reviews (user_id, dish_id, rating, comment) VALUES
(1, 1, 5, '非常好吃，很辣很过瘾'),
(1, 3, 4, '西红柿很新鲜，鸡蛋很嫩'),
(2, 2, 4, '鸡丁很嫩，花生很脆'),
(2, 7, 5, '酸甜可口，里脊很嫩'),
(3, 6, 4, '酸辣适中，很开胃'),
(3, 8, 5, '鱼肉很新鲜，麻辣适中');

-- 更新菜品平均评分
UPDATE Dishes d
SET average_rating = (SELECT AVG(rating) FROM Reviews r WHERE r.dish_id = d.dish_id)
WHERE dish_id IN (SELECT DISTINCT dish_id FROM Reviews);

-- 插入一些推荐数据
INSERT INTO Recommendations (user_id, dish_id) VALUES
(1, 8), -- 为用户1推荐水煮鱼
(2, 10), -- 为用户2推荐红豆沙
(3, 1); -- 为用户3推荐麻辣香锅