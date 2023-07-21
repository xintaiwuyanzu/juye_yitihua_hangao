package com.dr.archive.onlineGuide.entity;

import com.dr.archive.batch.entity.AbstractArchiveBatch;
import com.dr.archive.util.Constants;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.ColumnType;
import com.dr.framework.core.orm.annotations.Table;

@Table(name = Constants.TABLE_PREFIX + "BUSINESS_GUIDANCE_DICTIONARY", comment = "分类字典", module = Constants.MODULE_NAME)
public class BusinessGuidanceCategoryDictionary extends AbstractArchiveBatch {

    @Column(comment = "类别")
    private String cType;

    @Column(comment = "问题")
    private String cProblem;

    @Column(comment = "结果",type = ColumnType.CLOB)
    private String cResult;

    @Column(comment = "创建人单位id")
    private String createOrgId;

    @Column(comment = "创建人单位名称")
    private String createOrgName;

    @Column(comment = "创建人ID")
    private String createUserId;

    @Column(comment = "创建人姓名")
    private String creatUserName;



    public String getcType() {
    return cType;
    }

    public String getCreateOrgId() {
        return createOrgId;
    }

    public void setCreateOrgId(String createOrgId) {
        this.createOrgId = createOrgId;
    }

    public String getCreateOrgName() {
        return createOrgName;
    }

    public void setCreateOrgName(String createOrgName) {
        this.createOrgName = createOrgName;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreatUserName() {
        return creatUserName;
    }

    public void setCreatUserName(String creatUserName) {
        this.creatUserName = creatUserName;
    }

    public void setcType(String cType) {
        this.cType = cType;
    }

    public String getcProblem() {
        return cProblem;
    }

    public void setcProblem(String cProblem) {
        this.cProblem = cProblem;
    }

    public String getcResult() {
        return cResult;
    }

    public void setcResult(String cResult) {
        this.cResult = cResult;
    }
}