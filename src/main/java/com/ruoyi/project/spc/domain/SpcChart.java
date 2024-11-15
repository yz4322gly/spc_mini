package com.ruoyi.project.spc.domain;

import java.util.Date;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;

/**
 * spc管制图对象 spc_chart
 * 
 * @author guolinyuan
 * @date 2024-03-14
 */
@Data
public class SpcChart
{

    /** ID */
    private Long id;
    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
    /** 管制图名 */
    @Excel(name = "管制图名")
    private String name;
    /** 参数key */
    @Excel(name = "参数key")
    private String paramKey;
    /** 管制图类型 */
    @Excel(name = "管制图类型")
    private Integer type;
    /** 批次号 */
    @Excel(name = "批次号")
    private String lotId;
    /** 过滤条件:设备号 */
    @Excel(name = "过滤条件:设备号")
    private String toolId;
    /** 过滤条件:配方号 */
    @Excel(name = "过滤条件:配方号")
    private String recipeId;
    /** 过滤条件:产品号,可选 */
    @Excel(name = "过滤条件:产品号,可选")
    private String productId;
    /** 过滤条件:站点号 */
    @Excel(name = "过滤条件:站点号")
    private String siteId;
    /** 过滤条件:工艺号 */
    @Excel(name = "过滤条件:工艺号")
    private String processId;
    /** 预留字段1_可选 */
    @Excel(name = "预留字段1_可选")
    @ExcelProperty("extra1_id")
    private String extra1Id;
    /** 预留字段2_可选 */
    @Excel(name = "预留字段2_可选")
    @ExcelProperty("extra2_id")
    private String extra2Id;
    /** 上规格线 */
    @Excel(name = "上规格线")
    private Double usl;
    /** 下规格线 */
    @Excel(name = "下规格线")
    private Double lsl;
    /** 上控制线 */
    @Excel(name = "上控制线")
    private Double ucl;
    /** 下控制线 */
    @Excel(name = "下控制线")
    private Double lcl;
    /** 设计目标值 */
    @Excel(name = "设计目标值")
    private Double target;
    /** 检查oos方式 */
    @Excel(name = "检查oos方式")
    private Integer checkOos;
    /** 检查ooc方式 */
    @Excel(name = "检查ooc方式")
    private String checkOoc;
}
