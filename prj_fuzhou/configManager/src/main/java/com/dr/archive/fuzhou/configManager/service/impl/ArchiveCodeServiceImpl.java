package com.dr.archive.fuzhou.configManager.service.impl;

import com.dr.archive.fuzhou.configManager.bo.GenerationRules;
import com.dr.archive.fuzhou.configManager.generationRules.service.GenerationRulesService;
import com.dr.archive.fuzhou.configManager.service.ArchiveCodeService;
import com.dr.archive.fuzhou.configManager.service.ConfigManagerClient;
import com.dr.archive.manage.form.service.impl.DefaultArchiveDataManager;
import com.dr.archive.batch.query.BatchCreateQuery;
import com.dr.archive.model.entity.ArchiveEntity;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.form.core.service.FormDefinitionService;
import com.dr.framework.common.form.engine.model.core.FormModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @Author: caor
 * @Date: 2022-01-06 15:15
 * @Description:
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ArchiveCodeServiceImpl implements ArchiveCodeService {
    @Autowired
    ConfigManagerClient client;
    @Autowired
    DefaultArchiveDataManager defaultArchiveDataManager;
    @Autowired
    GenerationRulesService generationRulesService;
    @Autowired
    FormDefinitionService formDefinitionService;

    @Override
    public String getArchiveByCategoryCodeAndYear(String typeCode, BatchCreateQuery query) {
        StringBuilder result = new StringBuilder();
        //查询档案数据 有档号的、重复的档号、字段为空的需要提示
        List<FormData> formDataList = defaultArchiveDataManager.findDataByQuery(query);
        for (FormData formData : formDataList) {
            formData.get(ArchiveEntity.COLUMN_YEAR);
            if (StringUtils.isEmpty(formData.get(ArchiveEntity.COLUMN_YEAR))) {
                result.append("题名【").append(formData.getString(ArchiveEntity.COLUMN_TITLE)).append("】的年度为空！");
            } else {
                //获取规则，没有规则的需要提示
                List<GenerationRules> generationRulesList = client.getGenerationRules(typeCode, formData.get(ArchiveEntity.COLUMN_YEAR));
                FormModel formModel = formDefinitionService.selectFormDefinitionById(formData.getFormDefinitionId());
                String archiveCode = generationRulesService.doGenArchiveCode(formData, typeCode, formModel);
                formData.put(ArchiveEntity.COLUMN_ARCHIVE_CODE, archiveCode);
                defaultArchiveDataManager.updateFormData(formData, query.getFondId(), query.getCategoryId());
            }
        }
        return result.toString();
    }
}
