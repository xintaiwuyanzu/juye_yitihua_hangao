package com.dr.archive.fuzhou.configManager.service.impl;

import com.dr.archive.fuzhou.configManager.bo.SaveTermBo;
import com.dr.archive.fuzhou.configManager.service.ConfigManagerClient;
import com.dr.archive.fuzhou.configManager.service.SaveTermBoService;
import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.category.service.CategoryService;
import com.dr.archive.manage.fond.service.FondService;
import com.dr.archive.managefondsdescriptivefile.service.ManagementService;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @Author: caor
 * @Date: 2022-04-22 17:14
 * @Description:
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SaveTermBoServiceImpl implements SaveTermBoService {
    @Autowired
    ConfigManagerClient configManagerClient;
    @Autowired
    ManagementService managementService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    FondService fondService;

    @Override
    public String getCompilationContent(String fondId) {
        List<SaveTermBo> saveTermBoList = configManagerClient.getSaveTermBo(SecurityHolder.get().currentOrganise().getOrganiseCode());
        StringBuilder stringBuilder = new StringBuilder();
        saveTermBoList.forEach(saveTermBo -> {
            Category category = categoryService.findCategoryByCode(saveTermBo.getCode(), fondId);
            stringBuilder.append(Optional.ofNullable(category.getName()).orElse("")).append("【").append(saveTermBo.getCode()).append("】的保管期限为：").append(saveTermBo.getPeriod()).append("，优先级：").append(saveTermBo.getPriority()).append("; ");
        });
        return stringBuilder.toString();
    }
}
