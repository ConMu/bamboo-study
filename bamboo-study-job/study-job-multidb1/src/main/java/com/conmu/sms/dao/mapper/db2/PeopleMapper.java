package com.conmu.sms.dao.mapper.db2;

import com.conmu.sms.dao.entity.People;
import org.springframework.stereotype.Repository;

/**
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