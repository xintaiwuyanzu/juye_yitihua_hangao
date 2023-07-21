package com.dr.archive.common.statistics.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

@Table(name = com.dr.archive.util.Constants.TABLE_PREFIX + "REPORTFORM", module = Constants.MODULE_NAME, comment = "统计报表")
public class ReportForm extends BaseStatusEntity<String> {

    @Column(comment = "导出类型", name = "pType")
    private String type;

    @Column(comment = "机构ID")
    private String orgId;

    @Column(comment = "导出年度")
    private String vintages;

    @Column(comment = "全宗")
    private String fondCode;

    @Column(comment = "操作人")
    private String exportUser;

    @Column(comment = "导出路径")
    private String downloadPath;

    public String getDownloadPath() {
        return downloadPath;
    }

    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    public String getExportUser() {
        return exportUser;
    }

    public void setExportUser(String exportUser) {
        this.exportUser = exportUser;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getVintages() {
        return vintages;
    }

    public void setVintages(String vintages) {
        this.vintages = vintages;
    }

    public String getFondCode() {
        return fondCode;
    }

    public void setFondCode(String fondCode) {
        this.fondCode = fondCode;
    }
}
