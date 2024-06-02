package com.dallxy.orderService.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 订单明细表
 */
@Data
@TableName(value = "t_order_item")
public class OrderItemDao implements Serializable {
    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单号
     */
    @TableField(value = "order_sn")
    private String orderSn;

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 用户名
     */
    @TableField(value = "username")
    private String username;

    /**
     * 列车ID
     */
    @TableField(value = "train_id")
    private Long trainId;

    /**
     * 车厢号
     */
    @TableField(value = "carriage_number")
    private String carriageNumber;

    /**
     * 座位类型
     */
    @TableField(value = "seat_type")
    private Integer seatType;

    /**
     * 座位号
     */
    @TableField(value = "seat_number")
    private String seatNumber;

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
     * 证件号
     */
    @TableField(value = "id_card")
    private String idCard;

    /**
     * 车票类型
     */
    @TableField(value = "ticket_type")
    private Integer ticketType;

    /**
     * 手机号
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 订单状态
     */
    @TableField(value = "`status`")
    private Integer status;

    /**
     * 订单金额
     */
    @TableField(value = "amount")
    private Integer amount;

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