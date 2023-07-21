package com.dr.archive.model.to;

import com.dr.framework.common.entity.BaseStatusEntity;

/**
 * 描述：档案字段检测结果表
 *
 * @author tuzl
 * @date 2020/7/10 15:03
 */
public class FieldResultTo extends BaseStatusEntity<String> {

    private String archiveId;
    private String archiveName;
    private String fieldName;
    private String fieldCode;
    private Integer testResult;
    private String testOpinion;

    public String getArchiveId() {
        return archiveId;
    }

    public String getFieldCode() {
        return fieldCode;
    }

    public String getArchiveName() {
        return archiveName;
    }

    public void setArchiveName(String archiveName) {
        this.archiveName = archiveName;
    }

    public void setFieldCode(String fieldCode) {
        this.fieldCode = fieldCode;
    }

    public void setArchiveId(String archiveId) {
        this.archiveId = archiveId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Integer getTestResult() {
        return testResult;
    }

    public void setTestResult(Integer testResult) {
        this.testResult = testResult;
    }

    public String getTestOpinion() {
        return testOpinion;
    }

    public void setTestOpinion(String testOpinion) {
        this.testOpinion = testOpinion;
    }
}
