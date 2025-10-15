package com.conmu.sms.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author mucongcong
 * @date 2025/10/15 16:38
 * @since
 **/
public class DynamicDataSource extends AbstractRoutingDataSource {
    /**
     * 每次请求动态请求哪一个数据源
     *
     * @return
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceHolder.getDataSource();
    }
}