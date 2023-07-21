package com.dr.archive.receive.online.service.impl;

import com.dr.archive.batch.service.impl.AbstractArchiveBatchDetailServiceImpl;
import com.dr.archive.receive.online.entity.SendCheckBatchDetail;
import org.springframework.stereotype.Service;

import static com.dr.archive.manage.form.service.ArchiveDataManager.STATUS_PRE;

/**
 * 移交发送
 *
 * @author dr
 */
@Service
public class SendCheckArchiveBatchDetailServiceImpl extends AbstractArchiveBatchDetailServiceImpl<SendCheckBatchDetail> {
    @Override
    public String getType() {
        return BATCH_TYPE_SEND_CHECK;
    }

    @Override
    public String getName() {
        return "移交";
    }

    @Override
    public void updateStatus(String ids, String type, String formDefinitionId) {
        dataManager.updateStatus(ids, STATUS_PRE, formDefinitionId);
    }
}
