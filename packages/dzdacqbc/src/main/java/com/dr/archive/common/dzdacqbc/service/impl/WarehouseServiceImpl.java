package com.dr.archive.common.dzdacqbc.service.impl;

import com.dr.archive.common.dzdacqbc.entity.EArchive;
import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.category.service.CategoryService;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.fond.service.FondService;
import com.dr.archive.manage.form.service.ArchiveDataManager;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.form.core.model.FormData;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
@Transactional(rollbackFor = Exception.class)
public class WarehouseServiceImpl {

    @Autowired
    CommonMapper commonMapper;
    @Autowired
    FondService fondService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    ArchiveDataManager archiveDataManager;

    public void insertByFormData(String formDefinitionId, String formDataId) {
        FormData formData = archiveDataManager.selectOneFormData(formDefinitionId, formDataId);
        Fond fond = fondService.findFondByCode(formData.getString("FOND_CODE"));
        Assert.isTrue(!ObjectUtils.isEmpty(fond), "未找到对应全宗，请检查全宗号");
        Category category = categoryService.findCategoryByCode(formData.getString("CATE_GORY_CODE"), fond.getId());
        Assert.isTrue(!ObjectUtils.isEmpty(category), "未找到对应门类，请检查门类代码");
        EArchive eArchive = new EArchive(formData);
        eArchive.setFondCode(fond.getCode());
        eArchive.setCategoryCode(category.getCode());
        eArchive.setFormDefinitionId(formDefinitionId);
        eArchive.setFormDataId(formDataId);
        commonMapper.insert(eArchive);
    }
}
