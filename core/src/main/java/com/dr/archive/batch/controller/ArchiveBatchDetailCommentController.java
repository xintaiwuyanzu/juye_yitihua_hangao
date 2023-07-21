package com.dr.archive.batch.controller;

import com.dr.archive.batch.entity.ArchiveBatchDetailComment;
import com.dr.archive.batch.entity.ArchiveBatchDetailCommentInfo;
import com.dr.archive.batch.service.ArchiveBatchDetailCommentService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 档案详情意见controller
 *
 * @author dr
 */
@RestController
@RequestMapping({"${common.api-path:/api}/batchDetailComment"})
public class ArchiveBatchDetailCommentController extends BaseServiceController<ArchiveBatchDetailCommentService, ArchiveBatchDetailComment> {

    @Override
    protected SqlQuery<ArchiveBatchDetailComment> buildPageQuery(HttpServletRequest request, ArchiveBatchDetailComment entity) {
        SqlQuery<ArchiveBatchDetailComment> sqlQuery = SqlQuery.from(ArchiveBatchDetailComment.class)
                //根据批次Id和详情Id查询数据
                .equal(ArchiveBatchDetailCommentInfo.BATCHID, entity.getBatchId())
                .equal(ArchiveBatchDetailCommentInfo.DETAILID, entity.getDetailId());
        return sqlQuery.orderByDesc(ArchiveBatchDetailCommentInfo.CREATEDATE);
    }

}
