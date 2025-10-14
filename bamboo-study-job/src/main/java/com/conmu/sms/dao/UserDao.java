package com.conmu.sms.dao;

import com.conmu.sms.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author mucongcong
 * @date 2025/10/14 15:49
 * @since
 **/

@Mapper
public interface UserDao {

    void insert(User user);

    User findByUsername(@Param("username") String username);
}