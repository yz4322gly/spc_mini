package com.ruoyi.project.spc.service;

import java.util.List;
import java.util.Map;
import com.ruoyi.project.spc.domain.SpcParam;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 参数组Service接口
 * 
 * @author guolinyuan
 * @date 2024-03-14
 */
public interface ISpcParamService  extends IService<SpcParam>
{
    /**
     * 分页查询参数组列表
     *
     * @return 参数组集合
     */
    List<SpcParam> queryPage(Map<String, Object> params);
}
