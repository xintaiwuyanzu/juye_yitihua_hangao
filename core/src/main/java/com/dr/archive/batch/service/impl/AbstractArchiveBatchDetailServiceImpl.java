package com.dr.archive.batch.service.impl;

import com.dr.archive.batch.entity.AbstractArchiveBatch;
import com.dr.archive.batch.entity.AbstractBatchDetailEntity;
import com.dr.archive.batch.query.BatchCreateQuery;
import com.dr.archive.batch.service.BaseArchiveBatchDetailService;
import com.dr.archive.batch.vo.BatchCount;
import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.util.SecurityHolderUtil;
import com.dr.framework.common.entity.IdEntity;
import com.dr.framework.common.entity.StatusEntity;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.page.Page;
import com.dr.framework.common.service.CommonService;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.jdbc.Relation;
import com.dr.framework.core.orm.sql.Column;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import static com.dr.archive.batch.entity.AbstractBatchDetailEntity.BATCH_ID_COLUMN_NAME;
import static com.dr.archive.manage.form.service.ArchiveDataManager.STATUS_PRE;

/**
 * 批次详情基础操作类
 *
 * @param <D>
 * @author dr
 */
public abstract class AbstractArchiveBatchDetailServiceImpl<D extends AbstractBatchDetailEntity> extends AbstractArchiveBatchDetailUtils<D> implements BaseArchiveBatchDetailService<D>, InitializingBean {
    /**
     * 用来执行异步操作
     */
    @Autowired
    protected Executor executor;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createDetail(BatchCreateQuery query, AbstractArchiveBatch entity) {
        String personId = SecurityHolder.get() == null ? "admin" : SecurityHolder.get().currentPerson().getId();
        SecurityHolder securityHolder = SecurityHolderUtil.checkSecurityHolder(organisePersonService, personId);
        executor.execute(() -> {
            SecurityHolder.set(securityHolder);
            //异步执行同步基本信息
            doCreateDetail(entity, query);
        });
    }

    @Override
    public Page<D> selectPage(AbstractArchiveBatch batch, BatchCreateQuery query, Integer start, Integer pageSize) {
        SqlQuery<D> sqlQuery = SqlQuery.from(getEntityClass())
                .equal(getEntityRelation().getColumn("batchId"), batch.getId()).orderBy(getEntityRelation().getColumn("id"));
        if (StringUtils.hasText(batch.getStatus())) {
            sqlQuery.equal(getEntityRelation().getColumn(StatusEntity.STATUS_COLUMN_KEY), batch.getStatus());
        }
        return selectPage(sqlQuery, start * pageSize, (start + 1) * pageSize);
    }


    @Override
    @Transactional(readOnly = true)
    public BatchCount count(String batchId) {
        Column batchIdColumn = entityRelation.getColumn("batchId");
        Column statusColumn = entityRelation.getColumn(StatusEntity.STATUS_COLUMN_KEY);
        Class entityClass = getEntityClass();
        long total = countTotal(batchId);
        long success = count(
                SqlQuery.from(entityClass)
                        .equal(batchIdColumn, batchId)
                        .equal(statusColumn, StatusEntity.STATUS_ENABLE)
        );
        long fail = count(
                SqlQuery.from(entityClass)
                        .equal(batchIdColumn, batchId)
                        .equal(statusColumn, StatusEntity.STATUS_UNKNOW_STR)
        );
        long undo = count(
                SqlQuery.from(entityClass)
                        .equal(batchIdColumn, batchId)
                        .equal(statusColumn, StatusEntity.STATUS_DISABLE_STR)
        );
        return new BatchCount(total, success, fail, undo);
    }

    @Override
    public long changeStatus(String id, String status, String advice, String targetValue) {
        Class entityClass = getEntityClass();
        Person person = SecurityHolder.get().currentPerson();
        SqlQuery sqlQuery = SqlQuery.from(entityClass)
                .set(entityRelation.getColumn(StatusEntity.STATUS_COLUMN_KEY), status)
                .set(entityRelation.getColumn("advice"), advice)
                .equal(entityRelation.getColumn(IdEntity.ID_COLUMN_NAME), id);
        if (!ObjectUtils.isEmpty(entityRelation.getColumn("guideContent"))) {
            sqlQuery.set(entityRelation.getColumn("guideContent"), advice);
        }
        if (StringUtils.hasText(targetValue)) {
            sqlQuery.set(entityRelation.getColumn("targetValue"), targetValue)
                    .set(entityRelation.getColumn("appraisalPerson"), person.getId())
                    .set(entityRelation.getColumn("appraisalDate"), System.currentTimeMillis());
        }
        return commonMapper.updateIgnoreNullByQuery(sqlQuery);
    }

    @Override
    public long deleteByBatchId(String batchId) {
        return commonMapper.deleteByQuery(SqlQuery.from(getEntityClass()).equal(getEntityRelation().getColumn(BATCH_ID_COLUMN_NAME), batchId));
    }

    /**
     * 异步同步档案基本信息到detail表中
     *
     * @param batch
     * @param query
     */
    @Async
    @Transactional(rollbackFor = Exception.class)
    protected void doCreateDetail(AbstractArchiveBatch batch, BatchCreateQuery query) {
        List<FormData> dataList = dataManager.findDataByQuery(query);
        dataList.forEach(d -> this.newBatchDetail(d, batch, query));
    }

    /**
     * 根据表单数据和批次信息创建批次详情
     *
     * @param data
     * @param batch
     * @param query
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    protected D newBatchDetail(FormData data, AbstractArchiveBatch batch, BatchCreateQuery query) {
        //创建对象
        D detail = newDetail(query);
        //绑定参数
       /* Fond fond = fondService.selectById(query.getFondId());
        Category category = categoryService.selectById(query.getCategoryId());
        bindBaseInfo(detail, data, fond, category, query);*/
        if (!StringUtils.isEmpty(query.getFondId()) && !StringUtils.isEmpty(query.getCategoryId())) {
            Fond fond = fondService.selectById(query.getFondId());
            Category category = categoryService.selectById(query.getCategoryId());
            bindBaseInfo(detail, data, fond, category, query);
        } else {
            CommonService.bindCreateInfo(detail);
        }
        //绑定批次Id
        detail.setBatchId(batch.getId());
        //插入数据
        commonMapper.insert(detail);
        return detail;
    }

    /**
     * 绑定参数实现
     *
     * @param detail
     * @param data
     * @param fondTo
     * @param category
     */
    @Deprecated
    protected void bindBaseInfo(D detail, FormData data, Fond fondTo, Category category, BatchCreateQuery query) {
        initDetail(detail, null, data, fondTo, category);
    }

    /**
     * 创建detail对象
     *
     * @return
     */
    protected D newDetail(BatchCreateQuery query) {
        try {
            return getEntityClass().newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void updateFormData(AbstractArchiveBatch archiveBatch) {
        Page page = selectPage(archiveBatch, null, 0, 10);
        List<AbstractBatchDetailEntity> detailList = page.getData();
        if (detailList.size() > 0) {
            List<String> formdataList = detailList.stream().map(AbstractBatchDetailEntity::getFormDataId).collect(Collectors.toList());
            String ids = String.join(",", formdataList);
            //修改档案数据信息为移交库状态
            updateStatus(ids, STATUS_PRE, detailList.get(0).getFormDefinitionId());
            doUpdateFormDate(archiveBatch, detailList);
        }
    }

    /**
     * 跟新档案库状态
     *
     * @param ids
     * @param type
     * @param formDefinitionId
     */
    protected void updateStatus(String ids, String type, String formDefinitionId) {
    }

    /**
     * 更新数据
     *
     * @param abstractBatchDetailEntityList
     */
    protected void doUpdateFormDate(AbstractArchiveBatch archiveBatch, List<AbstractBatchDetailEntity> abstractBatchDetailEntityList) {
        abstractBatchDetailEntityList.forEach(detail -> {
            FormData formData = dataManager.selectOneFormData(detail.getFormDefinitionId(), detail.getFormDataId());
            dataManager.updateFormData(formData, "", "");
        });
    }

    @Override
    public List<D> selectByBatchId(String batchId) {
        return selectList(SqlQuery.from(getEntityClass()).equal(getEntityRelation().getColumn(BATCH_ID_COLUMN_NAME), batchId));
    }

    @Override
    public Class<D> getDetailClass() {
        return getEntityClass();
    }

    @Override
    public Relation getDetailRelation() {
        return getEntityRelation();
    }
}
