package com.ruoyi.project.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.framework.config.RuoYiConfig;
import com.ruoyi.project.core.domain.CoreUpload;
import com.ruoyi.project.core.mapper.CoreUploadMapper;
import com.ruoyi.project.core.service.ICoreUploadService;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

/**
 * 文件导入记录Service业务层处理
 * 
 * @author huichangwei,guolinyuan
 * @date 2023-03-09
 */
@Service
public class CoreUploadServiceImpl  extends ServiceImpl<CoreUploadMapper, CoreUpload> implements ICoreUploadService
{
    /**
     * 分页查询文件导入记录列表
     * @return 文件导入记录集合
     */
    @Override
    public List<CoreUpload> queryPage(Map<String, Object> params)
    {
        return baseMapper.queryPage(params);
    }

    @Override
    public CoreUpload saveFile(MultipartFile file)
    {
        try
        {
            if (file == null ||  file.getOriginalFilename() == null)
            {
                throw new ServiceException("上传文件为空!");
            }
            else
            {
                String oldName = file.getOriginalFilename();
                int ix = oldName.lastIndexOf(".");
                String newName = oldName.substring(0, ix) + "_" + System.currentTimeMillis() + oldName.substring(ix);

                String filePath = RuoYiConfig.getUploadPathCore() + newName;
                //用以持久化文件的file
                File fileX = new File(filePath);
                FileOutputStream out = new FileOutputStream(fileX);
                IOUtils.copy(file.getInputStream(),out);
                out.close();


                CoreUpload coreUpload = new CoreUpload();
                coreUpload.setFileName(oldName);
                coreUpload.setFilePath(filePath);
                this.save(coreUpload);
                return coreUpload;
            }
        }
        catch (Exception e)
        {
            throw new ServiceException(e.getMessage());
        }
    }
}
