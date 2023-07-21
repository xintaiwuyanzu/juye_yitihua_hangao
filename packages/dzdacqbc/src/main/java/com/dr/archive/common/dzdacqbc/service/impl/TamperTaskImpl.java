package com.dr.archive.common.dzdacqbc.service.impl;

import com.dr.archive.common.dzdacqbc.entity.*;
import com.dr.archive.common.dzdacqbc.service.AlarmService;
import com.dr.archive.common.dzdacqbc.service.EArchiveService;
import com.dr.archive.util.DateTimeUtils;
import com.dr.archive.util.UUIDUtils;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.file.FileInfoHandler;
import com.dr.framework.common.task.entity.TaskDefinition;
import com.dr.framework.common.task.entity.TaskInstance;
import com.dr.framework.common.task.service.TaskTypeProvider;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 篡改检测
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TamperTaskImpl implements TaskTypeProvider {

    final ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Autowired
    CommonMapper commonMapper;
    @Autowired
    EArchiveService eArchiveService;
    @Autowired
    protected FileInfoHandler fileInfoHandler;
    private int finalCount = 0;

    @Override
    public String type() {
        return "tamper_task";
    }

    @Override
    public String name() {
        return "篡改检测定时任务";
    }

    @Override
    public String executeTask(TaskDefinition taskDefinition, TaskInstance taskInstance, Person person, Map<String, String> map) {
        doDetect();
        return DateTimeUtils.longToDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm") + "篡改检测启动成功！";
    }


    public synchronized void doDetect() {
        List<EFileInfo> files = commonMapper.selectByQuery(SqlQuery.from(EFileInfo.class).join(
                EFileInfoInfo.ARCHIVEID, EArchiveInfo.ID));
        CountDownLatch latch = new CountDownLatch(files.size());
        for (EFileInfo efile : files) {
            executorService.execute(() -> {
                try {
                    String path = efile.getFilePath();
                    File file = new File(path);
                    if (file.exists()) {
                        String fileHash = this.fileInfoHandler.fileHash(new FileInputStream(file));
                        if (!fileHash.equals(efile.getDigest())) {
                            //向报警信息中添加报警
                            addAlarm(efile);
                            //添加档案被检测次数
                            if (StringUtils.hasText(efile.getArchiveId())) {
                                EArchive eArchive = eArchiveService.selectById(efile.getArchiveId());
                                if (eArchive != null) {
                                    eArchive.setLastBackupsDate(System.currentTimeMillis());
                                    eArchive.setTestingNum(eArchive.getTestingNum() + 1);
                                    eArchive.setArchiveStatus("1");//设为异常
                                    commonMapper.updateIgnoreNullById(eArchive);
                                }
                            }

                            finalCount++;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }
        try {
            //等待所有线程执行完毕
            latch.await();//主程序执行到await()函数会阻塞等待线程的执行，直到计数为0
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void addAlarm(EFileInfo efile) {
        CqbcAlarm cqbcAlarm = new CqbcAlarm();
        cqbcAlarm.setId(UUIDUtils.getUUID());
        cqbcAlarm.setCreateDate(System.currentTimeMillis());
        cqbcAlarm.setAlarmType(AlarmService.ALARM_TYPE_TAMPER);
        cqbcAlarm.setAlarmContent(efile.getFileName() + "被篡改");
        cqbcAlarm.setFileId(efile.getId());
        cqbcAlarm.setArchiveId(efile.getArchiveId());
        cqbcAlarm.setAlarmDate(System.currentTimeMillis());
        cqbcAlarm.setProcessState("0");
        commonMapper.insertIgnoreNull(cqbcAlarm);
    }

}
