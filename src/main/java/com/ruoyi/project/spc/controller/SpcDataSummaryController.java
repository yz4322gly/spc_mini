package com.ruoyi.project.spc.controller;

import java.util.List;
import java.util.Map;
import java.util.Arrays;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.project.spc.domain.SpcDataSummary;
import com.ruoyi.project.spc.service.ISpcDataSummaryService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 数据汇总Controller
 * 
 * @author guolinyuan
 * @date 2024-03-14
 */
@RestController
@RequestMapping("/spc/summary")
public class SpcDataSummaryController extends BaseController
{
    @Autowired
    private ISpcDataSummaryService spcDataSummaryService;

    /**
     * 注释点
     */
    @PreAuthorize("@ss.hasPermi('spc:summary:edit')")
    @GetMapping("/remarkSummary")
    public AjaxResult remarkSummary(@RequestParam Long id,@RequestParam String remark)
    {
        spcDataSummaryService.update().set("remark",remark).eq("id",id).update();
        return AjaxResult.success();
    }

    /**
     * 隐藏/显示点
     */
    @PreAuthorize("@ss.hasPermi('spc:summary:edit')")
    @GetMapping("/hiddenSummary")
    public AjaxResult hiddenSummary(@RequestParam Long id,@RequestParam Integer validFlag)
    {
        spcDataSummaryService.update().set("valid_flag",validFlag).eq("id",id).update();
        return AjaxResult.success();
    }

    /**
     * 查询数据汇总列表
     */
    @PreAuthorize("@ss.hasPermi('spc:summary:list')")
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam Map<String, Object> params)
    {
        startPage();
        List<SpcDataSummary> list = spcDataSummaryService.queryPage(params);
        return getDataTable(list);
    }

    /**
     * 导出数据汇总列表
     */
    @PreAuthorize("@ss.hasPermi('spc:summary:export')")
    @Log(title = "数据汇总", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response,@RequestParam Map<String, Object> params)
    {
        List<SpcDataSummary> list = spcDataSummaryService.queryPage(params);
        ExcelUtil<SpcDataSummary> util = new ExcelUtil<SpcDataSummary>(SpcDataSummary.class);
        util.exportExcel(response, list, "数据汇总数据");
    }

    /**
     * 获取数据汇总详细信息
     */
    @PreAuthorize("@ss.hasPermi('spc:summary:query')")
    @GetMapping("/info")
    public AjaxResult getInfo(@RequestParam Long id)
    {
        return AjaxResult.success(spcDataSummaryService.getById(id));
    }

    /**
     * 新增数据汇总
     */
    @PreAuthorize("@ss.hasPermi('spc:summary:add')")
    @Log(title = "数据汇总", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody SpcDataSummary spcDataSummary)
    {
        return toAjax(spcDataSummaryService.save(spcDataSummary));
    }

    /**
     * 修改数据汇总
     */
    @PreAuthorize("@ss.hasPermi('spc:summary:edit')")
    @Log(title = "数据汇总", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody SpcDataSummary spcDataSummary)
    {
        return toAjax(spcDataSummaryService.updateById(spcDataSummary));
    }

    /**
     * 删除数据汇总
     */
    @PreAuthorize("@ss.hasPermi('spc:summary:remove')")
    @Log(title = "数据汇总", businessType = BusinessType.DELETE)
    @PostMapping("/delete")
    public AjaxResult remove(@RequestBody Long[] ids)
    {
        return toAjax(spcDataSummaryService.removeByIds(Arrays.asList(ids)));
    }
}
