package com.dr.archive.manage.handover.service;

import com.dr.archive.batch.service.BaseArchiveBatchService;
import com.dr.archive.manage.handover.entity.ArchiveBatchHandOverLib;
import com.dr.framework.core.security.SecurityHolder;

/**
 * 档案到期移交库service
 *
 * @author dr
 */
public interface ArchiveBatchHandOverLibService extends BaseArchiveBatchService<ArchiveBatchHandOverLib> {
    /*
     * 检测中
     */
    String STATUS_CHECK = "9";
    /**
     * 待移交
     */
    String STATUS_WAITING = "10";
    /**
     * 移交审核中
     */
    String STATUS_HAND_AUDIT = "11";

    /**
     * 移交审核通过
     */
    String STATUS_HAND_DONE = "12";
    /**
     * 延期审核中
     */
    String STATUS_DELAY_AUDIT = "21";
    /**
     * 已延期
     */
    String STATUS_DELAY_DONE = "22";

    /**
     * 启动到期鉴定
     */
    void start(SecurityHolder securityHolder);

    /**
     * 批量更新指定批次的Id
     *
     * @param ids
     * @param status
     * @return
     */
    long updateStatus(String ids, String status);

    /**
     * 计算指定批次的 载体起止顺序号 概要信息
     *
     * @param id
     * @return
     */
    String computeSumInfo(String id);


}
