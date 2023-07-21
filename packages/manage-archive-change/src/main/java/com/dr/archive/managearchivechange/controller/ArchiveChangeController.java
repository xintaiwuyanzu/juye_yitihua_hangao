package com.dr.archive.managearchivechange.controller;

import com.dr.archive.batch.query.BatchCreateQuery;
import com.dr.archive.managearchivechange.entity.ArchiveChangeRecord;
import com.dr.archive.managearchivechange.entity.ArchiveChangeRecordInfo;
import com.dr.archive.managearchivechange.service.ArchiveChangeService;
import com.dr.archive.model.query.ArchiveDataQuery;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/archiveChange")
public class ArchiveChangeController extends BaseServiceController<ArchiveChangeService, ArchiveChangeRecord> {

    @Override
    protected SqlQuery<ArchiveChangeRecord> buildPageQuery(HttpServletRequest httpServletRequest, ArchiveChangeRecord archiveChangeRecord) {
        return SqlQuery.from(ArchiveChangeRecord.class).orderByDesc(ArchiveChangeRecordInfo.CREATEDATE).like(ArchiveChangeRecordInfo.TITLE, archiveChangeRecord.getTitle()).like(ArchiveChangeRecordInfo.ARCHIVECODE, archiveChangeRecord.getArchiveCode());
    }

    /**
     * 档案调整 单条数据
     *
     * @param changeRecord
     * @return
     */
    @RequestMapping("/archiveChange")
    public ResultEntity archiveChange(ArchiveChangeRecord changeRecord) {
        service.archiveChange(changeRecord);
        return ResultEntity.success();
    }

    @RequestMapping("/detailByErrorCorrectionId")
    public ResultEntity detailByErrorCorrectionId(HttpServletRequest request, ArchiveChangeRecord entity, String detailByErrorCorrectionId) {
        return ResultEntity.success(service.detailByErrorCorrectionId(detailByErrorCorrectionId));
    }

    /**
     * 档案调整 多条数据
     * 福州项目接收暂存库，批量修改功能，无需审核
     *
     * @param changeRecord
     * @return
     */
    @RequestMapping("/archiveChangeMulti")
    public ResultEntity archiveChangeMulti(ArchiveChangeRecord changeRecord, @RequestParam(name = ArchiveDataQuery.QUERY_KEY, required = false) String queryContent, BatchCreateQuery query) {
        query.parseQuery(queryContent);
        service.archiveChangeMulti(query, changeRecord);
        return ResultEntity.success();
    }
}
