package com.dr.archive.onlineGuide.entity;

import com.dr.archive.batch.entity.AbstractArchiveBatch;
import com.dr.archive.util.Constants;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * 这是存的归类库
 */
@Table(name = Constants.TABLE_PREFIX + "BUSINESS_GUIDANCE_CLASSIFI", comment = "指导归类库", module = Constants.MODULE_NAME)
public class BusinessGuidanceClassifiBatch extends AbstractArchiveBatch {

    @Column(comment = "归类名称")
    private String classifiName;
    @Column(comment = "归类ID")
    private String did;
    @Column(comment = "批次id")
    private String batchId;

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getClassifiName() {
        return classifiName;
    }

    public void setClassifiName(String classifiName) {
        this.classifiName = classifiName;
    }
}
