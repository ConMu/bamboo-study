package com.conmu.sms.service;

import com.conmu.sms.dao.entity.People;
import com.conmu.sms.dao.mapper.PeopleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author mucongcong
 * @date 2025/10/14 17:30
 * @since
 **/
@Service
public class PeopleService {

    @Autowired
    private PeopleMapper peopleMapper;

    public int insert(People people) {
        return peopleMapper.insert(people);
    }

    public People findByName(String name) {
        return peopleMapper.findByName(name);
    }

    public People findById(Long id) {
        return peopleMapper.findById(id);
    }

    public int update(People people) {
        return peopleMapper.update(people);
    }

    public int deleteById(Long id) {
        return peopleMapper.deleteById(id);
    }
}
