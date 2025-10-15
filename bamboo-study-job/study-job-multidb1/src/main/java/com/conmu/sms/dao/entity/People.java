package com.conmu.sms.dao.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 人员实体类
 * @author mucongcong
 * @date 2025/10/14 17:35
 * @since
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "People", description = "人员实体")
public class People extends BaseEntity {

    @ApiModelProperty(value = "姓名", required = true, example = "张三")
    private String name;

    @ApiModelProperty(value = "年龄", example = "25")
    private Integer age;

    @ApiModelProperty(value = "邮箱", example = "zhangsan@example.com")
    private String email;

    @ApiModelProperty(value = "电话号码", example = "13900139000")
    private String phone;

    @ApiModelProperty(value = "性别", notes = "1-男 2-女 0-未知", example = "1")
    private Integer gender;

    @ApiModelProperty(value = "部门ID", example = "1")
    private Long departmentId;

    @ApiModelProperty(value = "职位", example = "软件工程师")
    private String position;

    @ApiModelProperty(value = "地址", example = "北京市朝阳区")
    private String address;

    @ApiModelProperty(value = "备注", example = "优秀员工")
    private String remark;
}