package com.dr.archive.examine.entity;

import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.ColumnType;
import com.dr.framework.core.orm.annotations.Table;
import com.dr.framework.util.Constants;


/**
 * 公共信息表
 */
@Table(name = Constants.COMMON_TABLE_PREFIX + "Public_Information", module = Constants.COMMON_MODULE_NAME, comment = "公共信息表")
public class PublicInformation extends BaseStatusEntity<String> {

    //检查通知
    public static final String INFOTYPE_CHECK = "check";
    //调整通知
    public static final String INFOTYPE_ADJUST = "adjust";


    public static final String STATUS_WEIDU = "0";//未读
    public static final String STATUS_YIDU = "1";//已读

    public static final String checkType_zfjc = "zfjcCheck";//未读
    public static final String checkType_ndjc = "ndjcCheck";//已读

    @Column(comment = "业务id")
    private String refId;

    @Column(comment = "业务类型")
    private String checkType;

    @Column(comment = "发送者id")
    private String sendId;

    @Column(comment = "发送者名称")
    private String sendName;


    @Column(comment = "发送者单位id")
    private String sendUnitId;

    @Column(comment = "发送者单位")
    private String sendUnitName;

    @Column(comment = "接收者id")
    private String toId;

    @Column(comment = "接收者名称")
    private String toName;

    @Column(comment = "接收者单位id")
    private String toUnitId;

    @Column(comment = "接收者单位")
    private String toUnitName;

    @Column(comment = "信息类型")
    private String infoType;

    @Column(comment = "信息内容", type = ColumnType.CLOB)
    private String infoContent;

    @Column(comment = "发送时间")
    private long sendTime;

    public String getCheckType() {
        return checkType;
    }

    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getSendId() {
        return sendId;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
    }

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }


    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getSendUnitId() {
        return sendUnitId;
    }

    public void setSendUnitId(String sendUnitId) {
        this.sendUnitId = sendUnitId;
    }

    public String getSendUnitName() {
        return sendUnitName;
    }

    public void setSendUnitName(String sendUnitName) {
        this.sendUnitName = sendUnitName;
    }

    public String getToUnitId() {
        return toUnitId;
    }

    public void setToUnitId(String toUnitId) {
        this.toUnitId = toUnitId;
    }

    public String getToUnitName() {
        return toUnitName;
    }

    public void setToUnitName(String toUnitName) {
        this.toUnitName = toUnitName;
    }

    public String getInfoType() {
        return infoType;
    }

    public void setInfoType(String infoType) {
        this.infoType = infoType;
    }

    public String getInfoContent() {
        return infoContent;
    }

    public void setInfoContent(String infoContent) {
        this.infoContent = infoContent;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }
}
