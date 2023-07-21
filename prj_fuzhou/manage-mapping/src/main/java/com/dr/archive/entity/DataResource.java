package com.dr.archive.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * @author: yang
 * @create: 2022-06-10 17:16
 **/
@Table(name = Constants.TABLE_PREFIX + "Mapping_DataResource", module = Constants.MODULE_NAME, comment = "元数据")
public class DataResource extends BaseStatusEntity<String> {
    @Column(comment = "表单定义id")
    private String formId;
    @Column(comment = "元数据名称")
    private String formName;
    @Column(comment = "元数据数量")
    private long dataNum;
    @Column(comment = "编码")
    private String formCode;
    @Column(comment = "关系对象标记")
    private String markResult;

    public DataResource() {
    }

    public DataResource(String formId, String formName, long dataNum, String formCode, String markResult) {
        this.formId = formId;
        this.formName = formName;
        this.dataNum = dataNum;
        this.formCode = formCode;
        this.markResult = markResult;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public long getDataNum() {
        return dataNum;
    }

    public void setDataNum(long dataNum) {
        this.dataNum = dataNum;
    }

    public String getFormCode() {
        return formCode;
    }

    public void setFormCode(String formCode) {
        this.formCode = formCode;
    }

    public String getMarkResult() {
        return markResult;
    }

    public void setMarkResult(String markResult) {
        this.markResult = markResult;
    }

}
