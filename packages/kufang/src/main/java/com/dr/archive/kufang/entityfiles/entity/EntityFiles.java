package com.dr.archive.kufang.entityfiles.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * 实体档案辅助管理
 * 每个单位智能管理每个单位实体档案，根据organiseId判断
 */
@Table(name = Constants.TABLE_PREFIX + "EntityFiles", module = Constants.MODULE_NAME, comment = "实体档案")
public class EntityFiles extends BaseStatusEntity<String> {
    @Column(comment = "档号", length = 100)
    private String archiveCode;
    @Column(comment = "题名", length = 200)
    private String title;
    @Column(comment = "档案ID", length = 200)//与电子档案对应
    private String dataId;
    @Column(comment = "全宗ID", length = 200)
    private String fondId;
    @Column(comment = "全宗号", length = 200)
    private String fondCode;
    @Column(comment = "全宗名称", length = 200)
    private String fondName;
    @Column(comment = "门类id", length = 200)
    private String classId;
    @Column(comment = "门类代码", length = 200)
    private String classCode;
    @Column(comment = "门类名称", length = 200)
    private String className;
    @Column(comment = "档案类别", length = 200)
    private String archiveType;
    @Column(comment = "库房id", length = 50)
    private String locId;
    @Column(comment = "库房区id", length = 100)
    private String areaId;
    @Column(comment = "库房区名称", length = 100)
    private String areaNo;
    @Column(comment = "caseId", length = 100)
    private String caseId;
    @Column(comment = "层", length = 100)
    private String caseNo;
    @Column(comment = "AB面", length = 100)
    private String columnNum;//弃用
    @Column(comment = "lie", length = 100)
    private String columnNo;
    @Column(comment = "层号", length = 100)
    private String layer;
    @Column(comment = "格子id", length = 100)
    private String layerId;
    @Column(comment = "机构id", length = 100)
    private String organiseId;
    @Column(comment = "导入记录ID", length = 100)
    private String batchId;
    @Column(comment = "档案盒", length = 100)
    private String archiveBox;
    @Column(comment = "盒id", length = 100)
    private String parentId;
    @Column(comment = "是否为卷内文件")
    private String isJNWJ;
    @Column(comment = "案卷档号")
    private String ajdh;
    @Column(comment = "表单类型")
    private String modelType;

    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }

    public String getIsJNWJ() {
        return isJNWJ;
    }

    public void setIsJNWJ(String isJNWJ) {
        this.isJNWJ = isJNWJ;
    }

    public String getAjdh() {
        return ajdh;
    }

    public void setAjdh(String ajdh) {
        this.ajdh = ajdh;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getFondId() {
        return fondId;
    }

    public void setFondId(String fondId) {
        this.fondId = fondId;
    }

    public String getFondCode() {
        return fondCode;
    }

    public void setFondCode(String fondCode) {
        this.fondCode = fondCode;
    }

    public String getFondName() {
        return fondName;
    }

    public void setFondName(String fondName) {
        this.fondName = fondName;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getArchiveBox() {
        return archiveBox;
    }

    public void setArchiveBox(String archiveBox) {
        this.archiveBox = archiveBox;
    }

    public String getLocId() {
        return locId;
    }

    public void setLocId(String locId) {
        this.locId = locId;
    }

    public String getOrganiseId() {
        return organiseId;
    }

    public void setOrganiseId(String organiseId) {
        this.organiseId = organiseId;
    }

    public String getLayerId() {
        return layerId;
    }

    public void setLayerId(String layerId) {
        this.layerId = layerId;
    }

    public String getArchiveCode() {
        return archiveCode;
    }

    public void setArchiveCode(String archiveCode) {
        this.archiveCode = archiveCode;
    }

    public String getArchiveType() {
        return archiveType;
    }

    public void setArchiveType(String archiveType) {
        this.archiveType = archiveType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCaseNo() {
        return caseNo;
    }

    public void setCaseNo(String caseNo) {
        this.caseNo = caseNo;
    }

    public String getColumnNo() {
        return columnNo;
    }

    public void setColumnNo(String columnNo) {
        this.columnNo = columnNo;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getColumnNum() {
        return columnNum;
    }

    public void setColumnNum(String columnNum) {
        this.columnNum = columnNum;
    }

    public String getLayer() {
        return layer;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaNo() {
        return areaNo;
    }

    public void setAreaNo(String areaNo) {
        this.areaNo = areaNo;
    }
}
