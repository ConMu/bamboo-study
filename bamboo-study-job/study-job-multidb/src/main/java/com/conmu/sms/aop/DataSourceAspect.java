package com.conmu.sms.aop;

import com.conmu.sms.config.DataSourceHolder;
import com.conmu.sms.config.MapperContextHolder;
import com.conmu.sms.config.MapperDataSourceRegistry;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
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

    @Autowired
    private MapperDataSourceRegistry registry;

    /**
     * 在Mapper方法执行前设置数据源和上下文
     */
    @Before("execution(* com.conmu.sms.dao.mapper..*(..))")
    public void beforeMapperMethod(JoinPoint joinPoint) {
        // 获取当前执行的Mapper类名
        String mapperClass = joinPoint.getTarget().getClass().getInterfaces()[0].getName();

        // 设置Mapper上下文
        MapperContextHolder.setCurrentMapper(mapperClass);

        // 从注册表获取对应的数据源
        String dataSource = registry.getDataSource(mapperClass);

        // 设置数据源
        DataSourceHolder.setDataSource(dataSource);

        String mapperSimpleName = mapperClass.substring(mapperClass.lastIndexOf(".") + 1);
        String methodName = joinPoint.getSignature().getName();

        System.out.println("🎯 [Mapper AOP] " + mapperSimpleName + "." + methodName + " -> 数据源: " + dataSource);
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