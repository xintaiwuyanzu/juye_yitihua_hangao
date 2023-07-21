package com.dr.archive.manage.archiveMetadata.controller;

import com.dr.archive.manage.archiveMetadata.entity.ArchiveMetadataRecord;
import com.dr.archive.manage.archiveMetadata.entity.ArchiveMetadataRecordInfo;
import com.dr.archive.manage.archiveMetadata.service.ArchiveMetadataRecordService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.dr.archive.util.Constants.ORG_TYPE_DAS;

/**
 * 档案元数据变更记录
 *
 * @author hyj
 */

@RestController
@RequestMapping("/api/archiveMetadataRecord")
public class ArchiveMetadataRecordController extends BaseServiceController<ArchiveMetadataRecordService, ArchiveMetadataRecord> {

    @Override
    protected SqlQuery<ArchiveMetadataRecord> buildPageQuery(HttpServletRequest httpServletRequest, ArchiveMetadataRecord archiveMetadataRecord) {
        return SqlQuery.from(ArchiveMetadataRecord.class).equal(ArchiveMetadataRecordInfo.FORMDEFINITIONID, archiveMetadataRecord.getFormDefinitionId()).equal(ArchiveMetadataRecordInfo.FORMDATAID, archiveMetadataRecord.getFormDataId())
                //档案室用户只能查看档案室权限的过程元数据，档案馆可以查看全部的，目前只根据档案数据查询元数据，不需要加其他权限进行过滤
                .equal(ArchiveMetadataRecordInfo.QXGL, ORG_TYPE_DAS.equals(SecurityHolder.get().currentOrganise().getOrganiseType()) ? SecurityHolder.get().currentOrganise().getOrganiseType() : "").orderByDesc(ArchiveMetadataRecordInfo.CREATEDATE);
    }
}
