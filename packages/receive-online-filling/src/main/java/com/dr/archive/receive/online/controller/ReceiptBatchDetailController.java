package com.dr.archive.receive.online.controller;

import com.dr.archive.manage.form.service.ArchiveDataManager;
import com.dr.archive.model.entity.ArchiveEntity;
import com.dr.archive.receive.online.entity.ArchiveBatchDetailReceiveOnline;
import com.dr.archive.receive.online.entity.ArchiveBatchDetailReceiveOnlineInfo;
import com.dr.archive.receive.online.service.ArchiveBatchDetailReceiveOnlineService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.form.annotations.Form;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 在线接收详情表
 *
 * @author caor
 * @date 2021-08-28 16:35
 */
@RestController
@RequestMapping("${common.api-path:/api}/receive/onlineDetail")
public class ReceiptBatchDetailController extends BaseServiceController<ArchiveBatchDetailReceiveOnlineService, ArchiveBatchDetailReceiveOnline> {
    @Autowired
    CommonMapper commonMapper;
    @Autowired
    ArchiveDataManager dataManager;

    @Override
    protected SqlQuery<ArchiveBatchDetailReceiveOnline> buildPageQuery(HttpServletRequest httpServletRequest, ArchiveBatchDetailReceiveOnline archiveBatchDetailReceiveOnline) {
        return SqlQuery.from(ArchiveBatchDetailReceiveOnline.class)
                .equal(ArchiveBatchDetailReceiveOnlineInfo.BATCHID, archiveBatchDetailReceiveOnline.getBatchId())
                .like(ArchiveBatchDetailReceiveOnlineInfo.OFDNAME, archiveBatchDetailReceiveOnline.getOfdName())
                .like(ArchiveBatchDetailReceiveOnlineInfo.TITLE, archiveBatchDetailReceiveOnline.getTitle())
                .like(ArchiveBatchDetailReceiveOnlineInfo.ARCHIVECODE, archiveBatchDetailReceiveOnline.getArchiveCode())
                .orderBy(ArchiveBatchDetailReceiveOnlineInfo.CREATEDATE);
    }

    //删除数据,原文,目录
    @RequestMapping({"/deleteDetail"})
    public ResultEntity deleteDetail(String id) {
        return ResultEntity.success(service.deleteDetail(id));
    }

    //更新目录
    @RequestMapping(value = "/updateForm")
    public ResultEntity updateForm(HttpServletRequest request,
                                   @Form FormData formData,
                                   String fondId,
                                   String categoryId,
                                   String detailId) {
        //更新档案目录
        dataManager.updateFormData(formData, fondId, categoryId);
        //更新批次详情的档号,题名,年度信息
        ArchiveBatchDetailReceiveOnline detailReceiveOnline = service.selectById(detailId);
        detailReceiveOnline.setArchiveCode(formData.get(ArchiveEntity.COLUMN_ARCHIVE_CODE));
        detailReceiveOnline.setTitle(formData.get(ArchiveEntity.COLUMN_TITLE));
        detailReceiveOnline.setYear(formData.get(ArchiveEntity.COLUMN_YEAR));
        return ResultEntity.success(service.updateById(detailReceiveOnline));
    }
}
