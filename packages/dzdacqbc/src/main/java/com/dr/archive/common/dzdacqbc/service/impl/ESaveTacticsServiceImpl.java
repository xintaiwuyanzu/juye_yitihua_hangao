package com.dr.archive.common.dzdacqbc.service.impl;

import com.dr.archive.common.dzdacqbc.entity.ESaveTactics;
import com.dr.archive.common.dzdacqbc.entity.ESaveTacticsInfo;
import com.dr.archive.common.dzdacqbc.service.ESaveTacticsService;
import com.dr.framework.common.service.CacheAbleService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ESaveTacticsServiceImpl extends CacheAbleService<ESaveTactics> implements ESaveTacticsService {

    /**
     * 设置默认
     *
     * @param tactics
     */
    @Override
    public void updateDefault(ESaveTactics tactics) {
        SqlQuery sqlQuery = SqlQuery.from(ESaveTactics.class).equal(ESaveTacticsInfo.ISDEFAULT, "1");
        Object o = commonMapper.selectOneByQuery(sqlQuery);
        if (ObjectUtils.isNotEmpty(o)) {
            ESaveTactics ESaveTactics = (ESaveTactics) o;
            ESaveTactics.setIsDefault("0");
            //查询出现在为默认的，修改为非默认
            commonMapper.updateById(ESaveTactics);
        }
        //将待修改的改为默认
        commonMapper.updateById(tactics);
    }

    @Override
    public boolean checkIsStart() {
        return commonMapper.countByQuery(SqlQuery.from(ESaveTactics.class).equal(ESaveTacticsInfo.ISSTART, "1")) == 1;
    }

    @Override
    protected String getCacheName() {
        return "ESaveTactics";
    }
}
