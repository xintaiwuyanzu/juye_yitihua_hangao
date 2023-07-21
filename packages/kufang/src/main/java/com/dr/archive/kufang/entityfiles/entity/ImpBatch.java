package com.dr.archive.kufang.entityfiles.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * 实体档案辅助管理
 * 档案导入记录表
 */
@Table(name = Constants.TABLE_PREFIX + "ImpBatch", module = Constants.MODULE_NAME, comment = "实体档案")
public class ImpBatch extends BaseStatusEntity<String> {

    @Column(comment = "记录名称", length = 100)
    private String recordName;
    @Column(comment = "导入类型", length = 200)
    private String archiveType;
    @Column(comment = "类型", length = 200)
    private String remarks;
    @Column(comment = "导入数量", length = 200)
    private String quantity;
    @Column(comment = "导入成功数量", length = 200)
    private String quantitySuc;
    @Column(comment = "导入失败数量", length = 200)
    private String quantityErr;
    @Column(comment = "机构id", length = 100)
    private String organiseId;

    public String getQuantitySuc() {
        return quantitySuc;
    }

    public void setQuantitySuc(String quantitySuc) {
        this.quantitySuc = quantitySuc;
    }

    public String getQuantityErr() {
        return quantityErr;
    }

    public void setQuantityErr(String quantityErr) {
        this.quantityErr = quantityErr;
    }

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    public String getArchiveType() {
        return archiveType;
    }

    public void setArchiveType(String archiveType) {
        this.archiveType = archiveType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getOrganiseId() {
        return organiseId;
    }

    public void setOrganiseId(String organiseId) {
        this.organiseId = organiseId;
    }
}
