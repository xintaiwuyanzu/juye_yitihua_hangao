package com.dr.archive.manage.handover.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * 自定义设定预警时限和移交时限
 *
 * @author cuiyj
 */
@Table(name = Constants.TABLE_PREFIX + "HAND_OVER_CONFIG", module = Constants.MODULE_NAME, comment = "移交自定义配置")
public class HandOverConfig extends BaseStatusEntity<String> {

    @Column(name = "warningTime", comment = "预警时间", length = 100)
    private long warningTime;
    @Column(name = "handoverTime", comment = "移交时限", length = 100)
    private long handoverTime;
    @Column(comment = "机构", length = 50, order = 11)
    private String organiseId;

    @Column(comment = "到期数量")
    private long allNum;
    @Column(comment = "移交数量")
    private long sendNum;


    public String getOrganiseId() {
        return organiseId;
    }

    public void setOrganiseId(String organiseId) {
        this.organiseId = organiseId;
    }

    public long getWarningTime() {
        return warningTime;
    }

    public void setWarningTime(long warningTime) {
        this.warningTime = warningTime;
    }

    public long getHandoverTime() {
        return handoverTime;
    }

    public void setHandoverTime(long handoverTime) {
        this.handoverTime = handoverTime;
    }

    public long getAllNum() {
        return allNum;
    }

    public void setAllNum(long allNum) {
        this.allNum = allNum;
    }

    public long getSendNum() {
        return sendNum;
    }

    public void setSendNum(long sendNum) {
        this.sendNum = sendNum;
    }
}
