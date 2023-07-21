package com.dr.archive.archivecar.service.impl;

import com.dr.archive.archivecar.entity.ArchiveCarBatch;
import com.dr.archive.archivecar.entity.ArchiveCarDetail;
import com.dr.archive.archivecar.entity.ArchiveCarDetailInfo;
import com.dr.archive.archivecar.service.ArchiveCarBatchService;
import com.dr.archive.archivecar.service.ArchiveCarDetailService;
import com.dr.archive.batch.service.impl.AbstractArchiveBatchDetailUtils;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @Author: caor
 * @Date: 2022-01-18 19:39
 * @Description:
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ArchiveCarDetailServiceImpl extends AbstractArchiveBatchDetailUtils<ArchiveCarDetail> implements ArchiveCarDetailService {
    @Autowired
    ArchiveCarBatchService archiveCarBatchService;

    /**
     * 获取档案信息，添加详情数据后更新批次表数量
     *
     * @param entity
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public long insert(ArchiveCarDetail entity) {
        Assert.isTrue(StringUtils.hasText(entity.getBatchId()), "档案车编码不能为空");
        Assert.isTrue(StringUtils.hasText(entity.getFormDataId()), "表单数据Id不能为空");
        long sameBatchCount = count(
                SqlQuery.from(ArchiveCarDetail.class)
                        .equal(ArchiveCarDetailInfo.FORMDATAID, entity.getFormDataId())
                        .equal(ArchiveCarDetailInfo.BATCHID, entity.getBatchId())
        );
        ArchiveCarBatch archiveCarBatch = archiveCarBatchService.selectById(entity.getBatchId());

        if (sameBatchCount > 0) {
            //已经添加到档案车了
            return archiveCarBatch.getDetailNum();
        } else {
            Assert.isTrue(StringUtils.hasText(entity.getFormDefinitionId()), "表单定义Id不能为空");
            FormData formData = dataManager.selectOneFormData(entity.getFormDefinitionId(), entity.getFormDataId());

            initDetail(entity, formData);

            entity.setArchiveTag(entity.getArchiveTag());
            entity.setBatchType(archiveCarBatch.getBatchType());
            entity.setBatchId(entity.getBatchId());
            super.insert(entity);

            long total = countTotal(archiveCarBatch.getId());
            archiveCarBatch.setDetailNum(total);

            //更新批次详情总数
            commonMapper.updateIgnoreNullById(archiveCarBatch);

            return total;
        }
    }

    @Override
    public long deleteById(String... ids) {
        //只处理批量删除，单条数据删除有问题
        String[] idsArray = ids[0].split(",");
        for (String s : idsArray) {
            ArchiveCarBatch archiveCarBatch = archiveCarBatchService.selectById(commonMapper.selectById(ArchiveCarDetail.class, s).getBatchId());
            archiveCarBatch.setDetailNum(archiveCarBatch.getDetailNum() > 0 ? archiveCarBatch.getDetailNum() - 1 : 0);
            archiveCarBatchService.updateById(archiveCarBatch);
        }
        return super.deleteById(idsArray);
    }


    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<ArchiveCarDetail> selectByBatchAndTag(String batchId, String tag) {
        SqlQuery<ArchiveCarDetail> sqlQuery = SqlQuery.from(ArchiveCarDetail.class)
                .equal(ArchiveCarDetailInfo.BATCHID, batchId);
        if (StringUtils.hasText(tag) && !"all".equalsIgnoreCase(tag)) {
            sqlQuery.equal(ArchiveCarDetailInfo.ARCHIVETAG, tag);
        }
        return selectList(sqlQuery);
    }
}