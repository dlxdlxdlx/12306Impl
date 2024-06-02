package com.dallxy.ticketService.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 车站表
 */
@Data
@TableName(value = "t_station")
public class StationDao implements Serializable {
    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 车站编号
     */
    @TableField(value = "code")
    private String code;

    /**
     * 车站名称
     */
    @TableField(value = "`name`")
    private String name;

    /**
     * 拼音
     */
    @TableField(value = "spell")
    private String spell;

    /**
     * 车站地区
     */
    @TableField(value = "region")
    private String region;

    /**
     * 车站地区名称
     */
    @TableField(value = "region_name")
    private String regionName;

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