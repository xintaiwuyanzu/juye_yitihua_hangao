package com.dr.archive.managearchivechange.service.impl;

import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.category.entity.CategoryInfo;
import com.dr.archive.manage.category.service.CategoryService;
import com.dr.archive.managearchivechange.entity.ErrorCorrection;
import com.dr.archive.managearchivechange.entity.ErrorCorrectionInfo;
import com.dr.archive.managearchivechange.service.ErrorCorrectionService;
import com.dr.framework.common.entity.StatusEntity;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: caor
 * @Date: 2022-03-25 14:59
 * @Description:
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ErrorCorrectionServiceImpl extends DefaultBaseService<ErrorCorrection> implements ErrorCorrectionService {
    @Autowired
    CategoryService categoryService;
    @Override
    public long insert(ErrorCorrection entity) {
        //默认状态为0 待纠错
        if (StringUtils.isEmpty(entity.getStatus())) {
            entity.setStatus(StatusEntity.STATUS_DISABLE_STR);
        }
        //默认类型为2 推送
        if (StringUtils.isEmpty(entity.getErrorType())) {
            entity.setErrorType("2");
        }
        //默认添加人名称
        if (StringUtils.isEmpty(entity.getCreatePersonName())) {
            entity.setCreatePersonName(SecurityHolder.get().currentPerson().getUserName());
        }
        //默认添加机构id
        if (StringUtils.isEmpty(entity.getOrgId())) {
            entity.setOrgId(SecurityHolder.get().currentOrganise().getId());
        }
        if (!StringUtils.isEmpty(entity.getCategoryCode())){
            Category category = categoryService.selectOne(SqlQuery.from(Category.class).equal(CategoryInfo.CODE, entity.getCategoryCode()));
            entity.setCategoriesId(category.getId());
        }
        return super.insert(entity);
    }
}
