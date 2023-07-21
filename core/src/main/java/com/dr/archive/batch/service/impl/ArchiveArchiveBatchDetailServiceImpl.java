package com.dr.archive.batch.service.impl;

import com.dr.archive.batch.entity.AbstractArchiveBatch;
import com.dr.archive.batch.entity.ArchiveBatchDetail;
import com.dr.archive.batch.query.BatchCreateQuery;
import com.dr.framework.common.form.core.model.FormData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.dr.archive.manage.form.service.ArchiveDataManager.STATUS_MANAGE;

/**
 * 归档
 *
 * @author: caor
 * @date: 2020/11/18 1:31
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ArchiveArchiveBatchDetailServiceImpl extends AbstractArchiveBatchDetailServiceImpl<ArchiveBatchDetail> {

    @Override
    protected ArchiveBatchDetail newBatchDetail(FormData data, AbstractArchiveBatch batch, BatchCreateQuery query) {
        //修改档案数据信息为管理库状态
        dataManager.updateStatus(data.getId(), STATUS_MANAGE, data.getFormDefinitionId());
        return super.newBatchDetail(data, batch, query);
    }

    @Override
    public String getType() {
        return BATCH_TYPE_ARCHIVE;
    }

    @Override
    public String getName() {
        return "归档";
    }
}
