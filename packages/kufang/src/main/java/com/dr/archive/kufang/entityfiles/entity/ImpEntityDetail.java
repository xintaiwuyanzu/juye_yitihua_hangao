package com.dr.archive.kufang.entityfiles.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * 实体档案导入记录
 */
@Table(name = Constants.TABLE_PREFIX + "ImpEntityDetail", module = Constants.MODULE_NAME, comment = "实体档案导入记录")
public class ImpEntityDetail extends BaseStatusEntity<String> {


    @Column(comment = "档号", length = 100)
    private String archiveCode;
    @Column(comment = "题名", length = 200)
    private String title;
    @Column(comment = "档案ID", length = 200)//与实体档案对应
    private String dataId;
    @Column(comment = "全宗ID", length = 100)
    private String fondId;
    @Column(comment = "全宗号", length = 100)
    private String fondCode;
    @Column(comment = "全宗名称", length = 200)
    private String fondName;

    @Column(comment = "批次id", length = 100)
    private String batchId;

    @Column(comment = "机构id", length = 100)
    private String orgId;

    @Column(comment = "档案类型", length = 100)
    private String archiveType;

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

}
