package com.dr.archive.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseDescriptionEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * @author lych
 * @date 2023/2/3 上午 9:00
 * @mood happy
 */
@Table(name= Constants.TABLE_PREFIX+"Borrowing_Registration_Details",comment = "申请借阅详情表",module = Constants.MODULE_NAME)
public class BorrowingRegistrationDetails extends BaseDescriptionEntity<String> {


    @Column(comment = "档号",order = 21)
    private String ARCHIVE_CODE;

    @Column(comment = "题名",order = 22)
    private String TITLE;

    @Column(comment = "全宗",order = 23)
    private String FOND_CODE;

    @Column(comment = "门类",order = 23)
    private String CATE_GORY_CODE;

    @Column(comment = "年度",order = 24)
    private String VINTAGES;

    @Column(comment = "表单id",order = 25)
    private String formDefinitionId;

    @Column(comment = "档案id",order = 26)
    private String formId;

    @Column(comment = "申请借阅批次id",order = 26)
    private String borrowingId;

    public String getBorrowingId() {
        return borrowingId;
    }

    public void setBorrowingId(String borrowingId) {
        this.borrowingId = borrowingId;
    }

    public String getARCHIVE_CODE() {
        return ARCHIVE_CODE;
    }

    public void setARCHIVE_CODE(String ARCHIVE_CODE) {
        this.ARCHIVE_CODE = ARCHIVE_CODE;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getFOND_CODE() {
        return FOND_CODE;
    }

    public void setFOND_CODE(String FOND_CODE) {
        this.FOND_CODE = FOND_CODE;
    }

    public String getCATE_GORY_CODE() {
        return CATE_GORY_CODE;
    }

    public void setCATE_GORY_CODE(String CATE_GORY_CODE) {
        this.CATE_GORY_CODE = CATE_GORY_CODE;
    }

    public String getVINTAGES() {
        return VINTAGES;
    }

    public void setVINTAGES(String VINTAGES) {
        this.VINTAGES = VINTAGES;
    }

    public String getFormDefinitionId() {
        return formDefinitionId;
    }

    public void setFormDefinitionId(String formDefinitionId) {
        this.formDefinitionId = formDefinitionId;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }
}
