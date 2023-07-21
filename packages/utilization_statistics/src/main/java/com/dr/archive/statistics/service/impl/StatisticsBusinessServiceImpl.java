package com.dr.archive.statistics.service.impl;

import com.dr.archive.receive.online.entity.ArchiveBatchReceiveOnline;
import com.dr.archive.receive.online.entity.ArchiveBatchReceiveOnlineInfo;
import com.dr.archive.receive.online.service.ArchiveOnlineReceiveService;
import com.dr.archive.statistics.entity.StatisticsBusiness;
import com.dr.archive.statistics.service.StatisticsBusinessService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * @Author: caor
 * @Date: 2022-06-29 15:51
 * @Description:
 */
@Primary
@Service
public class StatisticsBusinessServiceImpl extends DefaultBaseService implements StatisticsBusinessService {
    @Autowired
    ArchiveOnlineReceiveService archiveOnlineReceiveService;

    //在线接收
    @Override
    public void updateCountByBusiness() {
        //查询所有数据
        long allNum = getNumOnline(null);
        //入库中 5
        long inProcessNum = getNumOnline("0");
        //入库完成 6
        long passNum = getNumOnline("6");
        StatisticsBusiness statisticsBusiness = new StatisticsBusiness();
        statisticsBusiness.setAllNum(allNum);
        statisticsBusiness.setInProcessNum(inProcessNum);
        statisticsBusiness.setPassNum(passNum);
        statisticsBusiness.setUnPassNum(allNum - inProcessNum - passNum);
        commonMapper.insert(statisticsBusiness);
    }

    /**
     * 根据不同状态查询数量
     *
     * @param status
     * @return TODO 暂时不考虑权限
     */
    long getNumOnline(String status) {
        return commonMapper.countByQuery(
                SqlQuery.from(ArchiveBatchReceiveOnline.class, false)
                        .column(ArchiveBatchReceiveOnlineInfo.DETAILNUM.sum())
                        .equal(ArchiveBatchReceiveOnlineInfo.STATUS, status)
        );
    }
    //离线接收
    //入库
    //鉴定
    //利用
    //指导
    //移交
    //整理（调整）
}
