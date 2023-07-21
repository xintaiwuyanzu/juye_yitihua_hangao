package com.dr.archive.manage.handover.service.impl;

import com.dr.archive.manage.handover.entity.HandOverConfig;
import com.dr.archive.manage.handover.entity.HandOverConfigInfo;
import com.dr.archive.manage.handover.service.ArchiveBatchHandOverLibService;
import com.dr.archive.manage.handover.service.HandOverConfigService;
import com.dr.framework.common.service.CommonService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Calendar;
import java.util.List;

/**
 * 到期移交配置信息
 *
 * @author dr
 */
@Service
public class HandOverConfigServiceImpl extends DefaultBaseService<HandOverConfig> implements HandOverConfigService {

    @Autowired
    ArchiveBatchHandOverLibService handOverService;

    /**
     * 获取指定机构本年度到期移交配置
     *
     * @param organiseId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public HandOverConfig loadThisYearConfig(String organiseId) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DATE, 0);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        SqlQuery<HandOverConfig> configSqlQuery = SqlQuery.from(HandOverConfig.class).equal(HandOverConfigInfo.ORGANISEID, organiseId);
        configSqlQuery.greaterThanEqual(HandOverConfigInfo.CREATEDATE, calendar.getTimeInMillis());
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
        configSqlQuery.lessThanEqual(HandOverConfigInfo.CREATEDATE, calendar.getTimeInMillis());

        List<HandOverConfig> configs = selectList(configSqlQuery);
        if (configs.size() == 1) {
            return configs.get(0);
        } else {
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HandOverConfig saveStart(HandOverConfig handOverConfig, boolean start, String organiseId) {
        handOverConfig.setStatus(start ? STATUS_RUNNING : STATUS_STOP);
        if (StringUtils.hasText(handOverConfig.getId()) && exists(handOverConfig.getId())) {
            CommonService.bindCreateInfo(handOverConfig);
            //更新操作
            commonMapper.updateIgnoreNullById(handOverConfig);
        } else {
            CommonService.bindCreateInfo(handOverConfig);
            //新增数据
            Assert.isTrue(loadThisYearConfig(organiseId) == null, "同一年度只能添加一配置");
            handOverConfig.setOrganiseId(organiseId);
            insert(handOverConfig);
        }
        if (start) {
            SecurityHolder securityHolder = SecurityHolder.get();
            handOverService.start(securityHolder);
        }
        return handOverConfig;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean toggleConfig(String id) {
        HandOverConfig config = selectById(id);
        switch (config.getStatus()) {
            case STATUS_STOP:

                config.setStatus(STATUS_RUNNING);
                updateById(config);
                SecurityHolder securityHolder = SecurityHolder.get();
                handOverService.start(securityHolder);
                break;
            case STATUS_RUNNING:
                config.setStatus(STATUS_STOP);
                updateById(config);
                break;
            case STATUS_END:
                throw new RuntimeException("任务已办结");
            default:
                return false;
        }
        return true;
    }
}
