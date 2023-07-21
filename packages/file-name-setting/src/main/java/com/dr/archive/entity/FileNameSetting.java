package com.dr.archive.entity;


import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.common.form.util.Constants;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

@Table(name = Constants.TABLE_PREFIX + "file_name_setting", module = Constants.MODULE_NAME, comment = "档案名称配置表")
public class FileNameSetting extends BaseStatusEntity<String> {
    @Column(comment = "表单id", length = 100)
    private String formDefinitionId;
    @Column(comment = "全宗号",length = 100)
    private String fondNumber;
    @Column(comment="门类代码",length = 100)
    private String archivalCategoryCode;
    @Column(comment="年度",length = 100)
    private String year_ar;
    @Column(comment = "保管期限",length = 100)
    private String retentionPeriod;
    @Column(comment = "顺序号",length = 100)
    private String orderNumber;
    @Column(comment = "最后顺序号",length = 100)
    private String endOrderNumber;
    @Column(comment = "连接符",length = 100)
    private String connectorOne;

    public String getFormDefinitionId() {
        return formDefinitionId;
    }

    public void setFormDefinitionId(String formDefinitionId) {
        this.formDefinitionId = formDefinitionId;
    }

    public String getFondNumber() {
        return fondNumber;
    }

    public void setFondNumber(String fondNumber) {
        this.fondNumber = fondNumber;
    }

    public String getArchivalCategoryCode() {
        return archivalCategoryCode;
    }

    public void setArchivalCategoryCode(String archivalCategoryCode) {
        this.archivalCategoryCode = archivalCategoryCode;
    }

    public String getYear_ar() {
        return year_ar;
    }

    public void setYear_ar(String year_ar) {
        this.year_ar = year_ar;
    }

    public String getRetentionPeriod() {
        return retentionPeriod;
    }

    public void setRetentionPeriod(String retentionPeriod) {
        this.retentionPeriod = retentionPeriod;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getConnectorOne() {
        return connectorOne;
    }

    public void setConnectorOne(String connectorOne) {
        this.connectorOne = connectorOne;
    }

    public String getEndOrderNumber() {
        return endOrderNumber;
    }

    public void setEndOrderNumber(String endOrderNumber) {
        this.endOrderNumber = endOrderNumber;
    }
}
