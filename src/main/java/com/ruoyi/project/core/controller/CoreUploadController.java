package com.ruoyi.project.core.controller;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.core.domain.CoreUpload;
import com.ruoyi.project.core.service.ICoreUploadService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 文件导入记录Controller
 *
 * @author huichangwei, guolinyuan
 * @date 2023-03-09
 */
@RestController
@RequestMapping("/core/upload")
public class CoreUploadController extends BaseController
{
    @Autowired
    private ICoreUploadService coreUploadService;




    /**
     * 查询文件导入记录列表
     */
    @PreAuthorize("@ss.hasPermi('core:upload:list')")
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam Map<String, Object> params)
    {
        startPage();
        List<CoreUpload> list = coreUploadService.queryPage(params);
        return getDataTable(list);
    }

    /**
     * 导出文件导入记录列表
     */
    @PreAuthorize("@ss.hasPermi('core:upload:export')")
    @Log(title = "文件导入记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestParam Map<String, Object> params)
    {
        List<CoreUpload> list = coreUploadService.queryPage(params);
        ExcelUtil<CoreUpload> util = new ExcelUtil<CoreUpload>(CoreUpload.class);
        util.exportExcel(response, list, "文件导入记录数据");
    }

    /**
     * 获取文件导入记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('core:upload:query')")
    @GetMapping("/info")
    public AjaxResult getInfo(@RequestParam Long id)
    {
        return AjaxResult.success(coreUploadService.getById(id));
    }

    /**
     * 新增文件导入记录
     */
    @PreAuthorize("@ss.hasPermi('core:upload:add')")
    @Log(title = "文件导入记录", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody CoreUpload coreUpload)
    {
        return toAjax(coreUploadService.save(coreUpload));
    }

    /**
     * 修改文件导入记录
     */
    @PreAuthorize("@ss.hasPermi('core:upload:edit')")
    @Log(title = "文件导入记录", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody CoreUpload coreUpload)
    {
        return toAjax(coreUploadService.updateById(coreUpload));
    }

    /**
     * 删除文件导入记录
     */
    @PreAuthorize("@ss.hasPermi('core:upload:remove')")
    @Log(title = "文件导入记录", businessType = BusinessType.DELETE)
    @PostMapping("/delete")
    public AjaxResult remove(@RequestBody String[] ids)
    {
        return toAjax(coreUploadService.removeByIds(Arrays.asList(ids)));
    }

    /**
     * 下载请求
     */
    @PostMapping("/download")
    @PreAuthorize("@ss.hasPermi('core:upload:list')")
    public void download(Long id, HttpServletResponse response)
    {

        CoreUpload coreUpload = coreUploadService.getById(id);
        try (ServletOutputStream outputStream = response.getOutputStream())
        {
            if (coreUpload == null)
            {
                outputStream.write(JSON.toJSON(AjaxResult.error("需下载的文件不存在!")).toString().getBytes(StandardCharsets.UTF_8));
            }
            else
            {
                response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
                response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(coreUpload.getFileName(), "UTF-8"));
                File file = new File(coreUpload.getFilePath());
                IOUtils.copy(Files.newInputStream(file.toPath()), outputStream);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}