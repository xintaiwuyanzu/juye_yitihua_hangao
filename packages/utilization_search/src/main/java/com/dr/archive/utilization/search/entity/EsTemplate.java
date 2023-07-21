package com.dr.archive.utilization.search.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseOrderedEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.ColumnType;
import com.dr.framework.core.orm.annotations.Table;

/**
 * es动态模板配置表
 *
 * @author qyf
 */
@Table(name = com.dr.archive.util.Constants.TABLE_PREFIX + "ESTEMPLATE", module = Constants.MODULE_NAME, comment = "es动态模板配置表")
public class EsTemplate extends BaseOrderedEntity {
    @Column(comment = "模板类型")
    private String templateType; // 1 索引模板；2 查询模板
    @Column(comment = "模板内容", type = ColumnType.CLOB)
    private String templateContent;
    @Column(comment = "描述")
    private String description;
    @Column(comment = "模板名称")
    private String templateName;

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTemplateContent() {
        return templateContent;
    }

    public void setTemplateContent(String templateContent) {
        this.templateContent = templateContent;
    }

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }
}
