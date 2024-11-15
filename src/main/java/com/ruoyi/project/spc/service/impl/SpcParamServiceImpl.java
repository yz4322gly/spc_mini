package com.ruoyi.project.spc.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.project.spc.mapper.SpcParamMapper;
import com.ruoyi.project.spc.domain.SpcParam;
import com.ruoyi.project.spc.service.ISpcParamService;

/**
 * 参数组Service业务层处理
 * 
 * @author guolinyuan
 * @date 2024-03-14
 */
@Service
public class SpcParamServiceImpl  extends ServiceImpl<SpcParamMapper, SpcParam> implements ISpcParamService
{
    /**
     * 分页查询参数组列表
     * @return 参数组集合
     */
    @Override
    public List<SpcParam> queryPage(Map<String, Object> params)
    {
        return baseMapper.queryPage(params);
    }
}
