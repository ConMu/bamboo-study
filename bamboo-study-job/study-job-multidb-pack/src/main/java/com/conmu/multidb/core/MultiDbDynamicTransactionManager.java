package com.conmu.multidb.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;

/**
 * 多数据源动态事务管理器
 * 根据当前数据源上下文自动路由到对应的事务管理器
 * 
 * 使用方式：
 * 1. Demo模块为每个数据源创建独立的PlatformTransactionManager Bean
 * 2. Bean命名规则：{dataSourceKey}TransactionManager
 * 3. 本类会自动发现并路由到对应的事务管理器
 * 
 * @author mucongcong
 * @date 2025/12/03
 */
public class MultiDbDynamicTransactionManager implements PlatformTransactionManager, ApplicationContextAware {
    
    private static final Logger logger = LoggerFactory.getLogger(MultiDbDynamicTransactionManager.class);
    
    private ApplicationContext applicationContext;
    private AbstractDataSourceConfigProvider configProvider;

    public MultiDbDynamicTransactionManager(AbstractDataSourceConfigProvider configProvider) {
        this.configProvider = configProvider;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        logger.info("🎯 [MultiDbDynamicTransactionManager] ApplicationContext初始化完成");
    }
    
    /**
     * 获取当前数据源对应的事务管理器
     */
    private PlatformTransactionManager getCurrentTransactionManager() {
        String currentDataSource = DataSourceContextHolder.getDataSource();
        
        if (currentDataSource == null) {
            logger.debug("🔍 [MultiDbDynamicTransactionManager] 无数据源上下文，使用默认事务管理器");
            return getDefaultTransactionManager();
        }
        
        // 按命名约定查找事务管理器：{dataSourceKey}TransactionManager
        String txManagerBeanName = currentDataSource + "TransactionManager";
        
        if (applicationContext.containsBean(txManagerBeanName)) {
            PlatformTransactionManager txManager = applicationContext.getBean(txManagerBeanName, PlatformTransactionManager.class);
            logger.debug("🔍 [MultiDbDynamicTransactionManager] 使用事务管理器: {} → {}", currentDataSource, txManagerBeanName);
            return txManager;
        } else {
            logger.warn("⚠️ [MultiDbDynamicTransactionManager] 数据源 {} 对应的事务管理器 {} 不存在，使用默认", currentDataSource, txManagerBeanName);
            return getDefaultTransactionManager();
        }
    }
    
    /**
     * 动态获取默认事务管理器
     * 每次都重新获取，支持默认数据源的动态变化
     */
    private PlatformTransactionManager getDefaultTransactionManager() {
        String defaultDataSourceKey = configProvider.getDefaultDataSourceKey();
        String defaultTxManagerBeanName = defaultDataSourceKey + "TransactionManager";

        if (applicationContext.containsBean(defaultTxManagerBeanName)) {
            PlatformTransactionManager txManager = applicationContext.getBean(defaultTxManagerBeanName, PlatformTransactionManager.class);
            logger.debug("🎯 [MultiDbDynamicTransactionManager] 使用默认事务管理器: {}", defaultTxManagerBeanName);
            return txManager;
        } else {
            throw new IllegalStateException("默认事务管理器 '" + defaultTxManagerBeanName + "' 不存在，请确保在Demo中创建该Bean");
        }
    }
    
    @Override
    public TransactionStatus getTransaction(TransactionDefinition definition) throws TransactionException {
        PlatformTransactionManager txManager = getCurrentTransactionManager();
        String dataSource = DataSourceContextHolder.getDataSource();
        
        logger.debug("🔄 [MultiDbDynamicTransactionManager] 开始事务: {} [{}]", 
                    dataSource != null ? dataSource : "default", 
                    definition.getName());
        
        return txManager.getTransaction(definition);
    }
    
    @Override
    public void commit(TransactionStatus status) throws TransactionException {
        PlatformTransactionManager txManager = getCurrentTransactionManager();
        String dataSource = DataSourceContextHolder.getDataSource();
        
        logger.debug("✅ [MultiDbDynamicTransactionManager] 提交事务: {}", 
                    dataSource != null ? dataSource : "default");
        
        txManager.commit(status);
    }
    
    @Override
    public void rollback(TransactionStatus status) throws TransactionException {
        PlatformTransactionManager txManager = getCurrentTransactionManager();
        String dataSource = DataSourceContextHolder.getDataSource();
        
        logger.debug("❌ [MultiDbDynamicTransactionManager] 回滚事务: {}", 
                    dataSource != null ? dataSource : "default");
        
        txManager.rollback(status);
    }
}