package com.dr.archive.appraisal.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseCreateInfoEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;


@Table(name = Constants.TABLE_PREFIX + "manage_appraisal_rules", module = Constants.MODULE_NAME, comment = "档案鉴定过滤词库")
public class AppraisalRules extends BaseCreateInfoEntity {

    @Column(comment = "规则名称")
    private String rulesName;

    @Column(comment = "优先级")
    private String priority;

    @Column(comment = "辅助鉴定结果")
    private String auxiliaryResult;

    @Column(comment = "开放范围")
    private String openRange;

    /**
     * word：先匹配关键词，匹配到以后退出当前规则；
     * special：先匹配专题，匹配到以后退出当前规则；
     * all：匹配所有专题和关键词
     */
    @Column(comment = "规则匹配类型")
    private String matchingType;

    @Column(comment = "机构ID")
    private String orgId;

    @Column(comment = "依据ID")
    private String basisId;

    private String isApply;

    private Object keyWordList;

    private Object specialList;


    public String getRulesName() {
        return rulesName;
    }

    public void setRulesName(String rulesName) {
        this.rulesName = rulesName;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getAuxiliaryResult() {
        return auxiliaryResult;
    }

    public void setAuxiliaryResult(String auxiliaryResult) {
        this.auxiliaryResult = auxiliaryResult;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getBasisId() {
        return basisId;
    }

    public void setBasisId(String basisId) {
        this.basisId = basisId;
    }

    public String getIsApply() {
        return isApply;
    }

    public void setIsApply(String isApply) {
        this.isApply = isApply;
    }

    public String getOpenRange() {
        return openRange;
    }

    public void setOpenRange(String openRange) {
        this.openRange = openRange;
    }

    public Object getKeyWordList() {
        return keyWordList;
    }

    public void setKeyWordList(Object keyWordList) {
        this.keyWordList = keyWordList;
    }

    public Object getSpecialList() {
        return specialList;
    }

    public void setSpecialList(Object specialList) {
        this.specialList = specialList;
    }

    public String getMatchingType() {
        return matchingType;
    }

    public void setMatchingType(String matchingType) {
        this.matchingType = matchingType;
    }
}
