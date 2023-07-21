package com.dr.archive.manage.archiveMetadata.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.ColumnType;
import com.dr.framework.core.orm.annotations.Table;

/**
 * 档案元数据变更记录表
 *
 * @author hyj
 */

@Table(name = Constants.TABLE_PREFIX + "metadata_record", module = Constants.MODULE_NAME, comment = "管理过程元数据")
public class ArchiveMetadataRecord extends BaseStatusEntity<String> {
    @Column(comment = "表单Id", length = 100)
    private String formDefinitionId;
    @Column(comment = "档案数据id", length = 100)
    private String formDataId;
    @Column(comment = "档号", length = 100)
    private String archiveCode;
    @Column(comment = "变更人名称")
    private String changePersonName;
    @Column(comment = "全宗号", length = 100)
    private String fondCode;
    @Column(comment = "全宗Id")
    private String fondId;
    @Column(comment = "全宗名称", length = 100)
    private String fondName;
    @Column(comment = "档案门类代码", length = 100)
    private String categoryCode;
    @Column(comment = "门类Id")
    private String categoryId;
    @Column(comment = "门类名称", length = 100)
    private String categoryName;
    //补充福建省要求的管理数据类别及要求
    @Column(comment = "立档单位名称", length = 100)
    private String LDDWMC;
    @Column(comment = "年度", length = 100)
    private String ND;
    @Column(comment = "保管期限", length = 100)
    private String BGQX;
    @Column(comment = "机构或问题")
    private String JGHWT;
    @Column(comment = "室编件号", length = 100)
    private String SBJH;
    @Column(comment = "题名")
    private String TIMING;
    @Column(comment = "责任者")
    private String ZRZ;
    @Column(comment = "存放位置")
    private String CFWZ;
    @Column(comment = "业务状态")
    private String YWZT;
    @Column(comment = "业务行为")
    private String YWXW;
    @Column(comment = "行为依据")
    private String XWYJ;
    @Column(comment = "行为描述", type = ColumnType.CLOB)
    private String XWMS;
    @Column(comment = "权限管理")
    private String QXGL;
    @Column(comment = "修改字段code")
    private String alterCode;
    @Column(comment = "修改字段显示名称")
    private String alterName;
    @Column(comment = "原始值")
    private String oldValue;
    @Column(comment = "修改值")
    private String newValue;


    public ArchiveMetadataRecord() {
    }

    public ArchiveMetadataRecord(String formDefinitionId, String formDataId, String fondCode, String categoryCode, String YWXW, String XWYJ, String XWMS, String YWZT, String alterCode, String alterName, String oldValue, String newValue, String createPersonId) {
        this.formDefinitionId = formDefinitionId;
        this.formDataId = formDataId;
        this.fondCode = fondCode;
        this.categoryCode = categoryCode;
        this.YWXW = YWXW;
        this.XWYJ = XWYJ;
        this.XWMS = XWMS;
        this.alterCode = alterCode;
        this.alterName = alterName;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.YWZT = YWZT;
        super.setCreatePerson(createPersonId);
    }

    public ArchiveMetadataRecord(String formDefinitionId, String formDataId, String fondCode, String categoryCode, String YWXW, String XWYJ, String XWMS, String YWZT, String alterCode, String alterName, String oldValue, String newValue, String createPersonId, long createDate) {
        this.formDefinitionId = formDefinitionId;
        this.formDataId = formDataId;
        this.fondCode = fondCode;
        this.categoryCode = categoryCode;
        this.YWXW = YWXW;
        this.XWYJ = XWYJ;
        this.XWMS = XWMS;
        this.alterCode = alterCode;
        this.alterName = alterName;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.YWZT = YWZT;
        super.setCreatePerson(createPersonId);
        super.setCreateDate(createDate);
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

    public String getArchiveCode() {
        return archiveCode;
    }

    public void setArchiveCode(String archiveCode) {
        this.archiveCode = archiveCode;
    }

    public String getChangePersonName() {
        return changePersonName;
    }

    public void setChangePersonName(String changePersonName) {
        this.changePersonName = changePersonName;
    }

    public String getFondCode() {
        return fondCode;
    }

    public void setFondCode(String fondCode) {
        this.fondCode = fondCode;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getLDDWMC() {
        return LDDWMC;
    }

    public void setLDDWMC(String LDDWMC) {
        this.LDDWMC = LDDWMC;
    }

    public String getND() {
        return ND;
    }

    public void setND(String ND) {
        this.ND = ND;
    }

    public String getBGQX() {
        return BGQX;
    }

    public void setBGQX(String BGQX) {
        this.BGQX = BGQX;
    }

    public String getJGHWT() {
        return JGHWT;
    }

    public void setJGHWT(String JGHWT) {
        this.JGHWT = JGHWT;
    }

    public String getSBJH() {
        return SBJH;
    }

    public void setSBJH(String SBJH) {
        this.SBJH = SBJH;
    }

    public String getTIMING() {
        return TIMING;
    }

    public void setTIMING(String TIMING) {
        this.TIMING = TIMING;
    }

    public String getZRZ() {
        return ZRZ;
    }

    public void setZRZ(String ZRZ) {
        this.ZRZ = ZRZ;
    }

    public String getCFWZ() {
        return CFWZ;
    }

    public void setCFWZ(String CFWZ) {
        this.CFWZ = CFWZ;
    }

    public String getYWZT() {
        return YWZT;
    }

    public void setYWZT(String YWZT) {
        this.YWZT = YWZT;
    }

    public String getYWXW() {
        return YWXW;
    }

    public void setYWXW(String YWXW) {
        this.YWXW = YWXW;
    }

    public String getXWYJ() {
        return XWYJ;
    }

    public void setXWYJ(String XWYJ) {
        this.XWYJ = XWYJ;
    }

    public String getXWMS() {
        return XWMS;
    }

    public void setXWMS(String XWMS) {
        this.XWMS = XWMS;
    }

    public String getQXGL() {
        return QXGL;
    }

    public void setQXGL(String QXGL) {
        this.QXGL = QXGL;
    }

    public String getAlterCode() {
        return alterCode;
    }

    public void setAlterCode(String alterCode) {
        this.alterCode = alterCode;
    }

    public String getAlterName() {
        return alterName;
    }

    public void setAlterName(String alterName) {
        this.alterName = alterName;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }
}
