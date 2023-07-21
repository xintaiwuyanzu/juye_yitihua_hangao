package com.dr.archive.specialTag.service.impl;

import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.category.service.CategoryService;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.fond.service.FondService;
import com.dr.archive.manage.form.service.ArchiveDataManager;
import com.dr.archive.specialTag.entity.SpecialTag;
import com.dr.archive.specialTag.entity.SpecialTagArchive;
import com.dr.archive.specialTag.entity.SpecialTagArchiveInfo;
import com.dr.archive.specialTag.entity.SpecialTagInfo;
import com.dr.archive.specialTag.service.SpecialTagArchiveService;
import com.dr.archive.specialTag.service.SpecialTagService;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

import static com.dr.archive.model.entity.ArchiveEntity.COLUMN_CATEGORY_CODE;
import static com.dr.archive.model.entity.ArchiveEntity.COLUMN_FOND_CODE;

/**
 * @author: qiuyf
 * @date: 2022/6/18 14:09
 */
@Service
public class SpecialTagArchiveServiceImpl extends DefaultBaseService<SpecialTagArchive> implements SpecialTagArchiveService {
    @Autowired
    ArchiveDataManager dataManager;
    @Autowired
    protected FondService fondService;
    @Autowired
    protected CategoryService categoryService;
    @Autowired
    SpecialTagService specialTagService;

    @Override
    public long addSpecialTag(String tagId, String formDataId, String formDefinitionId) {
        SpecialTag specialTag = specialTagService.selectById(tagId);
        Assert.isTrue(specialTag != null, "该标签不在专题标签库中!");
        List<SpecialTagArchive> specialTagArchives = selectList(SqlQuery.from(SpecialTagArchive.class)
                .equal(SpecialTagArchiveInfo.SPECIALTAGID, specialTag.getId())
                .equal(SpecialTagArchiveInfo.FORMDEFINITIONID, formDefinitionId)
                .equal(SpecialTagArchiveInfo.FORMDATAID, formDataId));
        Assert.isTrue(specialTagArchives.size() == 0, "该档案已有该专题标签,不可重复添加!");
        FormData formData = dataManager.selectOneFormData(formDefinitionId, formDataId);
        SpecialTagArchive specialTagArchive = new SpecialTagArchive();
        specialTagArchive.setSpecialTagId(specialTag.getId());
        specialTagArchive.setSpecialTagName(specialTag.getTagName());
        specialTagArchive.bindFormData(formData);
        Fond fond = fondService.findFondByCode(formData.getString(COLUMN_FOND_CODE));
        if (fond != null) {
            specialTagArchive.bindFondInfo(fond);
            Category category = categoryService.findCategoryByCode(formData.getString(COLUMN_CATEGORY_CODE), fond.getId());
            if (category != null) {
                specialTagArchive.bindCategoryInfo(category);
            }
        }

        return insert(specialTagArchive);
    }

    @Override
    public SqlQuery selectTagCount(SpecialTag specialTag) {
        SqlQuery sqlQuery = SqlQuery.from(SpecialTag.class, false)
                .leftOuterJoin(SpecialTagInfo.ID, SpecialTagArchiveInfo.SPECIALTAGID)
                .column(SpecialTagInfo.ID.alias("tagLibId"))
                .column(SpecialTagInfo.TAGNAME)
                .column(SpecialTagInfo.TAGDESCRIBE)
                .column(SpecialTagArchiveInfo.FORMDATAID.count().alias("archiveCount"))
                .equal(SpecialTagInfo.LEAF, 1);
        if (!StringUtils.isEmpty(specialTag.getTagName())) {
            sqlQuery.like(SpecialTagInfo.TAGNAME, specialTag.getTagName());
        }
        sqlQuery.groupBy(SpecialTagInfo.ID)
                .groupBy(SpecialTagInfo.TAGNAME)
                .orderByDesc(SpecialTagArchiveInfo.FORMDATAID.count().alias("archiveCount"), SpecialTagInfo.CREATEDATE.max())
                .setReturnClass(Map.class);
        return sqlQuery;
    }
}
