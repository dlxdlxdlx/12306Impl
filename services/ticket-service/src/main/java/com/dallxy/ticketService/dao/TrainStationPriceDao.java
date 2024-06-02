package com.dallxy.ticketService.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 列车站点价格表
 */
@Data
@TableName(value = "t_train_station_price")
public class TrainStationPriceDao implements Serializable {
    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 车次ID
     */
    @TableField(value = "train_id")
    private Long trainId;

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
     * 座位类型
     */
    @TableField(value = "seat_type")
    private Integer seatType;

    /**
     * 车票价格
     */
    @TableField(value = "price")
    private Integer price;

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