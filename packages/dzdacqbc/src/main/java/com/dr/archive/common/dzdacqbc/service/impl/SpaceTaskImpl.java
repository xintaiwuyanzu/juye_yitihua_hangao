package com.dr.archive.common.dzdacqbc.service.impl;

import com.dr.archive.common.dzdacqbc.entity.*;
import com.dr.archive.common.dzdacqbc.service.AlarmService;
import com.dr.archive.util.DateTimeUtils;
import com.dr.archive.util.UUIDUtils;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.file.FileInfoHandler;
import com.dr.framework.common.task.entity.TaskDefinition;
import com.dr.framework.common.task.entity.TaskInstance;
import com.dr.framework.common.task.service.TaskTypeProvider;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.apache.xmlbeans.xml.stream.Space;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 存储空间检测
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SpaceTaskImpl implements TaskTypeProvider {

    final ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Autowired
    CommonMapper commonMapper;
    @Autowired
    protected FileInfoHandler fileInfoHandler;

    private static final String DECIMAL_FORMAT_PATTERN = "######0.0";

    @Override
    public String type() {
        return "space_task";
    }

    @Override
    public String name() {
        return "存储空间定时检测任务";
    }

    @Override
    public String executeTask(TaskDefinition taskDefinition, TaskInstance taskInstance, Person person, Map<String, String> map) {
        doDetect();
        return DateTimeUtils.longToDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm") + "存储空间检测启动成功！";
    }


    public synchronized void doDetect() {
        List<ESaveSpaces> list = commonMapper.selectByQuery(SqlQuery.from(ESaveSpaces.class).equal(ESaveSpacesInfo.ISDEFAULT,"true"));

        CountDownLatch latch = new CountDownLatch(list.size());
        for (ESaveSpaces spaces : list) {
            executorService.execute(() -> {
                try {
                    DecimalFormat df = new DecimalFormat(DECIMAL_FORMAT_PATTERN);
                    File file = new File(spaces.getCatalogue());
                    if (!file.isDirectory()) {
                        return;
                    }

                    double totalSpace = (double) file.getTotalSpace() / 1024 / 1024 / 1024;
                    double usableSpace = (double) file.getUsableSpace() / 1024 / 1024 / 1024;
                    double percent = Double.parseDouble(df.format((totalSpace - usableSpace) / totalSpace * 100));
                    if(percent > 80){//使用空间大于80的时候报警
                        //向报警信息中添加报警
                        CqbcAlarm cqbcAlarm = new CqbcAlarm();
                        cqbcAlarm.setId(UUIDUtils.getUUID());
                        cqbcAlarm.setCreateDate(System.currentTimeMillis());
                        cqbcAlarm.setAlarmType(AlarmService.ALARM_TYPE_DISK);
                        cqbcAlarm.setAlarmContent(spaces.getSpaceName() + "的空间已使用" + String.valueOf(percent) + "%，请及时处理！");
                        cqbcAlarm.setFileId(spaces.getId());//存储空间id
                        cqbcAlarm.setAlarmDate(System.currentTimeMillis());
                        cqbcAlarm.setProcessState("0");
                        commonMapper.insertIgnoreNull(cqbcAlarm);
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

}
