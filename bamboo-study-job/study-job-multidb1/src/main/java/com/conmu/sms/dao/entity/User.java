package com.conmu.sms.dao.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户实体类
 * @author mucongcong
 * @date 2025/10/14 15:51
 * @since
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "User", description = "用户实体")
public class User extends BaseEntity {

    @ApiModelProperty(value = "用户名", required = true, example = "admin")
    private String username;

    @ApiModelProperty(value = "密码", example = "123456")
    private String password;

    @ApiModelProperty(value = "真实姓名", example = "管理员")
    private String realName;

    @ApiModelProperty(value = "邮箱", example = "admin@example.com")
    private String email;

    @ApiModelProperty(value = "电话", example = "13800138000")
    private String phone;

    @ApiModelProperty(value = "用户状态", notes = "1-正常 0-禁用", example = "1")
    private Integer status = 1;

    @ApiModelProperty(value = "角色ID", example = "1")
    private Long roleId;
}
