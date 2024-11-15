package com.ruoyi.project.spc.mapper;

import java.util.List;
import java.util.Map;
import com.ruoyi.project.spc.domain.SpcChart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * spc管制图Mapper接口
 * 
 * @author guolinyuan
 * @date 2024-03-14
 */
@Mapper
public interface SpcChartMapper extends BaseMapper<SpcChart>
{
    /**
     * 查询spc管制图列表
     * 
     * @return spc管制图集合
     */
    List<SpcChart> queryPage(@Param("params") Map<String, Object> params);

    List<SpcChart> getByParamKey(@Param("paramKey") String paramKey);

}
