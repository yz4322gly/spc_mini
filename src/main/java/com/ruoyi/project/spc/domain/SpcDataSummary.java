package com.ruoyi.project.spc.domain;

import java.util.Date;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;

/**
 * 数据汇总对象 spc_data_summary
 * 
 * @author guolinyuan
 * @date 2024-03-14
 */
@Data
public class SpcDataSummary
{

    /** ID */
    private Long id;
    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
    /** 批次号 */
    @Excel(name = "批次号")
    private String lotId;
    /** 数据时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "数据时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date dataTime;
    /** 参数组名 */
    @Excel(name = "参数组名")
    private String paramKey;
    /** 平均值 */
    @Excel(name = "平均值")
    private Double mean;

    /** 中位数 */
    @Excel(name = "中位数")
    private Double mid;
    private Double q1;
    private Double q3;
    /** 极差 */
    @Excel(name = "极差")
    private Double rangeSpan;
    /** 标准差 */
    @Excel(name = "标准差")
    private Double std;
    /** 最大值 */
    @Excel(name = "最大值")
    private Double max;
    /** 最小值 */
    @Excel(name = "最小值")
    private Double min;
    /** 样本容量 */
    @Excel(name = "样本容量")
    private Integer sampleSize;
    /** 过滤条件:设备号 */
    @Excel(name = "过滤条件:设备号")
    private String toolId;
    /** 过滤条件:配方号 */
    @Excel(name = "过滤条件:配方号")
    private String recipeId;
    /** 过滤条件:产品号 */
    @Excel(name = "过滤条件:产品号")
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
    /** 有效标记 */
    @Excel(name = "有效标记")
    private Integer validFlag;
    @Excel(name = "手动注释")
    private String remark;

}
