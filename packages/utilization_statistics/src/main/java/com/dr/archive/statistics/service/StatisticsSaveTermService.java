package com.dr.archive.statistics.service;

import com.dr.archive.statistics.entity.StatisticsSaveTerm;
import com.dr.framework.common.service.BaseService;

import java.util.List;

/**
 * @Author: caor
 * @Date: 2022-06-29 11:24
 * @Description:
 */
public interface StatisticsSaveTermService extends BaseService {
    List<StatisticsSaveTerm> getStatisticsSaveTermList(StatisticsSaveTerm statisticsSaveTerm);

    long insertStatisticsSaveTerm(StatisticsSaveTerm statisticsSaveTerm);

    /**
     * 按照保管期限统计档案数量，todo 权限暂时没处理
     *
     * @return
     */
    List<StatisticsSaveTerm> countBySaveTerm();

    /**
     * 更新 按照保管期限统计档案数量，todo 权限暂时没处理
     *
     * @return
     */
    boolean updateCountBySaveTerm();
}
