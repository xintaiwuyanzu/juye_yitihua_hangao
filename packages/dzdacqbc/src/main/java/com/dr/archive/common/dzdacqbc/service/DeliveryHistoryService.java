package com.dr.archive.common.dzdacqbc.service;

import com.dr.archive.common.dzdacqbc.entity.EArchiveBatch;
import com.dr.framework.core.process.service.ProcessContext;

public interface DeliveryHistoryService {

    void insetDeliveryHistory(EArchiveBatch DeliveryBatch, ProcessContext context);
}
