package com.ruoyi.project.spc.service;

import java.util.List;
import java.util.Map;
import com.ruoyi.project.spc.domain.SpcData;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * spc主数据Service接口
 * 
 * @author guolinyuan
 * @date 2024-03-14
 */
public interface ISpcDataService  extends IService<SpcData>
{
    /**
     * 分页查询spc主数据列表
     *
     * @return spc主数据集合
     */
    List<SpcData> queryPage(Map<String, Object> params);

    /**
     * 保存数据以及解析一次上传的若干数据
     * @param data
     */
    void saveAndSummary(List<SpcData> data);

}
