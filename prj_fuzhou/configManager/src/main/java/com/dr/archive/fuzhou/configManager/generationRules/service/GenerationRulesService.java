package com.dr.archive.fuzhou.configManager.generationRules.service;

import com.dr.archive.fuzhou.configManager.bo.GenerationRules;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.form.engine.model.core.FormModel;

import java.util.List;

public interface GenerationRulesService {
    /**
     * 根据门类年度获取档号生成规则配置
     *
     * @param cateGoryId
     * @param year
     * @return
     */
    List<GenerationRules> getGenerationRules(String cateGoryId, String year);

    /**
     * 根据表单数据和门类Id生成档号
     *
     * @param data
     * @param categoryId
     * @return
     */
    String genArchiveCode(FormData data, String categoryId);

    String doGenArchiveCode(FormData data, String categoryCode);

    String doGenArchiveCode(FormData data, String categoryCode, FormModel formModel);
}
