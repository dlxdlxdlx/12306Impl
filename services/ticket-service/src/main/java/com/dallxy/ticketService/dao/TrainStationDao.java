package com.dallxy.ticketService.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 列车站点表
 */
@Data
@TableName(value = "t_train_station")
public class TrainStationDao implements Serializable {
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
     * 车站ID
     */
    @TableField(value = "station_id")
    private Long stationId;

    /**
     * 站点顺序
     */
    @TableField(value = "`sequence`")
    private String sequence;

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
     * 起始城市
     */
    @TableField(value = "start_region")
    private String startRegion;

    /**
     * 终点城市
     */
    @TableField(value = "end_region")
    private String endRegion;

    /**
     * 到站时间
     */
    @TableField(value = "arrival_time")
    private Date arrivalTime;

    /**
     * 出站时间
     */
    @TableField(value = "departure_time")
    private Date departureTime;

    /**
     * 停留时间，单位分
     */
    @TableField(value = "stopover_time")
    private Integer stopoverTime;

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