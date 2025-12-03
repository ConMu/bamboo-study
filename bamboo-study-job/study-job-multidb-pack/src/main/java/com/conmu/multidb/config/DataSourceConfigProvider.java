package com.conmu.multidb.config;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * 数据源配置提供者接口
 * 外部使用方需要实现此接口来提供具体的数据源配置
 * 
 * @author mucongcong
 * @date 2025/12/01
 */
public interface DataSourceConfigProvider {

    /**
     * @Description 初始化数据源
     **/
    void initDataSource();

    /**
     * @Description 初始化mapper到数据源的映射关系
     **/
    void initMapperDataSourceMappings();

    /**
     * @Description 初始化mapper拦截包路径
     **/
    void initAspectMapperPackages();

    /**
     * 获取默认数据源标识
     * @return 默认数据源key
     */
    String getDefaultDataSourceKey();
}