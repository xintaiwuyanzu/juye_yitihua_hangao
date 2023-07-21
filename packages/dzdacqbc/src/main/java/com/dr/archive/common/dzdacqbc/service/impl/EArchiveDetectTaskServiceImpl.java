package com.dr.archive.common.dzdacqbc.service.impl;

import com.dr.archive.common.dzdacqbc.entity.*;
import com.dr.archive.common.dzdacqbc.service.*;
import com.dr.framework.common.service.CommonService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.common.task.entity.TaskDefinition;
import com.dr.framework.common.task.entity.TaskInstance;
import com.dr.framework.common.task.service.TaskTypeProvider;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 检测任务相关service
 *
 * @author dr
 */
@Service
public class EArchiveDetectTaskServiceImpl extends DefaultBaseService<EArchiveDetectTask> implements EArchiveDetectTaskService, TaskTypeProvider, InitializingBean {
    ThreadPoolExecutor executorService;
    @Autowired
    ClassificationService classificationService;
    @Autowired
    EArchiveService eArchiveService;
    @Autowired
    ESaveTacticsService eSaveTacticsService;
    /**
     * 当前正在运行的任务
     */
    private EArchiveDetectTask currentTask;
    private boolean suspend = false;

    /**
     * 启动检测任务
     */
    protected synchronized void startDetect() {
        //如果检测队列的数量不存在 或者当前的currenTask中的检测数量和已检测数量相等
        if (currentTask == null || currentTask.getCurrentCount() >= currentTask.getTargetTotal()) {
            //如果当前的任务为空，尝试从数据库中查询一条任，有可能是上次异常关闭未完成的任务
            List<EArchiveDetectTask> tasks = commonMapper.selectLimitByQuery(
                    SqlQuery.from(EArchiveDetectTask.class)
                            .notEqual(EArchiveDetectTaskInfo.STATUS, STATUS_DONE)
                            .notEqual(EArchiveDetectTaskInfo.TARGETTOTAL, 0)
                            .orderByDesc(EArchiveDetectTaskInfo.UPDATEDATE)
                    , 0, 1);
            if (!tasks.isEmpty()) {
                currentTask = tasks.get(0);
            }
        }
        if (currentTask != null) {
            while (true) {
                // 当前的currenTask中的检测数量和已检测数量相等
                if (currentTask.getCurrentCount() >= currentTask.getTargetTotal()) {
                    break;
                }
                //TODO 这里需要睡几秒，可能获取的数据是不精确的
                //应该没问题，就算不准确，正好能让电脑休息一会
                int runningCount = executorService.getActiveCount();
                if (runningCount >= DETECT_POOL_SIZE) {
                    break;
                }
                SecurityHolder securityHolder = SecurityHolder.get();

                executorService.execute(() -> {
                    SecurityHolder.set(securityHolder);
                    doDetect();
                });
            }
        }
    }

    /**
     * 真正执行检测操作
     */
    private void doDetect() {
        // 根据classifId、档案最后检测时间查询档案
        List<EArchive> archiveToDetect = commonMapper.selectLimitByQuery(
                SqlQuery.from(EArchive.class).isNotNull(EArchiveInfo.SPACEID)
                        .equal(EArchiveInfo.CLASSIFICATIONID, currentTask.getClassifiId())
                        .lessThan(EArchiveInfo.LASTTESTDATE, currentTask.getStartDate())
                        .or()
                        .equal(EArchiveInfo.LASTTESTDATE, 0)
                        .orderByDesc(EArchiveInfo.LASTTESTDATE),
                0,
                PAGE_SIZE
        );
        //System.out.println("ea"+archiveToDetect.size());
        // 执行检测档案任务
        DetectContext context = new DetectContext();
        for (EArchive eArchive : archiveToDetect) {
            doDetectArchive(eArchive, context);
        }

        //保存检测数据
        for (CqbcAlarm cqbcAlarm : context.cqbcAlarms) {
            CommonService.bindCreateInfo(cqbcAlarm);
            commonMapper.insert(cqbcAlarm);
        }
        for (EArchiveDetectTaskDetail detail : context.detectTaskDetails) {
            CommonService.bindCreateInfo(detail);
            commonMapper.insert(detail);
        }
        //更改当前检测任务中已完成的数量
        currentTask.setCurrentCount(currentTask.getCurrentCount() + archiveToDetect.size());
        // 判断数量是否相等并改变状态
        if (currentTask.getCurrentCount() >= currentTask.getTargetTotal()) {
            currentTask.setStatus("30");
        }

        if (System.currentTimeMillis() >= goodsTime()) {
            currentTask.setStatus("20");
            suspend = true;
        }
        //更改数据库
        updateById(currentTask);
        //开始检测下一页数据
        startDetect();
    }

    private long goodsTime() {
        Date date = new Date();
        date.setDate(date.getDate());
        date.setHours(8);
        date.setMinutes(0);
        date.setSeconds(0);
        Long goodsTime = date.getTime();
        return goodsTime;
    }

    /**
     * 真正执行单个电子文件的检测
     *
     * @param eArchive
     * @param context
     */
    private void doDetectArchive(EArchive eArchive, DetectContext context) {
        //检测文件夹是否存在
        EArchiveDetectTaskDetail detail = eArchiveService.detectEArchive(eArchive);
        detail.setTaskId(currentTask.getId());
        if (DETAIL_STATUS_FAIL.equals(detail.getStatus())) {
            CqbcAlarm cqbcAlarm = new CqbcAlarm();

            cqbcAlarm.setAlarmType(AlarmService.ALARM_TYPE_ARCHIVE);
            cqbcAlarm.setAlarmContent(detail.getDescription());
            cqbcAlarm.setFileId(eArchive.getId());
            cqbcAlarm.setArchiveId(eArchive.getId());
            cqbcAlarm.setAlarmDate(System.currentTimeMillis());
            cqbcAlarm.setProcessState("0");
            cqbcAlarm.setClassificationId(eArchive.getClassificationId());
            cqbcAlarm.setFondCode(eArchive.getFondCode());
            context.cqbcAlarms.add(cqbcAlarm);
        }
        ESaveSpaces eSaveSpaces = commonMapper.selectById(ESaveSpaces.class, eArchive.getSpaceId());
        eSaveSpaces.setLatestDetectDate(System.currentTimeMillis());
        context.detectTaskDetails.add(detail);
    }

    @Override
    public String executeTask(TaskDefinition taskDefinition, TaskInstance taskInstance, Person person, Map<String, String> map) {
        System.out.println("任务开始");
        //先计算需要执行的检测任务
        computeDetectTask();
        //在执行检测
        startDetect();
        return "启动成功";
    }

    /**
     * 计算待检测任务
     */
    private synchronized void computeDetectTask() {
        //寻找所有需要检测的分类
        List<CqbcClassification> cqbcClassifications = classificationService.selectList(SqlQuery.from(CqbcClassification.class));
        for (CqbcClassification cqbcClassification : cqbcClassifications) {
            //创建检测批次
            waitDetect(cqbcClassification);
        }
    }

    @Override
    public void afterPropertiesSet() {
        ThreadFactory springThreadFactory = new CustomizableThreadFactory("earchive-detect-pool-");

        executorService = new ThreadPoolExecutor(
                DETECT_POOL_SIZE,
                DETECT_POOL_SIZE,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                springThreadFactory
        );
        startDetect();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void waitDetect(CqbcClassification cqbcClassification) {
        //条件查询
        long waitingCount = count(
                SqlQuery.from(EArchiveDetectTask.class)
                        .equal(EArchiveDetectTaskInfo.CLASSIFIID, cqbcClassification.getId())
                        .notEqual(EArchiveDetectTaskInfo.STATUS, STATUS_DONE)
        );
        //如果该项任务正在执行或者已经在队列了，不需要处理
        if (waitingCount == 0) {
            //该项任务没有执行
            List<EArchiveDetectTask> detectTasks = commonMapper.selectLimitByQuery(
                    SqlQuery.from(EArchiveDetectTask.class)
                            .equal(EArchiveDetectTaskInfo.CLASSIFIID, cqbcClassification.getId())
                            .equal(EArchiveDetectTaskInfo.STATUS, STATUS_DONE),
                    0, 1);
            EArchiveDetectTask latestDetect = detectTasks.isEmpty() ? null : detectTasks.get(0);
            if (latestDetect == null) {
                //如果之前从来没有检测过，直接添加全部检测队列
                createDetectTask(cqbcClassification, DETECT_TYPE_FULL);
            } else {
                //之前检测过，需要计算
                long endDate = latestDetect.getEndDate();
                //从上次检测完成时间开始计算
                ESaveTactics saveTactics = eSaveTacticsService.selectById(cqbcClassification.getTacticsId());

                //全量检测毫秒时常
                long fullDetectMil = (Long.parseLong(saveTactics.getGlobalDetection()) * 24 * 60 * 60 * 1000);
                //抽样检测毫秒时常
                long sample = (Long.parseLong(saveTactics.getSampleProportion()) * 24 * 60 * 60 * 1000);

                if (DETECT_TYPE_FULL.equals(latestDetect.getDetectType())) {
                    //如果上次检测的类型是全量检测
                    //本次执行抽样检测的逻辑
                    long nextTargetDate = endDate + sample;
                    if (System.currentTimeMillis() >= nextTargetDate) {
                        //如果当前日期已经超出了下次检测时间，开启抽样检测
                        createDetectTask(cqbcClassification, DETECT_TYPE_SAMP);
                    }
                } else {
                    //上次是抽样检测，最开始肯定有全量检测，查询上次全量检测记录
                    EArchiveDetectTask latestFullDetect = commonMapper.selectLimitByQuery(
                            SqlQuery.from(EArchiveDetectTask.class)
                                    .equal(EArchiveDetectTaskInfo.CLASSIFIID, cqbcClassification.getId())
                                    .equal(EArchiveDetectTaskInfo.STATUS, STATUS_DONE)
                                    .equal(EArchiveDetectTaskInfo.DETECTTYPE, DETECT_TYPE_FULL)
                            , 0, 1).get(0);
                    //如果到了全量检测的周期，本次执行全量检测
                    if (System.currentTimeMillis() >= latestFullDetect.getEndDate() + fullDetectMil) {
                        createDetectTask(cqbcClassification, DETECT_TYPE_FULL);
                    } else if (System.currentTimeMillis() >= endDate + sample) {
                        //不需要执行全量检测，在检测是否需要执行简单检测
                        createDetectTask(cqbcClassification, DETECT_TYPE_SAMP);
                    }
                }
            }
        }
    }

    /**
     * 根据检测分类创建全局检测任务
     *
     * @param classification
     * @param detectType
     */
    private void createDetectTask(CqbcClassification classification, String detectType) {
        boolean isFull = DETECT_TYPE_FULL.equalsIgnoreCase(detectType);
        EArchiveDetectTask task = new EArchiveDetectTask();
        task.setClassifiId(classification.getId());
        task.setClassifiName(classification.getClassificationName());
        task.setStatus(STATUS_WAITING);

        task.setTaskName(String.join(
                "-",
                classification.getClassificationName(),
                (isFull ? "全量检测" : "抽样检测"),
                DateFormatUtils.ISO_8601_EXTENDED_DATE_FORMAT.format(System.currentTimeMillis())
        ));
        task.setDetectType(detectType);
        //先计算出来总数
        long totalCount = eArchiveService.countByClass(classification.getId());
        if (isFull) {
            task.setDetectProportion(10000);
            //如果是全量检测，目标检测数量
            task.setTargetTotal(totalCount);
        } else {
            //如果是抽样检测，则需要算出来比例
            ESaveTactics saveTactics = eSaveTacticsService.selectById(classification.getTacticsId());
            double proportion = Double.parseDouble(saveTactics.getSampleProportion());
            task.setDetectProportion((long) (proportion * 100));
            task.setTargetTotal((long) (totalCount * proportion / 100));
        }
        task.setCurrentCount(0);
        task.setStartDate(System.currentTimeMillis());
        classification.setLastTestTime(System.currentTimeMillis());
        commonMapper.updateIgnoreNullById(classification);
        // 判断一下如果总长度为零就算了
        if (task.getTargetTotal() != 0) {
            insert(task);
        }

    }

    /**
     * 检测上下文
     */
    static class DetectContext {
        List<EArchiveDetectTaskDetail> detectTaskDetails = new ArrayList<>();
        List<CqbcAlarm> cqbcAlarms = new ArrayList<>();
    }

    @Override
    public String type() {
        return "EArchive_Detect";
    }

    @Override
    public String name() {
        return "电子文件长期保存检测";
    }
}
