package com.dr.archive.common.dzdacqbc.service;

import com.dr.archive.common.dzdacqbc.entity.ESaveOffLine;
import com.dr.framework.common.service.BaseService;

public interface ESaveOffLineService extends BaseService<ESaveOffLine> {

    ESaveOffLine addBefore(ESaveOffLine entity);

    ESaveOffLine changeBefore(ESaveOffLine entity);

    long sendDataToClient(String offLineId);
}
