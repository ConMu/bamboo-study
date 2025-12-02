package com.conmu.multidb.config;

import com.conmu.multidb.core.MultiDbDynamicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 多数据源自动配置类
 * 负责初始化和装配所有多数据源相关组件
 *
 * ⚠️ 使用条件：必须提供DataSourceConfigProvider实现类
 *
 * @author mucongcong
 * @date 2025/12/01
 */
@Configuration
@EnableAspectJAutoProxy
@ConditionalOnBean(DataSourceConfigProvider.class)
public class MultiDbAutoConfiguration {
    
    private static final Logger logger = LoggerFactory.getLogger(MultiDbAutoConfiguration.class);

    @Autowired
    private DataSourceConfigProvider configProvider;
    
    /**
     * 创建动态数据源Bean
     * @return MultiDbDynamicDataSource实例
     */
    @Bean
    @ConditionalOnMissingBean(DataSource.class)
    public MultiDbDynamicDataSource multiDbDynamicDataSource() {
        logger.info("🚀 [MultiDbAutoConfiguration] 开始初始化多数据源...");

        MultiDbDynamicDataSource dynamicDataSource = new MultiDbDynamicDataSource();

        // 获取所有数据源配置
        Map<String, DataSource> dataSources = configProvider.getDataSources();
        if (dataSources == null || dataSources.isEmpty()) {
            throw new IllegalStateException("数据源配置不能为空，请检查DataSourceConfigProvider实现");
        }

        // 设置目标数据源
        Map<Object, Object> targetDataSources = new HashMap<>();
        dataSources.forEach((key, dataSource) -> {
            targetDataSources.put(key, dataSource);
            logger.info("📊 [MultiDbAutoConfiguration] 注册数据源: {} → {}", key, dataSource.getClass().getSimpleName());
        });
        dynamicDataSource.setTargetDataSources(targetDataSources);

        // 设置默认数据源
        String defaultKey = configProvider.getDefaultDataSourceKey();
        if (defaultKey == null || !dataSources.containsKey(defaultKey)) {
            throw new IllegalStateException("默认数据源 '" + defaultKey + "' 不存在于配置中");
        }

        DataSource defaultDataSource = dataSources.get(defaultKey);
        dynamicDataSource.setDefaultTargetDataSource(defaultDataSource);

        logger.info("🎯 [MultiDbAutoConfiguration] 设置默认数据源: {}", defaultKey);
        logger.info("✅ [MultiDbAutoConfiguration] 多数据源初始化完成，共 {} 个数据源", dataSources.size());
        
        return dynamicDataSource;
    }
    

}