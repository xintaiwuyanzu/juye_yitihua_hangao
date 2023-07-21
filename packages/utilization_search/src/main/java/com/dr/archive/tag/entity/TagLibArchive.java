package com.dr.archive.tag.entity;

import com.dr.archive.model.entity.AbstractArchiveRelateEntity;
import com.dr.archive.util.Constants;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * @author: qiuyf
 * @date: 2022/3/14 18:30
 * 档案与标签库关系表
 */
@Table(name = com.dr.archive.util.Constants.TABLE_PREFIX + "TAG_LIB_ARCHIVE", module = Constants.MODULE_NAME, comment = "标签档案关系表")
public class TagLibArchive extends AbstractArchiveRelateEntity {
    //国家标准库 对应实体类StdTag
    public static final String SOURCETYPE_GUOJIA = "GUOJIA";
    //基础事实 对应实体类FactTag
    public static final String SOURCETYPE_JICHUSHISHI = "JICHUSHISHI";
    //基础事实 对应实体类FactTag
    public static final String SOURCETYPE_ZIDINGYI = "ZIDINGYI";
    @Column(comment = "标签库id")
    private String tagLibId;
    @Column(comment = "标签名")
    private String tagName;
    @Column(comment = "完整名称")
    private String fullLable;

    @Column(comment = "添加人姓名")
    private String createPersonName;

    @Column(comment = "状态", name = "status_info")
    private String status;

    /**
     * 国家库、事实、自定义
     */
    @Column(comment = "数据来源")
    private String sourceType;

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getTagLibId() {
        return tagLibId;
    }

    public void setTagLibId(String tagLibId) {
        this.tagLibId = tagLibId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getFullLable() {
        return fullLable;
    }

    public void setFullLable(String fullLable) {
        this.fullLable = fullLable;
    }

    public String getCreatePersonName() {
        return createPersonName;
    }

    public void setCreatePersonName(String createPersonName) {
        this.createPersonName = createPersonName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
