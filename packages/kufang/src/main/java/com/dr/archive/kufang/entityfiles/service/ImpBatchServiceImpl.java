package com.dr.archive.kufang.entityfiles.service;


import com.dr.archive.kufang.entityfiles.entity.ImpBatch;
import com.dr.archive.util.DateTimeUtils;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class ImpBatchServiceImpl extends DefaultBaseService<ImpBatch> implements ImpBatchService {

    /**
     * 添加导入批次
     */
    @Override
    public ImpBatch addBatch(String archiveType, String quantity, String impId) {
        ImpBatch impBatch = new ImpBatch();
        impBatch.setOrganiseId(SecurityHolder.get().currentOrganise().getId());
        impBatch.setArchiveType(archiveType);
        impBatch.setRecordName(SecurityHolder.get().currentPerson().getUserName() + "在"
                + DateTimeUtils.longToDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm") + "导入实体档案");
        impBatch.setId(impId);
        impBatch.setQuantity(quantity);
        insert(impBatch);
        return impBatch;
    }
}
