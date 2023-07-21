package com.dr.archive.managefondsdescriptivefile.entity;

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
 * <p>
 * 移交状态
 * transferStatus:
 * 1、已移交
 * 0、未移交
 */
@Table(name = Constants.TABLE_PREFIX + "MANAGEMENT", module = Constants.MODULE_NAME, comment = "全宗卷类")
public class Management extends BaseStatusEntity<String> {
    //查询需要
    @Column(comment = "全宗id", length = 200)
    public String fondId;
    @Column(comment = "全宗号", length = 200)
    public String fond_code;
    @Column(comment = "全宗名称", length = 10, order = 2)
    public String fondName;
    @Column(comment = "文件材料编号", length = 200, order = 3)
    public String archive_code;
    @Column(comment = "题名", length = 500, order = 4)
    public String title;
    @Column(comment = "分类ID", length = 50, order = 5)
    public String managementTypeId;
    //查询需要，与全宗卷信息关联使用
    @Column(comment = "分类CODE", length = 50, order = 5)
    public String managementTypeCode;
    @Column(comment = "分类名称", length = 500, order = 6)
    public String managementTypeName;
    @Column(comment = "小分类ID", length = 50, order = 7)//目前未用到
    public String dossierTypeId;
    @Column(comment = "小分类名称", length = 500, order = 8)
    public String dossierTypeName;
    @Column(comment = "保管期限", length = 50, order = 11)
    public String retentionPeriod;
    @Column(comment = "页数", length = 50, order = 11)
    public String totalNumberOfPages;
    @Column(comment = "标题", length = 500)
    public String compilationTitle;
    @Column(comment = "内容", type = ColumnType.CLOB)
    public String compilationContent;
    @Column(comment = "行政区划", length = 200)
    public String administrative;
    @Column(comment = "盒号", length = 200)
    public String boxNumber;
    @Column(comment = "件号", length = 200)
    public String pieceNumber;
    @Column(comment = "当前机构Id", length = 200)
    public String currentOrganId;
    @Column(comment = "当前机构名称", length = 200)
    public String currentOrganName;
    @Column(comment = "责任人", length = 200)
    public String personLiable;
    @Column(comment = "起始年度", length = 50)
    public String vintagesStart;
    @Column(comment = "终止年度", length = 50)
    public String vintagesEnd;
    /**
     * 全宗卷查看、统计时用到
     */
    @Column(comment = "编写年度", length = 50)
    public String vintages;
    @Column(comment = "文件时间")
    public String fileTime;
    @Column(comment = "移交状态", length = 10)
    public String transferStatus;

    public Management() {
    }

    /**
     * @param id
     * @param fondCode           全宗号
     * @param managementTypeCode 分类号 类型从 ManagementService.class取值
     * @param title              题名
     * @param compilationContent 内容
     * @param personLiable       责任人姓名
     * @param fileTime           文件形成时间
     * @param vintagesStart      起始年度
     * @param vintagesEnd        终止年度
     */
    public Management(String id, String fondCode, String managementTypeCode, String title, String compilationContent, String personLiable, String fileTime, String vintagesStart, String vintagesEnd) {
        super.setId(id);
        this.fond_code = fondCode;
        this.managementTypeCode = managementTypeCode;
        this.title = title;
        this.compilationContent = compilationContent;
        this.personLiable = personLiable;
        this.fileTime = fileTime;
        this.vintagesStart = vintagesStart;
        this.vintagesEnd = vintagesEnd;
    }

    public String getFondId() {
        return fondId;
    }

    public void setFondId(String fondId) {
        this.fondId = fondId;
    }

    public String getFond_code() {
        return fond_code;
    }

    public void setFond_code(String fond_code) {
        this.fond_code = fond_code;
    }

    public String getFondName() {
        return fondName;
    }

    public void setFondName(String fondName) {
        this.fondName = fondName;
    }

    public String getArchive_code() {
        return archive_code;
    }

    public void setArchive_code(String archive_code) {
        this.archive_code = archive_code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getManagementTypeId() {
        return managementTypeId;
    }

    public void setManagementTypeId(String managementTypeId) {
        this.managementTypeId = managementTypeId;
    }

    public String getManagementTypeCode() {
        return managementTypeCode;
    }

    public void setManagementTypeCode(String managementTypeCode) {
        this.managementTypeCode = managementTypeCode;
    }

    public String getManagementTypeName() {
        return managementTypeName;
    }

    public void setManagementTypeName(String managementTypeName) {
        this.managementTypeName = managementTypeName;
    }

    public String getDossierTypeId() {
        return dossierTypeId;
    }

    public void setDossierTypeId(String dossierTypeId) {
        this.dossierTypeId = dossierTypeId;
    }

    public String getDossierTypeName() {
        return dossierTypeName;
    }

    public void setDossierTypeName(String dossierTypeName) {
        this.dossierTypeName = dossierTypeName;
    }

    public String getRetentionPeriod() {
        return retentionPeriod;
    }

    public void setRetentionPeriod(String retentionPeriod) {
        this.retentionPeriod = retentionPeriod;
    }

    public String getTotalNumberOfPages() {
        return totalNumberOfPages;
    }

    public void setTotalNumberOfPages(String totalNumberOfPages) {
        this.totalNumberOfPages = totalNumberOfPages;
    }

    public String getCompilationTitle() {
        return compilationTitle;
    }

    public void setCompilationTitle(String compilationTitle) {
        this.compilationTitle = compilationTitle;
    }

    public String getCompilationContent() {
        return compilationContent;
    }

    public void setCompilationContent(String compilationContent) {
        this.compilationContent = compilationContent;
    }

    public String getAdministrative() {
        return administrative;
    }

    public void setAdministrative(String administrative) {
        this.administrative = administrative;
    }

    public String getBoxNumber() {
        return boxNumber;
    }

    public void setBoxNumber(String boxNumber) {
        this.boxNumber = boxNumber;
    }

    public String getPieceNumber() {
        return pieceNumber;
    }

    public void setPieceNumber(String pieceNumber) {
        this.pieceNumber = pieceNumber;
    }

    public String getCurrentOrganId() {
        return currentOrganId;
    }

    public void setCurrentOrganId(String currentOrganId) {
        this.currentOrganId = currentOrganId;
    }

    public String getCurrentOrganName() {
        return currentOrganName;
    }

    public void setCurrentOrganName(String currentOrganName) {
        this.currentOrganName = currentOrganName;
    }

    public String getPersonLiable() {
        return personLiable;
    }

    public void setPersonLiable(String personLiable) {
        this.personLiable = personLiable;
    }

    public String getVintagesStart() {
        return vintagesStart;
    }

    public void setVintagesStart(String vintagesStart) {
        this.vintagesStart = vintagesStart;
    }

    public String getVintagesEnd() {
        return vintagesEnd;
    }

    public void setVintagesEnd(String vintagesEnd) {
        this.vintagesEnd = vintagesEnd;
    }

    public String getVintages() {
        return vintages;
    }

    public void setVintages(String vintages) {
        this.vintages = vintages;
    }

    public String getFileTime() {
        return fileTime;
    }

    public void setFileTime(String fileTime) {
        this.fileTime = fileTime;
    }

    public String getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }
}
