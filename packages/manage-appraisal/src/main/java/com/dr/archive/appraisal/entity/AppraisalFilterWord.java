package com.dr.archive.appraisal.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseCreateInfoEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;


@Table(name = Constants.TABLE_PREFIX + "manage_appraisal_filterword", module = Constants.MODULE_NAME, comment = "档案鉴定过滤词")
public class AppraisalFilterWord extends BaseCreateInfoEntity {

    @Column(comment = "词库Id")
    private String groupId;

    @Column(comment = "全宗编码")
    private String fondCode;

    @Column(comment = "单位名称")
    private String orgName;

    @Column(comment = "目录号")
    private String catalogueCode;

    @Column(comment = "卷号")
    private String ajh;

    @Column(comment = "专题名")
    private String special;

    @Column(comment = "题名")
    private String title;

    @Column(comment = "原文")
    private String content;

    @Column(comment = "密级敏感词")
    private String s_level;

    @Column(comment = "依据")
    private String basis;

    @Column(comment = "结果")
    private String auxiliaryResult;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getFondCode() {
        return fondCode;
    }

    public void setFondCode(String fondCode) {
        this.fondCode = fondCode;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getCatalogueCode() {
        return catalogueCode;
    }

    public void setCatalogueCode(String catalogueCode) {
        this.catalogueCode = catalogueCode;
    }

    public String getAjh() {
        return ajh;
    }

    public void setAjh(String ajh) {
        this.ajh = ajh;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getS_level() {
        return s_level;
    }

    public void setS_level(String s_level) {
        this.s_level = s_level;
    }

    public String getBasis() {
        return basis;
    }

    public void setBasis(String basis) {
        this.basis = basis;
    }

    public String getAuxiliaryResult() {
        return auxiliaryResult;
    }

    public void setAuxiliaryResult(String auxiliaryResult) {
        this.auxiliaryResult = auxiliaryResult;
    }
}
