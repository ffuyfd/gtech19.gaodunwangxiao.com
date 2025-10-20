-- 创建数据库
CREATE DATABASE IF NOT EXISTS gtech19 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 切换到gtech19数据库
USE gtech19;

-- 创建用户表
CREATE TABLE IF NOT EXISTS user (
    id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    nickname VARCHAR(50) DEFAULT NULL COMMENT '昵称',
    email VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    phone VARCHAR(20) DEFAULT NULL COMMENT '手机',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_username (username)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 插入测试数据
INSERT INTO user (username, password, nickname, email, phone, create_time, update_time)
VALUES ('admin', '123456', '管理员', 'admin@example.com', '13800138000', NOW(), NOW());

INSERT INTO user (username, password, nickname, email, phone, create_time, update_time)
VALUES ('user1', '123456', '用户1', 'user1@example.com', '13800138001', NOW(), NOW());