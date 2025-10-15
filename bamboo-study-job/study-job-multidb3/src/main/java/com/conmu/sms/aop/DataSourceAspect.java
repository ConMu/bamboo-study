package com.conmu.sms.aop;

import com.conmu.sms.config.DataSourceHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

/**
 * @author mucongcong
 * @date 2025/10/15 16:41
 * @since
 **/
@Aspect
@Order(1)
@Configuration
@Slf4j
public class DataSourceAspect {
    /**
     * 数据库名称在请求头中的key（从header中取）
     */
    private static final String DSNO = "dsNo";

    /**
     * 切入点,放在controller的每个方法上进行切入,更新数据源
     */
    @Pointcut("execution(* com.conmu.sms.controller..*.*(..))")
    private void anyMethod() {
    }

    @Before("anyMethod()")
    public void dataSourceChange() {
        // 请求头head中获取对应数据库编号 name=dsNo
        String dsNo = ((ServletRequestAttributes) Objects
                .requireNonNull(RequestContextHolder.getRequestAttributes()))
                .getRequest()
                .getHeader(DSNO);
        System.out.println("当前数据源: " + dsNo);
        if (StringUtils.isEmpty(dsNo)) {
            log.warn("数据源不存在，采用默认ds0");
            dsNo = "ds0";
        }
        // 根据请求头中数据库名称来更改对应的数据源（核心）
        DataSourceHolder.setDataSource(dsNo);
    }

    @After("anyMethod()")
    public void after() {
        // 数据源重置（必须在请求完成后清空ThreadLocal线程上下文，否则会内存溢出）
        DataSourceHolder.clearDataSource();
    }
}