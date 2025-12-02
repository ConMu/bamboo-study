package com.conmu.multidb.core;

import com.conmu.multidb.config.DataSourceConfigProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 数据库管理路由持有者
 * 负责管理Mapper到数据源的映射关系，支持热切换
 * 
 * @author mucongcong
 * @date 2025/12/01
 */
@Component
@ConditionalOnBean(DataSourceConfigProvider.class)
public class DbManageRouteHolder {
    
    private static final Logger logger = LoggerFactory.getLogger(DbManageRouteHolder.class);

    @Autowired
    private DataSourceConfigProvider configProvider;
    
    /**
     * Mapper类名到数据源的映射关系
     * key: Mapper全类名, value: 数据源标识
     */
    private final ConcurrentHashMap<String, String> mapperDataSourceMap = new ConcurrentHashMap<>();

    /**
     * 初始化路由配置
     */
    @PostConstruct
    private void init() {
        try {
            logger.info("🚀 [DbManageRouteHolder] 开始初始化数据源路由...");

            // 加载明确配置的映射
            loadInitialMappings();

            // 启动自动重载定时器（如果配置了的话）
            startAutoReloadScheduler();

            logger.info("✅ [DbManageRouteHolder] 路由初始化完成，特殊配置 {} 个Mapper", mapperDataSourceMap.size());

        } catch (Exception e) {
            logger.error("❌ [DbManageRouteHolder] 初始化失败: {}", e.getMessage());
            throw new RuntimeException("DbManageRouteHolder初始化失败", e);
        }
    }
    
    /**
     * 重新加载配置 - 增量更新，只更新有变化的映射
     * 使用synchronized确保多线程安全
     */
    public synchronized void reload() {
        logger.debug("🔄 [DbManageRouteHolder] 开始重新加载配置...");

        try {
            // 1. 获取配置提供者的最新映射
            Map<String, String> latestMappings = configProvider.getMapperDataSourceMappings();
            if (latestMappings == null) {
                latestMappings = new HashMap<>();
            }

            int updatedCount = 0;
            int addedCount = 0;
            int removedCount = 0;

            // 2. 处理新增和更新
            for (Map.Entry<String, String> entry : latestMappings.entrySet()) {
                String mapper = entry.getKey();
                String newDataSource = entry.getValue();
                String currentDataSource = mapperDataSourceMap.get(mapper);

                if (currentDataSource == null) {
                    // 新增映射
                    mapperDataSourceMap.put(mapper, newDataSource);
                    addedCount++;
                    logger.info("➕ [新增] {} → {}", getSimpleMapperName(mapper), newDataSource);
                } else if (!currentDataSource.equals(newDataSource)) {
                    // 更新映射
                    mapperDataSourceMap.put(mapper, newDataSource);
                    updatedCount++;
                    logger.info("🔄 [更新] {} [{}→{}]", getSimpleMapperName(mapper), currentDataSource, newDataSource);
                }
            }

            // 3. 处理删除 - 移除配置中不再存在的映射，回退到默认数据源
            Set<String> currentMappers = new HashSet<>(mapperDataSourceMap.keySet());
            Set<String> latestMappers = latestMappings.keySet();

            for (String mapper : currentMappers) {
                if (!latestMappers.contains(mapper)) {
                    String removedDataSource = mapperDataSourceMap.remove(mapper);
                    if (removedDataSource != null) {
                        removedCount++;
                        logger.info("↩️ [回退] {} [{}→默认:{}]",
                                  getSimpleMapperName(mapper),
                                  removedDataSource,
                                  configProvider.getDefaultDataSourceKey());
                    }
                }
            }

            logger.debug("✅ [DbManageRouteHolder] 配置重新加载完成: 新增{}个, 更新{}个, 删除{}个, 总计{}个",
                       addedCount, updatedCount, removedCount, mapperDataSourceMap.size());

        } catch (Exception e) {
            logger.error("❌ [DbManageRouteHolder] 重新加载失败，保持原有配置: {}", e.getMessage());
            throw new RuntimeException("DbManageRouteHolder重新加载失败", e);
        }
    }
    
    /**
     * 获取指定Mapper对应的数据源
     * @param mapperClassName Mapper类全名
     * @return 数据源标识
     */
    public String get(String mapperClassName) {
        String dataSource = mapperDataSourceMap.get(mapperClassName);
        if (dataSource == null) {
            dataSource = configProvider.getDefaultDataSourceKey();
            logger.debug("🔍 [DbManageRouteHolder] Mapper {} → {} (默认)", getSimpleMapperName(mapperClassName), dataSource);
        } else {
            logger.debug("🔍 [DbManageRouteHolder] Mapper {} → {} (配置)", getSimpleMapperName(mapperClassName), dataSource);
        }
        return dataSource;
    }
    

    /**
     * 加载初始映射配置
     */
    private void loadInitialMappings() {
        loadInitialMappings(mapperDataSourceMap);
    }

    /**
     * 加载映射配置到指定Map
     */
    private void loadInitialMappings(ConcurrentHashMap<String, String> targetMap) {
        Map<String, String> mappings = configProvider.getMapperDataSourceMappings();
        if (mappings != null && !mappings.isEmpty()) {
            targetMap.putAll(mappings);
            logger.info("📋 [DbManageRouteHolder] 加载配置: {} 项", mappings.size());
        }
    }


    /**
     * 启动自动重载定时器
     */
    private void startAutoReloadScheduler() {
        long intervalSeconds = configProvider.getAutoReloadIntervalSeconds();

        if (intervalSeconds <= 0) {
            logger.info("⏰ [DbManageRouteHolder] 自动定时重载功能未启用");
            return;
        }

        Executors.newSingleThreadScheduledExecutor(r -> {
            Thread thread = new Thread(r, "multidb-auto-reload");
            thread.setDaemon(true);
            return thread;
        }).scheduleAtFixedRate(() -> {
            try {
                reload();
            } catch (Exception e) {
                logger.error("❌ [DbManageRouteHolder] 自动重载失败: {}", e.getMessage());
            }
        }, intervalSeconds, intervalSeconds, TimeUnit.SECONDS);

        logger.info("⏰ [DbManageRouteHolder] 启动自动重载定时器，间隔: {}秒", intervalSeconds);
    }

    /**
     * 获取Mapper简短名称
     */
    private String getSimpleMapperName(String fullClassName) {
        return fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
    }
    
}