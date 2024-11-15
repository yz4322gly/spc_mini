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
import com.ruoyi.project.spc.domain.SpcParam;
import com.ruoyi.project.spc.service.ISpcParamService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 参数组Controller
 * 
 * @author guolinyuan
 * @date 2024-03-14
 */
@RestController
@RequestMapping("/spc/param")
public class SpcParamController extends BaseController
{
    @Autowired
    private ISpcParamService spcParamService;


    /**
     * 查询参数组列表
     */
    @PreAuthorize("@ss.hasPermi('spc:param:list')")
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam Map<String, Object> params)
    {
        startPage();
        List<SpcParam> list = spcParamService.queryPage(params);
        return getDataTable(list);
    }

    /**
     * 导出参数组列表
     */
    @PreAuthorize("@ss.hasPermi('spc:param:export')")
    @Log(title = "参数组", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response,@RequestParam Map<String, Object> params)
    {
        List<SpcParam> list = spcParamService.queryPage(params);
        ExcelUtil<SpcParam> util = new ExcelUtil<SpcParam>(SpcParam.class);
        util.exportExcel(response, list, "参数组数据");
    }

    /**
     * 获取参数组详细信息
     */
    @PreAuthorize("@ss.hasPermi('spc:param:query')")
    @GetMapping("/info")
    public AjaxResult getInfo(@RequestParam Long id)
    {
        return AjaxResult.success(spcParamService.getById(id));
    }

    /**
     * 新增参数组
     */
    @PreAuthorize("@ss.hasPermi('spc:param:add')")
    @Log(title = "参数组", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody SpcParam spcParam)
    {
        return toAjax(spcParamService.save(spcParam));
    }

    /**
     * 修改参数组
     */
    @PreAuthorize("@ss.hasPermi('spc:param:edit')")
    @Log(title = "参数组", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody SpcParam spcParam)
    {
        return toAjax(spcParamService.updateById(spcParam));
    }

    /**
     * 删除参数组
     */
    @PreAuthorize("@ss.hasPermi('spc:param:remove')")
    @Log(title = "参数组", businessType = BusinessType.DELETE)
    @PostMapping("/delete")
    public AjaxResult remove(@RequestBody Long[] ids)
    {
        return toAjax(spcParamService.removeByIds(Arrays.asList(ids)));
    }
}
