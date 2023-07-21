package com.dr.archive.receive.online.service.impl;

import com.dr.archive.batch.entity.AbstractArchiveBatch;
import com.dr.archive.batch.query.BatchCreateQuery;
import com.dr.archive.batch.service.ArchiveBatchService;
import com.dr.archive.formMap.bo.FormKeyMap;
import com.dr.archive.manage.impexpscheme.service.impl.AbstractDataParserArchiveBatchDetailService;
import com.dr.archive.model.entity.AbstractArchiveEntity;
import com.dr.archive.receive.online.entity.ExpBatch;
import com.dr.archive.receive.online.entity.ExpBatchDetail;
import com.dr.archive.receive.online.service.ExpBatchDetailService;
import com.dr.archive.util.UUIDUtils;
import com.dr.framework.common.entity.StatusEntity;
import com.dr.framework.common.form.core.model.FormData;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 导出
 *
 * @author: dr
 * @date: 2020/11/18 1:31
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ExpArchiveBatchDetailServiceImpl extends AbstractDataParserArchiveBatchDetailService<ExpBatchDetail> implements ExpBatchDetailService {

    @Autowired
    ArchiveBatchService archiveBatchService;

    @Override
    @Async
    protected void doCreateDetail(AbstractArchiveBatch archiveBatch, BatchCreateQuery query) {
//        AbstractArchiveReceiveBatch batch = new AbstractArchiveReceiveBatch();
        ExpBatch batch = new ExpBatch();
        BeanUtils.copyProperties(archiveBatch, batch);
        batch.setMineType(query.getMineType());
        //更新批次状态
        archiveBatch.setStartDate(System.currentTimeMillis());
        archiveBatch.setStatus("2");
        //创建导出文件目录
        String targetDir = commonFileConfig.getUploadDirWithDate("exp");
        //创建导出文件
        String targetFile = String.join(File.separator, targetDir, UUIDUtils.getUUID() + "." + dataParser.getFileSuffix(batch.getMineType()));
        batch.setFileLocation(targetFile);
        commonMapper.updateById(batch);
        //查询映射方案
        List<FormKeyMap> formKeyMaps = formKeyMapService.getFormKeyMap(query.getImpSchemaId());
        //创建接收文件
        List<FormData> dataList = dataManager.findDataByQuery(query);
        try {
            File file = new File(targetFile);
            if (!file.exists()) {
                file.createNewFile();
            }
            dataParser.writeData(
                    formKeyMaps.stream().map(FormKeyMap::getTargetCode).distinct().toArray(String[]::new),
                    new ExpIterator(
                            dataList.listIterator(),
                            batch,
                            query,
                            formKeyMaps
                    ),
                    batch.getMineType(),
                    Paths.get(targetFile)
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        batch.setStatus(StatusEntity.STATUS_ENABLE_STR);
        batch.setEndDate(System.currentTimeMillis());
        commonMapper.updateById(batch);
    }

    class ExpIterator implements Iterator<Map<String, Object>> {
        final Iterator<FormData> formDataIterator;
        final AbstractArchiveBatch batch;
        final BatchCreateQuery query;
        final List<FormKeyMap> formKeyMaps;

        ExpIterator(Iterator<FormData> formDataIterator, AbstractArchiveBatch batch, BatchCreateQuery query, List<FormKeyMap> formKeyMaps) {
            this.formDataIterator = formDataIterator;
            this.batch = batch;
            this.query = query;
            this.formKeyMaps = formKeyMaps;
        }

        @Override
        public boolean hasNext() {
            return formDataIterator.hasNext();
        }

        @Override
        public Map<String, Object> next() {
            FormData data = formDataIterator.next();
            newBatchDetail(data, batch, query);
            return convertData(data, formKeyMaps);
        }
    }

    /**
     * 将表单数据转换成目标格式
     *
     * @param formData
     * @param impExpSchemeItems
     * @return
     */
    private Map<String, Object> convertData(FormData formData, List<FormKeyMap> impExpSchemeItems) {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put(AbstractArchiveEntity.ID_COLUMN_NAME, formData.getId());
        objectMap.put(AbstractArchiveEntity.COLUMN_ARCHIVE_CODE, formData.getString(AbstractArchiveEntity.COLUMN_ARCHIVE_CODE));
        for (FormKeyMap item : impExpSchemeItems) {
            Object value = formData.get(item.getFieldCode());
            objectMap.put(item.getTargetCode(), value);
        }
        return objectMap;
    }

    @Override
    public String getType() {
        return BATCH_TYPE_EXP;
    }

    @Override
    public String getName() {
        return "导出";
    }

}
