package com.dr.archive.receive.online.service;

import com.dr.archive.batch.service.BaseArchiveBatchDetailService;
import com.dr.archive.receive.online.entity.ArchiveBatchDetailReceiveOnline;
import com.dr.archive.receive.online.entity.ArchiveBatchReceiveOnline;

/**
 * @author caor
 * @date 2021-08-28 12:49
 */
public interface ArchiveBatchDetailReceiveOnlineService extends BaseArchiveBatchDetailService<ArchiveBatchDetailReceiveOnline> {

    /**
     * 在线接收入库操作
     *
     * @param online
     */
    void filling(ArchiveBatchReceiveOnline online);

    long deleteDetail(String id);

}
