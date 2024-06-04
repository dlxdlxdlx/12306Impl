package com.dallxy.payService.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 支付表
 */
@Data
@TableName(value = "t_pay")
public class Pay implements Serializable {
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
     * 商户订单号
     */
    @TableField(value = "out_order_sn")
    private String outOrderSn;

    /**
     * 支付渠道
     */
    @TableField(value = "channel")
    private String channel;

    /**
     * 支付环境
     */
    @TableField(value = "trade_type")
    private String tradeType;

    /**
     * 订单标题
     */
    @TableField(value = "subject")
    private String subject;

    /**
     * 商户订单号
     */
    @TableField(value = "order_request_id")
    private String orderRequestId;

    /**
     * 交易总金额
     */
    @TableField(value = "total_amount")
    private Integer totalAmount;

    /**
     * 三方交易凭证号
     */
    @TableField(value = "trade_no")
    private String tradeNo;

    /**
     * 付款时间
     */
    @TableField(value = "gmt_payment")
    private Date gmtPayment;

    /**
     * 支付金额
     */
    @TableField(value = "pay_amount")
    private Integer payAmount;

    /**
     * 支付状态
     */
    @TableField(value = "`status`")
    private String status;

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