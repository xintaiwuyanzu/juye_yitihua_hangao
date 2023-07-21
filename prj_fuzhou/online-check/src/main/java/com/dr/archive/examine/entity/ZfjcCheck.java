package com.dr.archive.examine.entity;


import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.ColumnType;
import com.dr.framework.core.orm.annotations.Table;
import com.dr.framework.util.Constants;

/**
 * 执法检查
 *
 * @author cuiyj
 */
@Table(name = Constants.COMMON_TABLE_PREFIX + "ZfjcCheck", module = Constants.COMMON_MODULE_NAME, comment = "执法检查")
public class ZfjcCheck extends BaseStatusEntity<String> {


    public static final String status_add = "0";//新增
    public static final String status_banjie = "9";//办结

    /**
     * （一）贯彻实施档案法规监督检查
     * （二）对档案工作的监督检查
     * （三）依法接收档案执法检查
     */
    public static final String categoryId_type_one = "1";
    public static final String categoryId_type_two = "2";
    public static final String categoryId_type_three = "3";

    public static final String categoryId_type_one_des = "贯彻实施档案法规监督检查";
    public static final String categoryId_type_two_des = "对档案工作的监督检查";
    public static final String categoryId_type_three_des = "依法接收档案执法检查";

    public static final String checkType_nd = "nd";//年度检查
    public static final String checkType_zf = "zf";//执法检查

    @Column(comment = "检查类别id", length = 200)
    private String categoryId;

    @Column(comment = "检查类别名", length = 500)
    private String categoryName;

    @Column(comment = "检查类别小项id", length = 500)
    private String categorySmallId;

    @Column(comment = "检查类别小项名", length = 500)
    private String categorySmallName;

    @Column(comment = "categoryId与categorySmallId关联表", type = ColumnType.CLOB)
    private String categoryIdContact;

    @Column(comment = "检查人员", type = ColumnType.CLOB)
    private String personName;

    @Column(comment = "检查人员id", type = ColumnType.CLOB)
    private String personId;

    @Column(comment = "检查单位", type = ColumnType.CLOB)
    private String organiseName;

    @Column(comment = "检查单位id", type = ColumnType.CLOB)
    private String organiseId;

    @Column(comment = "调整回复", type = ColumnType.CLOB)
    private String reply;

    @Column(comment = "整改意见", type = ColumnType.CLOB)
    private String opinion;

    @Column(comment = "审批意见", type = ColumnType.CLOB)
    private String applyContent;

    @Column(comment = "批次", length = 300)
    private String pici;


    //    1、计划检查时间
    //    2、实际检查时间
    //    3、检查结果填写时间
    //    4、整改意见到期时间
    //    5、整改完成时间
    @Column(comment = "计划检查时间", length = 300)
    private long jihuaTime;
    @Column(comment = "实际检查时间", length = 300)
    private long shijiTime;
    @Column(comment = "结果填写时间", length = 300)
    private long jieguoTime;
    @Column(comment = "通知整改时间", length = 300)
    private long tongzhiTime;
    @Column(comment = "整改到期时间", length = 300)
    private long zhenggaiTime;
    @Column(comment = "整改完成时间", length = 300)
    private long wanchengTime;

    @Column(comment = "检查结果", length = 10)
    private Integer checkResult;

    @Column(comment = "公示", length = 50)
    private String publish; // “0” 不公示 “1” 公示

    @Column(comment = "年度检查还是执法检查", length = 10)
    private String checkType;//nd  zf

    @Column(comment = "创建单位Id")
    private String createOrgId;

    @Column(comment = "创建单位名称")
    private String createOrgName;

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

    public String getCategoryIdContact() {
        return categoryIdContact;
    }

    public void setCategoryIdContact(String categoryIdContact) {
        this.categoryIdContact = categoryIdContact;
    }

    public String getCheckType() {
        return checkType;
    }

    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }

    public String getApplyContent() {
        return applyContent;
    }

    public void setApplyContent(String applyContent) {
        this.applyContent = applyContent;
    }

    public String getPublish() {
        return publish;
    }

    public void setPublish(String publish) {
        this.publish = publish;
    }

    public Integer getCheckResult() {
        return checkResult;
    }

    public void setCheckResult(Integer checkResult) {
        this.checkResult = checkResult;
    }

    public String getPici() {
        return pici;
    }

    public void setPici(String pici) {
        this.pici = pici;
    }

    public long getTongzhiTime() {
        return tongzhiTime;
    }

    public void setTongzhiTime(long tongzhiTime) {
        this.tongzhiTime = tongzhiTime;
    }

    public long getJihuaTime() {
        return jihuaTime;
    }

    public void setJihuaTime(long jihuaTime) {
        this.jihuaTime = jihuaTime;
    }

    public long getShijiTime() {
        return shijiTime;
    }

    public void setShijiTime(long shijiTime) {
        this.shijiTime = shijiTime;
    }

    public long getJieguoTime() {
        return jieguoTime;
    }

    public void setJieguoTime(long jieguoTime) {
        this.jieguoTime = jieguoTime;
    }

    public long getZhenggaiTime() {
        return zhenggaiTime;
    }

    public void setZhenggaiTime(long zhenggaiTime) {
        this.zhenggaiTime = zhenggaiTime;
    }

    public long getWanchengTime() {
        return wanchengTime;
    }

    public void setWanchengTime(long wanchengTime) {
        this.wanchengTime = wanchengTime;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public String getCategorySmallId() {
        return categorySmallId;
    }

    public void setCategorySmallId(String categorySmallId) {
        this.categorySmallId = categorySmallId;
    }

    public String getCategorySmallName() {
        return categorySmallName;
    }

    public void setCategorySmallName(String categorySmallName) {
        this.categorySmallName = categorySmallName;
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

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }
}
