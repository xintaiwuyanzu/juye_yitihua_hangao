package com.dr.archive.kufang.entityfiles.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

@Table(name = Constants.TABLE_PREFIX + "XIUFU", module = Constants.MODULE_NAME, comment = "档案修复")
public class XiuFu extends BaseStatusEntity<String> {
    @Column(comment = "名称", length = 50)
    private String fileName;
    @Column(comment = "编号", length = 50)
    private String fileNum;
    @Column(comment = "记录人", length = 50)
    private String personName;
    @Column(comment = "库房名称", length = 200)
    private String kuFangMingCheng;
    @Column(comment = "维修原因", length = 50)
    private String maintenanceReasons;
    @Column(comment = "维修记录", length = 100)
    private String maintenanceRecord;
    @Column(comment = "送修人", length = 100)
    private String sendPeople ;
    @Column(comment = "维修人", length = 100)
    private String maintenancePeople ;
    @Column(comment = "备注", length = 50)
    private String mark;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileNum() {
        return fileNum;
    }

    public void setFileNum(String fileNum) {
        this.fileNum = fileNum;
    }

    public String getMaintenanceReasons() {
        return maintenanceReasons;
    }

    public void setMaintenanceReasons(String maintenanceReasons) {
        this.maintenanceReasons = maintenanceReasons;
    }

    public String getMaintenanceRecord() {
        return maintenanceRecord;
    }

    public void setMaintenanceRecord(String maintenanceRecord) {
        this.maintenanceRecord = maintenanceRecord;
    }

    public String getSendPeople() {
        return sendPeople;
    }

    public void setSendPeople(String sendPeople) {
        this.sendPeople = sendPeople;
    }

    public String getMaintenancePeople() {
        return maintenancePeople;
    }

    public void setMaintenancePeople(String maintenancePeople) {
        this.maintenancePeople = maintenancePeople;
    }


    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getKuFangMingCheng() {
        return kuFangMingCheng;
    }

    public void setKuFangMingCheng(String kuFangMingCheng) {
        this.kuFangMingCheng = kuFangMingCheng;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }
}
