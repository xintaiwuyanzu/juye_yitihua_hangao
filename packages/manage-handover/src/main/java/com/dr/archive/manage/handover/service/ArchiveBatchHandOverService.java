package com.dr.archive.manage.handover.service;

import com.dr.archive.batch.service.BaseArchiveBatchService;
import com.dr.archive.manage.handover.entity.ArchiveBatchHandOver;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.organise.entity.Person;

import java.util.List;

/**
 * 档案到期移交批次service
 *
 * @author dr
 */
public interface ArchiveBatchHandOverService extends BaseArchiveBatchService<ArchiveBatchHandOver> {
    /**
     * 批次类型移交
     */
    String BATCH_TYPE_HAND_OVER = "handOver";
    /**
     * 批次类型延期
     */
    String BATCH_TYPE_DELAY = "delay";
    /**
     * 编辑中
     */
    String STATUS_EDIT = "10";
    /**
     * 内部审核中
     */
    String STATUS_AUDIT_DAS = "20";
    /**
     * 审核不通过
     */
    String STATUS_NO_PASS_DAS = "21";
    /**
     * 档案馆待接收
     */
    String STATUS_RECEIVE_DAG = "25";
    /**
     * 档案馆审核中
     */
    String STATUS_AUDIT_DAG = "30";
    /**
     * 档案馆审核不通过
     */
    String STATUS_NO_PASS_DAG = "31";
    /**
     * 已办结
     */
    String STATUS_DONE = "40";

    /**
     * 添加批次
     *
     * @param person
     * @param organise
     * @param fondId
     * @param libIds
     * @param type
     * @return
     */
    ArchiveBatchHandOver newBatch(Person person, Organise organise, String fondId, String libIds, String type);

    /**
     * 档案馆接收或者不接受
     *
     * @param person
     * @param archiveBatchHandOver
     * @return
     */
    ArchiveBatchHandOver updateDag(Person person, ArchiveBatchHandOver archiveBatchHandOver);

    /**
     * 档案移交与接收总数
     */
    List<ArchiveBatchHandOver> total();

}
