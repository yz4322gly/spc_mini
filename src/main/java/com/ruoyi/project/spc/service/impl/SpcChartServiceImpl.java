package com.ruoyi.project.spc.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.project.spc.mapper.SpcChartMapper;
import com.ruoyi.project.spc.domain.SpcChart;
import com.ruoyi.project.spc.service.ISpcChartService;

/**
 * spc管制图Service业务层处理
 * 
 * @author guolinyuan
 * @date 2024-03-14
 */
@Service
public class SpcChartServiceImpl  extends ServiceImpl<SpcChartMapper, SpcChart> implements ISpcChartService
{
    /**
     * 分页查询spc管制图列表
     * @return spc管制图集合
     */
    @Override
    public List<SpcChart> queryPage(Map<String, Object> params)
    {
        return baseMapper.queryPage(params);
    }

    @Override
    public List<SpcChart> getByParamKey(String paramKey) {
        return baseMapper.getByParamKey(paramKey);
    }
}
