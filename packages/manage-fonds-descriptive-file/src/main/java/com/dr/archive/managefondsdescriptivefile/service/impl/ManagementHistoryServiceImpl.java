package com.dr.archive.managefondsdescriptivefile.service.impl;

import com.dr.archive.managefondsdescriptivefile.entity.ManagementHistory;
import com.dr.archive.managefondsdescriptivefile.entity.ManagementHistoryInfo;
import com.dr.archive.managefondsdescriptivefile.service.ManagementHistoryService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: caor
 * @Date: 2022-02-23 20:53
 * @Description:
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ManagementHistoryServiceImpl extends DefaultBaseService<ManagementHistory> implements ManagementHistoryService {
    @Override
    public long insert(ManagementHistory entity) {
        entity.setCreatePersonName(SecurityHolder.get().currentPerson().getUserName());
        Person person = SecurityHolder.get().currentPerson();
        entity.setCreatePerson(person.getId());
        entity.setUpdatePerson(person.getId());
        entity.setCreateDate(System.currentTimeMillis());
        entity.setUpdateDate(System.currentTimeMillis());
        return super.insert(entity);
    }

    @Override
    public long deleteByBussinessId(String bussinessId) {
        return commonMapper.deleteByQuery(SqlQuery.from(ManagementHistory.class).equal(ManagementHistoryInfo.MANAGEMENTID, bussinessId));
    }
}
