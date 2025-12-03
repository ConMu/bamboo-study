package com.conmu.multidb.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 数据库管理路由持有者
 * 负责管理Mapper到数据源的映射关系，支持热切换
 * 
 * @author mucongcong
 * @date 2025/12/01
 */
@Component
@ConditionalOnBean(AbstractDataSourceConfigProvider.class)
public class DbManageRouteHolder {

    private static final Logger logger = LoggerFactory.getLogger(DbManageRouteHolder.class);

    @Autowired
    private AbstractDataSourceConfigProvider configProvider;

    /**
     * Mapper类名到数据源的映射关系 - 直接引用共享Map
     * key: Mapper全类名, value: 数据源标识
     */
    private ConcurrentHashMap<String, String> mapperDataSourceManageMap;

    /**
     * 初始化路由配置
     */
    @PostConstruct
    private void init() {
        try {
            logger.info("🚀 [DbManageRouteHolder] 开始初始化数据源路由...");

            // 直接使用共享Map，实现真正的共享
            this.mapperDataSourceManageMap = configProvider.mapperDataSourceMap;

            logger.info("✅ [DbManageRouteHolder] 路由初始化完成，特殊配置 {} 个Mapper", mapperDataSourceManageMap.size());

        } catch (Exception e) {
            logger.error("❌ [DbManageRouteHolder] 初始化失败: {}", e.getMessage());
            throw new RuntimeException("DbManageRouteHolder初始化失败", e);
        }
    }
    

    /**
     * 获取指定Mapper对应的数据源
     * @param mapperClassName Mapper类全名
     * @return 数据源标识
     */
    String get(String mapperClassName) {
        String dataSourceKey = mapperDataSourceManageMap.get(mapperClassName);
        if (dataSourceKey == null || !configProvider.getDataSources().containsKey(dataSourceKey)) {
            dataSourceKey = configProvider.getDefaultDataSourceKey();
            logger.debug("🔍 [DbManageRouteHolder] Mapper {} → {} (默认)", getSimpleMapperName(mapperClassName), dataSourceKey);
        } else {
            logger.debug("🔍 [DbManageRouteHolder] Mapper {} → {} (配置)", getSimpleMapperName(mapperClassName), dataSourceKey);
        }
        return dataSourceKey;
    }
    

    /**
     * 获取Mapper简短名称
     */
    private String getSimpleMapperName(String fullClassName) {
        return fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
    }
}
