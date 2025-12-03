package com.conmu.multidb.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.conmu.multidb.core.AbstractDataSourceConfigProvider;
import com.conmu.multidb.core.MultiDbDynamicDataSource;
import com.conmu.multidb.core.MultiDbDynamicTransactionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 多数据源自动配置类
 * 负责初始化和装配所有多数据源相关组件
 * 现在没有循环依赖问题了，因为启动类排除了DataSourceAutoConfiguration
 *
 * ⚠️ 使用条件：必须提供DataSourceConfigProvider实现类
 *
 * @author mucongcong
 * @date 2025/12/01
 */
@Configuration
@EnableAspectJAutoProxy
@ConditionalOnBean(AbstractDataSourceConfigProvider.class)
@AutoConfigureBefore(DruidDataSourceAutoConfigure.class)
public class MultiDbAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(MultiDbAutoConfiguration.class);

    @Autowired
    private AbstractDataSourceConfigProvider configProvider;

    /**
     * 创建动态数据源Bean - 现在使用@Primary，因为没有循环依赖了
     * @return MultiDbDynamicDataSource实例
     */
    @Bean(name = "dataSource")
    @Primary
    public MultiDbDynamicDataSource dataSource() {
        logger.info("[MultiDbAutoConfiguration] 开始初始化多数据源...");

        MultiDbDynamicDataSource dynamicDataSource = new MultiDbDynamicDataSource();

        // 获取所有数据源配置
        Map<String, DataSource> dataSources = configProvider.getDataSources();
        if (dataSources.isEmpty()) {
            throw new IllegalStateException("数据源配置不能为空，请检查DataSourceConfigProvider实现");
        }

        // 设置目标数据源
        Map<Object, Object> targetDataSources = new HashMap<>();
        dataSources.forEach((key, dataSource) -> {
            targetDataSources.put(key, dataSource);
            logger.info("[MultiDbAutoConfiguration] 注册数据源: {} → {}", key, dataSource.getClass().getSimpleName());
        });
        dynamicDataSource.setTargetDataSources(targetDataSources);

        // 设置默认数据源
        String defaultKey = configProvider.getDefaultDataSourceKey();
        if (defaultKey == null || !dataSources.containsKey(defaultKey)) {
            throw new IllegalStateException("默认数据源 '" + defaultKey + "' 不存在于配置中");
        }

        DataSource defaultDataSource = dataSources.get(defaultKey);
        dynamicDataSource.setDefaultTargetDataSource(defaultDataSource);

        logger.info("[MultiDbAutoConfiguration] 设置默认数据源: {}", defaultKey);
        logger.info("[MultiDbAutoConfiguration] 多数据源初始化完成，共 {} 个数据源", dataSources.size());

        return dynamicDataSource;
    }

    /**
     * 创建动态事务管理器Bean
     * @return MultiDbDynamicTransactionManager实例
     */
    @Bean(name = "transactionManager")
    @Primary
    public PlatformTransactionManager transactionManager() {
        logger.info("[MultiDbAutoConfiguration] 开始初始化多数据源事务管理器...");

        MultiDbDynamicTransactionManager txManager = new MultiDbDynamicTransactionManager(configProvider);

        logger.info("[MultiDbAutoConfiguration] 多数据源事务管理器初始化完成");
        return txManager;
    }
}
