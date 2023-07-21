package com.dr.archive.service;

import com.dr.archive.entity.ArchivesBatchAddDetaile;
import com.dr.archive.receive.offline.entity.ArchiveBatchReceiveOffline;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.service.BaseService;
import com.dr.framework.core.organise.entity.Organise;

public interface ArchivesBatchAddDetailsService extends BaseService<ArchivesBatchAddDetaile> {
    void newBatchDetails(FormData formData, ArchiveBatchReceiveOffline archivesBatchAdd, Organise organise);
}
