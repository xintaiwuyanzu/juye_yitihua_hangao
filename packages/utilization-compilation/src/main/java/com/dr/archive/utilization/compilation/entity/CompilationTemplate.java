package com.dr.archive.utilization.compilation.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.ColumnType;
import com.dr.framework.core.orm.annotations.Table;

@Table(name = Constants.TABLE_PREFIX + "COMPILATION_TEMPLATE", module = Constants.MODULE_NAME, comment = "编研模板")
public class CompilationTemplate extends BaseStatusEntity<String> {

    @Column(comment = "模板分类名称", length = 500)
    private String templateName;
    @Column(comment = "分类code", length = 50)
    private String templateTypeCode;
    @Column(comment = "标题", length = 500)
    private String compilationTitle;
    @Column(comment = "引题", length = 2000)
    private String citationTitle;
    @Column(comment = "副标题", length = 2000)
    private String subTitle;
    @Column(comment = "关键字", length = 200)
    private String keyword;
    @Column(comment = "摘要", length = 2000)
    private String summary;
    @Column(comment = "内容", type = ColumnType.CLOB)
    private String compilationContent;
    @Column(comment = "来源", length = 500)
    private String sourceShow;
    @Column(comment = "作者", length = 500)
    private String author;
    @Column(comment = "文章logo", length = 500)
    private String logolink;
    @Column(comment = "业务类型")
    private String businessType;

    public String getCompilationTitle() {
        return compilationTitle;
    }

    public void setCompilationTitle(String compilationTitle) {
        this.compilationTitle = compilationTitle;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateTypeCode() {
        return templateTypeCode;
    }

    public void setTemplateTypeCode(String templateTypeCode) {
        this.templateTypeCode = templateTypeCode;
    }

    public String getCitationTitle() {
        return citationTitle;
    }

    public void setCitationTitle(String citationTitle) {
        this.citationTitle = citationTitle;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getCompilationContent() {
        return compilationContent;
    }

    public void setCompilationContent(String compilationContent) {
        this.compilationContent = compilationContent;
    }

    public String getSourceShow() {
        return sourceShow;
    }

    public void setSourceShow(String sourceShow) {
        this.sourceShow = sourceShow;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLogolink() {
        return logolink;
    }

    public void setLogolink(String logolink) {
        this.logolink = logolink;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }
}
