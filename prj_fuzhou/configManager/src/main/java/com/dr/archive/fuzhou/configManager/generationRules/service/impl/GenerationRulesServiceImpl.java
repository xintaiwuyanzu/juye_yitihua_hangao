package com.dr.archive.fuzhou.configManager.generationRules.service.impl;

import com.dr.archive.fuzhou.configManager.bo.GenerationRule;
import com.dr.archive.fuzhou.configManager.bo.GenerationRules;
import com.dr.archive.fuzhou.configManager.generationRules.service.GenerationRulesService;
import com.dr.archive.fuzhou.configManager.service.ConfigManagerClient;
import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.category.service.CategoryService;
import com.dr.archive.manage.form.service.ArchiveDataContext;
import com.dr.archive.manage.form.service.ArchiveDataPlugin;
import com.dr.archive.model.entity.AbstractArchiveEntity;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.form.core.service.FormDefinitionService;
import com.dr.framework.common.form.engine.model.core.FieldModel;
import com.dr.framework.common.form.engine.model.core.FormModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GenerationRulesServiceImpl implements GenerationRulesService, ArchiveDataPlugin {
    @Autowired
    ConfigManagerClient configManagerClient;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    CategoryService categoryService;
    @Autowired
    FormDefinitionService formDefinitionService;

    /**
     * 拦截数据添加场景
     *
     * @param data
     * @param context
     * @return
     */
    @Override
    public FormData beforeInsert(FormData data, ArchiveDataContext context) {
        String archiveCode = data.getString(AbstractArchiveEntity.COLUMN_ARCHIVE_CODE);
        //TODO 这里面的判断逻辑需要详细处理
        if (!StringUtils.hasText(archiveCode)) {
            archiveCode = doGenArchiveCode(data, context.getCategory().getCode());
            if (StringUtils.hasText(archiveCode)) {
                data.put(AbstractArchiveEntity.COLUMN_ARCHIVE_CODE, archiveCode);
            }
        }
        return data;
    }

    @Override
    public String genArchiveCode(FormData data, String categoryId) {
        Category category = categoryService.selectById(categoryId);
        Assert.isTrue(category != null, "未查询到指定的门类");
        return doGenArchiveCode(data, category.getCode());
    }

    @Override
    public List<GenerationRules> getGenerationRules(String cateGoryId, String year) {
        Category category = categoryService.selectById(cateGoryId);
        Assert.isTrue(category != null, "未查询到指定的门类");
        return doGetGenerationRules(category.getCode(), year);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 2;
    }

    protected List<GenerationRules> doGetGenerationRules(String categoryCode, String year) {
        List<GenerationRules> rules = configManagerClient.getGenerationRules(categoryCode, year);
        for (GenerationRules rule : rules) {
            if (StringUtils.hasText(rule.getRuleList())) {
                try {
                    List<GenerationRule> ruleList = objectMapper.readValue(rule.getRuleList(), objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, GenerationRule.class));
                    rule.setRules(ruleList.stream().sorted(Comparator.comparing(GenerationRule::getId)).collect(Collectors.toList()));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
        return rules;
    }

    /**
     * TODO 这里简单处理，将来逻辑复杂了还得详细处理
     *
     * @param data
     * @param categoryCode
     * @return
     */
    @Override
    public String doGenArchiveCode(FormData data, String categoryCode) {
        return doGenArchiveCode(data, categoryCode, null);
    }

    @Override
    public String doGenArchiveCode(FormData data, String categoryCode, FormModel formModel) {
        //先获取年度数据值
        String year = data.get(AbstractArchiveEntity.COLUMN_YEAR);
        if (StringUtils.hasText(year)) {
            List<GenerationRules> rules = doGetGenerationRules(categoryCode, year);
            if (rules != null && rules.size() == 1) {
                GenerationRules generationRules = rules.get(0);
                if (generationRules.getRules() != null) {
                    StringBuilder builder = new StringBuilder();
                    for (int i = 0; i < generationRules.getRules().size(); i++) {
                        GenerationRule rule = generationRules.getRules().get(i);
                        String fileNameOrAlias = rule.geteName();
                        if (formModel == null) {
                            formModel = formDefinitionService.selectFormDefinitionById(data.getFormDefinitionId());
                        }
                        FieldModel fieldModel = Optional.ofNullable(formModel.getFieldByAlias(fileNameOrAlias))
                                .orElse(formModel.getFieldByCode(fileNameOrAlias));
                        if (fieldModel != null) {
                            //获取字段值
                            // TODO 这里可能获取不到，还需要详细的处理
                            //TODO 这里的位数也没用到
                            String valueData = data.getString(fieldModel.getFieldCode());
                            if (i == 0) {
                                builder.append(valueData);
                            } else {
                                builder.append(rule.getSymbol()).append(valueData);
                            }
                        } else {
                            //TODO 元数据规则有错误，找不到指定的字段名称
                        }

                    }
                    return builder.toString();
                }
            }
        }
        return "";
    }

}