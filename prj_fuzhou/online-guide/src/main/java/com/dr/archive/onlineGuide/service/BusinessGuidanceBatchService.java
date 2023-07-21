package com.dr.archive.onlineGuide.service;

import com.dr.archive.onlineGuide.entity.BusinessGuidanceBatch;
import com.dr.archive.onlineGuide.entity.BusinessGuidanceBatchDetail;
import com.dr.framework.common.service.BaseService;

import java.util.List;

/**
 * @Author: caor
 * @Date: 2022-06-10 15:20
 * @Description:
 */
public interface BusinessGuidanceBatchService extends BaseService<BusinessGuidanceBatch> {
    /**
     * 发起单条记录申请
     *
     * @param formDefinitionId
     * @param formDataId
     * @return
     */
    BusinessGuidanceBatchDetail guidance(String formDefinitionId, String formDataId, String description);

    /**
     * 从业务指导类型档案发起申请
     *
     * @param carBatchId  档案车批次id
     * @param description 批量说明
     * @return
     */
    long batchApply(String carBatchId, String description);

    /**
     * 查询当前档案是否存在未完成的批次
     * @param formDefinitionId
     * @param formDataId
     * @return
     */
    String judgeExistence(String formDefinitionId , String formDataId);

    /**
     * 指导业务管理档案总数
     */
    List<BusinessGuidanceBatch> total();
}
