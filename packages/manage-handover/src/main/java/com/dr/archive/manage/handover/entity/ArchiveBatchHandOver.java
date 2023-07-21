package com.dr.archive.manage.handover.entity;

import com.dr.archive.batch.entity.AbstractArchiveBatch;
import com.dr.archive.util.Constants;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.ColumnType;
import com.dr.framework.core.orm.annotations.Table;

/**
 * 移交批次交接单据
 *
 * @author dr
 */
@Table(name = Constants.TABLE_PREFIX + "HAND_OVER_BATCH", comment = "档案到期移交批次", module = Constants.MODULE_NAME)
public class ArchiveBatchHandOver extends AbstractArchiveBatch {

    @Column(comment = "全宗Id")
    private String fondId;
    @Column(comment = "移交人名称")
    private String transferPersonName;
    @Column(comment = "移交单位id")
    private String sourceOrgId;
    @Column(comment = "移交单位名称")
    private String sourceOrgName;
    @Column(comment = "移交信息")
    private String transferMessage;

    @Column(comment = "接收人Id")
    private String tarPerId;
    @Column(comment = "接收人名称")
    private String tarPerName;

    @Column(comment = "接收单位名称")
    private String tarOrgId;

    @Column(comment = "接收单位名称")
    private String tarOrgName;

    @Column(name = "pSequence", comment = "载体起止顺序号", type = ColumnType.CLOB)
    private String sequence;

    @Column(comment = "移交库批次Id", type = ColumnType.CLOB)
    private String libIds;
    @Column(comment = "备注")
    private String other;

    @Column(comment = "延期目标时间", type = ColumnType.DATE)
    private long delayDay;


    public String getFondId() {
        return fondId;
    }

    public void setFondId(String fondId) {
        this.fondId = fondId;
    }

    public String getTransferPersonName() {
        return transferPersonName;
    }

    public void setTransferPersonName(String transferPersonName) {
        this.transferPersonName = transferPersonName;
    }

    public String getSourceOrgId() {
        return sourceOrgId;
    }

    public void setSourceOrgId(String sourceOrgId) {
        this.sourceOrgId = sourceOrgId;
    }

    public String getSourceOrgName() {
        return sourceOrgName;
    }

    public void setSourceOrgName(String sourceOrgName) {
        this.sourceOrgName = sourceOrgName;
    }

    public String getTransferMessage() {
        return transferMessage;
    }

    public void setTransferMessage(String transferMessage) {
        this.transferMessage = transferMessage;
    }

    public String getTarPerId() {
        return tarPerId;
    }

    public void setTarPerId(String tarPerId) {
        this.tarPerId = tarPerId;
    }

    public String getTarPerName() {
        return tarPerName;
    }

    public void setTarPerName(String tarPerName) {
        this.tarPerName = tarPerName;
    }

    public String getTarOrgId() {
        return tarOrgId;
    }

    public void setTarOrgId(String tarOrgId) {
        this.tarOrgId = tarOrgId;
    }

    public String getTarOrgName() {
        return tarOrgName;
    }

    public void setTarOrgName(String tarOrgName) {
        this.tarOrgName = tarOrgName;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getLibIds() {
        return libIds;
    }

    public void setLibIds(String libIds) {
        this.libIds = libIds;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public long getDelayDay() {
        return delayDay;
    }

    public void setDelayDay(long delayDay) {
        this.delayDay = delayDay;
    }
}
