package com.conmu.sms.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.conmu.multidb.config.DataSourceConfigProvider;
import com.conmu.multidb.core.DbManageRouteHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 多数据源映射配置提供者 - 集成数据源创建和配置
 *
 * @author mucongcong
 * @date 2025/12/02
 */
@Configuration
public class MultiDbConfigProvider implements DataSourceConfigProvider {

    @Autowired
    private DbManageRouteHolder routeHolder;

    /**
     * Mapper到数据源的映射配置 - 提供默认映射，也可从配置文件覆盖
     * key: Mapper类全名, value: 数据源标识
     */
    private Map<String, String> mapperMappings = getDefaultMapperMappings();

    /**
     * 提供默认的 Mapper 到数据源映射关系
     */
    private static Map<String, String> getDefaultMapperMappings() {
        Map<String, String> mappings = new HashMap<>();
        // SmsTemplateMapper 使用 ds0 (yunxin_recovery数据库)
        mappings.put("com.conmu.sms.mapper.SmsTemplateMapper", "ds0");
        // UserMapper 使用 ds1 (test数据库)
        mappings.put("com.conmu.sms.mapper.UserMapper", "ds1");
        return mappings;
    }

    /**
     * 将第1个数据源对象放入Spring容器中
     */
    @Bean(name = "multiDbDataSource1")
    @ConfigurationProperties(prefix = "spring.datasource.db1.druid")
    public DataSource multiDbDataSource1() {
        return DataSourceBuilder.create().type(DruidDataSource.class).build();
    }

    /**
     * 将第2个数据源对象放入Spring容器中
     */
    @Bean(name = "multiDbDataSource2")
    @ConfigurationProperties(prefix = "spring.datasource.db2.druid")
    public DataSource multiDbDataSource2() {
        return DataSourceBuilder.create().type(DruidDataSource.class).build();
    }

    @Override
    public Map<String, DataSource> getDataSources() {
        // 直接调用自己创建的数据源方法
        Map<String, DataSource> dataSources = new HashMap<>();
        dataSources.put("ds0", multiDbDataSource1());
        dataSources.put("ds1", multiDbDataSource2());
        return dataSources;
    }

    @Override
    public String getDefaultDataSourceKey() {
        // 写死默认数据源
        return "ds0";
    }

    @Override
    public Map<String, String> getMapperDataSourceMappings() {
        // 返回映射关系
        return mapperMappings;
    }

    @Override
    public String[] getMapperPackages() {
        // 写死扫描包路径
        return new String[]{"com.conmu.sms.mapper"};
    }

    /**
     * 动态更新单个映射 - 自动重载路由配置
     * @param mapperClass Mapper类名
     * @param dataSourceKey 数据源key
     */
    public void updateMapping(String mapperClass, String dataSourceKey) {
        this.mapperMappings.put(mapperClass, dataSourceKey);
        // 自动重载路由配置，对外层使用方无感
        routeHolder.reload();
    }

    /**
     * 动态更新多个映射 - 自动重载路由配置
     * @param newMappings 新的映射关系
     */
    public void updateMappings(Map<String, String> newMappings) {
        this.mapperMappings.putAll(newMappings);
        // 自动重载路由配置，对外层使用方无感
        routeHolder.reload();
    }

    /**
     * 删除映射 - 自动重载路由配置
     * @param mapperClass Mapper类名
     */
    public void removeMapping(String mapperClass) {
        this.mapperMappings.remove(mapperClass);
        // 自动重载路由配置，对外层使用方无感
        routeHolder.reload();
    }

    /**
     * 获取所有映射关系
     * @return 映射关系
     */
    public Map<String, String> getAllMappings() {
        return new HashMap<>(mapperMappings);
    }
}
