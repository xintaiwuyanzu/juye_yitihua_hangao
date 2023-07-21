package com.dr.archive.batch.service;

import com.dr.archive.batch.entity.AbstractArchiveBatch;
import com.dr.archive.batch.entity.AbstractBatchDetailEntity;
import com.dr.archive.batch.query.BatchCreateQuery;
import com.dr.archive.batch.vo.BatchCount;
import com.dr.framework.common.page.Page;
import com.dr.framework.common.service.BaseService;
import com.dr.framework.core.organise.entity.Person;

/**
 * 档案批次service
 *
 * @author dr
 */
public interface ArchiveBatchService extends BaseService<AbstractArchiveBatch> {
    /**
     * 根据查询条件创建一个批次记录
     *
     * @param query
     * @param person
     * @return
     */
    AbstractArchiveBatch newBatch(BatchCreateQuery query, Person person);

    /**
     * 统计批次信息
     *
     * @param type
     * @param batchId
     * @return
     */
    BatchCount count(String type, String batchId);

    /**
     * 更新批次状态
     *
     * @param entity
     * @return
     */
    @Override
    long updateById(AbstractArchiveBatch entity);

    /**
     * 更新一条详情数据的状态
     *
     * @param type
     * @param detailId
     * @param status
     * @param advice
     */
    AbstractBatchDetailEntity changeStatus(String type, String detailId, String status, String advice, String targetValue);

    /**
     * 根据type查询不同batchDetailPage
     *
     * @param batch
     * @param query
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Deprecated
    Page selectPage(AbstractArchiveBatch batch, BatchCreateQuery query, Integer pageIndex, Integer pageSize);

    /**
     * 根据detailDd查询详细信息
     *
     * @param id
     * @return
     */
    @Deprecated
    AbstractBatchDetailEntity detail(String id, String type);

    /**
     * 审核办结
     *
     * @param entity
     * @param finish
     */
    void doFinish(AbstractBatchDetailEntity entity, boolean finish);

}
