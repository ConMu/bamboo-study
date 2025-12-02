package com.conmu.multidb.core;

/**
 * 数据源上下文持有者
 * 使用ThreadLocal在当前线程中保存数据源标识
 * 
 * @author mucongcong
 * @date 2025/12/01
 */
public class DataSourceContextHolder {
    
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();
    
    /**
     * 设置当前线程的数据源
     * @param dataSourceKey 数据源标识
     */
    public static void setDataSource(String dataSourceKey) {
        contextHolder.set(dataSourceKey);
    }
    
    /**
     * 获取当前线程的数据源
     * @return 数据源标识
     */
    public static String getDataSource() {
        return contextHolder.get();
    }
    
    /**
     * 清除当前线程的数据源设置
     */
    public static void clearDataSource() {
        contextHolder.remove();
    }

}