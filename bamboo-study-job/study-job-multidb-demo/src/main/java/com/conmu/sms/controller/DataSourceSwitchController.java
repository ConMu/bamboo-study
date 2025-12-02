package com.conmu.sms.controller;

import com.conmu.multidb.core.DbManageRouteHolder;
import com.conmu.sms.config.MultiDbConfigProvider;
import com.conmu.sms.util.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据源管理控制器
 * 基于 study-job-multidb-pack 的配置管理
 * @author mucongcong
 * @date 2025/10/15 18:08
 * @since 1.0.0
 **/
@Api(tags = "数据源管理")
@RestController
@RequestMapping("/api/datasource")
public class DataSourceSwitchController {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceSwitchController.class);

    @Autowired
    private DbManageRouteHolder routeHolder;

    @Autowired
    private MultiDbConfigProvider configProvider;

    @ApiOperation(value = "查看当前Mapper数据源配置", notes = "显示Mapper的数据源路由情况")
    @GetMapping("/config")
    public ApiResult<Map<String, Object>> getCurrentConfig() {
        logger.info("🔍 [配置查询] 开始查询当前Mapper数据源配置");

        try {
            Map<String, Object> result = new HashMap<>();

            // 测试几个已知的Mapper
            String[] knownMappers = {
                "com.conmu.sms.dao.mapper.UserMapper",
                "com.conmu.sms.dao.mapper.PeopleMapper"
            };

            Map<String, String> mappings = new HashMap<>();
            for (String mapper : knownMappers) {
                String dataSource = routeHolder.get(mapper);
                String simpleName = mapper.substring(mapper.lastIndexOf(".") + 1);
                mappings.put(simpleName, dataSource);
                logger.info("📋 [Mapper路由] {} -> {}", simpleName, dataSource);
            }

            result.put("mappings", mappings);
            result.put("supportedDataSources", new String[]{"ds0", "ds1"});

            Map<String, String> descriptions = new HashMap<>();
            descriptions.put("ds0", "数据源1: yunxin_recovery数据库 (端口3306)");
            descriptions.put("ds1", "数据源2: test数据库 (端口4407)");
            result.put("description", descriptions);

            logger.info("✅ [配置查询] 查询成功, 返回 {} 个Mapper配置", mappings.size());
            return ApiResult.success("当前配置查询成功", result);
        } catch (Exception e) {
            logger.error("❌ [配置查询] 查询失败: {}", e.getMessage(), e);
            return ApiResult.error("查询配置失败: " + e.getMessage());
        }
    }

    @ApiOperation(value = "重新加载配置", notes = "触发配置重新加载，从 MultiDbConfigProvider 读取最新配置")
    @PostMapping("/reload")
    public ApiResult<String> reloadConfig() {
        logger.info("🔄 [配置重载] 开始重新加载数据源配置...");

        try {
            routeHolder.reload();
            logger.info("✅ [配置重载] 数据源配置重新加载成功");
            return ApiResult.success("✅ 配置重新加载成功");
        } catch (Exception e) {
            logger.error("❌ [配置重载] 配置重新加载失败: {}", e.getMessage(), e);
            return ApiResult.error("配置重新加载失败: " + e.getMessage());
        }
    }

    @ApiOperation(value = "更新Mapper映射", notes = "动态更新单个Mapper的数据源映射关系")
    @PostMapping("/mapping/update")
    public ApiResult<String> updateMapping(
            @ApiParam(value = "Mapper类的全限定名", required = true)
            @RequestParam String mapperClass,
            @ApiParam(value = "数据源key (ds0, ds1)", required = true)
            @RequestParam String dataSourceKey) {

        logger.info("🔧 [映射更新] 开始更新Mapper映射: {} -> {}", mapperClass, dataSourceKey);

        try {
            // 验证数据源key是否有效
            if (!dataSourceKey.matches("ds[0-9]+")) {
                logger.warn("⚠️ [映射更新] 无效的数据源key: {}", dataSourceKey);
                return ApiResult.error("无效的数据源key，应该是ds0, ds1等格式");
            }

            // 更新配置（内部会自动重载路由）
            configProvider.updateMapping(mapperClass, dataSourceKey);

            logger.info("✅ [映射更新] Mapper映射更新成功: {} -> {}", mapperClass, dataSourceKey);
            return ApiResult.success("✅ Mapper映射更新成功");
        } catch (Exception e) {
            logger.error("❌ [映射更新] 映射更新失败: {}", e.getMessage(), e);
            return ApiResult.error("映射更新失败: " + e.getMessage());
        }
    }

    @ApiOperation(value = "批量更新Mapper映射", notes = "批量更新多个Mapper的数据源映射关系")
    @PostMapping("/mapping/batch-update")
    public ApiResult<String> batchUpdateMappings(@RequestBody Map<String, String> newMappings) {
        logger.info("🔧 [批量映射更新] 开始批量更新Mapper映射，数量: {}", newMappings.size());

        try {
            // 验证所有数据源key
            for (Map.Entry<String, String> entry : newMappings.entrySet()) {
                if (!entry.getValue().matches("ds[0-9]+")) {
                    logger.warn("⚠️ [批量映射更新] 无效的数据源key: {} -> {}", entry.getKey(), entry.getValue());
                    return ApiResult.error("无效的数据源key: " + entry.getValue() + "，应该是ds0, ds1等格式");
                }
            }

            // 更新配置（内部会自动重载路由）
            configProvider.updateMappings(newMappings);

            logger.info("✅ [批量映射更新] 批量映射更新成功，更新数量: {}", newMappings.size());
            return ApiResult.success("✅ 批量映射更新成功，更新了 " + newMappings.size() + " 个映射");
        } catch (Exception e) {
            logger.error("❌ [批量映射更新] 批量映射更新失败: {}", e.getMessage(), e);
            return ApiResult.error("批量映射更新失败: " + e.getMessage());
        }
    }

    @ApiOperation(value = "删除Mapper映射", notes = "删除指定Mapper的数据源映射关系")
    @DeleteMapping("/mapping/{mapperClass}")
    public ApiResult<String> removeMapping(
            @ApiParam(value = "Mapper类的全限定名", required = true)
            @PathVariable String mapperClass) {

        logger.info("🗑️ [映射删除] 开始删除Mapper映射: {}", mapperClass);

        try {
            // 删除配置（内部会自动重载路由）
            configProvider.removeMapping(mapperClass);

            logger.info("✅ [映射删除] Mapper映射删除成功: {}", mapperClass);
            return ApiResult.success("✅ Mapper映射删除成功");
        } catch (Exception e) {
            logger.error("❌ [映射删除] 映射删除失败: {}", e.getMessage(), e);
            return ApiResult.error("映射删除失败: " + e.getMessage());
        }
    }

    @ApiOperation(value = "查看所有映射配置", notes = "查看所有Mapper的数据源映射配置")
    @GetMapping("/mapping/all")
    public ApiResult<Map<String, String>> getAllMappings() {
        logger.info("📋 [映射查询] 查询所有Mapper映射配置");

        try {
            Map<String, String> allMappings = configProvider.getAllMappings();
            logger.info("✅ [映射查询] 查询成功，映射数量: {}", allMappings.size());
            return ApiResult.success("查询成功", allMappings);
        } catch (Exception e) {
            logger.error("❌ [映射查询] 查询失败: {}", e.getMessage(), e);
            return ApiResult.error("查询映射配置失败: " + e.getMessage());
        }
    }
}
