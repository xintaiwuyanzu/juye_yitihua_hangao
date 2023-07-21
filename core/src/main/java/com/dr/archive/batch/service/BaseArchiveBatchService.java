package com.dr.archive.batch.service;

import com.dr.archive.batch.entity.AbstractArchiveBatch;
import com.dr.framework.common.service.BaseService;

/**
 * 批次抽象service父类
 *
 * @param <E>
 * @author dr
 */
public interface BaseArchiveBatchService<E extends AbstractArchiveBatch> extends BaseService<E> {
    /**
     * 重新计算批次里面所有详情统计数量
     *
     * @param id
     */
    default void computeTotal(String id) {
    }

}
