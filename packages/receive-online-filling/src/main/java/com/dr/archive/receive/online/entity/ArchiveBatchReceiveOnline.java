package com.dr.archive.receive.online.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * 在线接收批次
 *
 * @author dr
 */
@Table(name = Constants.TABLE_PREFIX + "BATCH_RECEIVE_ONLINE", comment = "在线接收批次", module = Constants.MODULE_NAME)
public class ArchiveBatchReceiveOnline extends AbstractArchiveReceiveBatch {
    @Column(comment = "全宗号", length = 200)
    private String fondCode;
    @Column(comment = "归档信息包总字节数", length = 500)
    private String sendSize;
    @Column(comment = "系统编号")
    private String systemNum;
    @Column(comment = "系统名称", length = 500)
    private String systemName;

    @Column(comment = "四性检测状态", length = 100)
    private String testStatus;
    @Column(comment = "四性检测消息", length = 500)
    private String testMessage;

    @Column(comment = "入库状态")
    private String fillingStatus;


    public String getFondCode() {
        return fondCode;
    }

    public void setFondCode(String fondCode) {
        this.fondCode = fondCode;
    }

    public String getSendSize() {
        return sendSize;
    }

    public void setSendSize(String sendSize) {
        this.sendSize = sendSize;
    }

    public String getSystemNum() {
        return systemNum;
    }

    public void setSystemNum(String systemNum) {
        this.systemNum = systemNum;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getTestStatus() {
        return testStatus;
    }

    public void setTestStatus(String testStatus) {
        this.testStatus = testStatus;
    }

    public String getTestMessage() {
        return testMessage;
    }

    public void setTestMessage(String testMessage) {
        this.testMessage = testMessage;
    }

    public String getFillingStatus() {
        return fillingStatus;
    }

    public void setFillingStatus(String fillingStatus) {
        this.fillingStatus = fillingStatus;
    }
}
