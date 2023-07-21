package com.dr.archive.common.dzdacqbc.service;

import com.dr.archive.common.dzdacqbc.entity.ESaveTactics;
import com.dr.framework.common.service.BaseService;

/**
 * 检测策略
 *
 * @author dr
 */
public interface ESaveTacticsService extends BaseService<ESaveTactics> {
    void updateDefault(ESaveTactics tactics);

    boolean checkIsStart();
}
