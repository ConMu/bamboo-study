package com.conmu.sms.service;

import com.conmu.sms.dao.entity.User;
import com.conmu.sms.dao.mapper.db1.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author mucongcong
 * @date 2025/10/14 17:28
 * @since
 **/
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public int insert(User user) {
        return userMapper.insert(user);
    }

    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    public int update(User user) {
        return userMapper.update(user);
    }
}
