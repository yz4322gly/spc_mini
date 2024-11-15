package com.ruoyi.project.spc.domain;

import java.util.Date;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;

/**
 * spc主数据对象 spc_data
 * 
 * @author guolinyuan
 * @date 2024-03-14
 */
@Data
public class SpcData
{

    /** ID */
    @ExcelIgnore
    private Long id;
    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ExcelIgnore
    private Date createTime;
    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ExcelIgnore
    private Date updateTime;
    /** 参数组key */
    @ExcelProperty("param_key")
    private String paramKey;
    /** 参数值 */
    @Excel(name = "参数值")
    @ExcelProperty("param_value")
    private String paramValue;
    /** 数据时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ExcelProperty("data_time")
    @Excel(name = "数据时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date dataTime;
    /** 手动注释 */
    @Excel(name = "手动注释")
    @ExcelProperty("remark")
    private String remark;
    /** 批次号 */
    @Excel(name = "批次号")
    @ExcelProperty("lot_id")
    private String lotId;
    /** 设备号 */
    @Excel(name = "设备号")
    @ExcelProperty("tool_id")
    private String toolId;
    /** 配方号 */
    @Excel(name = "配方号")
    @ExcelProperty("recipe_id")
    private String recipeId;
    /** 产品号 */
    @Excel(name = "产品号")
    @ExcelProperty("product_id")
    private String productId;
    /** 站点号 */
    @Excel(name = "站点号")
    @ExcelProperty("site_id")
    private String siteId;
    /** 工艺号 */
    @Excel(name = "工艺号")
    @ExcelProperty("process_id")
    private String processId;

    /** 预留字段1_可选 */
    @Excel(name = "预留字段1_可选")
    @ExcelProperty("extra1_id")
    private String extra1Id;
    /** 预留字段2_可选 */
    @Excel(name = "预留字段2_可选")
    @ExcelProperty("extra2_id")
    private String extra2Id;

    /** 有效标记 */
    @Excel(name = "有效标记")
    @ExcelIgnore
    private Integer validFlag;
}
