package com.ruoyi.project.spc.controller;

import java.util.*;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.exception.ExcelCommonException;
import com.ruoyi.project.core.domain.CoreUpload;
import com.ruoyi.project.core.service.ICoreUploadService;
import com.ruoyi.project.spc.listener.SpcDataListener;
import com.ruoyi.project.spc.service.ISpcDataSummaryService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.project.spc.domain.SpcData;
import com.ruoyi.project.spc.service.ISpcDataService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * spc主数据Controller
 *
 * @author guolinyuan
 * @date 2024-03-14
 */
@RestController
@RequestMapping("/spc/data")
public class SpcDataController extends BaseController {
    @Autowired
    private ISpcDataService spcDataService;

    @Autowired
    private ICoreUploadService coreUploadService;

    @Autowired
    ISpcDataSummaryService spcDataSummaryService;

    /**
     * 接口保存
     * 修改请参见,保持同一性 {@link this#uploadAndSummary(MultipartFile)}
     *
     * @param data
     * @return
     */
    @PreAuthorize("@ss.hasPermi('spc:data:add')")
    @PostMapping("saveAndSummary")
    public AjaxResult saveAndSummary(@RequestBody List<SpcData> data) {
        spcDataService.saveAndSummary(data);
        return AjaxResult.success();
    }


    /**
     * excel导入
     * 修改请参见,保持同一性 {@link this#saveAndSummary(List)}
     */
    @Transactional
    @PreAuthorize("@ss.hasPermi('spc:data:add')")
    @PostMapping("/uploadAndSummary")
    public AjaxResult uploadAndSummary(@RequestParam("file") MultipartFile file) {
        try {
            CoreUpload coreUpload = coreUploadService.saveFile(file);
            Set<String> keys = new HashSet<>();
            EasyExcel.read(file.getInputStream(), SpcData.class, new SpcDataListener(spcDataService, keys)).sheet().doRead();
            for (String key : keys) {
                spcDataSummaryService.summarizeFromDetails(key, null, null);
            }
            coreUploadService.save(coreUpload);
            return AjaxResult.success().put("msg", keys);
        } catch (ExcelCommonException e) {
            return AjaxResult.error("excel格式错误,不是标准的excel文件！");
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
    }


    /**
     * 查询spc主数据列表
     */
    @PreAuthorize("@ss.hasPermi('spc:data:list')")
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam Map<String, Object> params) {
        startPage();
        List<SpcData> list = spcDataService.queryPage(params);
        return getDataTable(list);
    }

    /**
     * 导出spc主数据列表
     */
    @PreAuthorize("@ss.hasPermi('spc:data:export')")
    @Log(title = "spc主数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestParam Map<String, Object> params) {
        List<SpcData> list = spcDataService.queryPage(params);
        ExcelUtil<SpcData> util = new ExcelUtil<SpcData>(SpcData.class);
        util.exportExcel(response, list, "spc主数据数据");
    }

    /**
     * 获取spc主数据详细信息
     */
    @PreAuthorize("@ss.hasPermi('spc:data:query')")
    @GetMapping("/info")
    public AjaxResult getInfo(@RequestParam Long id) {
        return AjaxResult.success(spcDataService.getById(id));
    }

    /**
     * 新增spc主数据
     */
    @PreAuthorize("@ss.hasPermi('spc:data:add')")
    @Log(title = "spc主数据", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody SpcData spcData) {
        return toAjax(spcDataService.save(spcData));
    }

    /**
     * 修改spc主数据
     */
    @PreAuthorize("@ss.hasPermi('spc:data:edit')")
    @Log(title = "spc主数据", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody SpcData spcData) {
        return toAjax(spcDataService.updateById(spcData));
    }

    /**
     * 删除spc主数据
     */
    @PreAuthorize("@ss.hasPermi('spc:data:remove')")
    @Log(title = "spc主数据", businessType = BusinessType.DELETE)
    @PostMapping("/delete")
    public AjaxResult remove(@RequestBody Long[] ids) {
        return toAjax(spcDataService.removeByIds(Arrays.asList(ids)));
    }
}
