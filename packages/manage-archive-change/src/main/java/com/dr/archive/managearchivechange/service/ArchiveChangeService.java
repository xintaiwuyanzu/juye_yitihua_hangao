package com.dr.archive.managearchivechange.service;

import com.dr.archive.batch.query.BatchCreateQuery;
import com.dr.archive.managearchivechange.entity.ArchiveChangeRecord;
import com.dr.framework.common.service.BaseService;

import java.util.List;

public interface ArchiveChangeService extends BaseService<ArchiveChangeRecord> {
    void archiveChange(ArchiveChangeRecord changeRecord);

    void archiveChangeMulti(BatchCreateQuery query, ArchiveChangeRecord changeRecord);

    List<ArchiveChangeRecord> detailByErrorCorrectionId(String detailByErrorCorrectionId);
}
