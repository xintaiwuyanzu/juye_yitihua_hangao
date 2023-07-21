package com.dr.archive.common.dzdacqbc.service.impl;

import com.dr.archive.common.dzdacqbc.entity.*;
import com.dr.archive.common.dzdacqbc.service.AlarmService;
import com.dr.archive.common.dzdacqbc.service.ESaveBackBatchDetailService;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;

/**
 * 电子文件预警
 *
 * @author dr
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AlarmServiceImpl extends DefaultBaseService<CqbcAlarm> implements AlarmService {

    @Autowired
    ESaveBackBatchDetailService eSaveBackBatchDetailService;

    @Override
    public long updateById(CqbcAlarm entity) {
        return commonMapper.updateIgnoreNullById(entity);
    }


    /**
     * 篡改检测恢复
     */
    @Override
    public ResultEntity earchiveRestore(String archiveId, String alarmId) {
        EArchive eArchive = commonMapper.selectOneByQuery(SqlQuery.from(EArchive.class).equal(EArchiveInfo.ID, archiveId));
        if (eArchive != null) {
            //查询未过期的备份记录
            List<ESaveBackBatchDetail> backList = commonMapper.selectByQuery(SqlQuery.from(ESaveBackBatchDetail.class)
                    .equal(ESaveBackBatchDetailInfo.ARCHIVEID, eArchive.getFormDataId()).equal(ESaveBackBatchDetailInfo.ISEXPIRE, "0")
                    .orderByDesc(ESaveBackBatchDetailInfo.CREATEDATE));
            if (backList.size() > 0) {
                ESaveBackBatchDetail detail = backList.get(0);
                String backPath = detail.getBackPath();
                try {
                    File fileB = new File(backPath);
                    if (!fileB.exists()) {
                        return ResultEntity.error("当前档案信息备份文件不存在！");
                    }
                    //恢复档案信息
                    eSaveBackBatchDetailService.dataRecovery(detail.getId(), "bj");
                    //更新报警信息
                    CqbcAlarm alarm = commonMapper.selectById(CqbcAlarm.class, alarmId);
                    if (alarm != null) {
                        alarm.setProcessState("1");
                        Person person = SecurityHolder.get().currentPerson();
                        alarm.setProcessUserId(person.getId());
                        alarm.setProcessUserName(person.getUserName());
                        alarm.setProcessDate(System.currentTimeMillis());
                        alarm.setProcessResult("已还原");
                        commonMapper.updateIgnoreNullById(alarm);
                        return ResultEntity.success("报警信息处理成功！");
                    }
                    return ResultEntity.error("当前档案没有压缩文件信息！");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                return ResultEntity.error("当前档案信息没有备份文件！");
            }
        }

        return ResultEntity.error("档案信息不存在");
    }
}
