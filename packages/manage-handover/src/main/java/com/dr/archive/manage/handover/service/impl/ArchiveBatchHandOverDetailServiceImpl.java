package com.dr.archive.manage.handover.service.impl;

import com.dr.archive.batch.service.impl.AbstractArchiveBatchDetailUtils;
import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.handover.entity.*;
import com.dr.archive.manage.handover.service.ArchiveBatchHandOverDetailService;
import com.dr.archive.manage.handover.service.ArchiveBatchHandOverService;
import com.dr.archive.manage.handover.vo.HandOverDetailVo;
import com.dr.archive.managefondsdescriptivefile.entity.Management;
import com.dr.archive.managefondsdescriptivefile.service.ManagementService;
import com.dr.archive.model.entity.ArchiveEntity;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.entity.StatusEntity;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.page.Page;
import com.dr.framework.common.service.CommonService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 到期移交详情service实现类
 *
 * @author dr
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ArchiveBatchHandOverDetailServiceImpl extends AbstractArchiveBatchDetailUtils<ArchiveBatchHandOverDetail> implements ArchiveBatchHandOverDetailService {
    @Autowired
    ManagementService managementService;
    @Autowired
    CommonMapper commonMapper;
    @Autowired
    ArchiveBatchHandOverService handOverService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ArchiveBatchHandOverDetail newDetail(ArchiveBatchHandOverLib batch, FormData formData, Fond fond, String detailType) {
        ArchiveBatchHandOverDetail detail = new ArchiveBatchHandOverDetail();
        detail.setDetailType(detailType);
        String cateCode = formData.getString(ArchiveEntity.COLUMN_CATEGORY_CODE);
        Category category = null;
        if (StringUtils.hasText(cateCode)) {
            category = categoryService.findCategoryByCode(cateCode, fond.getId());
        }
        initDetail(detail, batch, formData, fond, category);
        insert(detail);
        return detail;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addManageDetail(ArchiveBatchHandOver handOver) {
        String libIds = handOver.getLibIds();
        String[] libArr = libIds.split(",");
        //计算待移交库起止年度
        List<String> years = commonMapper.selectByQuery(SqlQuery.from(ArchiveBatchHandOverLib.class, false)
                .column(ArchiveBatchHandOverLibInfo.ARCHIVEYEAR)
                .in(ArchiveBatchHandOverLibInfo.ID, libArr)
                .groupBy(ArchiveBatchHandOverLibInfo.ARCHIVEYEAR)
                .setReturnClass(String.class)
        );
        years.sort(String::compareTo);
        String start = years.get(0);
        String end = start;
        if (years.size() > 1) {
            end = years.get(years.size() - 1);
        }
        for (Management management : managementService.getManagementList(handOver.getFondId(), start, end)) {
            doAddDetail(management, handOver.getId());
        }
    }

    private ArchiveBatchHandOverDetail doAddDetail(Management management, String id) {
        ArchiveBatchHandOverDetail detail = new ArchiveBatchHandOverDetail();
        detail.setArchiveCode(management.getArchive_code());
        detail.setBatchId(id);
        detail.setFondCode(management.getFond_code());
        detail.setFondName(management.getFondName());
        detail.setFormDataId(management.getId());
        detail.setArchiveCode(management.getArchive_code());

        //绑定档案基本信息
        detail.setStatus(StatusEntity.STATUS_DISABLE_STR);
        detail.setTitle(management.getTitle());
        detail.setYear(management.getVintagesStart());
        //绑定基本信息
        CommonService.bindCreateInfo(detail);
        insert(detail);
        return detail;
    }

    @Override
    public ArchiveBatchHandOverDetail addManageDetail(String batchId, String manageId) {
        Management management = managementService.selectById(manageId);
        return doAddDetail(management, batchId);
    }

    @Override
    public Page<HandOverDetailVo> getOverDetailData(String batchId, String handoverId, String archiveCode, int pageIndex, int pageSize) {
        SqlQuery<ArchiveBatchHandOverDetail> sqlQuery = SqlQuery.from(ArchiveBatchHandOverDetail.class);
        if (StringUtils.hasText(handoverId)) {
            ArchiveBatchHandOver handOver = handOverService.selectById(handoverId);
            String[] libIds = handOver.getLibIds().split(",");
            sqlQuery.in(ArchiveBatchHandOverDetailInfo.BATCHID, libIds);
        }
        if (StringUtils.hasText(batchId)) {
            sqlQuery.equal(ArchiveBatchHandOverDetailInfo.BATCHID, batchId);
        }
        if (StringUtils.hasText(archiveCode)) {
            sqlQuery.equal(ArchiveBatchHandOverDetailInfo.ARCHIVECODE, archiveCode);
        }
        sqlQuery.andNew().equal(ArchiveBatchHandOverDetailInfo.DETAILTYPE, "1").or().equal(ArchiveBatchHandOverDetailInfo.DETAILTYPE, "0");
        Page<HandOverDetailVo> details = commonMapper.selectPageByQuery(sqlQuery.setReturnClass(HandOverDetailVo.class), pageIndex * pageSize, (pageIndex + 1) * pageSize);
        for (HandOverDetailVo detail : details.getData()) {
            if ("1".equals(detail.getDetailType())) {
                //查询案卷下的卷内文件
                SqlQuery<ArchiveBatchHandOverDetail> sqlC = SqlQuery.from(ArchiveBatchHandOverDetail.class)
                        .like(ArchiveBatchHandOverDetailInfo.ARCHIVECODE, detail.getArchiveCode()).equal(ArchiveBatchHandOverDetailInfo.DETAILTYPE, "4");
                List<HandOverDetailVo> child = commonMapper.selectByQuery(sqlC.setReturnClass(HandOverDetailVo.class));
                detail.setVoList(child);
            }
        }
        return details;
    }

}
