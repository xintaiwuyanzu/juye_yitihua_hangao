package com.dr.archive.appraisal.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

@Table(name = Constants.TABLE_PREFIX + "manage_appraisal_form_record", module = Constants.MODULE_NAME, comment = "档案扫描表单记录")
public class AppraisalFormRecord extends BaseStatusEntity<String> {

    @Column(comment = "表单Id")
    private String formId;

    @Column(comment = "表单总数")
    private String total;

    @Column(comment = "已扫描数量")
    private String finishCount;


    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getFinishCount() {
        return finishCount;
    }

    public void setFinishCount(String finishCount) {
        this.finishCount = finishCount;
    }
}
