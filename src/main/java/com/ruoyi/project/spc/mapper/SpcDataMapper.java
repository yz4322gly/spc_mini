package com.ruoyi.project.spc.mapper;

import java.util.List;
import java.util.Map;
import com.ruoyi.project.spc.domain.SpcData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * spc主数据Mapper接口
 * 
 * @author guolinyuan
 * @date 2024-03-14
 */
@Mapper
public interface SpcDataMapper extends BaseMapper<SpcData>
{
    /**
     * 查询spc主数据列表
     * 
     * @return spc主数据集合
     */
    List<SpcData> queryPage(@Param("params") Map<String, Object> params);
}
