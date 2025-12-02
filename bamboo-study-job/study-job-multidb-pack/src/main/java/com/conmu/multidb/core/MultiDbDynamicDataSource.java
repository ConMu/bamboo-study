package com.conmu.multidb.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 动态数据源路由器
 * 继承AbstractRoutingDataSource，根据当前线程上下文决定使用哪个数据源
 *
 * @author mucongcong
 * @date 2025/12/01
 */
public class MultiDbDynamicDataSource extends AbstractRoutingDataSource {

    private static final Logger logger = LoggerFactory.getLogger(MultiDbDynamicDataSource.class);

    @Override
    protected Object determineCurrentLookupKey() {
        String dataSource = DataSourceContextHolder.getDataSource();

        // 输出调试信息
        if (dataSource != null) {
            logger.debug("🎯 [MultiDbDynamicDataSource] 当前线程数据源: {}", dataSource);
        } else {
            logger.debug("⚠️ [MultiDbDynamicDataSource] 当前线程未设置数据源，将使用默认数据源");
        }
        
        return dataSource;
    }
    
    /**
     * 在查找数据源后清理ThreadLocal，避免内存泄漏
     */
    @Override
    public java.sql.Connection getConnection() throws java.sql.SQLException {
        try {
            return super.getConnection();
        } finally {
            // 获取连接后清理ThreadLocal
            DataSourceContextHolder.clearDataSource();
        }
    }
    
    /**
     * 重载方法，支持用户名和密码的连接
     */
    @Override
    public java.sql.Connection getConnection(String username, String password) throws java.sql.SQLException {
        try {
            return super.getConnection(username, password);
        } finally {
            // 获取连接后清理ThreadLocal
            DataSourceContextHolder.clearDataSource();
        }
    }
}