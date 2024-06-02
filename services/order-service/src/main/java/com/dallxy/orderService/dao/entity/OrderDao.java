package com.dallxy.orderService.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 订单表
 */
@Data
@TableName(value = "t_order")
public class OrderDao implements Serializable {
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
     * 列车车次
     */
    @TableField(value = "train_number")
    private String trainNumber;

    /**
     * 乘车日期
     */
    @TableField(value = "riding_date")
    private Date ridingDate;

    /**
     * 出发站点
     */
    @TableField(value = "departure")
    private String departure;

    /**
     * 到达站点
     */
    @TableField(value = "arrival")
    private String arrival;

    /**
     * 出发时间
     */
    @TableField(value = "departure_time")
    private Date departureTime;

    /**
     * 到达时间
     */
    @TableField(value = "arrival_time")
    private Date arrivalTime;

    /**
     * 订单来源
     */
    @TableField(value = "`source`")
    private Integer source;

    /**
     * 订单状态
     */
    @TableField(value = "`status`")
    private Integer status;

    /**
     * 下单时间
     */
    @TableField(value = "order_time")
    private Date orderTime;

    /**
     * 支付方式
     */
    @TableField(value = "pay_type")
    private Integer payType;

    /**
     * 支付时间
     */
    @TableField(value = "pay_time")
    private Date payTime;

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