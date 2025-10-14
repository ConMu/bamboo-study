package com.conmu.demo;

import com.alibaba.fastjson.JSON;
import com.aliyun.green20220302.Client;
import com.aliyun.green20220302.models.DescribeUrlModerationResultRequest;
import com.aliyun.green20220302.models.DescribeUrlModerationResultResponse;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;

public class DescribeUrlModerationResultDemo {
    /**
     * 创建请求客户端
     *
     * @param accessKeyId
     * @param accessKeySecret
     * @param endpoint
     * @return
     * @throws Exception
     */
    public static Client createClient(String accessKeyId, String accessKeySecret, String endpoint) throws Exception {
        Config config = new Config();
        config.setAccessKeyId(accessKeyId);
        config.setAccessKeySecret(accessKeySecret);
        // 设置http代理。
        // config.setHttpProxy("http://10.10.xx.xx:xxxx");
        // 设置https代理。
        // config.setHttpsProxy("https://10.10.xx.xx:xxxx");
        // 接入区域和地址请根据实际情况修改
        config.setEndpoint(endpoint);
        return new Client(config);
    }

    public static DescribeUrlModerationResultResponse invokeFunction(String accessKeyId, String accessKeySecret, String endpoint) throws Exception {
        //注意，此处实例化的client请尽可能重复使用，避免重复建立连接，提升检测性能。
        Client client = createClient(accessKeyId, accessKeySecret, endpoint);

        // 创建RuntimeObject实例并设置运行参数
        RuntimeOptions runtime = new RuntimeOptions();

        //构造请求入参
        DescribeUrlModerationResultRequest request = new DescribeUrlModerationResultRequest();
        //将提交异步检测任务时返回值中的reqId作为获取结果的入参。例如：DFEXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXX44
        request.setReqId("DFEXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXX44");

        DescribeUrlModerationResultResponse response = null;
        try {
            response = client.describeUrlModerationResultWithOptions(request, runtime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public static void main(String[] args) throws Exception {
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
        String accessKeyId = System.getenv("ALIBABA_CLOUD_ACCESS_KEY_ID");
        String accessKeySecret = System.getenv("ALIBABA_CLOUD_ACCESS_KEY_SECRET");
        DescribeUrlModerationResultResponse response = invokeFunction(accessKeyId, accessKeySecret, "green-cip.cn-shanghai.aliyuncs.com");

        // 打印检测结果。
        if (response != null) {
            if (response.getStatusCode() == 200) {
                System.out.println(JSON.toJSONString(response.getBody()));
            } else {
                System.out.println("response not success. status:" + response.getStatusCode());
            }
        }

    }
}