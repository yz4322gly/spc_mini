package com.ruoyi.project.spc.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.spc.domain.SpcChart;
import com.ruoyi.project.spc.domain.SpcData;
import com.ruoyi.project.spc.domain.vo.SpcDataMiniVo;
import com.ruoyi.project.spc.domain.vo.SpcOocVo;
import com.ruoyi.project.spc.service.ISpcChartService;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.project.spc.mapper.SpcDataSummaryMapper;
import com.ruoyi.project.spc.domain.SpcDataSummary;
import com.ruoyi.project.spc.service.ISpcDataSummaryService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 数据汇总Service业务层处理
 *
 * @author guolinyuan
 * @date 2024-03-14
 */
@Service
public class SpcDataSummaryServiceImpl extends ServiceImpl<SpcDataSummaryMapper, SpcDataSummary> implements ISpcDataSummaryService {

    private static final Logger log = LoggerFactory.getLogger(SpcDataSummaryServiceImpl.class);
    @Autowired
    ISpcChartService spcChartService;

    /**
     * 分页查询数据汇总列表
     *
     * @return 数据汇总集合
     */
    @Override
    public List<SpcDataSummary> queryPage(Map<String, Object> params) {
        return baseMapper.queryPage(params);
    }

    @Override
    public void summarizeFromDetails(@NotNull String paramKey, String lotId, Date dataTime) {
        //先查询分组
        List<SpcDataMiniVo> oDetails = baseMapper.getSummaryDetailsMini(paramKey, lotId, dataTime);
        List<List<SpcDataMiniVo>> oDetailsGroup = new ArrayList<>();
        Integer nowGroupNumber;
        List<SpcDataMiniVo> nowGroupMember = new ArrayList<>();
        for (SpcDataMiniVo o : oDetails) {
            nowGroupNumber = o.getGroupNumber();
            if (nowGroupNumber == 1) {
                List<SpcDataMiniVo> groupMember = new ArrayList<>();
                oDetailsGroup.add(groupMember);
                nowGroupMember = groupMember;
            }
            nowGroupMember.add(o);
        }

        //遍历分好的组,每组汇总出来
        List<SpcDataSummary> summaryList = new ArrayList<>(oDetailsGroup.size());
        for (List<SpcDataMiniVo> groupMember : oDetailsGroup) {
            SpcDataSummary summary = new SpcDataSummary();
            DescriptiveStatistics stats = new DescriptiveStatistics();
            for (SpcDataMiniVo spcDataMiniVo : groupMember) {
                stats.addValue(spcDataMiniVo.getParamValue());
            }
            SpcDataMiniVo one = groupMember.get(0);
            summary.setMax(stats.getMax());
            summary.setMin(stats.getMin());
            summary.setMean(stats.getMean());
            summary.setStd(stats.getStandardDeviation());
            summary.setMid(stats.getPercentile(50));
            summary.setQ1(stats.getPercentile(25));
            summary.setQ3(stats.getPercentile(75));
            summary.setSampleSize(groupMember.size());
            summary.setRangeSpan(summary.getMax() - summary.getMin());
            summary.setParamKey(paramKey);
            summary.setLotId(one.getLotId());
            summary.setDataTime(one.getDataTime());
            summary.setToolId(one.getToolId());
            summary.setRecipeId(one.getRecipeId());
            summary.setProductId(one.getProductId());
            summary.setSiteId(one.getSiteId());
            summary.setProcessId(one.getProcessId());
            summary.setValidFlag(1);
            summaryList.add(summary);
        }
        //这边需要手动重写一个执行批量插入的操作
        this.saveOrUpdateBatchByUk(summaryList);
    }

    void saveOrUpdateBatchByUk(List<SpcDataSummary> summaryList)
    {
        int batchSize = 10;
        for (int i =0 ;i < summaryList.size() ; i = i + batchSize)
        {
            baseMapper.saveOrUpdateBatchByUk(summaryList.subList(i,Math.min(i+batchSize,summaryList.size())));
        }
    }


    /**
     * 未实现,待实现 TODO FIXME
     * @param paramKey 参数
     * @param startTime 检查的数据时间范围
     * @param endTime 检查的时间数据范围
     */
    @Override
    @Deprecated
    public void checkOOC(@NotNull String paramKey, Date startTime, Date endTime) {
        //首先取所有涉及到此参数的管制表
        List<SpcChart> charts = spcChartService.getByParamKey(paramKey);

        String startTimeS = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, startTime);
        String endTimeS = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, endTime);


        //转对象,取有效值
        for (SpcChart chart : charts) {
            List<SpcOocVo> chartOOC = JSON.parseArray(chart.getCheckOoc(), SpcOocVo.class);
            if (chartOOC != null) {
                //所有连续规则的最大值(maxContinuousRule),至少一个,因为有3点中2点这种东西
                //所有连续有效规则的涉及点位的最大值,取数据时会一次性取完成. 比如连续6点上升/下降,则会在startTime前取5条,endTime后取5条,共同参与计算
                int mcr = 1;
                for (SpcOocVo oneRule : chartOOC) {
                    //取有效规则
                    if (oneRule.getCheck() == 1) {
                        //取连续规则记最大值
                        switch (oneRule.getType()) {
                            case 2:
                            case 3:
                            case 4:
                            case 7:
                            case 8: {
                                int n = oneRule.getParam();
                                if (n > mcr) {
                                    mcr = n;
                                }
                                break;
                            }
                        }
                    }
                }

                // 按规则取数据
                Map<String, Object> p1 = new HashMap<>(3);
                p1.put("endTime", startTimeS);
                p1.put("sortOrder", "desc");
                p1.put("limit", mcr);

                Map<String, Object> p2 = new HashMap<>(2);
                p2.put("startTime", startTimeS);
                p2.put("endTime", endTimeS);

                Map<String, Object> p3 = new HashMap<>(3);
                p3.put("startTime", endTimeS);
                p3.put("sortOrder", "asc");
                p3.put("limit", mcr);

                // 分别取前置,后置,以及主数据
                List<SpcDataSummary> preData = chartSummaryData(chart, p1);
                List<SpcDataSummary> midData = chartSummaryData(chart, p2);
                List<SpcDataSummary> sufData = chartSummaryData(chart, p3);

                //依照类型定义数据列
                String summaryMethodName;
                Class<SpcDataSummary> summaryClass = SpcDataSummary.class;
                Class<DescriptiveStatistics> statsClass = DescriptiveStatistics.class;
                switch (chart.getType()) {
                    //均值图取均值
                    case 1:
                    case 2:
                        summaryMethodName = "getMean";
                        break;
                    //中位数图取中位数
                    case 3:
                        summaryMethodName = "getMid";
                        break;
                    default:
                        throw new ServiceException("未定义的图表类型");
                }

                // 再次遍历所有规则
                for (SpcOocVo oneRule : chartOOC) {
                    //取有效规则
                    if (oneRule.getCheck() == 1) {
                        switch (oneRule.getType()) {
                            //1个点,距离中心线大于n个标准差
                            case 1: {
                                //取midData即可
                                List<SpcDataSummary> exs = midData;
                                //获取参数
                                Integer n = oneRule.getParam();
                                //计算标准差
                                DescriptiveStatistics stats = new DescriptiveStatistics();

                                for (SpcDataSummary ex : exs) {
                                    //取出数据列
                                    try {
                                        stats.addValue((Double) summaryClass.getMethod(summaryMethodName).invoke(ex));
                                    } catch (IllegalAccessException | InvocationTargetException |
                                             NoSuchMethodException e) {
                                        throw new ServiceException("方法无法执行,SpcDataSummary对象无此方法" + summaryMethodName);
                                    }
                                }
                                double std = stats.getStandardDeviation();
                                //这边用math3的数组用了,省一点空间,math3应该不会变更数组顺序的把,如果变更了就再调整,自己建一个数组好了 FIXME
                                double[] exValues = stats.getValues();
                                for (int i = 0; i < exValues.length; i++) {
                                    if (exValues[i] > std * n) {
                                        //获取含更多信息的原数据
                                        SpcDataSummary ex = exs.get(i);
                                        //建立OOC异常
                                        log.error("OOC超规,lot:{},数据时间{},违反#1:1个点,距离中心线大于{}个标准差;即{}>{}",
                                                ex.getLotId(), ex.getDataTime(), n, exValues[i], std * n);
                                    }
                                }
                            }
                            break;
                            //连续n点在中心线同一侧
                            case 2: {
                                //取midData,以及前n-1条,后n-1条
                                List<SpcDataSummary> exs = new ArrayList<>();
                                //获取参数
                                Integer n = oneRule.getParam();

                                //获取最后一个元素的序号
                                if (preData != null && preData.size() > 0) {
                                    int nn = Math.min(preData.size(), n - 1);
                                    for (int i = nn - 1; i >= 0; i--) {
                                        exs.add(preData.get(i));
                                    }
                                }
                                exs.addAll(midData);
                                if (sufData != null && sufData.size() > 0) {
                                    int nn = Math.min(sufData.size(), n - 1);
                                    exs.addAll(sufData.subList(0, nn));
                                }
                                //组合出数据结果exs,exs算数据
                                //计算数据
                                DescriptiveStatistics stats = new DescriptiveStatistics();
                                for (SpcDataSummary ex : exs) {
                                    //取出数据列
                                    try {
                                        stats.addValue((Double) summaryClass.getMethod(summaryMethodName).invoke(ex));
                                    } catch (IllegalAccessException | InvocationTargetException |
                                             NoSuchMethodException e) {
                                        throw new ServiceException("方法无法执行,SpcDataSummary对象无此方法" + summaryMethodName);
                                    }
                                }
                                //计算CL
                                double cl;
                                try {
                                    if ("getMean".equals(summaryMethodName)) {
                                        cl = (double) statsClass.getMethod("getMean").invoke(stats);
                                    } else {
                                        cl = (double) statsClass.getMethod("getPercentile", double.class).invoke(stats, 50);
                                    }
                                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                                    throw new ServiceException("方法无法执行,DescriptiveStatistics对象无此方法");
                                }

                                //遍历数据
                                double[] exValues = stats.getValues();
                                //数据长度都没有n大,不用判断
                                if (exValues.length < n) {
                                    break;
                                }
                                //建立n宽度的滑动窗口
                                for (int i = 0; i < exValues.length - n; i++) {
                                    Boolean upSide = null;
                                    boolean ooc = true;
                                    for (int j = i ; j < i+n ; j++)
                                    {
                                        if (upSide == null)
                                        {
                                            upSide = exValues[j] > cl;
                                        }
                                        else
                                        {
                                            //出现不在同一侧的了,跳出
                                            if (exValues[j] > cl != upSide)
                                            {
                                                ooc = false;
                                                break;
                                            }
                                        }
                                    }
                                    if (ooc)
                                    {
                                        //获取含更多信息的原数据
                                        SpcDataSummary ex = exs.get(i);
                                        //建立OOC异常
                                        log.error("OOC超规,lot:{},数据时间{},违反#2:连续{}点在中心线同一侧;起始数据点:{}",
                                                ex.getLotId(), ex.getDataTime(), n,ex);
                                    }
                                }
                            }
                            //连续n点全部递增或递减
                            case 3:
                                break;
                            //连续n点,上下交错
                            case 4:
                                break;
                            //n+1个点中有n个点,距离中心线(同一侧)大于2个标准差
                            case 5:
                                break;
                            //n+1个点中有n个点,距离中心线(同一侧)大于1个标准差
                            case 6:
                                break;
                            //连续n点距离中心线(任意一侧)1个标准差以内
                            case 7:
                                break;
                            //连续n个点,距离中心线(任意一侧)大于1个标准差
                            case 8:
                                break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public List<SpcDataSummary> chartSummaryData(SpcChart chart, Map<String, Object> params) {
        parsingParams(chart, params);
        return baseMapper.chartSummaryData(chart, params);
    }

    private static void parsingParams(SpcChart chart, Map<String, Object> params) {
        final String comma = ",";
        if (StringUtils.isNotEmpty(chart.getToolId())) {
            String[] array = chart.getToolId().split(comma);
            params.put("chartToolId", Arrays.asList(array));
        }
        if (StringUtils.isNotEmpty(chart.getRecipeId())) {
            String[] array = chart.getRecipeId().split(comma);
            params.put("chartRecipeId", Arrays.asList(array));
        }
        if (StringUtils.isNotEmpty(chart.getProductId())) {
            String[] array = chart.getProductId().split(comma);
            params.put("chartProductId", Arrays.asList(array));
        }
        if (StringUtils.isNotEmpty(chart.getSiteId())) {
            String[] array = chart.getSiteId().split(comma);
            params.put("chartSiteId", Arrays.asList(array));
        }
        if (StringUtils.isNotEmpty(chart.getProcessId())) {
            String[] array = chart.getProcessId().split(comma);
            params.put("chartProcessId", Arrays.asList(array));
        }
        //检验可能出现的sql注入
        if (params.get("limit") != null && StringUtils.isNotEmpty(params.get("limit").toString())) {
            try {
                Integer.parseInt(params.get("limit").toString());
            } catch (Exception e) {
                throw new ServiceException("请不要执行sql注入攻击");
            }
        }
    }


    @Override
    public List<SpcData> listDataBySummary(Map<String, Object> params, SpcDataSummary summary) {
        return baseMapper.listDataBySummaryId(params, summary);
    }
}
