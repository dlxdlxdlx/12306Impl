package com.dallxy.ticketService.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 列车站点关系表
 */
@Data
@TableName(value = "t_train_station_relation")
public class TrainStationRelationDao implements Serializable {
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
     * 起始城市名称
     */
    @TableField(value = "start_region")
    private String startRegion;

    /**
     * 终点城市名称
     */
    @TableField(value = "end_region")
    private String endRegion;

    /**
     * 始发标识
     */
    @TableField(value = "departure_flag")
    private Boolean departureFlag;

    /**
     * 终点标识
     */
    @TableField(value = "arrival_flag")
    private Boolean arrivalFlag;

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