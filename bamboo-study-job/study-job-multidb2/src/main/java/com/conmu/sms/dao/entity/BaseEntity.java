package com.conmu.sms.dao.entity;

import lombok.Data;
import java.util.Date;

/**
 * 实体基类
 * @author mucongcong
 * @date 2025/10/14 17:51
 * @since
 **/
@Data
public class BaseEntity {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 更新时间
     */
    private Date updatedTime;

    /**
     * 创建人ID
     */
    private Long createdBy;

    /**
     * 更新人ID
     */
    private Long updatedBy;

    /**
     * 是否删除 0-未删除 1-已删除
     */
    private Integer deleted = 0;
}
