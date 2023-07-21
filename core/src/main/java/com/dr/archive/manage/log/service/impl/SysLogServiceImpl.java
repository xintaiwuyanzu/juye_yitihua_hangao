package com.dr.archive.manage.log.service.impl;

import com.dr.archive.manage.form.service.ArchiveDataContext;
import com.dr.archive.manage.form.service.ArchiveDataManager;
import com.dr.archive.manage.form.service.ArchiveDataPlugin;
import com.dr.archive.manage.log.entity.SysLogEntity;
import com.dr.archive.manage.log.entity.SysLogEntityInfo;
import com.dr.archive.manage.log.service.SysLogService;
import com.dr.archive.model.entity.ArchiveEntity;
import com.dr.archive.util.DateTimeUtils;
import com.dr.archive.util.UUIDUtils;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import com.dr.framework.core.security.bo.ClientInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author caor
 * @date 2020/12/22 20:02
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysLogServiceImpl extends DefaultBaseService<SysLogEntity> implements SysLogService, ArchiveDataPlugin {
    @Autowired
    ArchiveDataManager dataManager;

    @Override
    public void insertFormDataLog(FormData formData, String type) {
        SysLogEntity sysLogEntity = new SysLogEntity();
        //获取用户ip地址
        sysLogEntity.setCreateDate(DateTimeUtils.getMillis());
        sysLogEntity.setOperation(type + "题名为：【" + formData.getString(ArchiveEntity.COLUMN_TITLE) + "】，\"档号\"为：【" + formData.getString(ArchiveEntity.COLUMN_ARCHIVE_CODE) + "】，\"全宗号\"为：【" + formData.getString(ArchiveEntity.COLUMN_FOND_CODE) + "】");
        insertLog(sysLogEntity);

    }

    @Override
    public void insertLog(SysLogEntity sysLogEntity) {
        sysLogEntity.setId(UUIDUtils.getUUID());
        sysLogEntity.setOrganiseId(SecurityHolder.get().currentOrganise().getId());
        sysLogEntity.setCreateDate(DateTimeUtils.getMillis());
        sysLogEntity.setOperationAudit("正常");
        sysLogEntity.setAuditDescription("--");
        if (DateTimeUtils.getH24() > 20 || DateTimeUtils.getH24() < 8) {
            sysLogEntity.setOperationAudit("警告");
            sysLogEntity.setAuditDescription("非正常时间段操作");
        }
        SecurityHolder securityHolder = SecurityHolder.get();
        if (null != securityHolder) {
            ClientInfo clientInfo = securityHolder.getClientInfo();
            if (clientInfo != null) {
                sysLogEntity.setIp(clientInfo.getRemoteIp());
            }
            Person person = securityHolder.currentPerson();
            if (person != null) {
                //用户名
                sysLogEntity.setCreatePerson(person.getUserCode());
            }
            sysLogEntity.setCreateDate(DateTimeUtils.getMillis());
            super.insert(sysLogEntity);
        }
    }


    @Override
    public void deleteEntity(String ids) {
        String[] split = ids.split(",");
        for (String s : split) {
            deleteById(s);
        }
    }

    /**
     * 数据插入添加日志
     *
     * @param data
     * @param context
     * @return
     */
    @Override
    public FormData afterInsert(FormData data, ArchiveDataContext context) {
        insertFormDataLog(data, "添加");
        return ArchiveDataPlugin.super.afterInsert(data, context);
    }

    /**
     * 数据更新添加日志
     *
     * @param data
     * @param context
     * @return
     */
    @Override
    public FormData afterUpdate(FormData data, ArchiveDataContext context) {
        insertFormDataLog(data, "修改");
        return ArchiveDataPlugin.super.afterUpdate(data, context);
    }

    /**
     * 添加档案删除操作日志
     *
     * @param archiveIds
     * @param context
     * @return
     */
    @Override
    public Long beforeDelete(String archiveIds, ArchiveDataContext context) {
        String[] ids = archiveIds.split(",");
        for (String id : ids) {
            //添加操作日志
            insertFormDataLog(dataManager.selectOneFormData(context.getFormModel().getId(), id), "删除");
        }
        return ArchiveDataPlugin.super.beforeDelete(archiveIds, context);
    }

    @Override
    public Map<String, Long> operation() {

//        List<Map> count = commonMapper.selectByQuery(SqlQuery.from(SysLogEntity.class, false)
//                .column(SysLogEntityInfo.OPERATION, Column.count(SysLogEntityInfo.ID, "count"))
//                .groupBy(SysLogEntityInfo.OPERATION).setReturnClass(Map.class));
        //根据存在的名词查询数量
        Organise organise = SecurityHolder.get().currentOrganise();
        List<SysLogEntity> addTo;
        List<SysLogEntity> query;
        List<SysLogEntity> revise;
        List<SysLogEntity> delete;
        if (organise.getId().equals("root")){
            addTo = commonMapper.selectByQuery(SqlQuery.from(SysLogEntity.class).like(SysLogEntityInfo.OPERATION, "添加"));
            query = commonMapper.selectByQuery(SqlQuery.from(SysLogEntity.class).like(SysLogEntityInfo.OPERATION, "查询"));
            revise = commonMapper.selectByQuery(SqlQuery.from(SysLogEntity.class).like(SysLogEntityInfo.OPERATION, "修改"));
            delete = commonMapper.selectByQuery(SqlQuery.from(SysLogEntity.class).like(SysLogEntityInfo.OPERATION, "删除"));
        }else {
            addTo = commonMapper.selectByQuery(SqlQuery.from(SysLogEntity.class).equal(SysLogEntityInfo.ORGANISEID,SecurityHolder.get().currentOrganise().getId()).like(SysLogEntityInfo.OPERATION, "添加"));
            query = commonMapper.selectByQuery(SqlQuery.from(SysLogEntity.class).equal(SysLogEntityInfo.ORGANISEID,SecurityHolder.get().currentOrganise().getId()).like(SysLogEntityInfo.OPERATION, "查询"));
            revise = commonMapper.selectByQuery(SqlQuery.from(SysLogEntity.class).equal(SysLogEntityInfo.ORGANISEID,SecurityHolder.get().currentOrganise().getId()).like(SysLogEntityInfo.OPERATION, "修改"));
            delete = commonMapper.selectByQuery(SqlQuery.from(SysLogEntity.class).equal(SysLogEntityInfo.ORGANISEID,SecurityHolder.get().currentOrganise().getId()).like(SysLogEntityInfo.OPERATION, "删除"));
        }
        Map<String, Long> integerMap = new HashMap<>();
        integerMap.put("添加", (long) addTo.size());
        integerMap.put("查询", (long) query.size());
        integerMap.put("修改", (long) revise.size());
        integerMap.put("删除", (long) delete.size());


        return integerMap;
    }
    @Override
    public Map<String, Long> operationToday() {
        long now = System.currentTimeMillis();
        long daySecond = 60 * 60 * 24*1000;
        long dayTime = now - (now + 8 * 3600) % daySecond;
        //根据存在的名词查询数量
        Organise organise = SecurityHolder.get().currentOrganise();
        List<SysLogEntity> addTo;
        List<SysLogEntity> query;
        List<SysLogEntity> revise;
        List<SysLogEntity> delete;
        if (organise.getId().equals("root")){
             addTo = commonMapper.selectByQuery(SqlQuery.from(SysLogEntity.class).like(SysLogEntityInfo.OPERATION, "添加").greaterThanEqual(SysLogEntityInfo.CREATEDATE,dayTime));
             query = commonMapper.selectByQuery(SqlQuery.from(SysLogEntity.class).like(SysLogEntityInfo.OPERATION, "查询").greaterThanEqual(SysLogEntityInfo.CREATEDATE,dayTime));
             revise = commonMapper.selectByQuery(SqlQuery.from(SysLogEntity.class).like(SysLogEntityInfo.OPERATION, "修改").greaterThanEqual(SysLogEntityInfo.CREATEDATE,dayTime));
             delete = commonMapper.selectByQuery(SqlQuery.from(SysLogEntity.class).like(SysLogEntityInfo.OPERATION, "删除").greaterThanEqual(SysLogEntityInfo.CREATEDATE,dayTime));
        }else {
             addTo = commonMapper.selectByQuery(SqlQuery.from(SysLogEntity.class).equal(SysLogEntityInfo.ORGANISEID,SecurityHolder.get().currentOrganise().getId()).like(SysLogEntityInfo.OPERATION, "添加").greaterThanEqual(SysLogEntityInfo.CREATEDATE,dayTime));
             query = commonMapper.selectByQuery(SqlQuery.from(SysLogEntity.class).equal(SysLogEntityInfo.ORGANISEID,SecurityHolder.get().currentOrganise().getId()).like(SysLogEntityInfo.OPERATION, "查询").greaterThanEqual(SysLogEntityInfo.CREATEDATE,dayTime));
             revise = commonMapper.selectByQuery(SqlQuery.from(SysLogEntity.class).equal(SysLogEntityInfo.ORGANISEID,SecurityHolder.get().currentOrganise().getId()).like(SysLogEntityInfo.OPERATION, "修改").greaterThanEqual(SysLogEntityInfo.CREATEDATE,dayTime));
             delete = commonMapper.selectByQuery(SqlQuery.from(SysLogEntity.class).equal(SysLogEntityInfo.ORGANISEID,SecurityHolder.get().currentOrganise().getId()).like(SysLogEntityInfo.OPERATION, "删除").greaterThanEqual(SysLogEntityInfo.CREATEDATE,dayTime));
        }
        Map<String, Long> integerMap = new HashMap<>();
        integerMap.put("添加", (long) addTo.size());
        integerMap.put("查询", (long) query.size());
        integerMap.put("修改", (long) revise.size());
        integerMap.put("删除", (long) delete.size());


        return integerMap;
    }
}
