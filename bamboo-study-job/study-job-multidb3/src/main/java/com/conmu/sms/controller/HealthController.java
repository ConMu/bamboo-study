package com.conmu.sms.controller;

import com.conmu.sms.util.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 健康检查Controller
 * @author mucongcong
 * @date 2025/10/14 19:40
 * @since
 **/
@Api(tags = "健康检查接口")
@RestController
@RequestMapping("/health")
public class HealthController {

    @ApiOperation(value = "健康检查", notes = "检查应用服务是否正常运行")
    @GetMapping("/check")
    public ApiResult<Map<String, Object>> healthCheck() {
        Map<String, Object> result = new HashMap<>();
        result.put("status", "UP");
        result.put("application", "bamboo-study-job");
        result.put("version", "1.0.0");
        result.put("timestamp", System.currentTimeMillis());
        return ApiResult.success("应用运行正常", result);
    }
}