package com.dr.archive.appraisal.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;


@Table(name = Constants.TABLE_PREFIX + "manage_appraisal_keyword", module = Constants.MODULE_NAME, comment = "档案鉴定过滤词")
public class AppraisalKeyWord extends BaseStatusEntity<String> {

    /*
        关键词之间用逗号区分则为且的关系
     */
    @Column(comment = "关键词")
    private String keyWord;

    @Column(comment = "依据ID")
    private String basisId;

    @Column(comment = "规则ID")
    private String rulesId;

    @Column(comment = "组织机构Id")
    private String orgId;

    public String getRulesId() {
        return rulesId;
    }

    public void setRulesId(String rulesId) {
        this.rulesId = rulesId;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getBasisId() {
        return basisId;
    }

    public void setBasisId(String basisId) {
        this.basisId = basisId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
