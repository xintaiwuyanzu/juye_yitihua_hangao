package com.dr.archive.utilization.compilation.entity;

import com.dr.archive.model.entity.AbstractArchiveEntity;
import com.dr.archive.util.Constants;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * @Author: caor
 * @Date: 2022-08-20 16:52
 * @Description:
 */
@Table(name = Constants.TABLE_PREFIX + "COMPILATION_TASK_DETAIL", module = Constants.MODULE_NAME, comment = "编研任务详情表")
public class CompilationTaskDetail extends AbstractArchiveEntity {
    public static final String BUSINESS_TYPE_ARCHIVE = "archive";
    public static final String BUSINESS_TYPE_FONDFILE = "fondFile";
    public static final String BUSINESS_TYPE_PERSONFILE = "personFile";
    @Column(comment = "批次id")
    private String batchId;
    @Column(comment = "数据id")
    private String formDataId;
    @Column(comment = "表单id")
    private String formDefinitionId;
    /**
     * archive:档案  fondFile:全宗卷 personFile:个人资料（网盘）
     */
    @Column(comment = "业务类型")
    private String businessType;

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getFormDataId() {
        return formDataId;
    }

    public void setFormDataId(String formDataId) {
        this.formDataId = formDataId;
    }

    public String getFormDefinitionId() {
        return formDefinitionId;
    }

    public void setFormDefinitionId(String formDefinitionId) {
        this.formDefinitionId = formDefinitionId;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }
}
