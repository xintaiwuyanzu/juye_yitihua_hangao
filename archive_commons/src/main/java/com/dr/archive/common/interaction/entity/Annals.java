package com.dr.archive.common.interaction.entity;

import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.ColumnType;
import com.dr.framework.core.orm.annotations.Table;
import com.dr.framework.util.Constants;

import java.util.List;

@Table(name = Constants.COMMON_TABLE_PREFIX + "ANNALS", module = Constants.COMMON_MODULE_NAME, comment = "年报管理")
public class Annals extends BaseStatusEntity<String> {

    @Column(comment = "组织机构代码ID", length = 200)
    private String organiseId;
    @Column(comment = "组织机构代码", length = 200)
    private String organiseCode;
    @Column(comment = "组织机构名称", length = 200)
    private String organiseName;

    @Column(comment = "年报名称", length = 200)
    private String annalsName;
    @Column(comment = "填表人", length = 200)
    private String annalsPerson;

    @Column(comment = "表号", length = 200)
    private String annalsTable;
    @Column(comment = "制定机关", length = 200)
    private String annalsNact;

    @Column(comment = "批准机关", length = 200)
    private String annalsRatify;
    @Column(comment = "批准文号", length = 200)
    private String annalsRatifyNumber;

    @Column(comment = "有效期至", length = 200)
    private String annalsValidDate;
    @Column(comment = "内容", type = ColumnType.CLOB)
    private String annalsData;

    private List<String> atta;

    private String unitCode;
    public String getOrganiseName() {
        return organiseName;
    }

    public void setOrganiseName(String organiseName) {
        this.organiseName = organiseName;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getAnnalsTable() {
        return annalsTable;
    }

    public void setAnnalsTable(String annalsTable) {
        this.annalsTable = annalsTable;
    }

    public String getAnnalsNact() {
        return annalsNact;
    }

    public void setAnnalsNact(String annalsNact) {
        this.annalsNact = annalsNact;
    }

    public String getAnnalsRatify() {
        return annalsRatify;
    }

    public void setAnnalsRatify(String annalsRatify) {
        this.annalsRatify = annalsRatify;
    }

    public String getAnnalsRatifyNumber() {
        return annalsRatifyNumber;
    }

    public void setAnnalsRatifyNumber(String annalsRatifyNumber) {
        this.annalsRatifyNumber = annalsRatifyNumber;
    }

    public String getAnnalsValidDate() {
        return annalsValidDate;
    }

    public void setAnnalsValidDate(String annalsValidDate) {
        this.annalsValidDate = annalsValidDate;
    }

    public String getAnnalsName() {
        return annalsName;
    }

    public void setAnnalsName(String annalsName) {
        this.annalsName = annalsName;
    }

    public String getAnnalsPerson() {
        return annalsPerson;
    }

    public void setAnnalsPerson(String annalsPerson) {
        this.annalsPerson = annalsPerson;
    }

    public String getAnnalsData() {
        return annalsData;
    }

    public void setAnnalsData(String annalsData) {
        this.annalsData = annalsData;
    }

    public List<String> getAtta() {
        return atta;
    }

    public void setAtta(List<String> atta) {
        this.atta = atta;
    }

    public String getOrganiseId() {
        return organiseId;
    }

    public void setOrganiseId(String organiseId) {
        this.organiseId = organiseId;
    }

    public String getOrganiseCode() {
        return organiseCode;
    }

    public void setOrganiseCode(String organiseCode) {
        this.organiseCode = organiseCode;
    }
}
