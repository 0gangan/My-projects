-- 数据库名
CREATE DATABASE IF NOT EXISTS takeout_db CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE takeout_db;

-- 用户表
CREATE TABLE users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  phone VARCHAR(20),
  email VARCHAR(100),
  role VARCHAR(20) NOT NULL DEFAULT 'ROLE_USER', -- 角色：ROLE_USER, ROLE_MERCHANT, ROLE_ADMIN
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 商家表
CREATE TABLE merchants (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  address VARCHAR(255),
  phone VARCHAR(20),
  description TEXT,
  user_id BIGINT NOT NULL, -- 关联 users 表，商家登录账号
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_merchant_user FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 分类表
CREATE TABLE categories (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  description VARCHAR(255),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 菜品表
CREATE TABLE dishes (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  merchant_id BIGINT NOT NULL,
  category_id BIGINT,
  name VARCHAR(100) NOT NULL,
  description TEXT,
  price DECIMAL(10,2) NOT NULL,
  image_url VARCHAR(255),
  status TINYINT NOT NULL DEFAULT 1, -- 1 上架，0 下架
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_dish_merchant FOREIGN KEY (merchant_id) REFERENCES merchants(id),
  CONSTRAINT fk_dish_category FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- 订单表
CREATE TABLE orders (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  merchant_id BIGINT NOT NULL,
  total_amount DECIMAL(10,2) NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'PENDING', -- PENDING, ACCEPTED, DELIVERED, CANCELLED 等
  order_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  delivery_address VARCHAR(255),
  phone VARCHAR(20),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_order_user FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT fk_order_merchant FOREIGN KEY (merchant_id) REFERENCES merchants(id)
);

-- 订单详情表（每个订单的菜品明细）
CREATE TABLE order_details (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  order_id BIGINT NOT NULL,
  dish_id BIGINT NOT NULL,
  quantity INT NOT NULL DEFAULT 1,
  price DECIMAL(10,2) NOT NULL, -- 下单时的单价，防止菜品价格变动影响历史数据
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_orderdetail_order FOREIGN KEY (order_id) REFERENCES orders(id),
  CONSTRAINT fk_orderdetail_dish FOREIGN KEY (dish_id) REFERENCES dishes(id)
);

-- 购物车表（用户未下单的菜品）
CREATE TABLE carts (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  dish_id BIGINT NOT NULL,
  quantity INT NOT NULL DEFAULT 1,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_cart_user FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT fk_cart_dish FOREIGN KEY (dish_id) REFERENCES dishes(id),
  UNIQUE KEY unique_cart_item (user_id, dish_id)
);



INSERT INTO dishes (
    id, merchant_id, category_id, name, description, price, image_url,
    status, created_at, updated_at, available
) VALUES
(6, 1, 1, '宫保鸡丁', '经典川菜', 18.50, 'http://example.com/image.jpg', 1, '2025-06-11 12:19:14', '2025-06-11 12:19:14', TRUE),
(7, 1, 1, '鱼香肉丝', '经典川菜', 21.00, 'http://example.com/image.jpg', 1, '2025-06-11 12:34:57', '2025-06-15 22:42:44', TRUE),
(8, 3, NULL, '鱼香肉丝', '经典川菜', 28.50, 'xxx.jpg', 1, '2025-06-11 22:35:53', '2025-06-11 22:35:53', TRUE),
(9, 4, NULL, '炸鸡', '经典小吃', 20.00, 'xxx.jpg', 1, '2025-06-12 09:24:53', '2025-06-12 09:24:56', TRUE),
(10, 4, NULL, '汉堡', '经典小吃', 7.00, 'xxx.jpg', 1, '2025-06-12 09:25:05', '2025-06-12 09:25:02', TRUE);

INSERT INTO merchants (
    id, name, address, phone, description, user_id, created_at, updated_at
) VALUES
(1, '测试的商家', '福州鼓楼区软件园A区', '13800138000', NULL, 2, '2025-06-11 04:15:56', '2025-06-12 10:15:05'),
(3, '重庆小面旗舰店', '四川成都一环路南段99号', '13388887777', '正宗重庆小面，全国连锁', 1, '2025-06-11 14:04:43', '2025-06-12 05:32:10'),
(4, '华莱士', '福州马尾区xxx路', NULL, '炸鸡汉堡', 3, '2025-06-12 09:22:07', '2025-06-12 09:22:13');

INSERT INTO order_details (
    id, order_id, dish_id, quantity, price, created_at, updated_at
) VALUES
(4, 4, 6, 2, 30.00, '2025-06-11 12:35:34', '2025-06-11 12:35:34'),
(5, 4, 7, 1, 60.50, '2025-06-11 12:35:34', '2025-06-11 12:35:34'),
(6, 5, 6, 2, 37.00, '2025-06-11 19:40:59', '2025-06-11 19:40:59'),
(7, 5, 7, 1, 17.00, '2025-06-11 19:40:59', '2025-06-11 19:40:59'),
(8, 6, 6, 1, 18.50, '2025-06-11 20:48:09', '2025-06-11 20:48:09'),
(9, 7, 6, 20, 370.00, '2025-06-11 20:49:44', '2025-06-11 20:49:44'),
(10, 8, 6, 10, 185.00, '2025-06-11 20:58:22', '2025-06-11 20:58:22'),
(11, 9, 6, 5, 92.50, '2025-06-11 20:58:33', '2025-06-11 20:58:33'),
(12, 9, 7, 5, 85.00, '2025-06-11 20:58:33', '2025-06-11 20:58:33'),
(13, 10, 6, 1, 18.50, '2025-06-12 09:56:42', '2025-06-12 09:56:42'),
(14, 11, 6, 6, 18.50, '2025-06-12 09:57:08', '2025-06-12 09:57:08'),
(15, 11, 7, 6, 17.00, '2025-06-12 09:57:08', '2025-06-12 09:57:08'),
(16, 12, 6, 1, 18.50, '2025-06-12 10:27:46', '2025-06-12 10:27:46'),
(17, 13, 6, 4, 18.50, '2025-06-12 10:45:09', '2025-06-12 10:45:09'),
(18, 13, 7, 2, 21.00, '2025-06-12 10:45:09', '2025-06-12 10:45:09');

INSERT INTO orders (
    id, user_id, merchant_id, total_amount, status, order_time,
     created_at, updated_at, address
) VALUES
(4, 1, 1, 120.50, '待付款', '2025-06-11 04:00:00', '2025-06-11 12:35:34', '2025-06-11 12:35:34', '福建省福州市鼓楼区XX街道XX号'),
(5, 1, 1, 54.00, '待付款', '2025-06-11 11:40:59', '2025-06-11 19:40:59', '2025-06-11 19:40:59', '福州鼓楼区XXX小区XX栋'),
(6, 1, 1, 18.50, '待付款', '2025-06-11 12:48:09',  '2025-06-11 20:48:09', '2025-06-11 20:48:09', '福师大旗山校区桃9'),
(7, 1, 1, 370.00, '待付款', '2025-06-11 12:49:44',  '2025-06-11 20:49:44', '2025-06-11 20:49:44', '福师大旗山校区桃9'),
(8, 1, 1, 185.00, '待付款', '2025-06-11 12:58:23',  '2025-06-11 20:58:22', '2025-06-11 20:58:22', '福师大旗山校区桃9'),
(9, 1, 1, 177.50, '待付款', '2025-06-11 12:58:34',  '2025-06-11 20:58:33', '2025-06-11 20:58:33', '福师大旗山校区桃9'),
(10, 1, 1, 18.50, '待支付', '2025-06-12 01:56:42', '2025-06-12 09:56:42', '2025-06-12 09:56:42', '福师大旗山校区桃9'),
(11, 1, 1, 213.00, '待支付', '2025-06-12 01:57:09',  '2025-06-12 09:57:08', '2025-06-12 09:57:08', '福师大仓山校区'),
(12, 1, 1, 18.50, '待支付', '2025-06-12 02:27:46',  '2025-06-12 10:27:46', '2025-06-12 10:27:46', '福师大旗山校区桃9'),
(13, 1, 1, 116.00, '待支付', '2025-06-12 02:45:10',  '2025-06-12 10:45:09', '2025-06-12 10:45:09', '福师大仓山校区'),
(14, 1, 1, 137.00, '待支付', '2025-06-14 14:51:50',  '2025-06-14 22:51:49', '2025-06-14 22:51:49', '福师大旗山校区桃9'),
(15, 1, 1, 76.50, '待支付', '2025-06-15 14:36:17',  '2025-06-15 22:36:16', '2025-06-15 22:36:16', '福师大旗山校区桃9');

INSERT INTO users (
    id, username, password, phone, email, role, created_at, updated_at, address
) VALUES
(1, 'test_user', '$2a$10$Sbl486jRnl.MPbE1MtKKbutzF.AoPT6n48kIjFvESJsIQQzP2AfN2', '15115511550', 'testuser@163.com', 'BUYER', '2025-06-10 17:04:53', '2025-06-15 22:43:20', '福师大旗山校区桃9'),
(2, 'lzg', '$2a$10$1PNPypBox1wPc1V0gQf0Ce/vq0aqtEETIwaccSuc57O8R0KqN1aem', '15160166666', '3523099980@qq.com', 'MERCHANT', '2025-06-11 09:23:50', '2025-06-12 03:11:06', '福师大仓山校区'),
(3, 'wzy', '$2a$10$oofGSYEayyd1pm7emMLGq.RuIyqvc/xoO0DhbmrKYsW5Co3LiLEsq', '', '', 'MERCHANT', '2025-06-12 09:17:59', '2025-06-12 09:18:41', NULL),
(4, 'wbc', '$2a$10$4KMrXoRgPoY2PLEaGpdiXOhvFAR6W5/GWBOVexGT7bzKymqiN9VHu', '', '', 'BUYER', '2025-06-12 09:26:13', '2025-06-12 09:27:56', NULL);
