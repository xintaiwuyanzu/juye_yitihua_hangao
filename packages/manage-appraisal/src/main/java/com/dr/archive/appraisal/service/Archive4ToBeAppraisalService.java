package com.dr.archive.appraisal.service;

import com.dr.archive.appraisal.entity.AppraisalBatch;
import com.dr.archive.appraisal.entity.Archive4ToBeAppraisal;
import com.dr.framework.common.service.BaseService;
import com.dr.framework.core.security.SecurityHolder;

import java.util.Map;

/**
 * 待鉴定库service
 */
public interface Archive4ToBeAppraisalService extends BaseService<Archive4ToBeAppraisal> {
    /**
     * 每次扫描表单分页数量
     */
    int PAGE_SIZE = 1000;

    long updateByAppraisalBatch(AppraisalBatch appraisalBatch);

    Map statistics(String fondCodes, String appraisalType, String startVintages, String endVintages);

    void startScanArchive(SecurityHolder securityHolder) throws InterruptedException;

    Archive4ToBeAppraisal getOneByForm(String formDataId, String formDefinitionId);

    void removeCache(String orgId);


}
