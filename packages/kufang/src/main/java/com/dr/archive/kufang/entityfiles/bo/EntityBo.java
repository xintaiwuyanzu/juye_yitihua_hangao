package com.dr.archive.kufang.entityfiles.bo;

/**
 * 实体档案新增bo类
 */
public class EntityBo {

    private String id;

    private String archiveCode;

    private String title;

    private String archiveType;

    private String locId;
    //库房区域id
    private String areaId;
    //库房中的区号
    private String areaNo;

    private String caseId;

    private String caseNo;

    private String columnNum;

    private String columnNo;

    private String fondId;

    private String classCode;

    private String layer;
    //件/盒
    private String archiveBox;
    /**
     * 属性json串
     */
    private String propertyData;

    public String getFondId() {
        return fondId;
    }

    public void setFondId(String fondId) {
        this.fondId = fondId;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getArchiveBox() {
        return archiveBox;
    }

    public void setArchiveBox(String archiveBox) {
        this.archiveBox = archiveBox;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArchiveCode() {
        return archiveCode;
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

    public void setArchiveCode(String archiveCode) {
        this.archiveCode = archiveCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArchiveType() {
        return archiveType;
    }

    public void setArchiveType(String archiveType) {
        this.archiveType = archiveType;
    }

    public String getLocId() {
        return locId;
    }

    public void setLocId(String locId) {
        this.locId = locId;
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

    public String getLayer() {
        return layer;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }

    public String getPropertyData() {
        return propertyData;
    }

    public void setPropertyData(String propertyData) {
        this.propertyData = propertyData;
    }

    public String getAreaNo() {
        return areaNo;
    }

    public void setAreaNo(String areaNo) {
        this.areaNo = areaNo;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }
}
