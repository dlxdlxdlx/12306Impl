package com.dallxy.payService.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 退款记录表
 */
@Data
@TableName(value = "t_refund")
public class Refund implements Serializable {
    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 支付流水号
     */
    @TableField(value = "pay_sn")
    private String paySn;

    /**
     * 订单号
     */
    @TableField(value = "order_sn")
    private String orderSn;

    /**
     * 三方交易凭证号
     */
    @TableField(value = "trade_no")
    private String tradeNo;

    /**
     * 退款金额
     */
    @TableField(value = "amount")
    private Integer amount;

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
     * 座位类型
     */
    @TableField(value = "seat_type")
    private Integer seatType;

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
     * 真实姓名
     */
    @TableField(value = "real_name")
    private String realName;

    /**
     * 订单状态
     */
    @TableField(value = "`status`")
    private Integer status;

    /**
     * 退款时间
     */
    @TableField(value = "refund_time")
    private Date refundTime;

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
     * 删除标记 0：未删除 1：删除
     */
    @TableField(value = "del_flag")
    private Boolean delFlag;

    private static final long serialVersionUID = 1L;
}