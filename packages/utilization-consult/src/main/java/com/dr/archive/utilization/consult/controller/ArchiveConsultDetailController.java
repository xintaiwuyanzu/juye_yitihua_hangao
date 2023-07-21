package com.dr.archive.utilization.consult.controller;

import com.dr.archive.archivecar.entity.ArchiveCarBatch;
import com.dr.archive.archivecar.entity.ArchiveCarBatchInfo;
import com.dr.archive.archivecar.entity.ArchiveCarDetail;
import com.dr.archive.archivecar.entity.ArchiveCarDetailInfo;
import com.dr.archive.batch.controller.AbstractArchiveBatchDetailController;
import com.dr.archive.batch.entity.AbstractBatchDetailEntity;
import com.dr.archive.model.entity.ArchiveEntity;
import com.dr.archive.utilization.consult.entity.ArchiveBatchConsultInfo;
import com.dr.archive.utilization.consult.entity.ArchiveBatchDetailConsult;
import com.dr.archive.utilization.consult.entity.ArchiveBatchDetailConsultInfo;
import com.dr.archive.utilization.consult.service.ArchiveBatchDetailConsultService;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.jdbc.Relation;
import com.dr.framework.core.orm.sql.Column;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.dr.archive.model.entity.ArchiveEntity.COLUMN_ARCHIVE_CODE;
import static com.dr.archive.model.entity.ArchiveEntity.COLUMN_TITLE;

/**
 * 查档利用详情controller
 *
 * @author dr
 */
@RestController
@RequestMapping("${common.api-path:/api}/utilization/consult/details")
public class ArchiveConsultDetailController extends AbstractArchiveBatchDetailController<ArchiveBatchDetailConsultService, ArchiveBatchDetailConsult> {
    @Autowired
    CommonMapper commonMapper;

    @Override
    protected SqlQuery<ArchiveBatchDetailConsult> buildPageQuery(HttpServletRequest request, ArchiveBatchDetailConsult entity) {
        Relation relation = service.getDetailRelation();
        //详情基本都是根据批次Id查询列表数据的
        SqlQuery<ArchiveBatchDetailConsult> sqlQuery = SqlQuery.from(relation);
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
        //根据档号排序
        Column archiveCodeColumn = relation.getColumn(ArchiveEntity.COLUMN_ARCHIVE_CODE);
        if (archiveCodeColumn != null) {
            sqlQuery.orderBy(archiveCodeColumn);
        }

        List<ArchiveCarDetail> archiveCarDetails = commonMapper.selectByQuery(SqlQuery.from(ArchiveCarDetail.class).equal(ArchiveCarDetailInfo.BATCHID, entity.getBatchId()));
        archiveCarDetails.forEach(archiveCarDetail -> {
            List<ArchiveBatchDetailConsult> archiveBatchDetailConsults = commonMapper.selectByQuery(SqlQuery.from(ArchiveBatchDetailConsult.class).equal(ArchiveBatchDetailConsultInfo.BATCHID, archiveCarDetail.getBatchId()).equal(ArchiveBatchDetailConsultInfo.FORMDEFINITIONID, archiveCarDetail.getFormDefinitionId()));
            if (archiveBatchDetailConsults.size() ==0||archiveBatchDetailConsults.size()<archiveCarDetails.size()) {
                ArchiveBatchDetailConsult archiveBatchDetailConsult = new ArchiveBatchDetailConsult();
                archiveBatchDetailConsult.setCreatePerson(archiveCarDetail.getCreatePerson());
                archiveBatchDetailConsult.setUpdatePerson(archiveCarDetail.getUpdatePerson());
                archiveBatchDetailConsult.setFondName(archiveCarDetail.getFondName());
                archiveBatchDetailConsult.setFondCode(archiveCarDetail.getFondCode());
                archiveBatchDetailConsult.setCategoryCode(archiveCarDetail.getCategoryCode());
                archiveBatchDetailConsult.setCategoryId(archiveCarDetail.getCategoryId());
                archiveBatchDetailConsult.setCategoryName(archiveCarDetail.getCategoryName());
                archiveBatchDetailConsult.setFormDefinitionId(archiveCarDetail.getFormDefinitionId());
                archiveBatchDetailConsult.setFormDataId(archiveCarDetail.getFormDataId());
                archiveBatchDetailConsult.setArchiveCode(archiveCarDetail.getArchiveCode());
                archiveBatchDetailConsult.setTitle(archiveCarDetail.getTitle());
                archiveBatchDetailConsult.setNote(archiveCarDetail.getNote());
                archiveBatchDetailConsult.setYear(archiveCarDetail.getYear());
                archiveBatchDetailConsult.setBatchId(archiveCarDetail.getBatchId());
                archiveBatchDetailConsult.setSaveTerm(archiveCarDetail.getSaveTerm());
                if (!org.apache.commons.lang3.StringUtils.isEmpty(archiveCarDetail.getStatus())){
                    archiveBatchDetailConsult.setUseFile(archiveCarDetail.getStatus().equals("1"));
                }else {
                    archiveBatchDetailConsult.setUseFile(false);
                }
                archiveBatchDetailConsult.setId(UUID.randomUUID().toString());
                commonMapper.insertIgnoreNull(archiveBatchDetailConsult);
            }
        });
        List<ArchiveBatchDetailConsult> archiveBatchDetailConsults = commonMapper.selectByQuery(sqlQuery);
        if (archiveCarDetails.size()!=archiveBatchDetailConsults.size()){
            archiveBatchDetailConsults.forEach(archiveBatchDetailConsult -> {
                int count=0;
                for (ArchiveCarDetail archiveCarDetail : archiveCarDetails) {
                    if (archiveBatchDetailConsult.getFormDataId().equals(archiveCarDetail.getFormDataId())){
                        count++;
                    }
                }
                if (count==0){
                    commonMapper.deleteById(ArchiveBatchDetailConsult.class,archiveBatchDetailConsult.getId());
                }
            });
        }
        return sqlQuery;
    }
    /*
     * 查档案的利用情况*/
    @PostMapping({"/selectHistoryByForm"})
    public ResultEntity selectHistoryByForm(String formDefinitionId, String formDataId) {
        SqlQuery<Map> sqlQuery = SqlQuery.from(ArchiveBatchDetailConsult.class)
                .leftOuterJoin(ArchiveBatchDetailConsultInfo.BATCHID, ArchiveBatchConsultInfo.ID)
                .column(ArchiveBatchConsultInfo.USERNAME.max("userName"))
                .column(ArchiveBatchConsultInfo.ID.count("utilizeNum")) //利用次数
                .column(ArchiveBatchConsultInfo.USEWAY)
                .column(ArchiveBatchConsultInfo.USEFOR)
                .equal(ArchiveBatchDetailConsultInfo.FORMDEFINITIONID, formDefinitionId)
                .equal(ArchiveBatchDetailConsultInfo.FORMDATAID, formDataId)
                .groupBy(ArchiveBatchConsultInfo.USERID, ArchiveBatchConsultInfo.USEWAY, ArchiveBatchConsultInfo.USEFOR)
                .orderByDesc(ArchiveBatchDetailConsultInfo.CREATEDATE)
                .setReturnClass(Map.class);
        return ResultEntity.success(commonMapper.selectByQuery(sqlQuery));
    }
}