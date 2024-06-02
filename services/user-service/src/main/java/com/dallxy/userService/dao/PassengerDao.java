package com.dallxy.userService.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 乘车人表
 */
@Data
@TableName(value = "t_passenger")
public class PassengerDao implements Serializable {
    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    @TableField(value = "username")
    private String username;

    /**
     * 真实姓名
     */
    @TableField(value = "real_name")
    private String realName;

    /**
     * 证件类型
     */
    @TableField(value = "id_type")
    private Integer idType;

    /**
     * 证件号码
     */
    @TableField(value = "id_card")
    private String idCard;

    /**
     * 优惠类型
     */
    @TableField(value = "discount_type")
    private Integer discountType;

    /**
     * 手机号
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 添加日期
     */
    @TableField(value = "create_date")
    private Date createDate;

    /**
     * 审核状态
     */
    @TableField(value = "verify_status")
    private Integer verifyStatus;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 删除标识
     */
    @TableField(value = "del_flag")
    private Boolean delFlag;

    private static final long serialVersionUID = 1L;
}