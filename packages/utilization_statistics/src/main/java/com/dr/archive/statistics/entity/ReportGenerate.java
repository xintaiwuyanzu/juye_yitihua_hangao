package com.dr.archive.statistics.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * @author: yang
 * @create: 2022-07-22 14:59
 **/
@Table(name = Constants.TABLE_PREFIX + "Report_Generate", module = Constants.MODULE_NAME, comment = "年报导出记录")
public class ReportGenerate extends BaseStatusEntity<String> {
    @Column(comment = "导出人id")
    String personId;
    @Column(comment = "导出人名称")
    String personName;
    @Column(comment = "导出人所在机构")
    String organizeName;
    @Column(comment = "全宗code")
    String fondCode;
    @Column(comment = "全宗名称")
    String fondName;
    @Column(comment = "报表年度")
    String reportAnnual;
    @Column(comment = "报表路径")
    String reportPath;
    @Column(comment = "操作内容")
    String optionName;

    public ReportGenerate() {
    }

    public ReportGenerate(String personId, String personName, String organizeName, String fondCode, String fondName, String reportAnnual, String reportPath, String optionName) {
        this.personId = personId;
        this.personName = personName;
        this.organizeName = organizeName;
        this.fondCode = fondCode;
        this.fondName = fondName;
        this.reportAnnual = reportAnnual;
        this.reportPath = reportPath;
        this.optionName = optionName;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getOrganizeName() {
        return organizeName;
    }

    public void setOrganizeName(String organizeName) {
        this.organizeName = organizeName;
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

    public String getReportAnnual() {
        return reportAnnual;
    }

    public void setReportAnnual(String reportAnnual) {
        this.reportAnnual = reportAnnual;
    }

    public String getReportPath() {
        return reportPath;
    }

    public void setReportPath(String reportPath) {
        this.reportPath = reportPath;
    }
}
