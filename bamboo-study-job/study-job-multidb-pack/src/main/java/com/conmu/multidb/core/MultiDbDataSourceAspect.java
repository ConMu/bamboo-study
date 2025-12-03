package com.conmu.multidb.core;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.Set;

/**
 * 多数据源切面处理器 - 支持配置化拦截路径
 * 拦截Mapper方法调用，自动设置正确的数据源
 *
 * 支持拦截配置：
 * 1. 通过DataSourceConfigProvider.getMapperPackages()配置具体包路径
 * 2. 如果未配置包路径，默认拦截所有以Mapper结尾的接口方法
 *
 * @author mucongcong
 * @date 2025/12/01
 */
@Aspect
@Component
@ConditionalOnBean(AbstractDataSourceConfigProvider.class)
public class MultiDbDataSourceAspect {

    private static final Logger logger = LoggerFactory.getLogger(MultiDbDataSourceAspect.class);

    @Autowired
    private DbManageRouteHolder dbManageRouteHolder;

    @Autowired
    private AbstractDataSourceConfigProvider configProvider;

    /**
     * 配置的Mapper包路径集合，用于精确匹配
     */
    private Set<String> configuredPackages;

    @PostConstruct
    public void init() {
        // 获取配置的Mapper包路径
        this.configuredPackages = configProvider.mapperPackages;
        if (CollectionUtils.isEmpty(configuredPackages)) {
            logger.info("🎯 [MultiDbDataSourceAspect] 未配置拦截包路径，使用默认拦截策略: @Mapper注解 + *..*Mapper.*(..)");
        } else {
            logger.info("🎯 [MultiDbDataSourceAspect] 配置的Mapper包路径: {}", configuredPackages);
        }
        logger.info("✅ [MultiDbDataSourceAspect] 切面初始化完成");
    }

    /**
     * 主要切点：拦截带有@Mapper注解的类的所有方法（优先，最精确）
     */
    @Pointcut("@within(org.apache.ibatis.annotations.Mapper)")
    public void mapperAnnotationMethods() {}

    /**
     * 兜底切点：拦截以Mapper结尾但没有@Mapper注解的接口方法
     */
    @Pointcut("execution(* *..*Mapper.*(..)) && !@within(org.apache.ibatis.annotations.Mapper)")
    public void mapperNamingMethodsOnly() {}

    /**
     * 扩展切点：拦截带有@Repository注解但不是Mapper的类
     */
    @Pointcut("@within(org.springframework.stereotype.Repository) && !@within(org.apache.ibatis.annotations.Mapper) && !execution(* *..*Mapper.*(..))")
    public void repositoryOnlyMethods() {}

    /**
     * 拦截Mapper接口的方法调用 - 避免重复拦截
     */
    @Around("mapperAnnotationMethods() || mapperNamingMethodsOnly() || repositoryOnlyMethods()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取目标类的接口信息
        Class<?> targetClass = joinPoint.getTarget().getClass();
        String mapperClassName = getMapperInterface(targetClass);

        if (mapperClassName == null) {
            // 没有找到Mapper接口，跳过处理
            return joinPoint.proceed();
        }

        // 检查是否应该拦截这个Mapper
        if (!shouldIntercept(mapperClassName)) {
            return joinPoint.proceed();
        }

        try {
            // 从路由管理器获取数据源
            String dataSourceKey = dbManageRouteHolder.get(mapperClassName);

            // 设置当前线程的数据源
            DataSourceContextHolder.setDataSource(dataSourceKey);

            logger.debug("🔄 [MultiDbDataSourceAspect] {}.{}() → 数据源: {}",
                        getSimpleClassName(mapperClassName),
                        joinPoint.getSignature().getName(),
                        dataSourceKey);

            // 执行目标方法
            return joinPoint.proceed();

        } catch (Exception e) {
            logger.error("❌ [MultiDbDataSourceAspect] 执行失败: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * 判断是否应该拦截这个Mapper
     * @param mapperClassName Mapper接口全类名
     * @return true表示应该拦截，false表示跳过
     */
    private boolean shouldIntercept(String mapperClassName) {
        // 如果配置了包路径，检查是否在指定包下
        if (!configuredPackages.isEmpty()) {
            boolean inConfiguredPackage = configuredPackages.stream()
                .anyMatch(pkg -> mapperClassName.startsWith(pkg + ".") || mapperClassName.startsWith(pkg));

            if (!inConfiguredPackage) {
                logger.debug("⚠️ [MultiDbDataSourceAspect] {} 不在配置的包路径中，跳过拦截", getSimpleClassName(mapperClassName));
                return false;
            }

            logger.debug("✅ [MultiDbDataSourceAspect] {} 在配置包路径中，进行拦截", getSimpleClassName(mapperClassName));
        }

        return true;
    }

    /**
     * 从目标类中找到Mapper接口
     * @param targetClass 目标类（通常是MyBatis生成的代理类）
     * @return Mapper接口的全类名
     */
    private String getMapperInterface(Class<?> targetClass) {
        // 获取所有接口
        Class<?>[] interfaces = targetClass.getInterfaces();

        for (Class<?> interfaceClass : interfaces) {
            String className = interfaceClass.getName();
            // 查找以Mapper结尾的接口（包括@Repository注解的Mapper）
            if (className.endsWith("Mapper")) {
                return className;
            }
        }
        
        // 如果当前类没有找到，尝试查找父类的接口
        Class<?> superClass = targetClass.getSuperclass();
        if (superClass != null && superClass != Object.class) {
            return getMapperInterface(superClass);
        }
        
        return null;
    }
    
    /**
     * 获取类的简单名称
     */
    private String getSimpleClassName(String fullClassName) {
        return fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
    }
}