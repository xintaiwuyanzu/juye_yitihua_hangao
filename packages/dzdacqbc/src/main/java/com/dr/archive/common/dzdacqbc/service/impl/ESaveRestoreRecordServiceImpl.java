package com.dr.archive.common.dzdacqbc.service.impl;

import com.dr.archive.common.dzdacqbc.entity.EArchive;
import com.dr.archive.common.dzdacqbc.entity.ESaveBackBatchDetail;
import com.dr.archive.common.dzdacqbc.entity.ESaveRestoreRecord;
import com.dr.archive.common.dzdacqbc.service.EArchiveService;
import com.dr.archive.common.dzdacqbc.service.ESaveBackBatchDetailService;
import com.dr.archive.common.dzdacqbc.service.ESaveRestoreRecordService;
import com.dr.archive.util.DateTimeUtils;
import com.dr.archive.util.UUIDUtils;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ESaveRestoreRecordServiceImpl extends DefaultBaseService<ESaveRestoreRecord> implements ESaveRestoreRecordService {
    @Autowired
    EArchiveService eArchiveService;
    @Autowired
    ESaveBackBatchDetailService eSaveBackBatchDetailService;

    /**
     * 添加恢复记录
     *
     * @param batchId
     */
    @Override
    public void addRecord(String batchId, String detailId, String type) {
        ESaveRestoreRecord record = new ESaveRestoreRecord();
        record.setId(UUIDUtils.getUUID());
        record.setBatchId(batchId);
        Person person = SecurityHolder.get().currentPerson();
        record.setPersonId(person.getId());
        record.setPersonName(person.getUserName());
        ESaveBackBatchDetail detail = eSaveBackBatchDetailService.selectById(detailId);
        if (detail != null) {
            record.setArchiveCode(detail.getArchiveCode());
            record.setArchiveId(detail.getArchiveId());
            record.setCategoryCode(detail.getCategoryCode());
            record.setFond_code(detail.getFond_code());
            record.setFormDefinitionId(detail.getFormDefinitionId());
            record.setTiming(detail.getTiming());
            record.setBackPath(detail.getBackPath());
        }
        record.setRecordName(person.getUserName() + "在" + DateTimeUtils.longToDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + "进行数据恢复");
        record.setReType(type);
        insert(record);

    }
}
