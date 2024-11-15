package com.ruoyi.project.core.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 文件导入记录对象 core_upload
 * 
 * @author huichangwei,guolinyuan
 * @date 2023-03-09
 */
@Data
public class CoreUpload
{
    /** 主键 */
    @TableId
    private Long id;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;
    /** 文件的路径 */
    @Excel(name = "文件的路径")
    private String filePath;
    /** 文件名称 */
    @Excel(name = "文件名称")
    private String fileName;
}
