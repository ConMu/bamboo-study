-- 设置数据库字符集
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

-- 数据源1 (yunxin_recovery数据库) - 用于UserMapper
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) COMMENT '密码',
    real_name VARCHAR(100) COMMENT '真实姓名',
    email VARCHAR(150) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '电话',
    status INT DEFAULT 1 COMMENT '用户状态 1-正常 0-禁用',
    role_id BIGINT COMMENT '角色ID',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    created_by BIGINT COMMENT '创建人ID',
    updated_by BIGINT COMMENT '更新人ID',
    deleted INT DEFAULT 0 COMMENT '是否删除 0-未删除 1-已删除',
    INDEX idx_username (username),
    INDEX idx_status (status),
    INDEX idx_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 数据源2 (test数据库) - 用于PeopleMapper
CREATE TABLE IF NOT EXISTS people (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL COMMENT '姓名',
    age INT COMMENT '年龄',
    email VARCHAR(150) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '电话号码',
    gender INT COMMENT '性别 1-男 2-女 0-未知',
    department_id BIGINT COMMENT '部门ID',
    position VARCHAR(100) COMMENT '职位',
    address VARCHAR(500) COMMENT '地址',
    remark TEXT COMMENT '备注',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    created_by BIGINT COMMENT '创建人ID',
    updated_by BIGINT COMMENT '更新人ID',
    deleted INT DEFAULT 0 COMMENT '是否删除 0-未删除 1-已删除',
    INDEX idx_name (name),
    INDEX idx_department_id (department_id),
    INDEX idx_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='人员表';

-- 插入一些测试数据
-- 数据源1
INSERT IGNORE INTO users (username, password, real_name, email, phone, status, created_by, updated_by) VALUES
('admin', '$2a$10$123456', '管理员', 'admin@example.com', '13800138000', 1, 1, 1),
('user1', '$2a$10$234567', '用户1', 'user1@example.com', '13800138001', 1, 1, 1),
('test_user', '$2a$10$345678', '测试用户', 'test@example.com', '13800138002', 1, 1, 1);

-- 数据源2
INSERT IGNORE INTO people (name, age, email, phone, gender, position, address, created_by, updated_by) VALUES
('张三', 25, 'zhangsan@example.com', '13900139000', 1, '软件工程师', '北京市朝阳区', 1, 1),
('李四', 30, 'lisi@example.com', '13900139001', 2, '产品经理', '上海市浦东新区', 1, 1),
('王五', 28, 'wangwu@example.com', '13900139002', 1, '测试工程师', '深圳市南山区', 1, 1);
