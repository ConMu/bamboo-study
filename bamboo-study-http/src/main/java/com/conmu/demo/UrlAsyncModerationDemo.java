package com.conmu.demo;

import com.alibaba.fastjson.JSON;
import com.aliyun.green20220302.Client;
import com.aliyun.green20220302.models.UrlAsyncModerationRequest;
import com.aliyun.green20220302.models.UrlAsyncModerationResponse;
import com.aliyun.green20220302.models.UrlAsyncModerationResponseBody;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
/**
 * @author mucongcong
 * @date 2025/07/07 11:06
 * @since
 **/
public class UrlAsyncModerationDemo {
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

    public static UrlAsyncModerationResponse invokeFunction(String accessKeyId, String accessKeySecret, String endpoint) throws Exception {
        //注意，此处实例化的client请尽可能重复使用，避免重复建立连接，提升检测性能。
        Client client = createClient(accessKeyId, accessKeySecret, endpoint);

        // 创建RuntimeObject实例并设置运行参数
        RuntimeOptions runtime = new RuntimeOptions();

        // 检测参数构造。
        Map<String, String> serviceParameters = new HashMap<>();
        //公网可访问的URL。
        serviceParameters.put("url", "【四川九步科技】蛇年特惠，限时24小时！登录即领29-398元好礼 http://t.cn/A6gGkRFp 拒收请回复R");
        //检测数据唯一标识。
        serviceParameters.put("dataId", UUID.randomUUID().toString());
        //检测结果回调通知您的URL。
        serviceParameters.put("callback", "http://www.aliyun.com");
        //随机字符串，该值用于回调通知请求中的签名。
        serviceParameters.put("seed", "seedCode");
        //使用回调通知时（callback），设置对回调通知内容进行签名的算法。
        serviceParameters.put("cryptType", "SM3");

        UrlAsyncModerationRequest request = new UrlAsyncModerationRequest();
        // url检测service,示例：url_detection_pro
        request.setService("url_detection_pro");
        request.setServiceParameters(JSON.toJSONString(serviceParameters));

        UrlAsyncModerationResponse response = null;
        try {
            response = client.urlAsyncModerationWithOptions(request, runtime);
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
        UrlAsyncModerationResponse response = invokeFunction(accessKeyId, accessKeySecret, "green-cip.cn-shanghai.aliyuncs.com");

        // 打印检测结果。
        if (response != null) {
            if (response.getStatusCode() == 200) {
                //返回结果中包含reqId字段，获取异步检测结果时将该字段作为入参，获取图片检测结果
                UrlAsyncModerationResponseBody body = response.getBody();
                if (body.getCode() == 200) {
                    System.out.println(JSON.toJSONString(body));
                } else {
                    System.out.println("url moderation not success. code:" + body.getCode());
                }
            } else {
                System.out.println("response not success. status:" + response.getStatusCode());
            }
        }
    }
}