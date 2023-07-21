package com.dr.archive.kufang.entityfiles.service;

import com.dr.archive.kufang.entityfiles.entity.ImpBatch;
import com.dr.framework.common.service.BaseService;

public interface ImpBatchService extends BaseService<ImpBatch> {

    ImpBatch addBatch(String archiveType, String quantity, String impId);

}
