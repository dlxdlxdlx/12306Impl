package com.dallxy.ticketService.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 地区表
 */
@Data
@TableName(value = "t_region")
public class RegionDao implements Serializable {
    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 地区名称
     */
    @TableField(value = "`name`")
    private String name;

    /**
     * 地区全名
     */
    @TableField(value = "full_name")
    private String fullName;

    /**
     * 地区编码
     */
    @TableField(value = "code")
    private String code;

    /**
     * 地区首字母
     */
    @TableField(value = "`initial`")
    private String initial;

    /**
     * 拼音
     */
    @TableField(value = "spell")
    private String spell;

    /**
     * 热门标识
     */
    @TableField(value = "popular_flag")
    private Boolean popularFlag;

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