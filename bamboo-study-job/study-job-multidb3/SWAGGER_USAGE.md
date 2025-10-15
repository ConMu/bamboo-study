# Swagger数据源切换使用指南

## 🎯 **已完成的Swagger增强功能**

为了方便在Swagger UI中测试多数据源切换，我们已经完成了以下配置：

### ✅ **1. 全局dsNo参数配置**

在 `SwaggerConfig.java` 中添加了全局参数，所有接口都会自动显示 `dsNo` 请求头：

```java
// 全局参数配置
Parameter dsNoParameter = new ParameterBuilder()
    .name("dsNo")
    .description("数据源编号：ds0(数据源1-用户库) 或 ds1(数据源2-人员库)")
    .parameterType("header")
    .required(false)
    .defaultValue("ds0")
    .allowableValues("ds0", "ds1")
    .build();
```

### ✅ **2. API文档优化**

- **标题**: SpringBoot多数据源API文档 (动态切换版)
- **描述**: 包含详细的数据源使用说明
- **版本**: 升级到 2.0.0

### ✅ **3. 接口说明优化**

每个接口的`notes`都添加了建议的数据源选择：

```java
// 用户相关接口
@ApiOperation(value = "创建用户", notes = "在指定数据源中创建用户信息 (建议使用dsNo=ds0)")

// 人员相关接口  
@ApiOperation(value = "创建人员", notes = "在数据源2中创建人员信息 (建议使用dsNo=ds1)")
```

## 🚀 **Swagger UI使用方法**

### 访问地址
```
http://localhost:8891/swagger-ui.html
```

### 使用步骤

#### 1. **选择接口**
在Swagger UI中选择要测试的接口，例如 "创建用户"

#### 2. **设置dsNo参数**
在**Parameters**部分，您会看到全局的`dsNo`参数：

| 参数 | 值 | 说明 |
|------|-----|------|
| **dsNo** | `ds0` | 数据源1 (yunxin_recovery数据库) |
| **dsNo** | `ds1` | 数据源2 (test数据库) |

#### 3. **选择合适的数据源**
- **用户接口**: 建议选择 `ds0`
- **人员接口**: 建议选择 `ds1`  
- **测试接口**: 不指定或选择 `ds0`

#### 4. **执行请求**
点击 "Try it out" → 填写请求参数 → 点击 "Execute"

## 📋 **数据源映射表**

| dsNo | 数据库 | 端口 | 表名 | 适用接口 |
|------|--------|------|------|----------|
| `ds0` | yunxin_recovery | 3306 | users | 用户管理接口 |
| `ds1` | test | 4407 | people | 人员管理接口 |

## 🔧 **测试用例示例**

### 用户管理测试 (使用ds0)

#### 创建用户
```json
// 设置 dsNo = ds0
POST /api/user
{
  "username": "testuser",
  "password": "123456",
  "email": "test@example.com",
  "phone": "13800138000"
}
```

#### 查询用户
```
// 设置 dsNo = ds0
GET /api/user/testuser
```

### 人员管理测试 (使用ds1)

#### 创建人员
```json  
// 设置 dsNo = ds1
POST /api/people
{
  "name": "张三",
  "age": 25,
  "phone": "13900139000",
  "email": "zhangsan@example.com"
}
```

#### 查询人员
```
// 设置 dsNo = ds1  
GET /api/people/张三
```

## 🎯 **动态切换测试**

您也可以测试跨数据源的操作：

### 1. **错误的数据源使用**
```bash
# 用ds1查询用户表 (可能报错或返回空)
GET /api/user/admin
Header: dsNo = ds1
```

### 2. **正确的数据源使用**  
```bash
# 用ds0查询用户表 (正常)
GET /api/user/admin  
Header: dsNo = ds0
```

## 📊 **Swagger UI界面预览**

访问 http://localhost:8891/swagger-ui.html 后，您会看到：

1. **API文档标题**: SpringBoot多数据源API文档 (动态切换版)
2. **详细说明**: 包含数据源说明和使用方式
3. **全局参数**: 每个接口都会显示`dsNo`下拉选择框
4. **接口分组**: 用户管理接口 和 人员管理接口
5. **建议标识**: 每个接口都有推荐的数据源选择

## ⚠️ **注意事项**

1. **默认数据源**: 不设置dsNo时使用ds0
2. **数据源验证**: 只接受 `ds0` 和 `ds1` 两个值
3. **接口建议**: 遵循接口notes中的数据源建议
4. **错误处理**: 使用错误数据源时可能返回异常

---

## 🎊 **总结**

现在Swagger UI已经完美支持数据源切换：

- ✅ **全局参数**: 所有接口都显示dsNo选择
- ✅ **下拉选择**: 只能选择有效的数据源编号
- ✅ **默认值**: 自动设置为ds0
- ✅ **详细说明**: 清楚的数据源映射和使用指南
- ✅ **接口建议**: 每个接口都有推荐的数据源

这样就可以在Swagger UI中直观地测试多数据源动态切换功能了！🚀