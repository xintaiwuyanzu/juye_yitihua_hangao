package com.dr.archive.common.dzdacqbc.service.impl;

import com.dr.archive.common.dzdacqbc.entity.EArchiveBatch;
import com.dr.archive.common.dzdacqbc.entity.DeliveryHistory;
import com.dr.archive.common.dzdacqbc.service.DeliveryHistoryService;
import com.dr.archive.util.UUIDUtils;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.process.service.ProcessContext;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class DeliveryHistoryServiceImpl extends DefaultBaseService<DeliveryHistory> implements DeliveryHistoryService {
    @Autowired
    CommonMapper commonMapper;

    public void insetDeliveryHistory(EArchiveBatch DeliveryBatch, ProcessContext context) {
        if (DeliveryBatch != null && context != null) {
            DeliveryHistory deliveryHistory = new DeliveryHistory();
            deliveryHistory.setDeliveryId(DeliveryBatch.getId());
            deliveryHistory.setOperationOpinion(context.getBusinessParams().get("comment").toString());
            deliveryHistory.setSender(DeliveryBatch.getDeliveryPerson());
            deliveryHistory.setSenderTime(DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
            deliveryHistory.setReceiver(context.getPerson().getUserName());
            deliveryHistory.setReceiverTime(DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
            deliveryHistory.setId(UUIDUtils.getUUID());
            commonMapper.insert(deliveryHistory);
        }
    }
}
