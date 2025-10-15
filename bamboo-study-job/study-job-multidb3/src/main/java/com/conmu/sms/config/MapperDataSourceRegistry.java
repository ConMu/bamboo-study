package com.conmu.sms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Mapper级别的数据源注册表 - 支持热切换
 * @author mucongcong
 * @date 2025/10/15 18:03
 * @since 1.0.0
 **/
@Component
public class MapperDataSourceRegistry {

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Mapper类名到数据源的映射关系
     * key: Mapper全类名 (如: com.conmu.sms.dao.mapper.UserMapper)
     * value: 数据源标识 (ds0, ds1)
     */
    private final ConcurrentHashMap<String, String> mapperDataSourceMap = new ConcurrentHashMap<>();

    /**
     * 默认数据源配置
     */
    private static final String DEFAULT_DATASOURCE = "ds0";

    /**
     * Mapper包路径
     */
    private static final String MAPPER_PACKAGE = "com.conmu.sms.dao.mapper";

    /**
     * Spring容器初始化完成后自动扫描所有Mapper
     */
    @PostConstruct
    public void initDefaultConfig() {
        try {
            // 获取所有Mapper接口的Bean
            Map<String, Object> mapperBeans = applicationContext.getBeansOfType(Object.class);

            int discoveredMappers = 0;

            for (Map.Entry<String, Object> entry : mapperBeans.entrySet()) {
                Object bean = entry.getValue();

                // 检查是否是Mapper代理对象
                if (bean != null) {
                    Class<?>[] interfaces = bean.getClass().getInterfaces();

                    for (Class<?> interfaceClass : interfaces) {
                        String className = interfaceClass.getName();

                        // 检查是否是我们的Mapper包下的接口，并且以Mapper结尾
                        if (className.startsWith(MAPPER_PACKAGE) && className.endsWith("Mapper")) {
                            mapperDataSourceMap.put(className, DEFAULT_DATASOURCE);
                            discoveredMappers++;

                            String simpleName = className.substring(className.lastIndexOf(".") + 1);
                            System.out.println("🔍 [自动发现] " + simpleName + " -> " + DEFAULT_DATASOURCE);
                        }
                    }
                }
            }

            System.out.println("✅ [初始化完成] 自动发现并配置了 " + discoveredMappers + " 个Mapper，默认数据源: " + DEFAULT_DATASOURCE);

            // 如果没有发现任何Mapper，添加一个示例配置避免空配置
            if (discoveredMappers == 0) {
                System.out.println("⚠️  [警告] 未发现任何Mapper，添加示例配置");
                mapperDataSourceMap.put("com.conmu.sms.dao.mapper.UserMapper", DEFAULT_DATASOURCE);
                mapperDataSourceMap.put("com.conmu.sms.dao.mapper.PeopleMapper", DEFAULT_DATASOURCE);
            }

        } catch (Exception e) {
            System.err.println("❌ [错误] Mapper自动发现失败: " + e.getMessage());
            // 降级到手动配置
            fallbackToManualConfig();
        }
    }

    /**
     * 降级到手动配置
     */
    private void fallbackToManualConfig() {
        System.out.println("🔄 [降级] 使用手动配置模式");
        mapperDataSourceMap.put("com.conmu.sms.dao.mapper.PeopleMapper", DEFAULT_DATASOURCE);
        mapperDataSourceMap.put("com.conmu.sms.dao.mapper.UserMapper", DEFAULT_DATASOURCE);
    }
    
    /**
     * 根据Mapper获取对应的数据源
     * @param mapperClass Mapper类名
     * @return 数据源标识
     */
    public String getDataSource(String mapperClass) {
        return mapperDataSourceMap.getOrDefault(mapperClass, DEFAULT_DATASOURCE);
    }
    
    /**
     * 热切换：设置Mapper的数据源
     * @param mapperClass Mapper类名
     * @param dataSource 数据源标识
     * @return 是否切换成功
     */
    public boolean switchDataSource(String mapperClass, String dataSource) {
        if (mapperClass == null || dataSource == null) {
            return false;
        }
        
        // 验证数据源是否有效
        if (!"ds0".equals(dataSource) && !"ds1".equals(dataSource)) {
            return false;
        }
        
        String oldDataSource = mapperDataSourceMap.put(mapperClass, dataSource);
        System.out.println("📊 [热切换] Mapper: " + mapperClass + 
                          " 数据源切换: " + oldDataSource + " -> " + dataSource);
        return true;
    }
    
    /**
     * 获取所有Mapper的数据源配置
     * @return 配置映射表
     */
    public ConcurrentHashMap<String, String> getAllMappings() {
        return new ConcurrentHashMap<>(mapperDataSourceMap);
    }
    
    /**
     * 重置为默认配置
     */
    public void resetToDefault() {
        mapperDataSourceMap.clear();
        initDefaultConfig();
        System.out.println("🔄 [重置] Mapper数据源配置已重置为默认值");
    }
    
    /**
     * 获取当前配置的字符串表示
     */
    public String getConfigInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("📋 当前Mapper数据源配置:\n");
        mapperDataSourceMap.forEach((mapper, ds) -> {
            String simpleName = mapper.substring(mapper.lastIndexOf(".") + 1);
            sb.append("  - ").append(simpleName).append(" -> ").append(ds).append("\n");
        });
        return sb.toString();
    }
}