package com.conmu.sms.enums;

/**
 * 用户状态枚举
 * @author mucongcong
 * @date 2025/10/14 17:51
 * @since
 **/
public enum UserStatusEnum {
    
    DISABLED(0, "禁用"),
    ENABLED(1, "正常");
    
    private final Integer code;
    private final String desc;
    
    UserStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    public Integer getCode() {
        return code;
    }
    
    public String getDesc() {
        return desc;
    }
    
    public static UserStatusEnum getByCode(Integer code) {
        if (code == null) {
            return DISABLED;
        }
        for (UserStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return DISABLED;
    }
}