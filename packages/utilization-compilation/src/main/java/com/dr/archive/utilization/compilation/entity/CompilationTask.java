package com.dr.archive.utilization.compilation.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.ColumnType;
import com.dr.framework.core.orm.annotations.Table;

/**
 * 状态说明
 * status:
 * 0：待提交
 * 1：审核中
 * 2：通过
 * 3：未通过
 */
@Table(name = Constants.TABLE_PREFIX + "COMPILATION_TASK", module = Constants.MODULE_NAME, comment = "编研任务")
public class CompilationTask extends BaseStatusEntity<String> {

    @Column(comment = "编研主题", length = 500)
    private String compilationTitle;
    @Column(comment = "摘要", type = ColumnType.CLOB)
    private String compilationAbstract;
    @Column(comment = "全宗id", length = 50)
    private String fondId;
    @Column(comment = "全宗名称", length = 200)
    private String fondName;
    @Column(comment = "机构id", length = 50)
    private String organId;
    @Column(comment = "机构名称", length = 200)
    private String organName;
    @Column(comment = "机构编码", length = 200)
    private String organCode;
    /**
     * 编研类型:专题汇编、大事记、组织机构沿革、基础数字汇集
     */
    @Column(comment = "编研类型", length = 140)
    private String taskType;
    @Column(comment = "描述", length = 500)
    private String compilationDescribe;
    @Column(comment = "模板id", length = 50)
    private String templateId;
    @Column(comment = "内容", type = ColumnType.CLOB)
    private String compilationContent;
    @Column(comment = "发布时间", length = 200)
    private long publishDate;
    @Column(comment = "发布状态", length = 10)
    private String publishStatus;
    @Column(comment = "编研人", length = 100)
    private String creatPersonName;
    @Column(comment = "是否推送过", length = 100)
    private String pushed; //默认是0  推送过之后设置为 push

    public String getPushed() {
        return pushed;
    }

    public void setPushed(String pushed) {
        this.pushed = pushed;
    }

    public String getCompilationTitle() {
        return compilationTitle;
    }

    public void setCompilationTitle(String compilationTitle) {
        this.compilationTitle = compilationTitle;
    }

    public String getFondId() {
        return fondId;
    }

    public void setFondId(String fondId) {
        this.fondId = fondId;
    }

    public String getFondName() {
        return fondName;
    }

    public void setFondName(String fondName) {
        this.fondName = fondName;
    }

    public String getOrganId() {
        return organId;
    }

    public void setOrganId(String organId) {
        this.organId = organId;
    }

    public String getOrganName() {
        return organName;
    }

    public void setOrganName(String organName) {
        this.organName = organName;
    }

    public String getOrganCode() {
        return organCode;
    }

    public void setOrganCode(String organCode) {
        this.organCode = organCode;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getCompilationDescribe() {
        return compilationDescribe;
    }

    public void setCompilationDescribe(String compilationDescribe) {
        this.compilationDescribe = compilationDescribe;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getCompilationContent() {
        return compilationContent;
    }

    public void setCompilationContent(String compilationContent) {
        this.compilationContent = compilationContent;
    }

    public long getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(long publishDate) {
        this.publishDate = publishDate;
    }

    public String getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(String publishStatus) {
        this.publishStatus = publishStatus;
    }

    public String getCompilationAbstract() {
        return compilationAbstract;
    }

    public void setCompilationAbstract(String compilationAbstract) {
        this.compilationAbstract = compilationAbstract;
    }

    public String getCreatPersonName() {
        return creatPersonName;
    }

    public void setCreatPersonName(String creatPersonName) {
        this.creatPersonName = creatPersonName;
    }
}
