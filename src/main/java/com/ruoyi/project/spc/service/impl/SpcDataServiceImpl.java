package com.ruoyi.project.spc.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ruoyi.project.spc.service.ISpcDataSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.project.spc.mapper.SpcDataMapper;
import com.ruoyi.project.spc.domain.SpcData;
import com.ruoyi.project.spc.service.ISpcDataService;
import org.springframework.transaction.annotation.Transactional;

/**
 * spc主数据Service业务层处理
 * 
 * @author guolinyuan
 * @date 2024-03-14
 */
@Service
public class SpcDataServiceImpl  extends ServiceImpl<SpcDataMapper, SpcData> implements ISpcDataService
{
    @Autowired
    ISpcDataSummaryService spcDataSummaryService;

    /**
     * 分页查询spc主数据列表
     * @return spc主数据集合
     */
    @Override
    public List<SpcData> queryPage(Map<String, Object> params)
    {
        return baseMapper.queryPage(params);
    }

    /**
     * 此方法允许上传数据,然后解析
     * data允许混合数据,即允许不同key的数据混合上传
     * @param data
     */
    @Override
    @Transactional
    public void saveAndSummary(List<SpcData> dataList) {
        Set<String> keys = new HashSet<>();
        for (SpcData data : dataList) {
            keys.add(data.getParamKey());
        }
        this.saveBatch(dataList);
        for (String key : keys) {
            spcDataSummaryService.summarizeFromDetails(key,null,null);
        }
    }
}
