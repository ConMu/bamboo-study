package com.conmu.sms.controller;

import com.conmu.sms.dao.entity.People;
import com.conmu.sms.dao.entity.User;
import com.conmu.sms.service.PeopleService;
import com.conmu.sms.service.UserService;
import com.conmu.sms.util.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 跨数据源操作测试控制器
 * @author mucongcong
 * @date 2025/10/16 13:38
 * @since 1.0.0
 **/
@Api(tags = "跨数据源操作测试")
@RestController
@RequestMapping("/api/cross-db")
public class CrossDatabaseController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private PeopleService peopleService;
    
    @ApiOperation(value = "跨数据源操作测试", notes = "在一个请求中同时操作两个数据源")
    @PostMapping("/test")
    public ApiResult<Map<String, Object>> crossDatabaseTest(
            @ApiParam(name = "userName", value = "用户名", required = true, example = "test_user")
            @RequestParam String userName,
            @ApiParam(name = "peopleName", value = "人员姓名", required = true, example = "test_people")
            @RequestParam String peopleName) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            System.out.println("🚀 [跨数据源测试] 开始执行跨数据源操作 - 用户: " + userName + ", 人员: " + peopleName);
            
            // 1. 创建用户 (UserMapper -> ds1 -> yunxin_recovery数据库)
            User user = new User();
            user.setUsername(userName);
            user.setPassword("123456");
            user.setEmail(userName + "@example.com");
            user.setCreatedBy(1L);
            user.setUpdatedBy(1L);
            user.setDeleted(0);
            
            int userResult = userService.insert(user);
            result.put("userInsert", userResult > 0 ? "成功" : "失败");
            result.put("userDatabase", "yunxin_recovery (ds1)");
            
            // 2. 创建人员 (PeopleMapper -> ds0 -> test数据库)  
            People people = new People();
            people.setName(peopleName);
            people.setAge(25);
            people.setEmail(peopleName + "@example.com");
            people.setCreatedBy(1L);
            people.setUpdatedBy(1L);
            people.setDeleted(0);
            
            int peopleResult = peopleService.insert(people);
            result.put("peopleInsert", peopleResult > 0 ? "成功" : "失败");
            result.put("peopleDatabase", "test (ds0)");
            
            // 3. 查询验证
            User queryUser = userService.findByUsername(userName);
            result.put("userQuery", queryUser != null ? "找到用户: " + queryUser.getId() : "未找到用户");
            
            System.out.println("✅ [跨数据源测试] 跨数据源操作完成");
            
            return ApiResult.success("跨数据源操作完成", result);
            
        } catch (Exception e) {
            System.err.println("❌ [跨数据源测试] 操作失败: " + e.getMessage());
            e.printStackTrace();
            return ApiResult.error("跨数据源操作失败: " + e.getMessage());
        }
    }
    
    @ApiOperation(value = "查询操作测试", notes = "测试跨数据源查询")
    @GetMapping("/query")
    public ApiResult<Map<String, Object>> crossDatabaseQuery() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            System.out.println("🔍 [跨数据源查询] 开始查询测试");
            
            // 查询用户 (ds1)
            User user = userService.findByUsername("admin");
            result.put("userFromDs1", user != null ? "ID:" + user.getId() + ", 用户名:" + user.getUsername() : "未找到");
            
            // 查询人员 (ds0)  
            result.put("peopleFromDs0", "PeopleMapper使用ds0数据源");
            
            System.out.println("✅ [跨数据源查询] 查询完成");
            
            return ApiResult.success("跨数据源查询完成", result);
            
        } catch (Exception e) {
            System.err.println("❌ [跨数据源查询] 查询失败: " + e.getMessage());
            return ApiResult.error("跨数据源查询失败: " + e.getMessage());
        }
    }
    
    @ApiOperation(value = "数据源映射查看", notes = "查看当前的Mapper->数据源映射关系")
    @GetMapping("/mapping")
    public ApiResult<Map<String, String>> viewMapping() {
        Map<String, String> mapping = new HashMap<>();
        mapping.put("PeopleMapper", "ds0 (test数据库)");
        mapping.put("UserMapper", "ds1 (yunxin_recovery数据库)");
        mapping.put("说明", "系统会根据Mapper自动切换到对应的数据源");
        
        return ApiResult.success("当前数据源映射", mapping);
    }
}