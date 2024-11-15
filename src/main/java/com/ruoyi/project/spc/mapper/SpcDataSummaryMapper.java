package com.ruoyi.project.spc.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ruoyi.project.spc.domain.SpcChart;
import com.ruoyi.project.spc.domain.SpcData;
import com.ruoyi.project.spc.domain.SpcDataSummary;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.project.spc.domain.vo.SpcDataMiniVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 数据汇总Mapper接口
 * 
 * @author guolinyuan
 * @date 2024-03-14
 */
@Mapper
public interface SpcDataSummaryMapper extends BaseMapper<SpcDataSummary>
{
    /**
     * 查询数据汇总列表
     * 
     * @return 数据汇总集合
     */
    List<SpcDataSummary> queryPage(@Param("params") Map<String, Object> params);

    /**
     * 获取用以汇总的原始数据
     * @param paramKey
     * @param lotId
     * @param dataTime
     * @return
     */
    List<SpcDataMiniVo> getSummaryDetailsMini(@Param("paramKey") String paramKey, @Param("lotId") String lotId, @Param("dataTime") Date dataTime);

    /**
     * 按条件获取绘图用数据
     * @param chart
     * @param params
     * @return
     */
    List<SpcDataSummary> chartSummaryData(@Param("chart") SpcChart chart, @Param("params") Map<String, Object> params);


    List<SpcData> listDataBySummaryId(@Param("params") Map<String, Object> params, @Param("summary") SpcDataSummary summary);


    void saveOrUpdateBatchByUk(@Param("dataList") List<SpcDataSummary> spcDataSummaries);
}
