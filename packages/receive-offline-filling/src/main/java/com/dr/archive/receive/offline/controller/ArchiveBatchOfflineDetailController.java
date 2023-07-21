package com.dr.archive.receive.offline.controller;

import com.dr.archive.batch.entity.AbstractBatchDetailEntity;
import com.dr.archive.manage.form.service.ArchiveDataManager;
import com.dr.archive.model.entity.ArchiveEntity;
import com.dr.archive.receive.offline.entity.ArchiveBatchReceiveOfflineDetail;
import com.dr.archive.receive.offline.entity.ArchiveBatchReceiveOfflineDetailInfo;
import com.dr.archive.receive.offline.service.ArchiveBatchOfflineDetailService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.form.annotations.Form;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.core.orm.jdbc.Relation;
import com.dr.framework.core.orm.sql.Column;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.dr.archive.model.entity.ArchiveEntity.COLUMN_ARCHIVE_CODE;
import static com.dr.archive.model.entity.ArchiveEntity.COLUMN_TITLE;

/**
 * 离线接收详情表controller
 *
 * @author dr
 */
@RestController
@RequestMapping("${common.api-path:/api}/receive/offline/detail")
public class ArchiveBatchOfflineDetailController extends BaseServiceController<ArchiveBatchOfflineDetailService, ArchiveBatchReceiveOfflineDetail> {
    @Autowired
    ArchiveDataManager dataManager;

    @Override
    protected SqlQuery<ArchiveBatchReceiveOfflineDetail> buildPageQuery(HttpServletRequest request, ArchiveBatchReceiveOfflineDetail entity) {
        Relation relation = service.getDetailRelation();
        //详情基本都是根据批次Id查询列表数据的
        SqlQuery<ArchiveBatchReceiveOfflineDetail> sqlQuery = SqlQuery.from(relation);
        //批次Id列对象
        Column batchIdColumn = relation.getColumn(AbstractBatchDetailEntity.BATCH_ID_COLUMN_NAME);
        if (batchIdColumn != null && StringUtils.hasText(entity.getBatchId())) {
            sqlQuery.equal(batchIdColumn, entity.getBatchId());
        }

        //根据档号，题名查询数据
        Column codeColumn = relation.getColumn(COLUMN_ARCHIVE_CODE);
        if (codeColumn != null && StringUtils.hasText(entity.getArchiveCode())) {
            sqlQuery.like(codeColumn, entity.getArchiveCode());
        }
        //根据题名查询数据
        Column titleColumn = relation.getColumn(COLUMN_TITLE);
        if (titleColumn != null && StringUtils.hasText(entity.getTitle())) {
            sqlQuery.like(titleColumn, entity.getTitle());
        }

        sqlQuery.equal(ArchiveBatchReceiveOfflineDetailInfo.HOOKSTATUS, entity.getHookStatus());
        sqlQuery.equal(ArchiveBatchReceiveOfflineDetailInfo.TESTSTATUS, entity.getTestStatus());

        //根据档号排序
        Column archiveCodeColumn = relation.getColumn(ArchiveEntity.COLUMN_ARCHIVE_CODE);
        if (archiveCodeColumn != null) {
            sqlQuery.orderBy(archiveCodeColumn);
        }


        return sqlQuery;
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
        ArchiveBatchReceiveOfflineDetail batchReceiveOfflineDetail = service.selectById(detailId);
        batchReceiveOfflineDetail.setArchiveCode(formData.get(ArchiveEntity.COLUMN_ARCHIVE_CODE));
        batchReceiveOfflineDetail.setTitle(formData.get(ArchiveEntity.COLUMN_TITLE));
        batchReceiveOfflineDetail.setYear(formData.get(ArchiveEntity.COLUMN_YEAR));
        return ResultEntity.success(service.updateById(batchReceiveOfflineDetail));
    }
}
