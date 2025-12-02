-- 数据库字符集修复脚本
-- 执行此脚本前请先备份数据库

-- 修改数据库字符集
ALTER DATABASE yunxin_recovery CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER DATABASE test CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 修改users表字符集 (在yunxin_recovery数据库中执行)
USE yunxin_recovery;
ALTER TABLE users CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 修改people表字符集 (在test数据库中执行)
USE test;  
ALTER TABLE people CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 验证字符集设置
SHOW CREATE TABLE users;
SHOW CREATE TABLE people;