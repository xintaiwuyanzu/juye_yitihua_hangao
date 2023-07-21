package com.dr.archive.fournaturescheck.entity;

import com.dr.archive.batch.entity.AbstractBatchDetailEntity;
import com.dr.archive.util.Constants;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.ColumnType;
import com.dr.framework.core.orm.annotations.Table;

/**
 * @Author: caor
 * @Date: 2022-09-02 16:36
 * @Description:
 */
@Table(name = Constants.TABLE_PREFIX + "FOUR_NATURE_RECORD", module = Constants.MODULE_NAME, comment = "四性检测记录表")
public class FourNatureRecord extends AbstractBatchDetailEntity {
    @Column(comment = "业务id")
    private String businessId;
    @Column(comment = "检测类型")
    private String testRecordType;
    @Column(comment = "检测编码")
    private String testCode;
    @Column(comment = "检测名称")
    private String testName;
    @Column(comment = "检测对象值")
    private String archive_Data;
    @Column(comment = "检测结果", type = ColumnType.CLOB)
    private String testResult;

    public String getArchive_Data() {
        return archive_Data;
    }

    public void setArchive_Data(String archive_Data) {
        this.archive_Data = archive_Data;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getTestRecordType() {
        return testRecordType;
    }

    public void setTestRecordType(String testRecordType) {
        this.testRecordType = testRecordType;
    }

    public String getTestCode() {
        return testCode;
    }

    public void setTestCode(String testCode) {
        this.testCode = testCode;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getTestResult() {
        return testResult;
    }

    public void setTestResult(String testResult) {
        this.testResult = testResult;
    }
}
