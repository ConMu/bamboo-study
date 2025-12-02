package com.conmu.multidb.config;

import javax.sql.DataSource;
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
     * 获取所有数据源配置
     * @return key: 数据源标识(如ds0,ds1), value: DataSource实例
     */
    Map<String, DataSource> getDataSources();
    
    /**
     * 获取默认数据源标识
     * @return 默认数据源key
     */
    String getDefaultDataSourceKey();
    
    /**
     * 获取Mapper到数据源的初始映射关系
     * @return key: Mapper类全名, value: 数据源标识
     */
    Map<String, String> getInitialMapperDataSourceMappings();
    
    /**
     * 指定要扫描的Mapper包路径
     * 如果返回null或空数组，则拦截所有以Mapper结尾的接口方法
     * 如果指定了包路径，则只拦截这些包下的Mapper接口
     *
     * 示例：
     * - "com.example.mapper" 只拦截这个包下的Mapper
     * - "com.example" 拦截com.example及其子包下的Mapper
     */
    String[] getMapperPackages();

    /**
     * 是否启用热重载功能
     */
    default boolean isHotReloadEnabled() {
        return true;
    }
}