package com.dr.archive.utilization.consult.controller;

import com.dr.archive.archivecar.entity.ArchiveCarDetail;
import com.dr.archive.archivecar.entity.ArchiveCarDetailInfo;
import com.dr.archive.batch.controller.AbstractArchiveBatchController;
import com.dr.archive.utilization.consult.entity.ArchiveBatchConsult;
import com.dr.archive.utilization.consult.entity.ArchiveBatchConsultInfo;
import com.dr.archive.utilization.consult.service.ArchiveBatchConsultService;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 查档利用批次controller
 *
 * @author dr
 */
@RestController
@RequestMapping("${common.api-path:/api}/utilization/consult")
public class ArchiveConsultController extends AbstractArchiveBatchController<ArchiveBatchConsultService, ArchiveBatchConsult> {
    @Autowired
    CommonMapper commonMapper;

    @Override
    protected SqlQuery<ArchiveBatchConsult> buildPageQuery(HttpServletRequest request, ArchiveBatchConsult entity) {
        Person person = getUserLogin(request);
        SqlQuery sqlQuery = SqlQuery.from(ArchiveBatchConsult.class)
                .like(ArchiveBatchConsultInfo.BATCHNAME, entity.getBatchName())
                .like(ArchiveBatchConsultInfo.USERNAME, entity.getUserName())
                .orderByDesc(ArchiveBatchConsultInfo.CREATEDATE);
        if (entity.getSourceType().equals("org")) {
            sqlQuery.equal(ArchiveBatchConsultInfo.SOURCETYPE, entity.getSourceType());
        }
        if (!StringUtils.isEmpty(entity.getCreateOrgId())) {
            sqlQuery.equal(ArchiveBatchConsultInfo.CREATEORGID, SecurityHolder.get().currentOrganise().getId());
        }
        if (!StringUtils.isEmpty(entity.getReceiveOrgId())) {
            sqlQuery.equal(ArchiveBatchConsultInfo.RECEIVEORGID, SecurityHolder.get().currentOrganise().getId());
        } else {
            sqlQuery.equal(ArchiveBatchConsultInfo.CREATEPERSON, person.getId());
        }
        List<ArchiveBatchConsult> archiveBatchConsults = commonMapper.selectByQuery(sqlQuery);
        archiveBatchConsults.forEach(archiveBatchConsult -> {
            List<ArchiveCarDetail> archiveCarDetails = commonMapper.selectByQuery(SqlQuery.from(ArchiveCarDetail.class).equal(ArchiveCarDetailInfo.BATCHID, archiveBatchConsult.getId()));
            if (archiveBatchConsult.getDetailNum() != archiveCarDetails.size()) {
                archiveBatchConsult.setDetailNum(archiveCarDetails.size());
                service.updateById(archiveBatchConsult);
            }
        });
        return sqlQuery;
    }

    @PostMapping("/toEnd")
    public ResultEntity toEnd(String id, String remarks) {
        service.toEnd(id, remarks);
        return ResultEntity.success();
    }
}
