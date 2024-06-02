package com.dallxy.ticketService.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 车票表
 */
@Data
@TableName(value = "t_ticket")
public class TicketDao implements Serializable {
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
     * 座位号
     */
    @TableField(value = "seat_number")
    private String seatNumber;

    /**
     * 乘车人ID
     */
    @TableField(value = "passenger_id")
    private Long passengerId;

    /**
     * 车票状态
     */
    @TableField(value = "ticket_status")
    private Integer ticketStatus;

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