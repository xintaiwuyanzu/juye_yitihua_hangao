package com.dr.archive.manage.form.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

@Table(name = Constants.TABLE_PREFIX + "Register_Warehousing", module = Constants.MODULE_NAME, comment = "登记出入库记录表")
public class RegisterWarehousing extends BaseStatusEntity<String> {
    @Column(comment = "记录名称", length = 100,order = 10)
    private String record_name;

    @Column(comment = "操作人名称", length = 100,order = 10)
    private String person_name;

    @Column(comment = "状态", length = 100,order = 11)
    private String stateType;

    @Column(comment = "数量", length = 100,order = 12)
    private Integer quantity;

    @Column(comment = "原因", length = 100,order = 13)
    private String reason;

    @Column(comment = "年度", length = 100,order = 14)
    private String ND;

    @Column(comment = "备注", length = 100,order = 15)
    private String remarks;

    public RegisterWarehousing(){}

    public RegisterWarehousing(String id,String createPerson,Long createDate,Long updateDate, String person_name){
        this.setId(id);
        this.setCreatePerson(createPerson);
        this.setCreateDate(createDate);
        this.setUpdateDate(updateDate);
        this.setPerson_name(person_name);
    }

    public String getRecord_name() {
        return record_name;
    }

    public void setRecord_name(String record_name) {
        this.record_name = record_name;
    }

    public String getStateType() {
        return stateType;
    }

    public void setStateType(String stateType) {
        this.stateType = stateType;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getND() {
        return ND;
    }

    public void setND(String ND) {
        this.ND = ND;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getPerson_name() {
        return person_name;
    }

    public void setPerson_name(String person_name) {
        this.person_name = person_name;
    }
}
