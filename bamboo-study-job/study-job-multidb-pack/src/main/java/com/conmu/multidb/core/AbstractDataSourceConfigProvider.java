package com.conmu.multidb.core;

import com.conmu.multidb.config.DataSourceConfigProvider;

import javax.sql.DataSource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 数据源配置提供者抽象基类
 * 提供共享映射管理功能，子类只需实现数据源和包扫描相关方法
 *
 * @author mucongcong
 * @date 2025/12/01
 */
public abstract class AbstractDataSourceConfigProvider
        implements DataSourceConfigProvider {

    /**
     * k = mapperClass, v = dataSourceKey
     **/
    final ConcurrentHashMap<String, String> mapperDataSourceMap = new ConcurrentHashMap<>();

    /**
     * k = dataSourceKey, v = DataSource
     **/
    private final ConcurrentHashMap<String, DataSource> dataSources = new ConcurrentHashMap<>();

    /**
     * 扫描拦截包路径
     **/
    final Set<String> mapperPackages = new HashSet<>();

    protected abstract void init();

    /**
     * 返回数据源的只读视图
     */
    public final Map<String, DataSource> getDataSources() {
        return Collections.unmodifiableMap(dataSources);
    }

    /**
     * 返回mapperClass到数据源标识的只读映射
     */
    public final Map<String, String> getMapperDataSource() {
        return Collections.unmodifiableMap(mapperDataSourceMap);
    }

    /**
     * 获取拦截包路径
     **/
    public final Set<String> getMapperPackages() {
        return Collections.unmodifiableSet(mapperPackages);
    }

    /**
     * @Description 添加数据源
     *
     * @param dataSourceKey 数据源标识
     * @param dataSource 数据源
     **/
    protected void addDataSource(String dataSourceKey, DataSource dataSource) {
        dataSources.put(dataSourceKey, dataSource);
    }

    /**
     * 更新单个Mapper映射
     *
     * @param mapperClass Mapper类全名
     * @param dataSourceKey 数据源标识
     */
    protected void addMapper(String mapperClass, String dataSourceKey) {
        mapperDataSourceMap.put(mapperClass, dataSourceKey);
    }

    /**
     * 删除Mapper映射，采用默认数据源
     *
     * @param mapperClass Mapper类全名
     */
    protected void removeMapper(String mapperClass) {
        mapperDataSourceMap.remove(mapperClass);
    }

    /**
     * 添加拦截包路径
     *
     * @param mapperPackage 拦截包路径
     **/
    protected void addMapperPackages(String mapperPackage) {
        mapperPackages.add(mapperPackage);
    }

    /**
     * 删除拦截包路径
     *
     * @param mapperPackage 拦截包路径
     */
    protected void removeMapperPackages(String mapperPackage) {
        mapperPackages.remove(mapperPackage);
    }

}
