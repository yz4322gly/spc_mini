package com.ruoyi.project.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.project.core.domain.CoreUpload;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 文件导入记录Service接口
 * 
 * @author huichangwei,guolinyuan
 * @date 2023-03-09
 */
public interface ICoreUploadService  extends IService<CoreUpload>
{
    /**
     * 分页查询文件导入记录列表
     *
     * @return 文件导入记录集合
     */
    List<CoreUpload> queryPage(Map<String, Object> params);

    /**
     * 上传文件
     * @param file
     * @return
     */
    CoreUpload saveFile(MultipartFile file);

}
