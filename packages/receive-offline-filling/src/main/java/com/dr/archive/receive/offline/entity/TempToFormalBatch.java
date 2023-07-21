package com.dr.archive.receive.offline.entity;

import com.dr.archive.batch.entity.AbstractArchiveBatch;
import com.dr.archive.util.Constants;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * 入库批次表
 */
@Table(name = Constants.TABLE_PREFIX + "tempToFormalBatch", module = Constants.MODULE_NAME, comment = "临时库到正式库批次表")
public class TempToFormalBatch extends AbstractArchiveBatch {

    @Column(comment = "操作人id", length = 100, order = 10)
    private String personId;
    @Column(comment = "操作人名称", length = 100, order = 10)
    private String personName;

    @Column(comment = "四性检测状态", length = 10)
    private String testStatus;
    @Column(comment = "四性检测成功数量")
    private int testTrueNum;

    @Column(comment = "备注")
    private String remarks;

    @Column(comment = "移交入库id",length = 100,order = 10)
    private String registerWarehousingID;

    public String getRegisterWarehousingID() {
        return registerWarehousingID;
    }

    public void setRegisterWarehousingID(String registerWarehousingID) {
        this.registerWarehousingID = registerWarehousingID;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getTestStatus() {
        return testStatus;
    }

    public void setTestStatus(String testStatus) {
        this.testStatus = testStatus;
    }

    public int getTestTrueNum() {
        return testTrueNum;
    }

    public void setTestTrueNum(int testTrueNum) {
        this.testTrueNum = testTrueNum;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
