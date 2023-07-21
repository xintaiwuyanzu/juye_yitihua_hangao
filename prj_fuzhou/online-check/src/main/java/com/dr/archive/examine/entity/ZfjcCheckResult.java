package com.dr.archive.examine.entity;

import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.ColumnType;
import com.dr.framework.core.orm.annotations.Table;
import com.dr.framework.util.Constants;


/**
 * 执法检查结果单
 *
 * @author cuiyj
 */
@Table(name = Constants.COMMON_TABLE_PREFIX + "ZfjcCheckResult", module = Constants.COMMON_MODULE_NAME, comment = "执法检查结果单")
public class ZfjcCheckResult extends BaseStatusEntity<String> {

    public static final Integer ZFJCCHECKRESULT_WAIT = 0;//待检查
    public static final Integer ZFJCCHECKRESULT_PASS = 1;//通过
    public static final Integer ZFJCCHECKRESULT_NO_PASS = 2;//不通过


    @Column(comment = "检查人员", type = ColumnType.CLOB)
    private String personName;

    @Column(comment = "检查人员id", type = ColumnType.CLOB)
    private String personId;

    @Column(comment = "检查单位", type = ColumnType.CLOB)
    private String organiseName;

    @Column(comment = "检查单位id", type = ColumnType.CLOB)
    private String organiseId;

    @Column(comment = "检查表id", length = 100)
    private String pId;

    @Column(comment = "检查类别数字", length = 100)//就是1 2 3
    private String categoryNum;

    @Column(comment = "检查类别id", length = 100)
    private String categoryId;

    @Column(comment = "检查类别名", length = 100)
    private String categoryName;

    @Column(comment = "检查结果", length = 10)
    private Integer categoryResult;

    @Column(comment = "整改意见", length = 300)
    private String opinion;

    @Column(comment = "调整回复", length = 300)
    private String reply;

    @Column(comment = "检查情况", length = 300)
    private String situation;

    public String getSituation() {
        return situation;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }

    public String getCategoryNum() {
        return categoryNum;
    }

    public void setCategoryNum(String categoryNum) {
        this.categoryNum = categoryNum;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getOrganiseName() {
        return organiseName;
    }

    public void setOrganiseName(String organiseName) {
        this.organiseName = organiseName;
    }

    public String getOrganiseId() {
        return organiseId;
    }

    public void setOrganiseId(String organiseId) {
        this.organiseId = organiseId;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getCategoryResult() {
        return categoryResult;
    }

    public void setCategoryResult(Integer categoryResult) {
        this.categoryResult = categoryResult;
    }
}
