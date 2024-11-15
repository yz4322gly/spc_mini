package com.ruoyi.project.spc.service;

import java.util.List;
import java.util.Map;
import com.ruoyi.project.spc.domain.SpcChart;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * spc管制图Service接口
 * 
 * @author guolinyuan
 * @date 2024-03-14
 */
public interface ISpcChartService  extends IService<SpcChart>
{
    /**
     * 分页查询spc管制图列表
     *
     * @return spc管制图集合
     */
    List<SpcChart> queryPage(Map<String, Object> params);

    /**
     * 查询所有含paramKey的ooc规则
     * @param paramKey
     * @return
     */
    List<SpcChart> getByParamKey(String paramKey);
}
