package com.dr.archive.receive.offline.entity;

import com.dr.archive.receive.online.entity.AbstractArchiveReceiveDetail;
import com.dr.archive.util.Constants;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.ColumnType;
import com.dr.framework.core.orm.annotations.Table;

/**
 * 批次导入详情
 *
 * @author drarchive/receive/offline/record
 */
@Table(name = Constants.TABLE_PREFIX + "BATCH_RECEIVE_OFFLINE_DETAIL", comment = "离线导入详情", module = Constants.MODULE_NAME)
public class ArchiveBatchReceiveOfflineDetail extends AbstractArchiveReceiveDetail {
    @Column(comment = "挂接状态")
    private String hookStatus;
    @Column(comment = "挂接开始时间", type = ColumnType.DATE)
    private Long hookStartTime;
    @Column(comment = "挂接结束时间", type = ColumnType.DATE)
    private Long hookEndTime;
    //本次挂接上原文的数量
    @Column(comment = "挂接数量")
    private String hookNum;
    @Column(comment = "四性检测状态", length = 10)
    private String testStatus;
    @Column(comment = "具体执行了多少项检测")
    private long totalCount;
    @Column(comment = "成功通过数量")
    private long successCount;
    @Column(comment = "最后一次四性检测id")
    private String lastTestRecordId;

    public String getHookStatus() {
        return hookStatus;
    }

    public void setHookStatus(String hookStatus) {
        this.hookStatus = hookStatus;
    }

    public Long getHookStartTime() {
        return hookStartTime;
    }

    public void setHookStartTime(Long hookStartTime) {
        this.hookStartTime = hookStartTime;
    }

    public Long getHookEndTime() {
        return hookEndTime;
    }

    public void setHookEndTime(Long hookEndTime) {
        this.hookEndTime = hookEndTime;
    }

    public String getHookNum() {
        return hookNum;
    }

    public void setHookNum(String hookNum) {
        this.hookNum = hookNum;
    }

    public String getTestStatus() {
        return testStatus;
    }

    public void setTestStatus(String testStatus) {
        this.testStatus = testStatus;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public long getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(long successCount) {
        this.successCount = successCount;
    }

    public String getLastTestRecordId() {
        return lastTestRecordId;
    }

    public void setLastTestRecordId(String lastTestRecordId) {
        this.lastTestRecordId = lastTestRecordId;
    }
}
