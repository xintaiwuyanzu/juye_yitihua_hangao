package com.dr.archive.manage.form.service;

import java.util.Map;

/**
 * 用来构造
 *
 * @author dr
 * @see ArchiveDataContext
 */
public interface ArchiveDataContextFactory {
    /**
     * 根据参数创建context
     *
     * @param categoryId       分类Id
     * @param formDefinitionId 表单定义Id
     * @param params           额外参数
     * @return
     */
    default ArchiveDataContext buildContext(String categoryId, String formDefinitionId, Map<String, Object> params) {
        ArchiveDataContext context = buildContext(categoryId, formDefinitionId);
        context.addParams(params);
        return context;
    }

    /**
     * 根据参数创建context
     *
     * @param categoryId       分类Id
     * @param formDefinitionId 表单定义Id
     * @return
     */
    ArchiveDataContext buildContext(String categoryId, String formDefinitionId);

}
