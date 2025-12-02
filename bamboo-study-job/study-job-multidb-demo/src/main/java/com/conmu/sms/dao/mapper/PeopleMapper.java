package com.conmu.sms.dao.mapper;

import com.conmu.sms.dao.entity.People;
import org.springframework.stereotype.Repository;

/**
 * 人员Mapper - 支持多数据源动态切换
 * @author mucongcong
 * @date 2025/10/14 17:35
 * @since
 **/
@Repository
public interface PeopleMapper {

    int insert(People entity);

    People findByName(String name);

    People findById(Long id);

    int update(People record);
    
    int deleteById(Long id);
}