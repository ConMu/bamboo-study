package com.conmu.sms.config;

/**
 * @author mucongcong
 * @date 2025/10/15 16:37
 * @since
 **/
/**
 * @author qyz
 */
public class DataSourceHolder {

    /**
     * 线程本地环境 （存储数据库名称）
     */
    private static final ThreadLocal<String> DATA_SOURCES = new ThreadLocal<>();

    /**
     * 设置数据源(动态切换数据源),就是调用这个setDataSource方法
     */
    public static void setDataSource(String customerType) {
        DATA_SOURCES.set(customerType);
    }

    /**
     * 获取数据源
     */
    public static String getDataSource() {
        return DATA_SOURCES.get();
    }

    /**
     * 清除数据源
     */
    public static void clearDataSource() {
        DATA_SOURCES.remove();
    }
}