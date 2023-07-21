package com.dr.archive.common.dzdacqbc.entity;

import com.dr.archive.common.dzdacqbc.utils.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * 电子档案长期保存 离线备份
 */

@Table(name = Constants.DZDNCQBC + "offLine", module = Constants.MODULE_NAME, comment = "电子档案长期保存离线备份表")
public class ESaveOffLine extends BaseStatusEntity<String> {

    @Column(comment = "备份名称", length = 200)
    private String strategyName;

    @Column(comment = "备份数量")
    private Long strategyCount;

    @Column(comment = "全宗id")
    private String fondId;
    @Column(comment = "全宗号")
    private String fond_code;
    @Column(comment = "全宗名称")
    private String fond_name;

    @Column(comment = "门类id", length = 200)
    private String classId;
    @Column(comment = "门类代码", length = 200)
    private String classCode;
    @Column(comment = "门类名称", length = 200)
    private String className;

    @Column(comment = "年度")
    private String nd;

    @Column(comment = "介质id")
    private String mediumId;
    @Column(comment = "介质名称")
    private String mediumName;

    @Column(comment = "机构id")
    private String orgId;


    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }

    public Long getStrategyCount() {
        return strategyCount;
    }

    public void setStrategyCount(Long strategyCount) {
        this.strategyCount = strategyCount;
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

    public String getFond_name() {
        return fond_name;
    }

    public void setFond_name(String fond_name) {
        this.fond_name = fond_name;
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

    public String getNd() {
        return nd;
    }

    public void setNd(String nd) {
        this.nd = nd;
    }

    public String getMediumId() {
        return mediumId;
    }

    public void setMediumId(String mediumId) {
        this.mediumId = mediumId;
    }

    public String getMediumName() {
        return mediumName;
    }

    public void setMediumName(String mediumName) {
        this.mediumName = mediumName;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
