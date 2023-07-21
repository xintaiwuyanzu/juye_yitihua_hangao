package com.dr.archive.batch.service.impl;

import com.dr.archive.batch.entity.AbstractArchiveBatch;
import com.dr.archive.batch.entity.AbstractBatchDetailEntity;
import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.category.service.CategoryService;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.fond.service.FondService;
import com.dr.archive.manage.form.service.ArchiveDataManager;
import com.dr.archive.model.entity.ArchiveEntity;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.organise.service.OrganisePersonService;
import com.dr.framework.core.orm.sql.Column;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 最抽象档案详情表接口实现类
 *
 * @author dr
 */
public class AbstractArchiveBatchDetailUtils<D extends AbstractBatchDetailEntity> extends DefaultBaseService<D> {
    @Autowired
    protected ArchiveDataManager dataManager;
    @Autowired
    protected FondService fondService;
    @Autowired
    protected CategoryService categoryService;
    @Autowired
    protected OrganisePersonService organisePersonService;


    @Transactional(readOnly = true)
    public long countTotal(String batchId) {
        Column batchIdColumn = entityRelation.getColumn("batchId");
        Class entityClass = getEntityClass();
        return count(SqlQuery.from(entityClass).equal(batchIdColumn, batchId));
    }

    /**
     * 根据表单创建详情
     * <p>
     * 从表单数据中提取全宗和门类信息
     *
     * @param detail
     * @param formData
     */
    public void initDetail(D detail, FormData formData) {
        Fond fond = null;
        Category category = null;
        String fondCode = formData.getString(ArchiveEntity.COLUMN_FOND_CODE);
        if (StringUtils.hasText(fondCode)) {
            fond = fondService.findFondByCode(fondCode);
            if (fond != null) {
                String cateGoryCode = formData.getString(ArchiveEntity.COLUMN_CATEGORY_CODE);
                if (StringUtils.hasText(cateGoryCode)) {
                    category = categoryService.findCategoryByCode(cateGoryCode, fond.getId());
                }
            }
        }
        initDetail(detail, null, formData, fond, category);
    }

    public void initDetail(D detail, AbstractArchiveBatch batch, FormData formData, @Nullable Fond fond, @Nullable Category category) {
        if (batch != null) {
            //绑定批次
            detail.setBatchId(batch.getId());
        }
        detail.bindAll(formData, fond, category, true);
    }


}
