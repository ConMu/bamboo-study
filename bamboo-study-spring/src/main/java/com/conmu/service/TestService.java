package com.conmu.service;

import org.springframework.stereotype.Service;

/**
 * @author mucongcong
 * @date 2024/06/19 19:48
 * @since
 **/
@Service
public class TestService {
    private static final TestService instance = new TestService();

    private TestService () {}

    public static TestService getInstance() {
        return instance;
    }


}