package com.conmu.sms.config;

import com.conmu.multidb.config.DataSourceConfigProvider;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 多数据源配置提供者实现示例
 *
 * 这是一个示例实现，展示如何使用多数据源JAR包
 * 用户需要实现DataSourceConfigProvider接口来提供自己的配置
 *
 * @author mucongcong
 * @date 2025/12/01
 */
@Component
@ConfigurationProperties(prefix = "multidb")
public class ExampleDataSourceConfigProvider implements DataSourceConfigProvider {

    private static final Logger logger = LoggerFactory.getLogger(ExampleDataSourceConfigProvider.class);
    
    /**
     * 提供所有数据源配置
     * 这里使用硬编码的方式作为示例，实际项目中可以从配置文件、数据库等地方获取
     */
    @Override
    public Map<String, DataSource> getDataSources() {
        Map<String, DataSource> dataSources = new HashMap<>();
        
        // 主数据源 - MySQL
        HikariConfig mainConfig = new HikariConfig();
        mainConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        mainConfig.setJdbcUrl("jdbc:mysql://localhost:3306/main_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai");
        mainConfig.setUsername("root");
        mainConfig.setPassword("root123");
        mainConfig.setMaximumPoolSize(20);
        mainConfig.setMinimumIdle(5);
        mainConfig.setPoolName("MainDB-Pool");
        dataSources.put("main", new HikariDataSource(mainConfig));
        
        // 用户数据源 - MySQL
        HikariConfig userConfig = new HikariConfig();
        userConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        userConfig.setJdbcUrl("jdbc:mysql://localhost:3306/user_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai");
        userConfig.setUsername("root");
        userConfig.setPassword("root123");
        userConfig.setMaximumPoolSize(15);
        userConfig.setMinimumIdle(3);
        userConfig.setPoolName("UserDB-Pool");
        dataSources.put("user", new HikariDataSource(userConfig));
        
        // 日志数据源 - MySQL
        HikariConfig logConfig = new HikariConfig();
        logConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        logConfig.setJdbcUrl("jdbc:mysql://localhost:3306/log_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai");
        logConfig.setUsername("root");
        logConfig.setPassword("root123");
        logConfig.setMaximumPoolSize(10);
        logConfig.setMinimumIdle(2);
        logConfig.setPoolName("LogDB-Pool");
        dataSources.put("log", new HikariDataSource(logConfig));
        
        logger.info("📊 [ExampleDataSourceConfigProvider] 创建了 {} 个数据源", dataSources.size());
        
        return dataSources;
    }
    
    /**
     * 指定默认数据源
     */
    @Override
    public String getDefaultDataSourceKey() {
        return "main";
    }
    
    /**
     * 提供初始的Mapper -> DataSource映射配置
     * 这些映射会在系统启动时自动加载到DbManageRouteHolder中
     */
    @Override
    public Map<String, String> getInitialMapperDataSourceMappings() {
        Map<String, String> mappings = new HashMap<>();
        
        // 示例映射配置
        mappings.put("com.conmu.sms.mapper.UserMapper", "user");
        mappings.put("com.conmu.sms.mapper.OrderMapper", "main");
        mappings.put("com.conmu.sms.mapper.ProductMapper", "main");
        mappings.put("com.conmu.sms.mapper.LogMapper", "log");
        mappings.put("com.conmu.sms.mapper.AuditMapper", "log");
        
        // 对于@Repository注解的Mapper也同样支持
        mappings.put("com.conmu.sms.mapper.BackfillRecordMapper", "main");
        
        logger.info("🗺️ [ExampleDataSourceConfigProvider] 配置了 {} 个Mapper映射", mappings.size());
        
        return mappings;
    }
    
    /**
     * 指定要扫描的Mapper包路径
     * 
     * 配置说明：
     * 1. 如果返回 null 或空数组，将拦截所有以 Mapper 结尾的接口
     * 2. 如果指定包路径，只拦截这些包及其子包下的 Mapper 接口
     * 3. 支持多个包路径配置
     * 
     * 示例配置：
     * - "com.conmu.sms.mapper"     # 只拦截 com.conmu.sms.mapper 包下的Mapper
     * - "com.conmu.sms"           # 拦截 com.conmu.sms 及其所有子包下的Mapper
     * - "com.conmu"               # 拦截 com.conmu 及其所有子包下的Mapper
     */
    @Override
    public String[] getMapperPackages() {
        return new String[]{
            "com.conmu.sms.mapper",      // 主要Mapper包
            "com.conmu.sms.dao"          // 如果还有其他DAO包
        };
        
        // 其他配置示例：
        // return null;                     // 拦截所有Mapper
        // return new String[0];            // 拦截所有Mapper  
        // return new String[]{"com.conmu"}; // 拦截com.conmu下所有Mapper
    }
    
    /**
     * 是否启用热重载功能
     */
    @Override
    public boolean isHotReloadEnabled() {
        return true;
    }
}