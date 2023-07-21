package com.dr.archive.common.dzdacqbc.entity;

import com.dr.archive.common.dzdacqbc.utils.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * 电子档案长期保存 备份策略
 */

@Table(name = Constants.DZDNCQBC + "strategy", module = Constants.MODULE_NAME, comment = "电子档案长期保存备份策略表")
public class ESaveStrategy extends BaseStatusEntity<String> {

    @Column(comment = "策略名称", length = 200)
    private String strategyName;

    // 1:全量备份   2:增量备份
    @Column(comment = "策略类型", length = 200)
    private String strategyType;

    @Column(comment = "备份数量")
    private Long strategyCount;

    @Column(comment = "全宗id")
    public String fondId;
    @Column(comment = "全宗号")
    public String fond_code;
    @Column(comment = "全宗名称")
    public String fond_name;
    @Column(comment = "门类id", length = 200)
    private String classId;

    @Column(comment = "门类代码", length = 200)
    private String classCode;

    @Column(comment = "门类名称", length = 200)
    private String className;

    @Column(comment = "存储空间id")
    public String spacesId;
    @Column(comment = "存储空间名称")
    public String spaceName;

    @Column(comment = "机构id")
    public String orgId;


    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }

    public String getStrategyType() {
        return strategyType;
    }

    public void setStrategyType(String strategyType) {
        this.strategyType = strategyType;
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

    public String getSpacesId() {
        return spacesId;
    }

    public void setSpacesId(String spacesId) {
        this.spacesId = spacesId;
    }

    public String getSpaceName() {
        return spaceName;
    }

    public void setSpaceName(String spaceName) {
        this.spaceName = spaceName;
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

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
