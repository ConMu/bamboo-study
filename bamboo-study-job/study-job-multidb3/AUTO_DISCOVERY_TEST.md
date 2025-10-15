# 自动Mapper发现功能测试指南

## 🆕 **新功能亮点**

### ✨ **自动Mapper发现**
- **零维护**: 不再需要手动配置每个Mapper
- **自动扫描**: Spring启动时自动发现所有Mapper接口
- **默认配置**: 所有发现的Mapper默认使用ds0数据源
- **动态扩展**: 添加新Mapper时无需修改任何配置代码

## 🔄 **工作原理**

### 📡 **自动发现机制**
```java
@PostConstruct
public void initDefaultConfig() {
    // 1. 扫描Spring容器中的所有Bean
    // 2. 过滤出com.conmu.sms.dao.mapper包下的Mapper接口
    // 3. 自动注册到数据源映射表，默认使用ds0
    // 4. 输出发现日志，便于监控
}
```

### 🎯 **启动日志示例**
```
🔍 [自动发现] UserMapper -> ds0
🔍 [自动发现] PeopleMapper -> ds0
✅ [初始化完成] 自动发现并配置了 2 个Mapper，默认数据源: ds0
```

## 🧪 **功能测试**

### **Step 1: 启动应用观察自动发现**
```bash
cd bamboo-study-job/study-job-multidb3
mvn spring-boot:run

# 观察控制台输出，应该看到自动发现的日志
```

### **Step 2: 查看自动发现的配置**
```bash
curl "http://localhost:8892/api/datasource/config"

# 预期响应: 所有Mapper默认配置为ds0
{
  "code": 200,
  "data": {
    "totalMappers": 2,
    "mappings": {
      "com.conmu.sms.dao.mapper.UserMapper": "ds0",
      "com.conmu.sms.dao.mapper.PeopleMapper": "ds0"
    },
    "configInfo": "📋 当前Mapper数据源配置:\n  - UserMapper -> ds0\n  - PeopleMapper -> ds0\n"
  }
}
```

### **Step 3: 查看动态Mapper列表**
```bash
curl "http://localhost:8892/api/datasource/mappers"

# 响应会包含自动发现的所有Mapper
{
  "code": 200,
  "data": {
    "supportedMappers": ["UserMapper", "PeopleMapper"],
    "totalMappers": 2
  }
}
```

### **Step 4: 测试热切换**
```bash
# 将UserMapper切换到ds1
curl -X POST "http://localhost:8892/api/datasource/switch" \
  -d "mapperName=UserMapper&dataSource=ds1"

# 验证配置已更新
curl "http://localhost:8892/api/datasource/config"
```

### **Step 5: 测试业务接口**
```bash
# 用户操作现在使用ds1
curl -X POST "http://localhost:8892/api/user" \
  -H "Content-Type: application/json" \
  -d '{"username": "auto_user", "password": "123456"}'

# 人员操作仍然使用ds0  
curl -X POST "http://localhost:8892/api/people" \
  -H "Content-Type: application/json" \
  -d '{"name": "auto_people", "age": 30}'
```

## 🆕 **扩展测试**

### 添加新的Mapper (模拟扩展场景)

假设您添加了一个新的`ProductMapper`:

1. **创建Mapper接口**:
```java
// src/main/java/com/conmu/sms/dao/mapper/ProductMapper.java
@Repository
public interface ProductMapper {
    int insert(Product product);
    Product findById(Long id);
}
```

2. **重启应用**:
```bash
mvn spring-boot:run
```

3. **观察自动发现**:
```
🔍 [自动发现] UserMapper -> ds0
🔍 [自动发现] PeopleMapper -> ds0  
🔍 [自动发现] ProductMapper -> ds0    # ✅ 新增的Mapper自动发现
✅ [初始化完成] 自动发现并配置了 3 个Mapper，默认数据源: ds0
```

4. **验证新Mapper已支持热切换**:
```bash
# 查看支持的Mapper列表
curl "http://localhost:8892/api/datasource/mappers"

# 切换新Mapper到ds1
curl -X POST "http://localhost:8892/api/datasource/switch" \
  -d "mapperName=ProductMapper&dataSource=ds1"
```

## 🎯 **优势对比**

### ❌ **之前的手动模式**
```java
private void initDefaultConfig() {
    // 每次新增Mapper都要手动添加配置
    mapperDataSourceMap.put("com.conmu.sms.dao.mapper.PeopleMapper", "ds0");
    mapperDataSourceMap.put("com.conmu.sms.dao.mapper.UserMapper", "ds1");
    mapperDataSourceMap.put("com.conmu.sms.dao.mapper.ProductMapper", "ds0"); // 手动添加
    mapperDataSourceMap.put("com.conmu.sms.dao.mapper.OrderMapper", "ds0");   // 手动添加
    // ... 每个新Mapper都需要手动配置
}
```

### ✅ **现在的自动模式**
```java
@PostConstruct
public void initDefaultConfig() {
    // 自动发现所有Mapper，无需手动维护
    // 新增Mapper时零配置，自动生效
    // 降级机制保证稳定性
}
```

## 🔧 **技术细节**

### **自动发现算法**
1. **容器扫描**: 获取Spring容器中的所有Bean
2. **接口过滤**: 检查Bean的接口是否在Mapper包下
3. **命名验证**: 确认接口名以"Mapper"结尾
4. **自动注册**: 将符合条件的Mapper注册到配置表

### **降级机制**
- 如果自动发现失败，会降级到手动配置模式
- 保证系统的稳定性和可用性
- 详细的错误日志便于问题排查

### **性能优化**
- 仅在应用启动时执行一次扫描
- 使用ConcurrentHashMap确保线程安全
- 避免运行时的性能开销

## 🎊 **总结**

现在的自动Mapper发现功能实现了：

- ✅ **零维护**: 添加新Mapper无需修改配置代码
- ✅ **自动发现**: 启动时自动扫描并配置所有Mapper
- ✅ **统一默认**: 所有Mapper默认使用ds0，可随时热切换
- ✅ **向后兼容**: 保持所有现有API不变
- ✅ **稳定可靠**: 降级机制保证系统稳定性

这个改进让系统更加智能和易维护，真正实现了"添加Mapper即可用"的开发体验！🚀