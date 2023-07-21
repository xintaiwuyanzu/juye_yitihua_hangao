package com.dr.archive.utilization.search.service;

import com.dr.framework.common.form.engine.model.core.FormModel;

/**
 * 档案索引名称生成器
 *
 * @author dr
 */
public interface ArchiveIndexNameMaker {
    /**
     * 索引名称前缀
     */
    String INDEX_PREFIX = "archive_";

    /**
     * 创建索引名称
     *
     * @param formModel
     * @return
     */
    String makeIndexName(FormModel formModel);

}
