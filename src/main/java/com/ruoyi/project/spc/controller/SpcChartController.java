package com.ruoyi.project.spc.controller;

import java.util.List;
import java.util.Map;
import java.util.Arrays;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.project.spc.domain.SpcData;
import com.ruoyi.project.spc.domain.SpcDataSummary;
import com.ruoyi.project.spc.service.ISpcDataSummaryService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.project.spc.domain.SpcChart;
import com.ruoyi.project.spc.service.ISpcChartService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * spc管制图Controller
 *
 * @author guolinyuan
 * @date 2024-03-14
 */
@RestController
@RequestMapping("/spc/chart")
public class SpcChartController extends BaseController {
    @Autowired
    private ISpcChartService spcChartService;

    @Autowired
    private ISpcDataSummaryService spcDataSummaryService;


    /**
     * 查询spc管制图列表
     */
    @PreAuthorize("@ss.hasPermi('spc:chart:list')")
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam Map<String, Object> params) {
        startPage();
        List<SpcChart> list = spcChartService.queryPage(params);
        return getDataTable(list);
    }

    /**
     * 获取绘图数据
     * @param params
     * @return
     */
    @PreAuthorize("@ss.hasPermi('spc:chart:list')")
    @GetMapping("/chartSummaryData")
    public AjaxResult chartSummaryData(@RequestParam Map<String, Object> params) {
        SpcChart chart = spcChartService.getById(Long.parseLong(params.get("id").toString()));
        List<SpcDataSummary> summaryList = spcDataSummaryService.chartSummaryData(chart, params);
        return AjaxResult.success().put("chart", chart).put("summaryList", summaryList);
    }

    /**
     * 通过summary的id查询明细数据
     * @param id summary的id
     * @returns {*}
     */
    @PreAuthorize("@ss.hasPermi('spc:chart:list')")
    @GetMapping("/listDataBySummaryId")
    public TableDataInfo listDataBySummaryId(@RequestParam Long id,@RequestParam Map<String, Object> params)
    {
        SpcDataSummary summary = spcDataSummaryService.getById(id);
        if (summary == null)
        {
            throw new ServiceException("无明细数据可用!");
        }
        startPage();
        List<SpcData> dataList = spcDataSummaryService.listDataBySummary(params,summary);
        return getDataTable(dataList);
    }

    /**
     * 通过summary的id查询明细数据,以密集数组形式返回
     * @param id summary的id
     * @returns {*}
     */
    @PreAuthorize("@ss.hasPermi('spc:chart:list')")
    @GetMapping("/listDataRowBySummaryId")
    public AjaxResult listDataRowBySummaryId(@RequestParam Long id)
    {
        SpcDataSummary summary = spcDataSummaryService.getById(id);
        if (summary == null)
        {
            throw new ServiceException("无明细数据可用!");
        }
        List<Double> dataList = spcDataSummaryService.listDataRowBySummaryId(summary);
        return AjaxResult.success(dataList);
    }


    /**
     * 导出spc管制图列表
     */
    @PreAuthorize("@ss.hasPermi('spc:chart:export')")
    @Log(title = "spc管制图", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestParam Map<String, Object> params) {
        List<SpcChart> list = spcChartService.queryPage(params);
        ExcelUtil<SpcChart> util = new ExcelUtil<SpcChart>(SpcChart.class);
        util.exportExcel(response, list, "spc管制图数据");
    }

    /**
     * 获取spc管制图详细信息
     */
    @PreAuthorize("@ss.hasPermi('spc:chart:query')")
    @GetMapping("/info")
    public AjaxResult getInfo(@RequestParam Long id) {
        return AjaxResult.success(spcChartService.getById(id));
    }

    /**
     * 新增spc管制图
     */
    @PreAuthorize("@ss.hasPermi('spc:chart:add')")
    @Log(title = "spc管制图", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody SpcChart spcChart) {
        return toAjax(spcChartService.save(spcChart));
    }

    /**
     * 修改spc管制图
     */
    @PreAuthorize("@ss.hasPermi('spc:chart:edit')")
    @Log(title = "spc管制图", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody SpcChart spcChart) {
        return toAjax(spcChartService.updateById(spcChart));
    }

    /**
     * 保存/更新ooc的配置
     */
    @PreAuthorize("@ss.hasPermi('spc:chart:edit')")
    @Log(title = "更新ooc的配置", businessType = BusinessType.UPDATE)
    @PostMapping("/saveOOC")
    public AjaxResult saveOOC(@RequestBody SpcChart spcChart) {
        spcChartService.update().set("check_ooc",spcChart.getCheckOoc())
                .eq("id",spcChart.getId()).update();
        return AjaxResult.success();
    }

    /**
     * 删除spc管制图
     */
    @PreAuthorize("@ss.hasPermi('spc:chart:remove')")
    @Log(title = "spc管制图", businessType = BusinessType.DELETE)
    @PostMapping("/delete")
    public AjaxResult remove(@RequestBody Long[] ids) {
        return toAjax(spcChartService.removeByIds(Arrays.asList(ids)));
    }
}
