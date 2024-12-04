package com.conmu.utils;

import com.conmu.service.TestService;

/**
 * @author mucongcong
 * @date 2024/06/19 20:11
 * @since
 **/
public class TestUtils {
    public static TestService testService = SpringUtils.getBean(TestService.class);

    public static TestService get(){
        if (testService == null) {
            System.out.println(1);
        }
        return testService;
    }
}