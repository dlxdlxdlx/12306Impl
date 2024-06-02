package com.dallxy.ticketService.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 列车表
 */
@Data
@TableName(value = "t_train")
public class TrainDao implements Serializable {
    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 列车车次
     */
    @TableField(value = "train_number")
    private String trainNumber;

    /**
     * 列车类型 0：高铁 1：动车 2：普通车
     */
    @TableField(value = "train_type")
    private Integer trainType;

    /**
     * 列车标签 0：复兴号 1：智能动车组 2：静音车厢 3：支持选铺
     */
    @TableField(value = "train_tag")
    private String trainTag;

    /**
     * 列车品牌 0：GC-高铁/城际 1：D-动车 2：Z-直达 3：T-特快 4：K-快速 5：其他 6：复兴号 7：智能动车组
     */
    @TableField(value = "train_brand")
    private String trainBrand;

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
     * 销售时间
     */
    @TableField(value = "sale_time")
    private Date saleTime;

    /**
     * 销售状态 0：可售 1：不可售 2：未知
     */
    @TableField(value = "sale_status")
    private Integer saleStatus;

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