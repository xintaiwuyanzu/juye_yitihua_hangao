package com.dr.archive.manage.form.controller;

import com.dr.archive.manage.form.entity.RebuildIndexRecord;
import com.dr.archive.manage.form.entity.RebuildIndexRecordInfo;
import com.dr.archive.manage.form.service.RebuildIndexRecordService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 重建索引记录入口
 *
 * @author lirs
 * @date 2023/3/28 9:24
 */
@RestController
@RequestMapping(value = "/api/rebuildIndexRecord")
public class RebuildIndexRecordController extends BaseServiceController<RebuildIndexRecordService, RebuildIndexRecord> {

    @Override
    protected SqlQuery<RebuildIndexRecord> buildPageQuery(HttpServletRequest request, RebuildIndexRecord entity) {
        SqlQuery<RebuildIndexRecord> sqlQuery = SqlQuery.from(RebuildIndexRecord.class)
                .like(RebuildIndexRecordInfo.FORMCODE, entity.getFormCode())
                .like(RebuildIndexRecordInfo.FORMNAME, entity.getFormName())
                .like(RebuildIndexRecordInfo.CREATEPERSONNAME, entity.getCreatePersonName())
                .equal(RebuildIndexRecordInfo.REBUILDTYPE, entity.getRebuildType())
                .greaterThanEqual(RebuildIndexRecordInfo.STARTTIME, entity.getStartTime());
        if (!ObjectUtils.isEmpty(entity.getEndTime())) {
            sqlQuery.lessThanEqual(RebuildIndexRecordInfo.STARTTIME, entity.getEndTime() + (24 * 3600 - 1) * 1000);
        }
        return sqlQuery;
    }
}
