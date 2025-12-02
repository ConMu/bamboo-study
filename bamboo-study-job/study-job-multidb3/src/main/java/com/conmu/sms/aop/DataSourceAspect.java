package com.conmu.sms.aop;

import com.conmu.sms.config.DataSourceHolder;
import com.conmu.sms.config.MapperContextHolder;
import com.conmu.sms.config.MapperDataSourceRegistry;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Mapper级别的数据源切换切面
 * @author mucongcong
 * @date 2025/10/15 18:06
 * @since 1.0.0
 **/
@Aspect
@Component
public class DataSourceAspect {

    private static final Logger log = LoggerFactory.getLogger(DataSourceAspect.class);

    @Autowired
    private MapperDataSourceRegistry registry;

    /**
     * 在Mapper方法执行前设置数据源和上下文
     */
    @Before("execution(* com.conmu.sms.dao.mapper..*(..))")
    public void beforeMapperMethod(JoinPoint joinPoint) {
        // 获取正在执行的Mapper类名
        String mapperClass = joinPoint.getTarget().getClass().getInterfaces()[0].getName();
        log.info("🎯 [AOP拦截] Mapper调用: {}", mapperClass);

        // 从注册表中查找配置的数据源
        String dataSource = registry.getDataSource(mapperClass);
        log.info("🔍 [注册表查询] Mapper {} -> 数据源: {}", mapperClass, dataSource);

        // 设置当前线程的数据源
        if (dataSource != null) {
            DataSourceHolder.setDataSource(dataSource);
            log.info("✅ [ThreadLocal设置] 已设置当前线程数据源为: {}", dataSource);

            // 验证设置是否成功
            String verifyDs = DataSourceHolder.getDataSource();
            log.info("✔️ [ThreadLocal验证] 当前ThreadLocal中的数据源: {}", verifyDs);
        } else {
            log.warn("⚠️ [未找到配置] 未找到Mapper {} 的数据源配置，使用默认数据源", mapperClass);
        }
    }

    /**
     * 在Mapper方法执行后清理上下文
     */
    @After("execution(* com.conmu.sms.dao.mapper..*(..))")
    public void afterMapperMethod() {
        MapperContextHolder.clearContext();
        DataSourceHolder.clearDataSource();
        System.out.println("🧹 [Mapper AOP] 上下文已清理");
    }
}