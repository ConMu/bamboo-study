package com.conmu.multidb.core;

import com.conmu.multidb.config.DataSourceConfigProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 数据库管理路由持有者
 * 负责管理Mapper到数据源的映射关系，支持热切换
 * 
 * @author mucongcong
 * @date 2025/12/01
 */
@Component
public class DbManageRouteHolder {
    
    private static final Logger logger = LoggerFactory.getLogger(DbManageRouteHolder.class);

    @Autowired
    private ApplicationContext applicationContext;
    
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
    public void init() {
        try {
            logger.info("🚀 [DbManageRouteHolder] 开始初始化数据源路由...");

            // 1. 加载初始配置
            loadInitialMappings();

            // 2. 自动扫描Mapper
            scanMappers();

            logger.info("✅ [DbManageRouteHolder] 路由初始化完成，共配置 {} 个Mapper", mapperDataSourceMap.size());

        } catch (Exception e) {
            logger.error("❌ [DbManageRouteHolder] 初始化失败: {}", e.getMessage());
            throw new RuntimeException("DbManageRouteHolder初始化失败", e);
        }
    }
    
    /**
     * 重新加载配置
     */
    public void reload() {
        if (!configProvider.isHotReloadEnabled()) {
            logger.warn("⚠️ [DbManageRouteHolder] 热重载功能已禁用");
            return;
        }

        logger.info("🔄 [DbManageRouteHolder] 开始重新加载配置...");
        mapperDataSourceMap.clear();
        init();
        logger.info("✅ [DbManageRouteHolder] 配置重新加载完成");
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
            logger.warn("⚠️ [DbManageRouteHolder] Mapper {} 未找到配置，使用默认数据源: {}",
                       mapperClassName, dataSource);
        }
        return dataSource;
    }
    
    /**
     * 动态切换Mapper的数据源
     * @param mapperClassName Mapper类全名
     * @param dataSourceKey 目标数据源标识
     * @return 切换是否成功
     */
    public boolean switchMapper(String mapperClassName, String dataSourceKey) {
        // 验证数据源是否存在
        if (!configProvider.getDataSources().containsKey(dataSourceKey)) {
            logger.error("❌ [DbManageRouteHolder] 数据源不存在: {}", dataSourceKey);
            return false;
        }

        String oldDataSource = mapperDataSourceMap.put(mapperClassName, dataSourceKey);
        logger.info("🔄 [DbManageRouteHolder] Mapper切换: {} [{}→{}]",
                   getSimpleMapperName(mapperClassName), oldDataSource, dataSourceKey);
        return true;
    }
    
    /**
     * 获取所有Mapper的路由配置
     * @return 路由配置map的副本
     */
    public Map<String, String> getAllMappings() {
        return new ConcurrentHashMap<>(mapperDataSourceMap);
    }
    
    /**
     * 重置为默认配置
     */
    public void resetToDefault() {
        logger.info("🔄 [DbManageRouteHolder] 重置为默认配置...");
        mapperDataSourceMap.clear();
        loadInitialMappings();
        logger.info("✅ [DbManageRouteHolder] 已重置为默认配置");
    }
    
    /**
     * 加载初始映射配置
     */
    private void loadInitialMappings() {
        Map<String, String> initialMappings = configProvider.getInitialMapperDataSourceMappings();
        if (initialMappings != null && !initialMappings.isEmpty()) {
            mapperDataSourceMap.putAll(initialMappings);
            logger.info("📋 [DbManageRouteHolder] 加载初始配置: {} 项", initialMappings.size());
        }
    }

    /**
     * 自动扫描Mapper
     */
    private void scanMappers() {
        String[] mapperPackages = configProvider.getMapperPackages();
        if (mapperPackages == null || mapperPackages.length == 0) {
            logger.warn("⚠️ [DbManageRouteHolder] 未配置Mapper扫描包，跳过自动扫描");
            return;
        }

        Map<String, Object> allBeans = applicationContext.getBeansOfType(Object.class);
        int discoveredCount = 0;

        for (Map.Entry<String, Object> entry : allBeans.entrySet()) {
            Object bean = entry.getValue();
            if (bean == null) continue;

            Class<?>[] interfaces = bean.getClass().getInterfaces();
            for (Class<?> interfaceClass : interfaces) {
                String className = interfaceClass.getName();

                // 检查是否在指定包下且以Mapper结尾
                if (isInMapperPackage(className, mapperPackages) && className.endsWith("Mapper")) {
                    // 如果未配置，使用默认数据源
                    if (!mapperDataSourceMap.containsKey(className)) {
                        mapperDataSourceMap.put(className, configProvider.getDefaultDataSourceKey());
                        discoveredCount++;
                        logger.debug("🔍 [自动发现] {} → {}",
                                   getSimpleMapperName(className), configProvider.getDefaultDataSourceKey());
                    }
                }
            }
        }

        logger.info("🔍 [DbManageRouteHolder] 自动扫描发现 {} 个新Mapper", discoveredCount);
    }
    
    /**
     * 检查类名是否在指定的Mapper包下
     */
    private boolean isInMapperPackage(String className, String[] mapperPackages) {
        for (String packageName : mapperPackages) {
            if (className.startsWith(packageName)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 获取Mapper简短名称
     */
    private String getSimpleMapperName(String fullClassName) {
        return fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
    }
    
    /**
     * 获取配置信息的字符串表示
     */
    public String getConfigInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("📋 当前路由配置:\n");
        sb.append("  - 总计Mapper数量: ").append(mapperDataSourceMap.size()).append("\n");
        sb.append("  - 可用数据源: ").append(configProvider.getDataSources().keySet()).append("\n");
        sb.append("  - 默认数据源: ").append(configProvider.getDefaultDataSourceKey()).append("\n");
        sb.append("  - 热重载状态: ").append(configProvider.isHotReloadEnabled() ? "启用" : "禁用").append("\n");
        sb.append("  - 详细映射:\n");
        
        mapperDataSourceMap.forEach((mapper, ds) -> {
            sb.append("    ").append(getSimpleMapperName(mapper)).append(" → ").append(ds).append("\n");
        });
        
        return sb.toString();
    }
}