package com.dr.archive.common.dzdacqbc.service.impl;

import com.dr.archive.common.dzdacqbc.entity.*;
import com.dr.archive.common.dzdacqbc.service.*;
import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.category.entity.CategoryConfig;
import com.dr.archive.manage.category.entity.CategoryInfo;
import com.dr.archive.manage.category.service.CategoryConfigService;
import com.dr.archive.manage.category.service.CategoryService;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.fond.service.FondService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class ESaveStrategyServiceImpl extends DefaultBaseService<ESaveStrategy> implements ESaveStrategyService {
    @Autowired
    FondService fondService;
    @Autowired
    ESaveSpacesService eSaveSpacesService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    ESaveBackBatchService eSaveBackBatchService;
    @Autowired
    EArchiveService eArchiveService;
    @Autowired
    ESaveBackBatchDetailService eSaveBackBatchDetailService;
    @Autowired
    CategoryConfigService categoryConfigService;

    /**
     * 处理数据
     */
    @Override
    public ESaveStrategy dealData(ESaveStrategy strategy){
        if (StringUtils.hasText(strategy.getFondId())) {
            Fond fond = fondService.selectById(strategy.getFondId());
            if (fond != null) {
                strategy.setFond_code(fond.getCode());
                strategy.setFond_name(fond.getName());
            }
        }
        if (StringUtils.hasText(strategy.getClassId())) {
            Category category = categoryService.selectById(strategy.getClassId());
            if (category != null) {
                strategy.setClassName(category.getName());
                strategy.setClassCode(category.getCode());
            }
        }
        if (StringUtils.hasText(strategy.getSpacesId())) {
            ESaveSpaces spaces = eSaveSpacesService.selectById(strategy.getSpacesId());
            if (spaces != null) {
                strategy.setSpaceName(spaces.getSpaceName());
            }
        }
        strategy.setOrgId(SecurityHolder.get().currentOrganise().getId());
        return strategy;
    }
    /**
     * 获得当前全宗下，绑定过表单的门类
     */
    @Override
    public List<Category> getCateByBind(String fondId) {
        List<Category> total = new ArrayList<>();
        List<Category> list = commonMapper.selectByQuery(SqlQuery.from(Category.class).equal(CategoryInfo.PARENTID, fondId));
        for (Category parent : list) {
            CategoryConfig config = categoryConfigService.selectOneByCategoryId(parent.getId());
            if(config != null){
                total.add(parent);
            }
            List<Category> children = getChild(parent.getId(), parent.getName());
            total.addAll(children);
        }

        return total;
    }

    public List<Category> getChild(String pid, String pName){
        List<Category> children = commonMapper.selectByQuery(SqlQuery.from(Category.class).equal(CategoryInfo.PARENTID, pid));
        for (Category category : children) {
            CategoryConfig configc = categoryConfigService.selectOneByCategoryId(category.getId());
            if(configc != null){
                category.setName(pName + "-" + category.getName());
            }
        }
        return children;
    }


}
