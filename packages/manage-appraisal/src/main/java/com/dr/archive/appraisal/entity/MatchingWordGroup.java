package com.dr.archive.appraisal.entity;


import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseOrderedEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

import java.util.List;

@Table(name = Constants.TABLE_PREFIX + "manage_appraisal_MatchingWordGroup", module = Constants.MODULE_NAME, comment = "档案鉴定匹配到的词库")
public class MatchingWordGroup extends BaseOrderedEntity {


    @Column(comment = "待鉴定档案ID")
    private String toBeAppraisalId;

    @Column(comment = "匹配到的词库ID")
    private String wordGroupId;

    @Column(comment = "匹配到的词库名称")
    private String wordGroupName;

    @Column(comment = "词库鉴定结果参考")
    private String wordGroupResult;

    @Column(comment = "匹配到的关键词")
    private String keywords;

    @Column(comment = "优先级")
    private String priority;

    private List children;

    public String getToBeAppraisalId() {
        return toBeAppraisalId;
    }

    public void setToBeAppraisalId(String toBeAppraisalId) {
        this.toBeAppraisalId = toBeAppraisalId;
    }

    public String getWordGroupId() {
        return wordGroupId;
    }

    public void setWordGroupId(String wordGroupId) {
        this.wordGroupId = wordGroupId;
    }

    public String getWordGroupResult() {
        return wordGroupResult;
    }

    public void setWordGroupResult(String wordGroupResult) {
        this.wordGroupResult = wordGroupResult;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public List getChildren() {
        return children;
    }

    public void setChildren(List children) {
        this.children = children;
    }

    public String getWordGroupName() {
        return wordGroupName;
    }

    public void setWordGroupName(String wordGroupName) {
        this.wordGroupName = wordGroupName;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
