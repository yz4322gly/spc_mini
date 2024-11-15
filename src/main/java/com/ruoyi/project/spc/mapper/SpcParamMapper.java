package com.ruoyi.project.spc.mapper;

import java.util.List;
import java.util.Map;
import com.ruoyi.project.spc.domain.SpcParam;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 参数组Mapper接口
 * 
 * @author guolinyuan
 * @date 2024-03-14
 */
@Mapper
public interface SpcParamMapper extends BaseMapper<SpcParam>
{
    /**
     * 查询参数组列表
     * 
     * @return 参数组集合
     */
    List<SpcParam> queryPage(@Param("params") Map<String, Object> params);
}
