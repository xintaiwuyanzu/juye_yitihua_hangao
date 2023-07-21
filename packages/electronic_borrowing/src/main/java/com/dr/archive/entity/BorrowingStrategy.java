package com.dr.archive.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * @author lych
 * @date 2023/2/1 上午 10:43
 * @mood happy
 */
@Table(name = Constants.TABLE_PREFIX + "Borrowing_Strategy", comment = "电子借阅策略", module = Constants.MODULE_NAME)
public class BorrowingStrategy extends BaseStatusEntity<String> {

    @Column(comment = "机构id",length = 100)
    private String organiseId;

    @Column(comment = "机构名称",length = 100)
    private String organiseName;

    @Column(comment = "借阅名称",length = 100)
    private String strategyName;

    @Column(comment = "借阅期限", length = 100)
    private Long borrowingPeriod;

    /**
     * 0：否  1：是
     */
    @Column(comment = "是否打印",length = 100)
    private String printState;

    /**
     * 0：否  1：是
     */
    @Column(comment = "是否下载",length = 100)
    private String downloadState;



    /**
     * 0：否  1：是
     */
    @Column(comment = "是否带水印",length = 100)
    private String watermarkState;

    /**
     * 0：否  1：是
     */
    @Column(comment = "是否启用",length = 100)
    private String enableOrNot;
    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }

    public String getEnableOrNot() {
        return enableOrNot;
    }

    public void setEnableOrNot(String enableOrNot) {
        this.enableOrNot = enableOrNot;
    }

    public String getOrganiseId() {
        return organiseId;
    }

    public void setOrganiseId(String organiseId) {
        this.organiseId = organiseId;
    }

    public String getOrganiseName() {
        return organiseName;
    }

    public void setOrganiseName(String organiseName) {
        this.organiseName = organiseName;
    }

    public Long getBorrowingPeriod() {
        return borrowingPeriod;
    }

    public void setBorrowingPeriod(Long borrowingPeriod) {
        this.borrowingPeriod = borrowingPeriod;
    }

    public String getPrintState() {
        return printState;
    }

    public void setPrintState(String printState) {
        this.printState = printState;
    }

    public String getDownloadState() {
        return downloadState;
    }

    public void setDownloadState(String downloadState) {
        this.downloadState = downloadState;
    }

    public String getWatermarkState() {
        return watermarkState;
    }

    public void setWatermarkState(String watermarkState) {
        this.watermarkState = watermarkState;
    }
}
