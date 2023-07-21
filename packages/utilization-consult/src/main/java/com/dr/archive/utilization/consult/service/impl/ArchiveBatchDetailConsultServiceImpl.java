package com.dr.archive.utilization.consult.service.impl;

import com.dr.archive.archivecar.entity.ArchiveCarDetail;
import com.dr.archive.batch.service.impl.AbstractArchiveBatchDetailServiceImpl;
import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.utilization.consult.entity.ArchiveBatchConsult;
import com.dr.archive.utilization.consult.entity.ArchiveBatchDetailConsult;
import com.dr.archive.utilization.consult.entity.ArchiveBatchDetailConsultInfo;
import com.dr.archive.utilization.consult.service.ArchiveBatchDetailConsultService;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 查档详情service
 *
 * @author dr
 */
@Service
public class ArchiveBatchDetailConsultServiceImpl extends AbstractArchiveBatchDetailServiceImpl<ArchiveBatchDetailConsult> implements ArchiveBatchDetailConsultService {

    /**
     * 前端全文检索页面添加查档车按钮
     *
     * @param entity
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public long insert(ArchiveBatchDetailConsult entity) {
        SqlQuery<ArchiveBatchDetailConsult> sideSqlQuery =
                SqlQuery.from(ArchiveBatchDetailConsult.class)
                        .equal(ArchiveBatchDetailConsultInfo.BATCHID, entity.getBatchId())
                        .equal(ArchiveBatchDetailConsultInfo.FORMDATAID, entity.getFormDataId());
        //根据批次和数据id查询数据
        long saved = count(sideSqlQuery);
        if (saved > 0) {
            return 0;
        }
        Fond fond = fondService.findFondByCode(entity.getFondCode());
        Category category = categoryService.findCategoryByCode(entity.getCategoryCode(), fond.getId());
        FormData formData = dataManager.selectOneFormData(entity.getFormDefinitionId(), entity.getFormDataId());
        initDetail(entity, null, formData, fond, category);
        return super.insert(entity);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void copyDetail(ArchiveCarDetail detail, ArchiveBatchConsult batchConsult) {
        ArchiveBatchDetailConsult detailConsult = new ArchiveBatchDetailConsult();
        detail.cloneArchive(detailConsult);
        detailConsult.setBatchId(batchConsult.getId());
        insert(detailConsult);
    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

}
