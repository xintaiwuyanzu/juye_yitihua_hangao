package com.dr.archive.receive.online.archiveFiling.service;


import com.dr.archive.receive.online.archiveFiling.entity.FilingDetail;
import com.dr.archive.model.query.ArchiveDataQuery;
import com.dr.framework.common.service.BaseService;

import java.util.List;

public interface FilingService extends BaseService<FilingDetail> {
    String FILEING_TYPE_RECORD = "RECORD";
    String FILEING_TYPE_TASK = "TASK";

    /**
     * 查询该次入库申请的详情档案信息
     *
     * @param id   type为”RECODE“为filingId， type为”TASK“为taskId
     * @param type id类型
     * @return 档案详情信息
     */
    List<FilingDetail> queryArchiveDetail(String id, String type);

    /**
     * 入库审核
     *
     * @param type       ”1“通过， ”0“驳回
     * @param id         taskId
     * @param suggestion 审核意见
     */
    void examine(String type, String id, String suggestion);

    /**
     * 提交入库审核的检查
     *
     * @param dataQuery
     * @return 是否有在审核的档案
     */
    boolean checkFiling(ArchiveDataQuery dataQuery);
}
