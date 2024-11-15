package com.ruoyi.project.spc.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.fastjson.JSON;
import com.ruoyi.project.spc.domain.SpcData;
import com.ruoyi.project.spc.service.ISpcDataService;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @author guolinyuan
 */
@Slf4j
public class SpcDataListener implements ReadListener<SpcData>
{
    /**
     * 每隔10000条存储数据库，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 10000;
    /**
     * 缓存的数据
     */
    private List<SpcData> cachedDataList = new ArrayList<>(BATCH_COUNT);

    /**
     * 操作数据库的service
     */
    private final ISpcDataService spcDataService;

    private final Set<String> keys;


    public SpcDataListener(ISpcDataService spcDataService, Set<String> keys)
    {
        this.spcDataService = spcDataService;
        this.keys = keys;
    }

    /**
     * data的前后空格会自动删除,即自动执行了trim操作
     * @param data
     * @param context
     */
    @Override
    public void invoke(SpcData data, AnalysisContext context)
    {
        final Integer rowIndex = context.readRowHolder().getRowIndex();
        log.debug("解析到第{}行,普通数据:{}",rowIndex, JSON.toJSONString(data));
        cachedDataList.add(data);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (cachedDataList.size() >= BATCH_COUNT)
        {
            saveData();
            cachedDataList = new ArrayList<>(BATCH_COUNT);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext)
    {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData();
        log.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData()
    {
        log.info("{}条数据，开始存储数据库！", cachedDataList.size());
        spcDataService.saveBatch(cachedDataList);
        for (SpcData data : cachedDataList) {
            keys.add(data.getParamKey());
        }
        log.info("存储数据库成功！");
    }

}
