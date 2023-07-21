package com.dr.archive.common.accessControl.entity;

import com.dr.archive.common.utils.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * @author Mr.Zhu
 * @date 2022/5/17 - 11:13
 */
@Table(name = "member_authority", module = Constants.MODULE_NAME, comment = "人员权限表")
public class MemberAuthority extends BaseStatusEntity<String> {
    @Column(comment = "机构人员", length = 100)
    private String fondPeron;

    @Column(comment = "机构人员id", length = 100)
    private String fondPeronId;

    @Column(comment = "权限编号", length = 100)
    private String accessCode;

    @Column(comment = "权限级别", length = 100)
    private String accessLevel;

    @Column(comment = "级别编码", length = 100)
    private String levelCode;

    @Column(comment = "全宗名称", length = 100)
    private String fondName;

    @Column(comment = "全宗编号", length = 100)
    private String fondCode;

    @Column(comment = "全宗id", length = 100)
    private String fondId;

    @Column(comment = "门类名称", length = 100)
    private String categoryName;

    @Column(comment = "门类编号", length = 100)
    private String categoryCode;

    @Column(comment = "门类id", length = 100)
    private String categoryId;

    public String getFondPeronId() {
        return fondPeronId;
    }

    public void setFondPeronId(String fondPeronId) {
        this.fondPeronId = fondPeronId;
    }

    public String getFondPeron() {
        return fondPeron;
    }

    public void setFondPeron(String fondPeron) {
        this.fondPeron = fondPeron;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }

    public String getLevelCode() {
        return levelCode;
    }

    public void setLevelCode(String levelCode) {
        this.levelCode = levelCode;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public String getFondName() {
        return fondName;
    }

    public void setFondName(String fondName) {
        this.fondName = fondName;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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
}
