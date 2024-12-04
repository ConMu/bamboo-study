package com.conmu.controller;

import com.conmu.service.TestService;
import com.conmu.utils.SpringUtils;
import com.conmu.utils.TestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mucongcong
 * @date 2024/06/19 19:39
 * @since
 **/
@RestController
@RequestMapping("/test")
public class TestController {

    private static final Logger log = LoggerFactory.getLogger(TestController.class);
    @Autowired
    private TestService testService;

    protected TestService smsUplinkRecordService = SpringUtils.getBean(TestService.class);


    @RequestMapping(value = {"/1"}, method = RequestMethod.GET)
    public String test() {
        String beanSing = testService.toString();
//        String sig = testService.getInstance().toString();
//        return "beanSing " + beanSing + "\n sig " + sig + "\n" +"utilSig" + smsUplinkRecordService;
        TestService sig = TestUtils.get();
        log.info("sig " + sig);
        return "beanSing " + beanSing + "\n sig " + sig + "\n";
    }

}