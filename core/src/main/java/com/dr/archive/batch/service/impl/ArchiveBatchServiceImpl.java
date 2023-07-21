package com.dr.archive.batch.service.impl;

import com.dr.archive.batch.entity.AbstractArchiveBatch;
import com.dr.archive.batch.entity.AbstractBatchDetailEntity;
import com.dr.archive.batch.query.BatchCreateQuery;
import com.dr.archive.batch.service.ArchiveBatchService;
import com.dr.archive.batch.service.BaseArchiveBatchDetailService;
import com.dr.archive.batch.vo.BatchCount;
import com.dr.archive.manage.form.service.ArchiveDataManager;
import com.dr.archive.manage.task.entity.ArchiveTask;
import com.dr.archive.manage.task.entity.ArchiveTaskInfo;
import com.dr.archive.manage.task.service.ArchiveTaskService;
import com.dr.framework.common.entity.StatusEntity;
import com.dr.framework.common.page.Page;
import com.dr.framework.common.service.CommonService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dr
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ArchiveBatchServiceImpl extends DefaultBaseService<AbstractArchiveBatch> implements ArchiveBatchService {
    @Autowired
    ArchiveTaskService archiveTaskService;
    @Autowired
    ArchiveDataManager dataManager;
    @Autowired
    ArchiveBatchService archiveBatchService;
    Map<String, BaseArchiveBatchDetailService> baseBatchServiceMap;

    @Override
    public long deleteById(String... ids) {
        long result = super.deleteById(ids);
        for (String id : ids) {
            AbstractArchiveBatch batch = selectById(id);
            BaseArchiveBatchDetailService service = baseBatchServiceMap.get(batch.getBatchType());
            if (service != null) {
                service.deleteByBatchId(id);
            }
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AbstractBatchDetailEntity changeStatus(String type, String detailId, String status, String advice, String targetValue) {
        BaseArchiveBatchDetailService service = baseBatchServiceMap.get(type);
        service.changeStatus(detailId, status, advice, targetValue);
        return this.detail(detailId, type);
    }

    @Override
    public long updateById(AbstractArchiveBatch entity) {
        return commonMapper.updateIgnoreNullById(entity);
    }

    @Override
    public AbstractArchiveBatch newBatch(BatchCreateQuery query, Person person) {
        BaseArchiveBatchDetailService baseArchiveBatchDetailService = baseBatchServiceMap.get(query.getType());
        Assert.notNull(baseArchiveBatchDetailService, "不能处理：" + query.getType() + "类型的批量操作");

        AbstractArchiveBatch batch = new AbstractArchiveBatch();
        //创建批次信息
        batch.setStartDate(System.currentTimeMillis());
        batch.setStatus(StatusEntity.STATUS_DISABLE_STR);
        batch.setBatchType(query.getType());
       /* batch.setMineType(query.getMineType());
        batch.setFileLocation(query.getFileLocation());
        batch.setFileName(query.getFileName());*/

        String batchName = String.format("%s提交的%s", person.getUserName(), baseArchiveBatchDetailService.getName());
        if (!StringUtils.isEmpty(query.getBatchName())) {
            batch.setBatchName(query.getBatchName());
        } else {
            batch.setBatchName(batchName);
        }
        batch.setBeizhu(query.getBeizhu());
        batch.setOrgId(SecurityHolder.get().currentOrganise().getId());
        CommonService.bindCreateInfo(batch);
        commonMapper.insert(batch);
        baseArchiveBatchDetailService.createDetail(query, batch);
        return batch;
    }

    @Override
    public BatchCount count(String type, String batchId) {
        return baseBatchServiceMap.get(type).count(batchId);
    }

    @Override
    public Page selectPage(AbstractArchiveBatch batch, BatchCreateQuery query, Integer pageIndex, Integer pageSize) {
        BaseArchiveBatchDetailService service = baseBatchServiceMap.get(batch.getBatchType());
        return service.selectPage(batch, query, pageIndex, pageSize);
    }

    /**
     * 根据id 查询detail详细信息
     *
     * @param id
     * @return
     */
    @Override
    public AbstractBatchDetailEntity detail(String id, String type) {
        BaseArchiveBatchDetailService service = baseBatchServiceMap.get(type);
        return service.detail(id);
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        Map<String, BaseArchiveBatchDetailService> beans = getApplicationContext().getBeansOfType(BaseArchiveBatchDetailService.class);
        baseBatchServiceMap = Collections.synchronizedMap(new HashMap<>(beans.size()));
        beans.forEach((k, v) -> baseBatchServiceMap.put(v.getType(), v));
    }

    /**
     * 审核办结
     *
     * @param entity
     * @param finish
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doFinish(AbstractBatchDetailEntity entity, boolean finish) {
        Assert.isTrue(!StringUtils.isEmpty(entity.getBatchId()), "批次ID不能为空！");
        Assert.isTrue(!StringUtils.isEmpty(entity.getStatus()), "修改状态不能为空！");

        if (finish) {
            AbstractArchiveBatch archiveBatch = new AbstractArchiveBatch();
            archiveBatch.setId(entity.getBatchId());
            archiveBatch.setStatus(entity.getStatus());
            commonMapper.updateIgnoreNullById(archiveBatch);
            archiveBatch = selectById(archiveBatch.getId());
            BaseArchiveBatchDetailService service = baseBatchServiceMap.get(archiveBatch.getBatchType());
            //修改档案数据
            service.updateFormData(archiveBatch);
        }
        //查询批次下状态为0的任务
        ArchiveTask archiveTask = archiveTaskService.selectOne(SqlQuery.from(ArchiveTask.class).equal(ArchiveTaskInfo.BATCHID, entity.getBatchId()).equal(ArchiveTaskInfo.STATUS, '0'));
        //修改任务状态为已办结
        if (null != archiveTask && !StringUtils.isEmpty(archiveTask.getId())) {
            archiveTask.setStatus(entity.getStatus());
            archiveTaskService.updateById(archiveTask);
        }
    }

}
