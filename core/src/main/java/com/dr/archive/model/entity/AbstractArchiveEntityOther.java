package com.dr.archive.model.entity;

import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;

/**
 * 档案表抽象父类，用来统一规定各种类型的档案基本字段
 * <p>
 * 业务表可以继承这个类，然后添加自己的字段即可
 * <p>
 *
 * <strong>
 * 这个类可以用来做业务逻辑判断，增删改查不能用该类
 * <strong/>
 *
 * @author dr
 */
public class AbstractArchiveEntityOther extends BaseStatusEntity<String> implements ArchiveEntity {
    @Column(name = COLUMN_FOND_CODE, comment = "全宗号")
    private String fondCode;

    /**
     * 分类门类编码，最低一级
     */
    @Column(name = COLUMN_CATEGORY_CODE, comment = "分类编码")
    private String categoryCode;

    /*-----------------非档案属性---------------------------*/
    @Column(name = COLUMN_CATALOGUE_CODE, comment = "目录号")
    private String catalogueCode;
    @Column(name = COLUMN_SOURCE_TYPE, comment = "数据来源类型", length = 100)
    private String sourceType;
    /**
     * 短时间内用不到
     */
    @Column(name = COLUMN_SOURCE_ID, comment = "数据来源Id", length = 100)
    private String sourceId;
    @Column(name = COLUMN_KEY_WORDS, comment = "主题词", length = 800)
    private String keyWords;
    @Column(name = COLUMN_SUB_STATUS, comment = "状态", length = 100)
    private String subStatus;
    @Column(name = COLUMN_OPEN_SCOPE, comment = "开放范围", length = 100)
    private String openScope;
    @Column(name = COLUMN_YW_HAVE, comment = "是否有原文信息", length = 10)
    private String YW_HAVE;

    @Column(name = COLUMN_ORGANISEID, comment = "机构id")
    private String ORGANISEID;
    @Column(name = COLUMN_BQ, comment = "标签")
    private String BQ;
    @Column(name = COLUMN_SAVE_APPRAISAL_DATE, comment = "到期鉴定时间")
    private String SAVE_APPRAISAL_DATE;
    @Column(name = COLUMN_OPEN_APPRAISAL_DATE, comment = "开放鉴定时间")
    private String OPEN_APPRAISAL_DATE;
    @Column(name = COLUMN_FILE_COUNTS, comment = "原文数量")
    private String FILE_COUNTS;
    @Column(name = COLUMN_IS_TRANSFER, comment = "是否移交")
    private String IS_TRANSFER;

    public String getFondCode() {
        return fondCode;
    }

    public void setFondCode(String fondCode) {
        this.fondCode = fondCode;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getORGANISEID() {
        return ORGANISEID;
    }

    public void setORGANISEID(String ORGANISEID) {
        this.ORGANISEID = ORGANISEID;
    }
    //    @Column(name = COLUMN_BQ, comment = "标签", length = 1000)
//    private String TAG;
//
//    public String getTAG() {
//        return TAG;
//    }
//
//    public void setTAG(String TAG) {
//        this.TAG = TAG;
//    }

    public String getCatalogueCode() {
        return catalogueCode;
    }

    public void setCatalogueCode(String catalogueCode) {
        this.catalogueCode = catalogueCode;
    }


    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }

    public String getSubStatus() {
        return subStatus;
    }

    public void setSubStatus(String subStatus) {
        this.subStatus = subStatus;
    }

    public String getOpenScope() {
        return openScope;
    }

    public void setOpenScope(String openScope) {
        this.openScope = openScope;
    }

    public String getYW_HAVE() {
        return YW_HAVE;
    }

    public void setYW_HAVE(String YW_HAVE) {
        this.YW_HAVE = YW_HAVE;
    }

    public String getBQ() {
        return BQ;
    }

    public void setBQ(String BQ) {
        this.BQ = BQ;
    }

    public String getIS_TRANSFER() {
        return IS_TRANSFER;
    }

    public void setIS_TRANSFER(String IS_TRANSFER) {
        this.IS_TRANSFER = IS_TRANSFER;
    }

    public String getFILE_COUNTS() {
        return FILE_COUNTS;
    }

    public void setFILE_COUNTS(String FILE_COUNTS) {
        this.FILE_COUNTS = FILE_COUNTS;
    }

    public String getSAVE_APPRAISAL_DATE() {
        return SAVE_APPRAISAL_DATE;
    }

    public void setSAVE_APPRAISAL_DATE(String SAVE_APPRAISAL_DATE) {
        this.SAVE_APPRAISAL_DATE = SAVE_APPRAISAL_DATE;
    }

    public String getOPEN_APPRAISAL_DATE() {
        return OPEN_APPRAISAL_DATE;
    }

    public void setOPEN_APPRAISAL_DATE(String OPEN_APPRAISAL_DATE) {
        this.OPEN_APPRAISAL_DATE = OPEN_APPRAISAL_DATE;
    }
}
