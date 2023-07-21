package com.dr.archive.detection.entity;

import com.dr.archive.batch.entity.AbstractBatchDetailEntity;
import com.dr.archive.util.Constants;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * 四性检测结果记录表
 * <p>
 * 与具体的一条档案相关联，一条档案数据可以有多次四性检测记录
 *
 * @author caor
 * @date 2020-11-15 11:38
 */
@Table(name = Constants.TABLE_PREFIX + "TEST_RECORD", module = Constants.MODULE_NAME, comment = "四性检测记录表")
public class TestRecord extends AbstractBatchDetailEntity {

    @Column(comment = "业务id", length = 50)
    private String businessId;
    @Column(comment = "检测环节", length = 100)
    private String linkCode;

    @Column(comment = "具体执行了多少项检测")
    private long totalCount;
    @Column(comment = "成功通过数量")
    private long successCount;


    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getLinkCode() {
        return linkCode;
    }

    public void setLinkCode(String linkCode) {
        this.linkCode = linkCode;
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
}
