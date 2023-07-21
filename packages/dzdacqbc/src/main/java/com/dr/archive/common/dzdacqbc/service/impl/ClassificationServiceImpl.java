package com.dr.archive.common.dzdacqbc.service.impl;

import com.dr.archive.common.dzdacqbc.entity.CqbcClassification;
import com.dr.archive.common.dzdacqbc.entity.CqbcClassificationInfo;
import com.dr.archive.common.dzdacqbc.service.ClassificationService;
import com.dr.archive.common.dzdacqbc.utils.Constants;
import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.category.service.CategoryService;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.fond.service.FondService;
import com.dr.framework.common.service.CacheAbleService;
import com.dr.framework.core.orm.sql.Column;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ClassificationServiceImpl extends CacheAbleService<CqbcClassification> implements ClassificationService {
    @Autowired
    FondService fondService;
    @Autowired
    CategoryService categoryService;

    @Override
    public long insert(CqbcClassification entity) {
        entity.setIsDefault(Optional.ofNullable(entity.getIsDefault()).orElse("false"));
        bindFondAndCategory(entity);
        return super.insert(entity);
    }

    @Override
    public long updateById(CqbcClassification entity) {
        bindFondAndCategory(entity);
        return super.updateById(entity);
    }

    @Override
    protected String getCacheName() {
        return "CqbcClassification";
    }

    private void bindFondAndCategory(CqbcClassification entity) {
        if (StringUtils.hasText(entity.getFondId())) {
            Fond fond = fondService.selectById(entity.getFondId());
            if (fond != null) {
                entity.setFondName(fond.getName());
                entity.setFondNo(fond.getCode());
            }
        }
        if (StringUtils.hasText(entity.getClassId())) {
            Category category = categoryService.selectById(entity.getClassId());
            if (category != null) {
                entity.setClassName(category.getName());
                entity.setClassCode(category.getCode());
            }
        }
    }


    /**
     * 设置默认
     *
     * @param cqbcClassification
     */
    @Override
    public void updateDefault(CqbcClassification cqbcClassification) {
        SqlQuery sqlQuery = SqlQuery.from(CqbcClassification.class).equal(new Column(Constants.DZDNCQBC + "classification", "isDefault", ""), "true");
        Object o = commonMapper.selectOneByQuery(sqlQuery);
        if (ObjectUtils.isNotEmpty(o)) {
            CqbcClassification c = (CqbcClassification) o;
            c.setIsDefault("false");
            //查询出现在为默认的，修改为非默认
            long result1 = commonMapper.updateById(c);
            if (result1 > 0L) {
                this.cache.evictIfPresent(c.getId());
            }
        }
        //将待修改的改为默认
        long result2 = commonMapper.updateById(cqbcClassification);
        if (result2 > 0L) {
            this.cache.evictIfPresent(cqbcClassification.getId());
        }
    }

    /**
     * 根据全宗和门类id找到对应分类
     *
     * @param fondId
     * @param categoryId
     * @return
     */
    @Override
    public List<CqbcClassification> findClassByFondAndCategory(String fondId, String categoryId) {
        return commonMapper.selectByQuery(SqlQuery.from(CqbcClassification.class).equal(CqbcClassificationInfo.FONDID, fondId).and().equal(CqbcClassificationInfo.CLASSID, categoryId));
    }

    /**
     * 获得默认分类
     *
     * @return
     */
    @Override
    public CqbcClassification getDefaultClassification() {
        return commonMapper.selectOneByQuery(SqlQuery.from(CqbcClassification.class).equal(CqbcClassificationInfo.ISDEFAULT, "true"));
    }
}
