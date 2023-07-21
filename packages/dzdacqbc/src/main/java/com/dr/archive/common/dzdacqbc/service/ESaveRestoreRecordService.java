package com.dr.archive.common.dzdacqbc.service;

import com.dr.archive.common.dzdacqbc.entity.ESaveRestoreRecord;
import com.dr.framework.common.service.BaseService;

public interface ESaveRestoreRecordService extends BaseService<ESaveRestoreRecord> {

    void addRecord(String batchId, String detailId, String type);

}
