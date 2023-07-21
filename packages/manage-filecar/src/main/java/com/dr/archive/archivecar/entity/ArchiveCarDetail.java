package com.dr.archive.archivecar.entity;

import com.dr.archive.batch.entity.AbstractBatchDetailEntity;
import com.dr.archive.util.Constants;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * @Author: caor
 * @Date: 2022-01-18 19:33
 * @Description:
 */
@Table(name = Constants.TABLE_PREFIX + "CAR_DETAIL", comment = "档案车详情", module = Constants.MODULE_NAME)
public class ArchiveCarDetail extends AbstractBatchDetailEntity {
    @Column(comment = "档案车类型", length = 500)
    private String batchType;
    /**
     * 可能是字典，也可能是中文描述
     */
    @Column(comment = "档案标记编码", length = 500)
    private String archiveTag;
    @Column(comment = "描述", length = 500)
    private String description;


    public String getBatchType() {
        return batchType;
    }

    public void setBatchType(String batchType) {
        this.batchType = batchType;
    }

    public String getArchiveTag() {
        return archiveTag;
    }

    public void setArchiveTag(String archiveTag) {
        this.archiveTag = archiveTag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}