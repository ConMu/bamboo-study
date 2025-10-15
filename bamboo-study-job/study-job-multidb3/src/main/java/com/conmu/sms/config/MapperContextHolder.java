package com.conmu.sms.config;

/**
 * Mapper上下文持有器 - 用于追踪当前正在执行的Mapper
 * @author mucongcong
 * @date 2025/10/15 18:05
 * @since 1.0.0
 **/
public class MapperContextHolder {
    
    /**
     * ThreadLocal存储当前线程正在执行的Mapper类名
     */
    private static final ThreadLocal<String> CURRENT_MAPPER = new ThreadLocal<>();
    
    /**
     * 设置当前Mapper
     * @param mapperClass Mapper全类名
     */
    public static void setCurrentMapper(String mapperClass) {
        CURRENT_MAPPER.set(mapperClass);
        System.out.println("🎯 [上下文] 设置当前Mapper: " + 
                          (mapperClass != null ? mapperClass.substring(mapperClass.lastIndexOf(".") + 1) : "null"));
    }
    
    /**
     * 获取当前Mapper
     * @return Mapper全类名
     */
    public static String getCurrentMapper() {
        return CURRENT_MAPPER.get();
    }
    
    /**
     * 清除当前Mapper上下文
     */
    public static void clearContext() {
        String mapper = CURRENT_MAPPER.get();
        if (mapper != null) {
            System.out.println("🧹 [上下文] 清除Mapper上下文: " + 
                              mapper.substring(mapper.lastIndexOf(".") + 1));
        }
        CURRENT_MAPPER.remove();
    }
    
    /**
     * 获取当前Mapper的简名
     * @return Mapper简名 (如: UserMapper)
     */
    public static String getCurrentMapperSimpleName() {
        String mapper = getCurrentMapper();
        if (mapper == null) {
            return null;
        }
        return mapper.substring(mapper.lastIndexOf(".") + 1);
    }
}