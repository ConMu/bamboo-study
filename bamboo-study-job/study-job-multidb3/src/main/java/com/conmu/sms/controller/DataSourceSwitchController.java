package com.conmu.sms.controller;

import com.conmu.sms.config.MapperDataSourceRegistry;
import com.conmu.sms.util.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 数据源热切换管理控制器
 * @author mucongcong  
 * @date 2025/10/15 18:08
 * @since 1.0.0
 **/
@Api(tags = "数据源热切换管理")
@RestController
@RequestMapping("/api/datasource")
public class DataSourceSwitchController {
    
    @Autowired
    private MapperDataSourceRegistry registry;
    
    @ApiOperation(value = "查看当前Mapper数据源配置", notes = "显示所有Mapper当前绑定的数据源")
    @GetMapping("/config")
    public ApiResult<Map<String, Object>> getCurrentConfig() {
        try {
            ConcurrentHashMap<String, String> mappings = registry.getAllMappings();
            
            Map<String, Object> result = new HashMap<>();
            result.put("totalMappers", mappings.size());
            result.put("mappings", mappings);
            result.put("configInfo", registry.getConfigInfo());
            
            return ApiResult.success("当前配置查询成功", result);
        } catch (Exception e) {
            return ApiResult.error("查询配置失败: " + e.getMessage());
        }
    }
    
    @ApiOperation(value = "热切换Mapper数据源", notes = "动态修改指定Mapper的数据源绑定")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "mapperName", value = "Mapper名称", required = true, example = "UserMapper"),
        @ApiImplicitParam(name = "dataSource", value = "目标数据源", required = true,
                         allowableValues = "ds0,ds1", example = "ds0")
    })
    @PostMapping("/switch")
    public ApiResult<String> switchDataSource(
            @ApiParam(name = "mapperName", value = "Mapper名称 (如: UserMapper, PeopleMapper等)", required = true, example = "UserMapper")
            @RequestParam String mapperName,
            @ApiParam(name = "dataSource", value = "目标数据源 (ds0 或 ds1)", required = true, example = "ds0")
            @RequestParam String dataSource) {

        try {
            // 构建完整的Mapper类名
            String fullMapperClass = "com.conmu.sms.dao.mapper." + mapperName;

            // 验证Mapper是否存在于注册表中
            ConcurrentHashMap<String, String> allMappings = registry.getAllMappings();
            if (!allMappings.containsKey(fullMapperClass)) {
                return ApiResult.error("未找到Mapper: " + mapperName + "，支持的Mapper: " + getSupportedMapperNames());
            }
            
            // 执行热切换
            boolean success = registry.switchDataSource(fullMapperClass, dataSource);
            
            if (success) {
                String message = String.format("✅ 热切换成功: %s -> %s", mapperName, dataSource);
                System.out.println("🔥 " + message);
                return ApiResult.success(message);
            } else {
                return ApiResult.error("热切换失败，请检查参数");
            }
            
        } catch (Exception e) {
            return ApiResult.error("热切换异常: " + e.getMessage());
        }
    }
    
    @ApiOperation(value = "批量热切换", notes = "同时修改多个Mapper的数据源绑定")
    @PostMapping("/batch-switch")
    public ApiResult<Map<String, String>> batchSwitch(
            @ApiParam(name = "userDataSource", value = "User相关操作的数据源", allowableValues = "ds0,ds1", example = "ds1")
            @RequestParam(required = false) String userDataSource,
            @ApiParam(name = "peopleDataSource", value = "People相关操作的数据源", allowableValues = "ds0,ds1", example = "ds0")
            @RequestParam(required = false) String peopleDataSource) {
        
        try {
            Map<String, String> results = new HashMap<>();
            
            // 切换UserMapper数据源
            if (userDataSource != null) {
                boolean userResult = registry.switchDataSource("com.conmu.sms.dao.mapper.UserMapper", userDataSource);
                results.put("UserMapper", userResult ? "成功 -> " + userDataSource : "失败");
            }
            
            // 切换PeopleMapper数据源  
            if (peopleDataSource != null) {
                boolean peopleResult = registry.switchDataSource("com.conmu.sms.dao.mapper.PeopleMapper", peopleDataSource);
                results.put("PeopleMapper", peopleResult ? "成功 -> " + peopleDataSource : "失败");
            }
            
            if (results.isEmpty()) {
                return ApiResult.error("未指定任何切换操作");
            }
            
            return ApiResult.success("批量热切换完成", results);
            
        } catch (Exception e) {
            return ApiResult.error("批量热切换异常: " + e.getMessage());
        }
    }
    
    @ApiOperation(value = "重置为默认配置", notes = "将所有Mapper的数据源绑定重置为初始默认配置")
    @PostMapping("/reset")
    public ApiResult<String> resetToDefault() {
        try {
            registry.resetToDefault();
            return ApiResult.success("✅ 已重置为默认配置: 所有Mapper -> ds0");
        } catch (Exception e) {
            return ApiResult.error("重置配置失败: " + e.getMessage());
        }
    }
    
    @ApiOperation(value = "获取支持的Mapper列表", notes = "查看系统中所有支持热切换的Mapper")
    @GetMapping("/mappers")
    public ApiResult<Map<String, Object>> getSupportedMappers() {
        Map<String, Object> result = new HashMap<>();

        // 动态获取当前系统中的所有Mapper
        String[] supportedMappers = getSupportedMapperNames().split(", ");
        result.put("supportedMappers", supportedMappers);
        result.put("totalMappers", supportedMappers.length);
        result.put("supportedDataSources", new String[]{"ds0", "ds1"});

        Map<String, String> descriptions = new HashMap<>();
        descriptions.put("ds0", "数据源1: yunxin_recovery数据库 (端口3306)");
        descriptions.put("ds1", "数据源2: test数据库 (端口4407)");
        result.put("description", descriptions);

        return ApiResult.success("支持的配置项", result);
    }

    /**
     * 获取支持的Mapper名称列表
     * @return 逗号分隔的Mapper名称
     */
    private String getSupportedMapperNames() {
        ConcurrentHashMap<String, String> allMappings = registry.getAllMappings();
        StringBuilder sb = new StringBuilder();

        for (String fullClassName : allMappings.keySet()) {
            String simpleName = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(simpleName);
        }

        return sb.toString();
    }
}