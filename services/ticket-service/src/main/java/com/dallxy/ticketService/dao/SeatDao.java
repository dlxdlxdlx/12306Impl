package com.dallxy.ticketService.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 座位表
 */
@Data
@TableName(value = "t_seat")
public class SeatDao implements Serializable {
    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

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
     * 座位类型
     */
    @TableField(value = "seat_type")
    private Integer seatType;

    /**
     * 起始站
     */
    @TableField(value = "start_station")
    private String startStation;

    /**
     * 终点站
     */
    @TableField(value = "end_station")
    private String endStation;

    /**
     * 车票价格
     */
    @TableField(value = "price")
    private Integer price;

    /**
     * 座位状态
     */
    @TableField(value = "seat_status")
    private Integer seatStatus;

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