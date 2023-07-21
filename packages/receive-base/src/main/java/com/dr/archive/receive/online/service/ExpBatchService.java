package com.dr.archive.receive.online.service;

import com.dr.archive.batch.query.BatchCreateQuery;
import com.dr.archive.receive.online.entity.ExpBatch;
import com.dr.framework.common.service.BaseService;
import com.dr.framework.core.organise.entity.Person;

/**
 * @author: yang
 * @create: 2022-08-04 10:32
 **/
public interface ExpBatchService extends BaseService<ExpBatch> {
    void newBatch(BatchCreateQuery query, Person person);
}
