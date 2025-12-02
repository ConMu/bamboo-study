package com.conmu.multidb.config;

import com.conmu.multidb.core.MultiDbDynamicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 多数据源自动配置类
 * 负责初始化和装配所有多数据源相关组件
 * 
 * @author mucongcong
 * @date 2025/12/01
 */
@Configuration
@EnableAspectJAutoProxy
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
    
    /**
     * 创建MyBatis SqlSessionFactory
     * 使用动态数据源
     */
    @Bean
    @ConditionalOnMissingBean(SqlSessionFactory.class)
    public SqlSessionFactory sqlSessionFactory(MultiDbDynamicDataSource dynamicDataSource) throws Exception {
        logger.info("🔧 [MultiDbAutoConfiguration] 初始化MyBatis SqlSessionFactory...");

        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dynamicDataSource);

        // 设置MyBatis配置
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setCallSettersOnNulls(true);
        configuration.setLogImpl(org.apache.ibatis.logging.stdout.StdOutImpl.class);
        sessionFactory.setConfiguration(configuration);

        // 扫描Mapper XML文件
        try {
            sessionFactory.setMapperLocations(
                new PathMatchingResourcePatternResolver()
                    .getResources("classpath*:mapper/**/*.xml")
            );
            logger.info("📁 [MultiDbAutoConfiguration] 扫描Mapper XML: classpath*:mapper/**/*.xml");
        } catch (Exception e) {
            logger.warn("⚠️ [MultiDbAutoConfiguration] 未找到Mapper XML文件，跳过扫描: {}", e.getMessage());
        }

        logger.info("✅ [MultiDbAutoConfiguration] MyBatis SqlSessionFactory初始化完成");
        return sessionFactory.getObject();
    }
    
    /**
     * 打印配置信息
     */
    @Bean
    public MultiDbConfigInfoPrinter configInfoPrinter() {
        return new MultiDbConfigInfoPrinter();
    }
    
    /**
     * 配置信息打印器
     */
    public static class MultiDbConfigInfoPrinter {
        
        private static final Logger logger = LoggerFactory.getLogger(MultiDbConfigInfoPrinter.class);

        @Autowired
        private DataSourceConfigProvider configProvider;

        @javax.annotation.PostConstruct
        public void printConfigInfo() {
            String line = "============================================================";
            logger.info("\n{}", line);
            logger.info("🎉 多数据源JAR包配置信息");
            logger.info(line);

            Map<String, DataSource> dataSources = configProvider.getDataSources();
            logger.info("📊 数据源配置:");
            dataSources.forEach((key, ds) -> {
                logger.info("  - {} : {}", key, ds.getClass().getSimpleName());
            });

            logger.info("🎯 默认数据源: {}", configProvider.getDefaultDataSourceKey());
            logger.info("🔄 热重载状态: {}", configProvider.isHotReloadEnabled() ? "启用" : "禁用");

            String[] mapperPackages = configProvider.getMapperPackages();
            if (mapperPackages != null && mapperPackages.length > 0) {
                logger.info("📦 Mapper扫描包:");
                for (String pkg : mapperPackages) {
                    logger.info("  - {}", pkg);
                }
            }

            Map<String, String> initialMappings = configProvider.getInitialMapperDataSourceMappings();
            if (initialMappings != null && !initialMappings.isEmpty()) {
                logger.info("🗺️ 初始映射配置:");
                initialMappings.forEach((mapper, ds) -> {
                    String simpleMapperName = mapper.substring(mapper.lastIndexOf(".") + 1);
                    logger.info("  - {} → {}", simpleMapperName, ds);
                });
            }

            logger.info("{}\n", line);
        }
    }
}