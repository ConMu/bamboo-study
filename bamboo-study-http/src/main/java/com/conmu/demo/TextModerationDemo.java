package com.conmu.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.green20220302.Client;
import com.aliyun.green20220302.models.TextModerationRequest;
import com.aliyun.green20220302.models.TextModerationResponse;
import com.aliyun.green20220302.models.TextModerationResponseBody;
import com.aliyun.teaopenapi.models.Config;


public class TextModerationDemo {

    public static void main(String[] args) throws Exception {
        Config config = new Config();
        /**
         * 阿里云账号AccessKey拥有所有API的访问权限，建议您使用RAM用户进行API访问或日常运维。
         * 常见获取环境变量方式：
         * 方式一：
         *     获取RAM用户AccessKey ID：System.getenv("ALIBABA_CLOUD_ACCESS_KEY_ID");
         *     获取RAM用户AccessKey Secret：System.getenv("ALIBABA_CLOUD_ACCESS_KEY_SECRET");
         * 方式二：
         *     获取RAM用户AccessKey ID：System.getProperty("ALIBABA_CLOUD_ACCESS_KEY_ID");
         *     获取RAM用户AccessKey Secret：System.getProperty("ALIBABA_CLOUD_ACCESS_KEY_SECRET");
         */
        config.setAccessKeyId(System.getenv("ALIBABA_CLOUD_ACCESS_KEY_ID"));
        config.setAccessKeySecret(System.getenv("ALIBABA_CLOUD_ACCESS_KEY_SECRET"));
        //接入区域和地址请根据实际情况修改
        config.setRegionId("cn-shanghai");
        config.setEndpoint("green-cip.cn-shanghai.aliyuncs.com");
        //读取时超时时间，单位毫秒（ms）。
        config.setReadTimeout(6000);
        //连接时超时时间，单位毫秒（ms）。
        config.setConnectTimeout(3000);
        //设置http代理。
        //config.setHttpProxy("http://xx.xx.xx.xx:xxxx");
        //设置https代理。
        //config.setHttpsProxy("https://xx.xx.xx.xx:xxxx");
        Client client = new Client(config);

        JSONObject serviceParameters = new JSONObject();
        String content = "http://www.pornhub.com";
        serviceParameters.put("content", content);

        TextModerationRequest textModerationPlusRequest = new TextModerationRequest();
        // 检测类型
        textModerationPlusRequest.setService("url_detection");
        textModerationPlusRequest.setServiceParameters(serviceParameters.toJSONString());

        try {
            // 记录开始时间
            long startTime = System.currentTimeMillis();

            // 执行HTTP请求
            TextModerationResponse response = client.textModeration(textModerationPlusRequest);

            // 计算执行时间
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;

            System.out.println("HTTP请求执行时间: " + executionTime + "ms");

            if (response.getStatusCode() == 200) {
                TextModerationResponseBody result = response.getBody();
//                System.out.println("requestId = " + result.getRequestId());
//                System.out.println("code = " + result.getCode());
//                System.out.println("msg = " + result.getMessage());
                System.out.println(JSON.toJSONString(result));

                Integer code = result.getCode();
                if (200 == code) {
                    TextModerationResponseBody.TextModerationResponseBodyData data = result.getData();
//                    System.out.println(content + "  " + JSON.toJSONString(data, true));
                } else {
                    System.out.println("文本审核失败. code:" + code);
                }
            } else {
                System.out.println("请求失败. 状态码:" + response.getStatusCode());
            }
        } catch (Exception e) {
            System.err.println("请求过程中发生异常:");
            e.printStackTrace();
        }
    }
}