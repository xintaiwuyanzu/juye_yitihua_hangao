package com.dr.archive.kufang.entityfiles.vo;

import java.util.List;

/**
 * 实体档案导入Vo
 */
public class ImpEntityDetailVo {

    private String id;
    private String status;
    private String archiveCode;
    private String title;
    private String dataId;
    private String fondId;
    private String fondCode;
    private String fondName;

    private String batchId;
    private String orgId;
    private String archiveType;
    private List<ImpEntityDetailVo> voList;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public void setArchiveCode(String archiveCode) {
        this.archiveCode = archiveCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getArchiveType() {
        return archiveType;
    }

    public void setArchiveType(String archiveType) {
        this.archiveType = archiveType;
    }

    public List<ImpEntityDetailVo> getVoList() {
        return voList;
    }

    public void setVoList(List<ImpEntityDetailVo> voList) {
        this.voList = voList;
    }
}
