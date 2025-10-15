# SpringBoot多数据源热切换系统 (MultiDB3)

## 🚀 **核心特性**

### ✨ **革新性功能**
- **Mapper级数据源绑定**: 不同Mapper可以绑定不同的数据源
- **运行时热切换**: 无需重启应用即可修改数据源配置
- **细粒度控制**: 可精确控制每个表的数据源选择
- **实时生效**: 配置修改后立即生效，影响后续所有请求

## 📋 **系统架构**

### 🏗️ **核心组件**

#### 1. **MapperDataSourceRegistry** - 数据源注册表
- 管理Mapper到数据源的映射关系
- 支持运行时动态修改配置
- 线程安全的ConcurrentHashMap存储

#### 2. **MapperContextHolder** - 上下文管理器  
- 追踪当前正在执行的Mapper
- ThreadLocal存储，确保线程安全
- 自动清理上下文，避免内存泄漏

#### 3. **DataSourceAspect** - AOP切面
- 拦截Mapper方法调用
- 根据Registry配置自动切换数据源
- 统一的切面管理，无侵入性

#### 4. **DataSourceSwitchController** - 热切换API
- 提供RESTful接口管理数据源配置  
- 支持单个/批量切换操作
- 配置查询和重置功能

## 📊 **默认配置**

### 🎯 **初始Mapper绑定**
```
PeopleMapper -> ds0 (yunxin_recovery数据库:3306)
UserMapper   -> ds1 (test数据库:4407)
```

### 🔄 **数据流转过程**
```
1. 业务请求 -> Service -> Mapper
2. AOP拦截 -> 查询Registry配置
3. 设置上下文 -> 切换数据源
4. 执行SQL -> 清理上下文
```

## 🛠️ **使用方式**

### 📡 **1. 业务接口调用**

#### 创建用户 (自动使用ds1)
```bash
curl -X POST "http://localhost:8892/api/user" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser", 
    "password": "123456",
    "email": "test@example.com"
  }'
```

#### 创建人员 (自动使用ds0)  
```bash
curl -X POST "http://localhost:8892/api/people" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "张三",
    "age": 30,
    "email": "zhangsan@example.com"
  }'
```

### 🔥 **2. 热切换管理**

#### 查看当前配置
```bash
curl "http://localhost:8892/api/datasource/config"
```

#### 单个热切换
```bash
# 将UserMapper切换到ds0数据源  
curl -X POST "http://localhost:8892/api/datasource/switch" \
  -d "mapperName=UserMapper&dataSource=ds0"

# 将PeopleMapper切换到ds1数据源
curl -X POST "http://localhost:8892/api/datasource/switch" \
  -d "mapperName=PeopleMapper&dataSource=ds1"  
```

#### 批量热切换
```bash
curl -X POST "http://localhost:8892/api/datasource/batch-switch" \
  -d "userDataSource=ds0&peopleDataSource=ds1"
```

#### 重置为默认配置
```bash
curl -X POST "http://localhost:8892/api/datasource/reset"
```

## 🧪 **完整测试流程**

### **Step 1: 启动应用**
```bash
cd bamboo-study-job/study-job-multidb3
mvn spring-boot:run
```

### **Step 2: 查看初始配置**
```bash
curl "http://localhost:8892/api/datasource/config"

# 预期响应:
{
  "code": 200,
  "data": {
    "totalMappers": 2,
    "mappings": {
      "com.conmu.sms.dao.mapper.PeopleMapper": "ds0",
      "com.conmu.sms.dao.mapper.UserMapper": "ds1"
    }
  }
}
```

### **Step 3: 测试默认配置**
```bash
# 用户操作 -> ds1数据源
curl -X POST "http://localhost:8892/api/user" \
  -H "Content-Type: application/json" \
  -d '{"username": "user_in_ds1", "password": "123456"}'

# 人员操作 -> ds0数据源  
curl -X POST "http://localhost:8892/api/people" \
  -H "Content-Type: application/json" \
  -d '{"name": "people_in_ds0", "age": 25}'
```

### **Step 4: 执行热切换**
```bash
# 切换UserMapper到ds0
curl -X POST "http://localhost:8892/api/datasource/switch" \
  -d "mapperName=UserMapper&dataSource=ds0"

# 验证配置已更新
curl "http://localhost:8892/api/datasource/config"
```

### **Step 5: 验证热切换效果**
```bash
# 现在用户操作会使用ds0数据源
curl -X POST "http://localhost:8892/api/user" \
  -H "Content-Type: application/json" \  
  -d '{"username": "user_in_ds0", "password": "123456"}'

# 查询验证数据隔离
curl "http://localhost:8892/api/user/user_in_ds1"  # 找不到 (切换后在ds0找)
curl "http://localhost:8892/api/user/user_in_ds0"  # 能找到 (在ds0中)
```

## 📈 **控制台日志**

### 🔍 **AOP执行日志**
```
🎯 [上下文] 设置当前Mapper: UserMapper
🎯 [Mapper AOP] UserMapper.insert -> 数据源: ds1
📊 [热切换] Mapper: com.conmu.sms.dao.mapper.UserMapper 数据源切换: ds1 -> ds0
🎯 [Mapper AOP] UserMapper.insert -> 数据源: ds0
🧹 [Mapper AOP] 上下文已清理
```

## 🎯 **适用场景**

### 1. **业务数据隔离**
- 用户数据存储在高性能数据库
- 日志数据存储在归档数据库
- 临时数据存储在缓存数据库

### 2. **读写分离**  
- 写操作指向主数据库
- 读操作指向从数据库
- 热切换实现故障转移

### 3. **环境切换**
- 开发环境快速切换到测试数据
- 生产数据与备份数据之间切换
- A/B测试数据源切换

### 4. **负载均衡**
- 不同业务模块使用不同数据源
- 动态调整负载分布
- 高峰期数据源扩展

## ⚠️ **注意事项**

### 🚨 **重要限制**
1. **事务边界**: 单个事务内不要切换数据源
2. **数据一致性**: 确保目标数据源有相应的表结构
3. **并发安全**: 配置修改对正在执行的事务不会立即生效
4. **监控告警**: 建议添加数据源切换的监控和告警

### ✅ **最佳实践** 
1. **测试先行**: 在测试环境充分验证后再在生产环境切换
2. **备份重要**: 切换前备份重要配置和数据  
3. **监控日志**: 密切关注切换后的系统行为
4. **回滚准备**: 准备快速回滚的方案

## 🔧 **扩展指南**

### 添加新的Mapper支持
1. 在`MapperDataSourceRegistry.initDefaultConfig()`中添加配置
2. 更新`DataSourceSwitchController`的验证逻辑  
3. 修改Swagger文档中的支持列表

### 添加新的数据源
1. 在`application.yml`中配置新数据源
2. 更新`DynamicDataSourceConfig`注册新数据源
3. 修改Registry的验证逻辑支持新数据源标识

## 🎊 **总结**

MultiDB3实现了真正的企业级多数据源热切换系统：

- ✅ **零侵入**: 业务代码无需修改  
- ✅ **热切换**: 运行时动态调整配置
- ✅ **细粒度**: Mapper级别的精确控制
- ✅ **高可用**: 线程安全的并发处理
- ✅ **易管理**: 完整的REST API管理接口

这个系统为复杂的企业应用提供了灵活、可靠的数据源管理解决方案！🚀