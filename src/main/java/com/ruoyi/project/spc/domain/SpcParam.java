package com.ruoyi.project.spc.domain;

import java.util.Date;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;

/**
 * 参数组对象 spc_param
 * 
 * @author guolinyuan
 * @date 2024-03-14
 */
@Data
public class SpcParam
{

    /** ID */
    private Long id;
    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
    /** 参数组key */
    @Excel(name = "参数组key")
    private String paramKey;
    /** 参数描述 */
    @Excel(name = "参数描述")
    private String name;
    /** 上规格 */
    @Excel(name = "上规格")
    private Double usl;
    /** 下规格 */
    @Excel(name = "下规格")
    private Double lsl;
    /** 目标值 */
    @Excel(name = "目标值")
    private Double target;
    /** 备注 */
    @Excel(name = "备注")
    private String remark;
}
