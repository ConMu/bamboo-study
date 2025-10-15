# study-job-multidb2 服务问题分析报告

## 🚨 **严重问题**

### 1. **端口冲突** ⚠️
```yaml
# 问题：两个服务使用相同端口
study-job-multidb1: port 8890
study-job-multidb2: port 8890
```
**影响**: 无法同时启动两个服务，会导致端口占用错误。

### 2. **数据源配置不一致** ⚠️
- **multidb1**: 使用独立数据源配置 (`DB1DataSourceConfig`, `DB2DataSourceConfig`)
- **multidb2**: 使用动态数据源配置 (`DynamicDataSource`, `DataSourceAspect`)

**影响**: 两种不同的多数据源实现方式，可能导致混淆和维护困难。

### 3. **配置文件映射错误** ❌
```java
// DynamicDataSourceConfig.java 第13行
@MapperScan(basePackages = "com.haoqian.dynamic_data_dource.mapper", ...)
// ❌ 错误的包路径，应该是：
@MapperScan(basePackages = "com.conmu.sms.dao.mapper", ...)
```

### 4. **数据源配置前缀不匹配** ❌
```java
// DynamicDataSourceConfig.java
@ConfigurationProperties(prefix = "spring.datasource.db1")        // ❌
@ConfigurationProperties(prefix = "spring.datasource.secondary")  // ❌

// 但application.yml中配置是：
server.datasource.db1  // ✅
server.datasource.db2  // ✅
```

## ⚠️ **潜在问题**

### 5. **动态数据源实现不完整**
```java
// DataSourceAspect.java - AOP切面
@Pointcut("execution(* com.conmu.sms.controller..*.*(..))")
// 问题：依赖HTTP请求头来切换数据源，但Controller代码中没有实现数据源切换逻辑
```

### 6. **缺少数据源注解或声明**
- Service层没有明确指定使用哪个数据源
- 缺少 `@DS` 或类似的数据源切换注解
- Controller中没有设置请求头来指定数据源

### 7. **MyBatis配置路径问题**
```java
// DynamicDataSourceConfig.java
bean.setMapperLocations(
    new PathMatchingResourcePatternResolver()
        .getResources("classpath:com/conmu/sms/dao/mapper/*/*.xml"));
// 问题：路径模式可能无法正确匹配 db1/db2 子目录
```

## ✅ **修复方案**

### 1. **修复端口冲突**
```yaml
# application.yml
server:
  port: 8891  # 改为不同端口
```

### 2. **修复配置前缀**
```java
@ConfigurationProperties(prefix = "server.datasource.db1")
@ConfigurationProperties(prefix = "server.datasource.db2")
```

### 3. **修复包路径扫描**
```java
@MapperScan(basePackages = "com.conmu.sms.dao.mapper")
```

### 4. **完善动态数据源逻辑**
需要在Service层添加数据源切换逻辑：
```java
@Service
public class UserService {
    public int insert(User user) {
        DataSourceHolder.setDataSource("ds0"); // 使用数据源1
        return userMapper.insert(user);
    }
}
```

### 5. **修复XML路径映射**
```java
bean.setMapperLocations(
    new PathMatchingResourcePatternResolver()
        .getResources("classpath:com/conmu/sms/dao/mapper/**/*.xml"));
```

## 🎯 **架构建议**

### 方案A: 统一使用独立数据源配置 (推荐)
- 将multidb2改为与multidb1相同的配置方式
- 删除动态数据源相关代码
- 保持代码一致性和可维护性

### 方案B: 完善动态数据源实现
- 修复所有配置错误
- 添加完整的数据源切换逻辑
- 在Controller中添加数据源选择参数

## 📋 **测试验证**

修复后需要验证：
1. ✅ 应用可以正常启动
2. ✅ 两个数据源都能正常连接
3. ✅ CRUD操作能正确路由到对应数据源
4. ✅ Swagger API文档可以访问
5. ✅ 事务管理正常工作

## 🔧 **优先修复项**

1. **立即修复**: 端口冲突 (无法启动)
2. **立即修复**: 配置前缀错误 (数据源无法初始化)  
3. **立即修复**: 包路径扫描错误 (Mapper无法找到)
4. **后续优化**: 统一多数据源实现方式

---

**总结**: `study-job-multidb2` 存在多个严重的配置错误，需要立即修复才能正常运行。建议采用与 `multidb1` 相同的数据源配置方式以保持一致性。