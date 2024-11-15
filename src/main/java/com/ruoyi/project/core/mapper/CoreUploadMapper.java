package com.ruoyi.project.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.project.core.domain.CoreUpload;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 文件导入记录Mapper接口
 * 
 * @author huichangwei,guolinyuan
 * @date 2023-03-09
 */
@Mapper
public interface CoreUploadMapper extends BaseMapper<CoreUpload>
{
    /**
     * 查询文件导入记录列表
     * 
     * @return 文件导入记录集合
     */
    List<CoreUpload> queryPage(@Param("params") Map<String, Object> params);
}
