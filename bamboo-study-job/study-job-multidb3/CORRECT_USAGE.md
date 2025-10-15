# study-job-multidb2 正确使用指南

## 🎯 **您说得对！切面就足够了**

感谢您的提醒！确实，既然有了 `DataSourceAspect` 切面来处理数据源切换，Service层就不需要手动设置数据源了。

## ✅ **重构后的清爽架构**

### 切面自动处理数据源切换
```java
@Aspect
@Order(1)
@Configuration
public class DataSourceAspect {
    
    @Before("execution(* com.conmu.sms.controller..*.*(..))")
    public void dataSourceChange() {
        // 从请求头读取dsNo参数
        String dsNo = request.getHeader("dsNo");
        
        // 如果没有指定，默认使用ds0
        if (StringUtils.isEmpty(dsNo)) {
            dsNo = "ds0";
        }
        
        // 设置数据源
        DataSourceHolder.setDataSource(dsNo);
    }
    
    @After("execution(* com.conmu.sms.controller..*.*(..))")  
    public void after() {
        // 自动清理ThreadLocal
        DataSourceHolder.clearDataSource();
    }
}
```

### Service层保持简洁
```java
@Service
public class UserService {
    public int insert(User user) {
        return userMapper.insert(user);  // 简洁！无需手动设置数据源
    }
}

@Service 
public class PeopleService {
    public int insert(People people) {
        return peopleMapper.insert(people);  // 简洁！无需手动设置数据源
    }
}
```

## 🚀 **使用方式**

### 方法1: 通过请求头指定数据源

#### 使用数据源1 (ds0 - yunxin_recovery数据库)
```bash
# 创建用户
curl -X POST http://localhost:8891/api/user \
  -H "Content-Type: application/json" \
  -H "dsNo: ds0" \
  -d '{"username": "testuser", "password": "123456"}'

# 查询用户
curl -H "dsNo: ds0" http://localhost:8891/api/user/testuser
```

#### 使用数据源2 (ds1 - test数据库)
```bash  
# 创建人员
curl -X POST http://localhost:8891/api/people \
  -H "Content-Type: application/json" \
  -H "dsNo: ds1" \
  -d '{"name": "张三", "age": 25}'

# 查询人员  
curl -H "dsNo: ds1" http://localhost:8891/api/people/张三
```

### 方法2: 使用默认数据源

如果不传递 `dsNo` 请求头，自动使用 **ds0** (数据源1):

```bash
# 默认使用ds0数据源
curl -X POST http://localhost:8891/api/user \
  -H "Content-Type: application/json" \
  -d '{"username": "defaultuser"}'
```

## 🎯 **架构优势**

### 职责分离更清晰
- **Controller层**: 处理HTTP请求和响应
- **Service层**: 纯业务逻辑，无数据源切换代码  
- **AOP切面**: 专门负责数据源切换
- **DataSourceHolder**: ThreadLocal管理数据源上下文

### 代码更简洁
```java
// ❌ 之前：Service层混杂数据源逻辑
public int insert(User user) {
    DataSourceHolder.setDataSource("ds0");
    try {
        return userMapper.insert(user);
    } finally {
        DataSourceHolder.clearDataSource();
    }
}

// ✅ 现在：Service层专注业务逻辑
public int insert(User user) {
    return userMapper.insert(user);
}
```

### 更灵活的数据源选择
客户端可以根据业务需要动态选择数据源：

```javascript
// 前端动态选择数据源
const useDatabase1 = () => {
    return fetch('/api/user/admin', {
        headers: { 'dsNo': 'ds0' }
    });
};

const useDatabase2 = () => {
    return fetch('/api/people/张三', {
        headers: { 'dsNo': 'ds1' }  
    });
};
```

## 📋 **数据源映射**

| dsNo | 数据库 | 说明 | 表 |
|------|---------|------|-----|
| ds0 | yunxin_recovery | 默认数据源 | users |
| ds1 | test | 第二数据源 | people |

## 🔧 **Swagger测试**

在Swagger UI中测试时，添加请求头：

1. 访问: http://localhost:8891/swagger-ui.html
2. 点击接口的 "Try it out"
3. 在 **Parameters** 部分添加Header:
   - **Name**: `dsNo`  
   - **Value**: `ds0` 或 `ds1`
4. 执行请求

## ⚠️ **注意事项**

1. **默认数据源**: 不传递dsNo时使用ds0
2. **请求头名称**: 必须是 `dsNo` (区分大小写)
3. **有效值**: 只接受 `ds0` 和 `ds1`
4. **线程安全**: 切面自动处理ThreadLocal清理

---

## 🎊 **总结**

您的建议非常正确！使用AOP切面来处理数据源切换是更好的设计：

- ✅ **关注点分离**: 业务逻辑与数据源切换分离
- ✅ **代码简洁**: Service层无需手动管理数据源  
- ✅ **灵活性高**: 客户端可以动态选择数据源
- ✅ **维护方便**: 数据源切换逻辑集中在切面中

这就是为什么要使用AOP的经典场景！👍