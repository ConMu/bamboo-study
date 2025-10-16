package com.conmu.sms.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mucongcong
 * @date 2025/10/15 16:38
 * @since
 **/
@Configuration
@MapperScan(basePackages = "com.conmu.sms.dao.mapper", sqlSessionFactoryRef = "SqlSessionFactory")
public class DynamicDataSourceConfig {
    /**
     * 将第1个数据源对象放入Spring容器中
     *
     * @ConfigurationProperties 读取application.yml中spring.datasource.db1的配置参数并映射成为一个对象
     */
    @Bean(name = "dateSource1")
    @ConfigurationProperties(prefix = "spring.datasource.db1.druid")
    public DataSource DateSource1() {
        return DataSourceBuilder.create().type(DruidDataSource.class).build();
    }

    /**
     * 将第2个数据源对象放入Spring容器中
     */
    @Bean(name = "dateSource2")
    @ConfigurationProperties(prefix = "spring.datasource.db2.druid")
    public DataSource DateSource2() {
        return DataSourceBuilder.create().type(DruidDataSource.class).build();
    }

    /**
     * 将动态代理数据源对象放入Spring容器中
     */
    @Bean(name = "dynamicDataSource")
    public DynamicDataSource DynamicDataSource(@Qualifier("dateSource1") DataSource primaryDataSource,
                                               @Qualifier("dateSource2") DataSource secondaryDataSource) {
        // 这个地方是比较核心的targetDataSource 集合是我们数据库和名字之间的映射
        Map<Object, Object> targetDataSource = new HashMap<>();
        targetDataSource.put("ds0", primaryDataSource);
        targetDataSource.put("ds1", secondaryDataSource);

        DynamicDataSource dataSource = new DynamicDataSource();
        // 设置所有的数据源
        dataSource.setTargetDataSources(targetDataSource);
        // 设置默认使用的数据源对象
        dataSource.setDefaultTargetDataSource(primaryDataSource);

        return dataSource;
    }


    @Bean(name = "SqlSessionFactory")
    public SqlSessionFactory SqlSessionFactory(@Qualifier("dynamicDataSource") DataSource dynamicDataSource)
            throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dynamicDataSource);
        bean.setMapperLocations(
                // 设置数据库mapper的xml文件路径
                new PathMatchingResourcePatternResolver()
                        .getResources("classpath:com/conmu/sms/dao/mapper/**/*.xml"));
        return bean.getObject();
    }
}