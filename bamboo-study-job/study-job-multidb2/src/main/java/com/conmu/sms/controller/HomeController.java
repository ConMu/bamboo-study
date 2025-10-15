package com.conmu.sms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 首页Controller
 * @author mucongcong
 * @date 2025/10/14 18:25
 * @since
 **/
@Controller
public class HomeController {

    /**
     * 首页重定向到Swagger UI
     */
    @GetMapping("/")
    public String home() {
        return "redirect:/swagger-ui.html";
    }
}