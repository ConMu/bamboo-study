# 数据源配置修复和正确使用指南

## 🚨 **问题分析**

您遇到的错误是由两个问题引起的：

### 1. **数据源配置错误** ❌
```yaml
# 错误配置 - 使用url而不是jdbcUrl
db1:
  url: jdbc:mysql://...  # ❌ 应该是jdbcUrl
  
# 正确配置 - Druid数据源需要jdbcUrl
db1:  
  jdbcUrl: jdbc:mysql://...  # ✅ 正确
```

### 2. **数据源选择错误** ❌  
```bash
# 您使用的命令 - 错误的数据源选择
curl -H "dsNo: ds0" http://localhost:8891/api/people
#        ^^^^^ 错误！people接口应该使用ds1

# 正确的选择
curl -H "dsNo: ds1" http://localhost:8891/api/people
#        ^^^^^ 正确！
```

## ✅ **已修复的配置**

### 数据源配置修复
```yaml
server:
  datasource:
    db1:
      jdbcUrl: jdbc:mysql://127.0.0.1:3306/yunxin_recovery?...  # ✅ 使用jdbcUrl
      username: root
      password: xu20141210chen
      driverClassName: com.mysql.cj.jdbc.Driver
      # 添加了Druid连接池优化配置
      
    db2:  
      jdbcUrl: jdbc:mysql://127.0.0.1:4407/test?...  # ✅ 使用jdbcUrl
      username: root
      password: root
      driverClassName: com.mysql.cj.jdbc.Driver
      # 添加了Druid连接池优化配置
```

## 🎯 **正确的测试命令**

### 1. **创建人员** (使用数据源2 - ds1)
```bash
curl -X POST "http://localhost:8891/api/people" \
  -H "accept: */*" \
  -H "dsNo: ds1" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "张三",
    "age": 25,
    "email": "zhangsan@example.com",
    "phone": "13900139000",
    "address": "北京市",
    "gender": 1,
    "departmentId": 1,
    "position": "开发工程师"
  }'
```

### 2. **创建用户** (使用数据源1 - ds0)  
```bash
curl -X POST "http://localhost:8891/api/user" \
  -H "accept: */*" \
  -H "dsNo: ds0" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "123456",
    "email": "test@example.com", 
    "phone": "13800138000"
  }'
```

### 3. **查询人员** (使用数据源2 - ds1)
```bash
curl -H "dsNo: ds1" "http://localhost:8891/api/people/张三"
```

### 4. **查询用户** (使用数据源1 - ds0)
```bash  
curl -H "dsNo: ds0" "http://localhost:8891/api/user/testuser"
```

## 📋 **数据源映射表**

| dsNo | 数据库 | 端口 | 表名 | 适用接口 | 配置前缀 |
|------|--------|------|------|----------|----------|
| `ds0` | yunxin_recovery | 3306 | users | `/api/user/**` | server.datasource.db1 |
| `ds1` | test | 4407 | people | `/api/people/**` | server.datasource.db2 |

## 🔧 **重启应用进行测试**

修复配置后需要重启应用：

```bash
# 停止当前应用 (Ctrl+C)
# 重新启动
cd bamboo-study-job/study-job-multidb2
mvn spring-boot:run
```

启动成功后，应该看到类似日志：
```
Creating shared instance of singleton bean 'dateSource1'
Creating shared instance of singleton bean 'dateSource2' 
Creating shared instance of singleton bean 'dynamicDataSource'
```

## ⚠️ **注意事项**

1. **数据源选择**:
   - 人员相关操作 → 使用 `dsNo: ds1`
   - 用户相关操作 → 使用 `dsNo: ds0`
   - 测试接口 → 使用 `dsNo: ds0` 或不指定

2. **配置要点**:
   - Druid数据源使用 `jdbcUrl` 而不是 `url`  
   - 确保数据库连接信息正确
   - 检查数据库服务是否正常运行

3. **错误排查**:
   - 如果还有连接问题，检查数据库是否启动
   - 确认端口3306和4407是否可访问
   - 验证用户名密码是否正确

## 🚀 **测试验证步骤**

### Step 1: 启动应用
```bash
mvn spring-boot:run
```

### Step 2: 测试数据源1 (用户表)
```bash
curl -H "dsNo: ds0" "http://localhost:8891/api/test"
```

### Step 3: 测试数据源2 (人员表) 
```bash
curl -X POST "http://localhost:8891/api/people" \
  -H "dsNo: ds1" \
  -H "Content-Type: application/json" \
  -d '{"name": "测试用户", "age": 30}'
```

### Step 4: 查看控制台日志
应该看到切面输出：
```
当前数据源: ds0
当前数据源: ds1
```

---

## 🎊 **总结**

问题已修复：
- ✅ **配置修复**: `url` → `jdbcUrl`  
- ✅ **连接池优化**: 添加Druid配置
- ✅ **使用指南**: 明确的数据源选择规则

现在可以正常使用多数据源动态切换功能了！