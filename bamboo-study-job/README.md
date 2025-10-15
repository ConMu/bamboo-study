# SpringBoot Job 学习模块

## 项目介绍

这是一个Spring Boot Job相关学习的父项目，包含多个子模块，每个模块专注于不同的技术栈和实现方式。

## 项目结构

```
bamboo-study-job/                          # 父模块
├── pom.xml                               # 父模块POM，管理公共依赖版本
├── README.md                             # 项目总体说明
└── study-job-multidb1/                   # 子模块1：多数据源示例
    ├── pom.xml                          # 子模块POM
    ├── README.md                        # 子模块说明
    └── src/main/java/com/conmu/sms/
        ├── Application.java             # 启动类
        ├── config/                      # 数据源配置
        │   ├── DB1DataSourceConfig.java # 数据源1配置（UserMapper）
        │   └── DB2DataSourceConfig.java # 数据源2配置（PeopleMapper）
        ├── controller/
        │   └── TestController.java      # 测试控制器
        ├── dao/
        │   ├── entity/
        │   │   ├── User.java           # 用户实体
        │   │   └── People.java         # 人员实体
        │   └── mapper/
        │       ├── db1/
        │       │   └── UserMapper.java  # 数据源1的Mapper
        │       └── db2/
        │           └── PeopleMapper.java # 数据源2的Mapper
        └── service/
            ├── UserService.java         # 用户服务
            └── PeopleService.java       # 人员服务
```

## 子模块说明

### study-job-multidb1
SpringBoot + MyBatis + Druid 多数据源示例模块，演示了如何在同一个应用中配置和使用多个数据库。

**技术栈**:
- Spring Boot 2.1.3
- MyBatis
- Druid 连接池
- MySQL 数据库
- Swagger API 文档

**功能特点**:
- 多数据源配置管理
- 动态数据源切换
- 完整的CRUD操作
- RESTful API设计
- Swagger在线文档

详细使用说明请查看 [study-job-multidb1/README.md](study-job-multidb1/README.md)

## 数据源配置

### 数据源1 (DB1)
- **数据库**: yunxin_recovery
- **端口**: 3306
- **用途**: UserMapper操作
- **表**: users

### 数据源2 (DB2)  
- **数据库**: test
- **端口**: 4407
- **用途**: PeopleMapper操作
- **表**: people

## 配置说明

### application.yml
```yaml
server:
  port: 8890
  datasource:
    db1:
      type: com.alibaba.druid.pool.DruidDataSource
      url: jdbc:mysql://127.0.0.1:3306/yunxin_recovery?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&connectTimeout=5000
      username: root
      password: xu20141210chen
      driverClassName: com.mysql.cj.jdbc.Driver
    db2:
      type: com.alibaba.druid.pool.DruidDataSource
      url: jdbc:mysql://127.0.0.1:4407/test?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&connectTimeout=5000
      username: root
      password: root
      driverClassName: com.mysql.cj.jdbc.Driver
```

## 数据库初始化

请在相应的数据库中执行 `src/main/resources/init.sql` 脚本来创建表和初始化数据。

```sql
-- 在yunxin_recovery数据库中执行
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 在test数据库中执行  
CREATE TABLE IF NOT EXISTS people (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT,
    email VARCHAR(150),
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

## API测试

启动应用后，可以通过以下API测试多数据源功能：

### UserMapper (数据源1) API

```bash
# 创建用户 (DB1)
curl -X POST http://localhost:8890/api/user \
  -H "Content-Type: application/json" \
  -d '{"username": "testuser"}'

# 查询用户 (DB1)
curl http://localhost:8890/api/user/testuser
```

### PeopleMapper (数据源2) API

```bash
# 创建人员 (DB2)
curl -X POST http://localhost:8890/api/people \
  -H "Content-Type: application/json" \
  -d '{"name": "张三", "age": 25, "email": "zhangsan@example.com"}'

# 按姓名查询人员 (DB2)
curl http://localhost:8890/api/people/张三

# 按ID查询人员 (DB2)
curl http://localhost:8890/api/people/id/1

# 更新人员信息 (DB2)
curl -X PUT http://localhost:8890/api/people \
  -H "Content-Type: application/json" \
  -d '{"id": 1, "name": "张三", "age": 26, "email": "zhangsan_new@example.com"}'

# 删除人员 (DB2)
curl -X DELETE http://localhost:8890/api/people/1
```

## 技术要点

1. **多数据源配置**: 使用不同的包路径来分离不同数据源的Mapper
2. **事务管理**: 每个数据源都有独立的事务管理器
3. **连接池**: 使用Druid连接池管理数据库连接
4. **MyBatis配置**: 每个数据源都有独立的SqlSessionFactory和SqlSessionTemplate

## 启动项目

```bash
cd bamboo-study-job
mvn spring-boot:run
```

## 📱 **Swagger API文档**

启动项目后，可以通过以下方式访问API文档和测试界面：

- **Swagger UI**: http://localhost:8890/swagger-ui.html
- **API文档JSON**: http://localhost:8890/v2/api-docs
- **首页重定向**: http://localhost:8890/ (自动跳转到Swagger UI)

### 🎯 **在Swagger中测试多数据源**

1. **访问Swagger UI**: 打开浏览器访问 http://localhost:8890/swagger-ui.html
2. **选择接口**: 在"多数据源测试接口"分组中选择要测试的API
3. **填写参数**: 点击"Try it out"按钮，填写请求参数
4. **执行调用**: 点击"Execute"按钮发送请求
5. **查看结果**: 在Response部分查看返回结果

### 📊 **测试示例**

**创建用户 (数据源1)**:
```json
{
  "username": "testuser",
  "password": "123456", 
  "realName": "测试用户",
  "email": "test@example.com",
  "phone": "13800138888",
  "status": 1
}
```

**创建人员 (数据源2)**:
```json
{
  "name": "李明",
  "age": 30,
  "email": "liming@example.com", 
  "phone": "13900139999",
  "gender": 1,
  "position": "高级工程师",
  "address": "深圳市南山区"
}
```

## ⚠️ **重要提示**

### 数据库字符集配置
在使用前，请确保数据库支持UTF-8编码以正确处理中文：

1. **执行字符集修复脚本**:
   ```sql
   -- 在MySQL中执行 src/main/resources/fix_charset.sql
   source /path/to/fix_charset.sql
   ```

2. **或者手动执行以下命令**:
   ```sql
   ALTER DATABASE yunxin_recovery CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ALTER DATABASE test CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   
   ALTER TABLE users CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ALTER TABLE people CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

### 已修复的问题
- ✅ **中文编码问题**: 修复了中文字符无法正确存储的问题
- ✅ **数据类型处理**: 自动处理数字类型的phone字段转换为字符串
- ✅ **参数验证**: 添加了参数预处理和默认值设置
- ✅ **Jackson版本冲突**: 使用兼容Spring Boot 2.1.3的Jackson版本

访问 http://localhost:8890 即可开始测试。
