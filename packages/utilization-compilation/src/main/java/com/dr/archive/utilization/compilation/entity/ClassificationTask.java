package com.dr.archive.utilization.compilation.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

@Table(name = Constants.TABLE_PREFIX + "CLASSIFICATION_TASK", module = Constants.MODULE_NAME, comment = "编研分类")
public class ClassificationTask extends BaseStatusEntity<String> {

    @Column(comment = "编研分类名称" ,length=100)
    private String classIfCationName;
    @Column(comment = "编研分类编码" ,length=100)
    private String classIfCationTypeCode;
    @Column(comment = "说明" ,length=500)
    private String explainText;

    public String getClassIfCationName() {
        return classIfCationName;
    }

    public void setClassIfCationName(String classIfCationName) {
        this.classIfCationName = classIfCationName;
    }

    public String getClassIfCationTypeCode() {
        return classIfCationTypeCode;
    }

    public void setClassIfCationTypeCode(String classIfCationTypeCode) {
        this.classIfCationTypeCode = classIfCationTypeCode;
    }

    public String getExplainText() {
        return explainText;
    }

    public void setExplainText(String explainText) {
        this.explainText = explainText;
    }
}
