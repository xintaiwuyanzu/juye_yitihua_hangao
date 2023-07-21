package com.dr.archive.fuzhou.approve.service;

import com.dr.archive.fuzhou.approve.bo.ArchiveReceiveBo;

/**
 * 档案归档相关接口
 * <p>
 * 业务系统归档数据到档案室
 *
 * @author caor
 * @date 2021-08-17 17:05
 */
public interface ReceiptService {
    //数据来源类型  行政审批服务在线接收
    String SOURCE_TYPE_ONLINE = "ONLINE";

    /**
     * 根据归档参数执行归档操作
     *
     * @param receiveBo
     */
    void receiveArchive(ArchiveReceiveBo receiveBo);

}
