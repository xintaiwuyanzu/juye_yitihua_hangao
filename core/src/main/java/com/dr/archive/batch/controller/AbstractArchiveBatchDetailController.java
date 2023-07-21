package com.dr.archive.batch.controller;

import com.dr.archive.batch.entity.AbstractBatchDetailEntity;
import com.dr.archive.batch.service.BaseArchiveBatchDetailService;
import com.dr.archive.model.entity.ArchiveEntity;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.core.orm.jdbc.Relation;
import com.dr.framework.core.orm.sql.Column;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

import static com.dr.archive.model.entity.ArchiveEntity.COLUMN_ARCHIVE_CODE;
import static com.dr.archive.model.entity.ArchiveEntity.COLUMN_TITLE;

/**
 * 详情表基础 controller
 *
 * @param <S>
 * @param <E>
 * @author dr
 */
public abstract class AbstractArchiveBatchDetailController<S extends BaseArchiveBatchDetailService<E>, E extends AbstractBatchDetailEntity> extends BaseServiceController<S, E> {
    @Override
    protected SqlQuery<E> buildPageQuery(HttpServletRequest request, E entity) {
        Relation relation = service.getDetailRelation();
        //详情基本都是根据批次Id查询列表数据的
        SqlQuery<E> sqlQuery = SqlQuery.from(relation);
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
        return sqlQuery;
    }

}
