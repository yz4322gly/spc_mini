package com.ruoyi.project.spc.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * spc用以汇总到汇总数据的原始数据
 * 
 * @author guolinyuan
 * @date 2024-03-14
 */
@Data
public class SpcDataMiniVo
{
    /** 参数值 */
    @Excel(name = "参数值")
    private Double paramValue;
    /** 数据时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "数据时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date dataTime;
    /** 批次号 */
    @Excel(name = "批次号")
    private String lotId;
    /** 组内排序 */
    @Excel(name = "组内排序")
    private Integer groupNumber;

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

}
