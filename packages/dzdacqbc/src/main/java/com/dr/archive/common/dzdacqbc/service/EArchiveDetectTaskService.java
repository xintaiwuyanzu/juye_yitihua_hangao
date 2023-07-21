package com.dr.archive.common.dzdacqbc.service;

import com.dr.archive.common.dzdacqbc.entity.CqbcClassification;
import com.dr.archive.common.dzdacqbc.entity.EArchiveDetectTask;
import com.dr.framework.common.service.BaseService;

/**
 * 电子文件长期保存检测服务类
 *
 * @author dr
 */
public interface EArchiveDetectTaskService extends BaseService<EArchiveDetectTask> {
    /**
     * 检测线程池数量
     */
    int DETECT_POOL_SIZE = 1;
    /**
     * 每次检测数量的分页大小
     */
    int PAGE_SIZE = 5000;

    /**
     * 等待状态
     */
    String STATUS_WAITING = "10";
    /**
     * 正在检测
     */
    String STATUS_DETECT = "20";
    /**
     * 已经完成
     */
    String STATUS_DONE = "30";

    /**
     * 全部检测
     */
    String DETECT_TYPE_FULL = "full";
    /**
     * 抽样检测
     */
    String DETECT_TYPE_SAMP = "samp";

    /**
     * 检测详情成功状态
     */
    String DETAIL_STATUS_SUCCESS = "success";
    /**
     * 检测详情失败状态
     */
    String DETAIL_STATUS_FAIL = "fail";

    /**
     * 根据长期保存分类执行检测任务
     * 这里的检测可能是手动触发的
     * 所以该方法调用不做逻辑判断，直接加入检测队列
     *
     * @param cqbcClassification
     */
    void waitDetect(CqbcClassification cqbcClassification);

}
