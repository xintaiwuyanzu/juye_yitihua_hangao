package com.dr.archive.specialTag.entity;

import com.dr.archive.model.entity.AbstractArchiveRelateEntity;
import com.dr.archive.util.Constants;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * @author: qiuyf
 * 专题标签与档案关系表
 */
@Table(name = com.dr.archive.util.Constants.TABLE_PREFIX + "SPECIAL_TAG_ARCHIVE", module = Constants.MODULE_NAME, comment = "专题标签档案关系表")
public class SpecialTagArchive extends AbstractArchiveRelateEntity {
    @Column(comment = "标签库id")
    private String specialTagId;
    @Column(comment = "标签名")
    private String specialTagName;

    public String getSpecialTagId() {
        return specialTagId;
    }

    public void setSpecialTagId(String specialTagId) {
        this.specialTagId = specialTagId;
    }

    public String getSpecialTagName() {
        return specialTagName;
    }

    public void setSpecialTagName(String specialTagName) {
        this.specialTagName = specialTagName;
    }
}
