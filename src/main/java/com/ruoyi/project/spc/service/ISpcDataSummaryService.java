package com.ruoyi.project.spc.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ruoyi.project.spc.domain.SpcChart;
import com.ruoyi.project.spc.domain.SpcData;
import com.ruoyi.project.spc.domain.SpcDataSummary;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jetbrains.annotations.NotNull;

/**
 * 数据汇总Service接口
 * 
 * @author guolinyuan
 * @date 2024-03-14
 */
public interface ISpcDataSummaryService  extends IService<SpcDataSummary>
{
    /**
     * 分页查询数据汇总列表
     *
     * @return 数据汇总集合
     */
    List<SpcDataSummary> queryPage(Map<String, Object> params);

    /**
     * 从明细汇总数据,必须指定汇总哪一个key的,可以指定条件,值汇总某个lot/dataTime的
     * @param paramKey
     * @param lotId
     * @param dataTime
     */
    void summarizeFromDetails(@NotNull String paramKey, String lotId, Date dataTime);

    /**
     * 检查ooc
     * @param paramKey 参数
     * @param startTime 检查的数据时间范围
     * @param endTime 检查的时间数据范围
     */
    void checkOOC(@NotNull String paramKey,Date startTime,Date endTime);

    List<SpcDataSummary> chartSummaryData(SpcChart chart, Map<String, Object> params);


    List<SpcData> listDataBySummary(Map<String, Object> params, SpcDataSummary summary);

    List<Double> listDataRowBySummaryId(SpcDataSummary summary);
}
