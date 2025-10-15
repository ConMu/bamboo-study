package com.conmu.sms.dao.mapper.db1;

import com.conmu.sms.dao.entity.User;
import org.springframework.stereotype.Repository;

/**
 * @author mucongcong
 * @date 2022/07/12 14:09
 * @since
 **/
@Repository
public interface UserMapper {

    int insert(User entity);

    User findByUsername(String username);

    int update(User record);
}
