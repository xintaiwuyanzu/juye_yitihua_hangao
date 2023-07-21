package com.dr.archive.manage.handover.service;

import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.handover.entity.ArchiveBatchHandOver;
import com.dr.archive.manage.handover.entity.ArchiveBatchHandOverDetail;
import com.dr.archive.manage.handover.entity.ArchiveBatchHandOverLib;
import com.dr.archive.manage.handover.vo.HandOverDetailVo;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.page.Page;
import com.dr.framework.common.service.BaseService;
import org.springframework.lang.Nullable;

/**
 * 档案到期移交批次详情service
 *
 * @author dr
 */
public interface ArchiveBatchHandOverDetailService extends BaseService<ArchiveBatchHandOverDetail> {
    /**
     * 根据批次创建详情数据
     *
     * @param batch
     * @param formData
     * @param fond
     * @return
     */
    ArchiveBatchHandOverDetail newDetail(ArchiveBatchHandOverLib batch, FormData formData, @Nullable Fond fond, String detailType);

    /**
     * 批量添加档案全宗卷
     *
     * @param handOver
     */
    void addManageDetail(ArchiveBatchHandOver handOver);

    /**
     * 追加单条全宗卷信息
     *
     * @param batchId
     * @param manageId
     * @return
     */
    ArchiveBatchHandOverDetail addManageDetail(String batchId, String manageId);

    Page<HandOverDetailVo> getOverDetailData(String batchId, String handoverId, String archiveCode, int pageIndex, int pageSize);
}
