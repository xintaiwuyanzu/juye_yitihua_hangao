package com.dr.archive.common.dzdacqbc.service;

import com.dr.archive.common.dzdacqbc.entity.EArchive;
import com.dr.archive.common.dzdacqbc.entity.EArchiveBatch;
import com.dr.framework.common.service.BaseService;
import com.dr.framework.core.organise.entity.Person;

/**
 * 长期保存出库入库批次service
 *
 * @author dr
 */
public interface EArchiveBatchService extends BaseService<EArchiveBatch> {
    /**
     * 入库
     */
    String BATCH_TYPE_IN = "in";
    /**
     * 出库
     */
    String BATCH_TYPE_OUT = "out";

    EArchiveBatch addToBatch(EArchive eArchive, Person person);

    long checkSubmit(String deliveryId);

    long fastDelivery(String deliveryId);

    long updateNotNull(EArchiveBatch eArchiveBatch, String type);

    long addDownLoad(String batchId);

    EArchiveBatch insertEArchiveBatch(long num);
}
