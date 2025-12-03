# 多数据源JAR包 (MultiDB Pack)

这是一个通用的多数据源动态切换JAR包，支持根据Mapper接口自动选择对应的数据源，并提供热重载功能。

## 🏗️ 架构设计

### 核心组件

1. **`DataSourceConfigProvider`** - 配置接口，用户实现此接口提供数据源配置
2. **`DataSourceContextHolder`** - ThreadLocal上下文，存储当前线程的数据源key
3. **`DbManageRouteHolder`** - 路由管理器，管理Mapper到数据源的映射关系
4. **`MultiDbDynamicDataSource`** - 动态数据源，继承AbstractRoutingDataSource
5. **`MultiDbDataSourceAspect`** - AOP切面，拦截Mapper方法调用并自动切换数据源
6. **`MultiDbAutoConfiguration`** - 自动配置类，整合所有组件

### 工作原理

```
1. 应用启动时，MultiDbAutoConfiguration读取DataSourceConfigProvider的配置
2. 创建多个数据源并注册到MultiDbDynamicDataSource
3. DbManageRouteHolder根据配置建立Mapper→DataSource的映射关系
4. 当调用Mapper方法时，MultiDbDataSourceAspect拦截调用
5. 切面根据Mapper类名查找对应的数据源key
6. 将数据源key设置到DataSourceContextHolder
7. MultiDbDynamicDataSource根据ThreadLocal中的key选择对应的数据源
8. 方法执行完毕后自动清理ThreadLocal
```

## 📦 快速开始

### 1. 添加依赖

将此JAR包添加到你的项目依赖中（Maven或Gradle）。

### 2. 实现配置接口

```java
@Component
public class MyDataSourceConfigProvider implements DataSourceConfigProvider {
    
    @Override
    public Map<String, DataSource> getDataSources() {
        Map<String, DataSource> dataSources = new HashMap<>();
        
        // 配置主数据源
        HikariConfig mainConfig = new HikariConfig();
        mainConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        mainConfig.setJdbcUrl("jdbc:mysql://localhost:3306/main_db");
        mainConfig.setUsername("root");
        mainConfig.setPassword("password");
        dataSources.put("main", new HikariDataSource(mainConfig));
        
        // 配置用户数据源
        HikariConfig userConfig = new HikariConfig();
        userConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        userConfig.setJdbcUrl("jdbc:mysql://localhost:3306/user_db");
        userConfig.setUsername("root");
        userConfig.setPassword("password");
        dataSources.put("user", new HikariDataSource(userConfig));
        
        return dataSources;
    }
    
    
    @Override
    public Map<String, String> getMapperDataSourceMappings() {
        Map<String, String> mappings = new HashMap<>();
        mappings.put("com.example.mapper.UserMapper", "user");
        mappings.put("com.example.mapper.OrderMapper", "main");
        return mappings;
    }
    
    @Override
    public String[] getMapperPackages() {
        return new String[]{"com.example.mapper"};
    }
}
```

### 3. 创建Mapper接口

```java
@Mapper
public interface UserMapper {
    List<User> findAll();
    User findById(Long id);
}

@Mapper
public interface OrderMapper {
    List<Order> findByUserId(Long userId);
}
```

### 4. 启动应用

Spring Boot会自动发现并配置多数据源组件，无需额外配置。

## 🔄 动态管理

### 获取当前映射

```java
@Autowired
private DbManageRouteHolder routeHolder;

// 查看某个Mapper当前使用的数据源
String dataSourceKey = routeHolder.get("com.example.mapper.UserMapper");
```

### 实时配置更新

使用共享Map模式，所有配置变更都是实时生效的：

```java
@Autowired 
private AbstractDataSourceConfigProvider configProvider;

// 更新映射 - 立即生效
configProvider.updateMapping("com.example.UserMapper", "ds1");

// 批量更新 - 立即生效  
Map<String, String> newMappings = new HashMap<>();
configProvider.updateMappings(newMappings);
```

## ⚙️ 配置选项

### DataSourceConfigProvider接口方法说明

| 方法 | 说明 | 必需 |
|------|------|------|
| `getDataSources()` | 返回所有数据源配置 | ✅ |
| `getDefaultDataSourceKey()` | 指定默认数据源key | ✅ |
| `getMapperDataSourceMappings()` | Mapper映射配置 | ❌ |
| `getMapperPackages()` | Mapper包扫描路径 | ❌ |

## 📋 注意事项

1. **数据源key唯一性**：确保每个数据源的key在整个应用中唯一
2. **Mapper命名规范**：Mapper接口必须以"Mapper"结尾
3. **事务支持**：目前不支持跨数据源事务，每个方法调用只能操作一个数据源
4. **连接池配置**：建议为每个数据源合理配置连接池大小
5. **ThreadLocal清理**：框架会自动清理ThreadLocal，无需手动处理

## 🐛 故障排除

### 常见问题

1. **数据源未切换**
   - 检查Mapper接口是否以"Mapper"结尾
   - 确认映射配置是否正确
   - 查看控制台日志中的切面执行信息

2. **启动失败**
   - 检查DataSourceConfigProvider实现是否正确
   - 确认数据库连接配置是否有效
   - 查看是否缺少必需的依赖

3. **性能问题**
   - 调整各数据源的连接池配置
   - 检查数据库网络连接质量
   - 考虑是否频繁进行数据源切换

### 日志输出

启动时会输出详细的配置信息：

```
============================================================
🎉 多数据源JAR包配置信息
============================================================
📊 数据源配置:
  - main : HikariDataSource
  - user : HikariDataSource
🎯 默认数据源: main
🔄 热重载状态: 启用
📦 Mapper扫描包:
  - com.example.mapper
🗺️ 初始映射配置:
  - UserMapper → user
  - OrderMapper → main
============================================================
```

运行时会输出方法调用和数据源切换信息：

```
🔄 [MultiDbDataSourceAspect] UserMapper.findAll() → 数据源: user
🔄 [MultiDbDataSourceAspect] OrderMapper.findByUserId() → 数据源: main
```

## 📚 示例项目

        请根据实际项目需求实现 `DataSourceConfigProvider` 接口。

---

**作者**: mucongcong  
**版本**: 1.0.0  
**更新时间**: 2025/12/01