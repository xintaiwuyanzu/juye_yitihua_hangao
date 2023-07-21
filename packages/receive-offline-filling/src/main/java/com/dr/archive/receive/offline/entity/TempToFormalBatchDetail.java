package com.dr.archive.receive.offline.entity;

import com.dr.archive.batch.entity.AbstractBatchDetailEntity;
import com.dr.archive.util.Constants;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * 入库批次详情表
 */
@Table(name = Constants.TABLE_PREFIX + "tempToFormalBatchDetail", module = Constants.MODULE_NAME, comment = "临时库到正式库批次详情表")
public class TempToFormalBatchDetail extends AbstractBatchDetailEntity {
    @Column(comment = "四性检测状态", length = 10)
    private String testStatus;
    @Column(comment = "具体执行了多少项检测")
    private long totalCount;
    @Column(comment = "成功通过数量")
    private long successCount;
    @Column(comment = "最后一次四性检测id")
    private String lastTestRecordId;

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
