# SpringBoot + MyBatis + Druid 多数据源示例

## 模块介绍

这是一个SpringBoot + MyBatis + Druid多数据源的示例模块，演示了如何在同一个应用中配置和使用多个数据库。

## 技术栈

- **Spring Boot**: 2.1.3
- **MyBatis**: Spring Boot Starter 2.0.1
- **数据库连接池**: Alibaba Druid 1.1.10
- **数据库**: MySQL 8.0+
- **API文档**: Swagger 2.9.2
- **工具库**: Lombok, Jackson, Guava

## 项目特点

### 🎯 **多数据源管理**
- 支持多个数据库实例
- 独立的数据源配置
- 分离的Mapper和Service层
- 独立的事务管理

### 📊 **完整的CRUD操作**
- 用户管理 (数据源1)
- 人员管理 (数据源2)
- 逻辑删除支持
- 审计字段自动维护

### 🛠️ **企业级特性**
- 统一异常处理
- 标准API响应格式
- 中文编码支持
- 参数验证和预处理
- Swagger在线文档

## 数据源配置

### 数据源1 (DB1)
- **数据库**: yunxin_recovery
- **端口**: 3306
- **用途**: 用户信息管理
- **表**: users
- **包路径**: `com.conmu.sms.dao.mapper.db1`

### 数据源2 (DB2)  
- **数据库**: test
- **端口**: 4407
- **用途**: 人员信息管理
- **表**: people
- **包路径**: `com.conmu.sms.dao.mapper.db2`

## 快速开始

### 1. 数据库准备

执行 `src/main/resources/init.sql` 脚本创建表和初始化数据：

```sql
-- 执行字符集修复（重要！）
source src/main/resources/fix_charset.sql

-- 创建表和初始化数据
source src/main/resources/init.sql
```

### 2. 配置文件

检查 `src/main/resources/application.yml` 中的数据库连接信息：

```yaml
server:
  port: 8890
  datasource:
    db1:
      url: jdbc:mysql://127.0.0.1:3306/yunxin_recovery?serverTimezone=UTC&useUnicode=true&characterEncoding=utf8mb4&useSSL=false&allowPublicKeyRetrieval=true&connectTimeout=5000
      username: root
      password: your_password
    db2:
      url: jdbc:mysql://127.0.0.1:4407/test?serverTimezone=UTC&useUnicode=true&characterEncoding=utf8mb4&useSSL=false&allowPublicKeyRetrieval=true&connectTimeout=5000
      username: root
      password: your_password
```

### 3. 启动应用

```bash
cd study-job-multidb1
mvn spring-boot:run
```

### 4. 访问Swagger

打开浏览器访问: http://localhost:8890/swagger-ui.html

## API接口

### 用户管理 (数据源1)

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | `/api/user` | 创建用户 |
| GET | `/api/user/{username}` | 根据用户名查询 |
| PUT | `/api/user` | 更新用户信息 |

### 人员管理 (数据源2)

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | `/api/people` | 创建人员 |
| GET | `/api/people/{name}` | 根据姓名查询 |
| GET | `/api/people/id/{id}` | 根据ID查询 |
| PUT | `/api/people` | 更新人员信息 |
| DELETE | `/api/people/{id}` | 删除人员(逻辑删除) |

### 系统接口

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | `/health/check` | 健康检查 |
| GET | `/api/test` | 简单测试 |

## 测试示例

### 创建用户

```bash
curl -X POST http://localhost:8890/api/user \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "123456",
    "realName": "管理员",
    "email": "admin@example.com",
    "phone": "13800138000",
    "status": 1,
    "roleId": 1
  }'
```

### 创建人员

```bash
curl -X POST http://localhost:8890/api/people \
  -H "Content-Type: application/json" \
  -d '{
    "name": "张三",
    "age": 25,
    "email": "zhangsan@example.com",
    "phone": "13900139000",
    "gender": 1,
    "position": "软件工程师",
    "address": "北京市朝阳区"
  }'
```

## 数据库表结构

### users表 (数据源1)

```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
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
    deleted INT DEFAULT 0 COMMENT '是否删除 0-未删除 1-已删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

### people表 (数据源2)

```sql
CREATE TABLE people (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
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
    deleted INT DEFAULT 0 COMMENT '是否删除 0-未删除 1-已删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

## 核心配置解析

### 多数据源配置

```java
@Configuration
@MapperScan(basePackages = "com.conmu.sms.dao.mapper.db1",
           sqlSessionTemplateRef = "db1SqlSessionTemplate")
public class DB1DataSourceConfig {
    // 数据源1配置
}

@Configuration  
@MapperScan(basePackages = "com.conmu.sms.dao.mapper.db2",
           sqlSessionTemplateRef = "db2SqlSessionTemplate")
public class DB2DataSourceConfig {
    // 数据源2配置
}
```

### 包路径分离

```
com.conmu.sms.dao.mapper/
├── db1/                    # 数据源1的Mapper
│   └── UserMapper.java
└── db2/                    # 数据源2的Mapper
    └── PeopleMapper.java
```

### XML映射文件路径

```
src/main/resources/com/conmu/sms/dao/mapper/
├── db1/                    # 数据源1的XML
│   └── UserMapper.xml
└── db2/                    # 数据源2的XML
    └── PeopleMapper.xml
```

## 常见问题

### 1. 中文编码问题
确保数据库和连接URL都使用 `utf8mb4` 编码：
```sql
ALTER TABLE users CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. 数据源切换失败
检查 `@MapperScan` 的包路径配置是否正确。

### 3. Jackson序列化错误
已配置兼容Spring Boot 2.1.3的Jackson版本。

## 扩展功能

### 添加新数据源
1. 创建新的DataSourceConfig配置类
2. 在 `application.yml` 中添加数据源配置
3. 创建对应的Mapper包路径
4. 添加XML映射文件

### 集成其他特性
- 分页查询 (PageHelper)
- 数据库监控 (Druid Monitor)
- 读写分离
- 分库分表

## 性能优化

- 使用连接池管理数据库连接
- 合理配置连接池参数
- 使用预编译语句
- 添加数据库索引
- 实现查询结果缓存

---

更多技术细节和最佳实践，请参考源码中的注释和配置。