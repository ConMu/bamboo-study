package com.conmu.sms.util;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 统一API响应结果
 * @author mucongcong
 * @date 2025/10/14 18:20
 * @since
 **/
@Data
@ApiModel("API响应结果")
public class ApiResult<T> {
    
    @ApiModelProperty(value = "响应码", example = "200")
    private Integer code;
    
    @ApiModelProperty(value = "响应消息", example = "操作成功")
    private String message;
    
    @ApiModelProperty(value = "响应数据")
    private T data;
    
    @ApiModelProperty(value = "时间戳", example = "1634203020000")
    private Long timestamp;
    
    public ApiResult() {
        this.timestamp = System.currentTimeMillis();
    }
    
    public ApiResult(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }
    
    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<>(200, "操作成功", data);
    }
    
    public static <T> ApiResult<T> success(String message, T data) {
        return new ApiResult<>(200, message, data);
    }
    
    public static <T> ApiResult<T> error(String message) {
        return new ApiResult<>(500, message, null);
    }
    
    public static <T> ApiResult<T> error(Integer code, String message) {
        return new ApiResult<>(code, message, null);
    }
}