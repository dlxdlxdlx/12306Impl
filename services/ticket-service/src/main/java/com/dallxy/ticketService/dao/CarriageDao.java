package com.dallxy.ticketService.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 车厢表
 */
@Data
@TableName(value = "t_carriage")
public class CarriageDao implements Serializable {
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
     * 车厢类型
     */
    @TableField(value = "carriage_type")
    private Integer carriageType;

    /**
     * 座位数
     */
    @TableField(value = "seat_count")
    private Integer seatCount;

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