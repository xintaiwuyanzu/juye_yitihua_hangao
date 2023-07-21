package com.dr.archive.receive.online.archiveFiling.entity;


import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

@Table(name = com.dr.archive.util.Constants.TABLE_PREFIX + "filing_detail", module = Constants.MODULE_NAME, comment = "档案归档审核档案详情表")
public class FilingDetail extends BaseStatusEntity<String> {

    @Column(comment = "归档表Id", length = 100)
    private String filingId;

    @Column(comment = "全宗名", length = 100)
    private String fondName;

    @Column(comment = "全宗号", length = 100)
    private String fondCode;

    @Column(comment = "门类编号", length = 100)
    private String categoryCode;

    @Column(comment = "门类名称", length = 100)
    private String categoryName;

    @Column(comment = "门类id", length = 100)
    private String categoryId;

    @Column(comment = "表单定义id", length = 100)
    private String formDefinitionId;

    @Column(comment = "档案id", length = 100)
    private String formDataId;

    @Column(comment = "题名", length = 100)
    private String title;

    @Column(comment = "档号", length = 100)
    private String archiveCode;

    @Column(comment = "年度", length = 100)
    private String vintages;

    @Column(comment = "保管期限", length = 100)
    private String saveTerm;

    @Column(comment = "案卷档号", length = 100)
    private String ajdh;

    @Column(comment = "是否有原文", length = 100)
    private String YW_HAVE;

    public String getYW_HAVE() {
        return YW_HAVE;
    }

    public void setYW_HAVE(String YW_HAVE) {
        this.YW_HAVE = YW_HAVE;
    }

    public String getAjdh() {
        return ajdh;
    }

    public void setAjdh(String ajdh) {
        this.ajdh = ajdh;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getFilingId() {
        return filingId;
    }

    public void setFilingId(String filingId) {
        this.filingId = filingId;
    }

    public String getFondName() {
        return fondName;
    }

    public void setFondName(String fondName) {
        this.fondName = fondName;
    }

    public String getFondCode() {
        return fondCode;
    }

    public void setFondCode(String fondCode) {
        this.fondCode = fondCode;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getFormDefinitionId() {
        return formDefinitionId;
    }

    public void setFormDefinitionId(String formDefinitionId) {
        this.formDefinitionId = formDefinitionId;
    }

    public String getFormDataId() {
        return formDataId;
    }

    public void setFormDataId(String formDataId) {
        this.formDataId = formDataId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArchiveCode() {
        return archiveCode;
    }

    public void setArchiveCode(String archiveCode) {
        this.archiveCode = archiveCode;
    }

    public String getVintages() {
        return vintages;
    }

    public void setVintages(String vintages) {
        this.vintages = vintages;
    }

    public String getSaveTerm() {
        return saveTerm;
    }

    public void setSaveTerm(String saveTerm) {
        this.saveTerm = saveTerm;
    }
}
