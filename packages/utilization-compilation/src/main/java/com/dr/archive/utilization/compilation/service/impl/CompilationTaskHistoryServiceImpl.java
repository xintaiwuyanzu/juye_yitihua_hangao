package com.dr.archive.utilization.compilation.service.impl;

import com.dr.archive.utilization.compilation.entity.CompilationTaskHistory;
import com.dr.archive.utilization.compilation.entity.CompilationTaskHistoryInfo;
import com.dr.archive.utilization.compilation.service.CompilationTaskHistoryService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: caor
 * @Date: 2022-03-03 16:10
 * @Description:
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CompilationTaskHistoryServiceImpl extends DefaultBaseService<CompilationTaskHistory> implements CompilationTaskHistoryService {
    @Override
    public long insert(CompilationTaskHistory entity) {
        entity.setCreatePersonName(SecurityHolder.get().currentPerson().getUserName());
        Person person = SecurityHolder.get().currentPerson();
        entity.setCreatePerson(person.getId());
        entity.setUpdatePerson(person.getId());
        entity.setCreateDate(System.currentTimeMillis());
        entity.setUpdateDate(System.currentTimeMillis());
        return super.insert(entity);
    }

    @Override
    public long deleteByTaskId(String taskId) {
        return commonMapper.deleteByQuery(SqlQuery.from(CompilationTaskHistory.class).equal(CompilationTaskHistoryInfo.COMPILATIONTASKID, taskId));
    }
}