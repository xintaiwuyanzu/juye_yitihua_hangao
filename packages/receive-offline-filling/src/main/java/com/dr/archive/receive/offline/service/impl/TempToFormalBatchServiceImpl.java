package com.dr.archive.receive.offline.service.impl;

import com.dr.archive.detection.service.TestRecordService;
import com.dr.archive.manage.form.entity.RegisterWarehousing;
import com.dr.archive.manage.form.service.RegisterWarehousingDetailsService;
import com.dr.archive.manage.form.service.RegisterWarehousingService;
import com.dr.archive.model.query.ArchiveDataQuery;
import com.dr.archive.receive.offline.entity.TempToFormalBatch;
import com.dr.archive.receive.offline.entity.TempToFormalBatchDetail;
import com.dr.archive.receive.offline.entity.TempToFormalBatchDetailInfo;
import com.dr.archive.receive.offline.service.TempToFormalBatchDetailService;
import com.dr.archive.receive.offline.service.TempToFormalBatchService;
import com.dr.archive.util.SecurityHolderUtil;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.entity.StatusEntity;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.organise.service.OrganisePersonService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * 临时库到正式库前的四性检测批次
 */
@Service
public class TempToFormalBatchServiceImpl extends DefaultBaseService<TempToFormalBatch> implements TempToFormalBatchService {


    @Autowired
    TempToFormalBatchDetailService tempToFormalBatchDetailService;
    @Autowired
    RegisterWarehousingService registerWarehousingService;
    @Autowired
    CommonMapper commonMapper;
    @Autowired
    RegisterWarehousingDetailsService registerWarehousingDetailsService;
    /**
     * 创建批次信息
     */
    @Async
    @Override
    public Map newBatch(ArchiveDataQuery query, String status, String archiveType) {
        //创建入库批次
        TempToFormalBatch batch = new TempToFormalBatch();
        //初始化数据
        Person person = SecurityHolder.get().currentPerson();
        batch.setPersonId(person.getId());
        batch.setPersonName(person.getUserName());
        batch.setBatchName(person.getUserName() + "提交的从临时库到正式库");
        batch.setStartDate(System.currentTimeMillis());
        batch.setStatus(StatusEntity.STATUS_DISABLE_STR);
        batch.setOrgId(SecurityHolder.get().currentOrganise().getId());
        batch.setTestStatus(StatusEntity.STATUS_UNKNOW_STR);
        insert(batch);

        //移交入库批次记录
        RegisterWarehousing insert = registerWarehousingService.insertReg(status);
        if (insert != null) {
            batch.setRegisterWarehousingID(insert.getId());
        }
        //创建批次详情
        batch = tempToFormalBatchDetailService.createDetail2(query, batch, status, archiveType);
        Map map = new HashMap();
        map.put("detailNum", batch.getDetailNum());
        map.put("testTrueNum", batch.getTestTrueNum());
        return map;
    }

    /**
     * 获得入库检测的接收报告
     */
    @Override
    public Map<String, Long> getReport(String batchId) {
        Map<String, Long> integerMap = new HashMap<>();
        long detailNum = commonMapper.countByQuery(SqlQuery.from(TempToFormalBatchDetail.class)
                .equal(TempToFormalBatchDetailInfo.BATCHID, batchId));
        long fourSexTestSucessNum = commonMapper.countByQuery(SqlQuery.from(TempToFormalBatchDetail.class)
                .equal(TempToFormalBatchDetailInfo.BATCHID, batchId)
                .equal(TempToFormalBatchDetailInfo.TESTSTATUS, "1"));
        integerMap.put("detailNum", detailNum);
        integerMap.put("fourSexTestSucessNum", fourSexTestSucessNum);
        integerMap.put("fourSexTestFalseNum", detailNum - fourSexTestSucessNum);
        return integerMap;
    }

}
