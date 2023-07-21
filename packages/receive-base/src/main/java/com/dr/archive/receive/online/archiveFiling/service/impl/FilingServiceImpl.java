package com.dr.archive.receive.online.archiveFiling.service.impl;

import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.category.service.CategoryService;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.fond.service.FondService;
import com.dr.archive.manage.form.service.ArchiveDataManager;
import com.dr.archive.model.entity.ArchiveEntity;
import com.dr.archive.model.query.ArchiveDataQuery;
import com.dr.archive.receive.online.archiveFiling.entity.FilingDetail;
import com.dr.archive.receive.online.archiveFiling.entity.FilingDetailInfo;
import com.dr.archive.receive.online.archiveFiling.service.FilingService;
import com.dr.archive.util.UUIDUtils;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.process.service.ProcessContext;
import com.dr.framework.core.process.service.ProcessTypeProvider;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 在线接收入库操作
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FilingServiceImpl extends DefaultBaseService<FilingDetail> implements FilingService, ProcessTypeProvider {
    public static final String DATE_PATTEN = "yyyy-MM-dd HH:mm:ss";
    @Autowired
    CommonMapper commonMapper;
    @Autowired
    ArchiveDataManager dataManager;
    @Autowired
    FondService fondService;
    @Autowired
    CategoryService categoryService;

    /**
     * 判断待申请的档案中是否有在申请档案
     *
     * @param dataQuery
     * @return
     */
    @Override
    public boolean checkFiling(ArchiveDataQuery dataQuery) {
        List<FormData> archiveDatas = dataManager.findDataByQuery(dataQuery);
        if (archiveDatas.isEmpty()) {
            return true;
        } else {
            return commonMapper.countByQuery(SqlQuery.from(FilingDetail.class)
                    .in(FilingDetailInfo.FORMDATAID,
                            archiveDatas.stream()
                                    .map(FormData::getId)
                                    .collect(Collectors.toList())
                    ).equal(FilingDetailInfo.STATUS, "0")) == 0;
        }
    }


    @Override
    public void onBeforeStartProcess(ProcessContext context) {
        //启动前设置流程名称
        context.setProcessInstanceTitle(DateFormatUtils.format(new Date(), DATE_PATTEN) + "入库审核");
    }

    @Override
    public void onAfterStartProcess(ProcessContext context) {
        //保存业务数据
        String formDefinitionId = (String) context.getBusinessParams().get("formDefinitionId");
        Assert.isTrue(StringUtils.hasText(formDefinitionId), "表单id为空");
        //向任务详情表加入档案详情 根据条件查询档案数据
        ArchiveDataQuery query = buildArchiveDataQuery(context.getBusinessParams());
        List<FormData> formDataList = dataManager.findDataByQuery(query);
        for (FormData data : formDataList) {
            //查询全宗
            Fond fond = fondService.findFondByCode(data.get(ArchiveEntity.COLUMN_FOND_CODE));
            //查询门类
            Category category = categoryService.findCategoryByCode(data.get(ArchiveEntity.COLUMN_CATEGORY_CODE), fond.getId());
            FilingDetail detail = initFilingDetail(data, context, fond, category);
            insert(detail);
            //TODO 案卷文件类型的档案入库
        }
    }

    private FilingDetail initFilingDetail(FormData data, ProcessContext context, Fond fond, Category category) {
        FilingDetail detail = new FilingDetail();
        //直接设置业务外键为流程实例Id
        detail.setFilingId(context.getProcessInstance().getId());
        detail.setId(UUIDUtils.getUUID());
        detail.setStatus("0");
        detail.setFondName(fond.getName());
        detail.setFondCode(fond.getCode());
        detail.setCategoryName(category.getName());
        detail.setCategoryCode(category.getCode());
        detail.setCategoryId(category.getId());
        detail.setFormDefinitionId(data.getFormDefinitionId());
        detail.setFormDataId(data.getId());
        detail.setTitle(data.get(ArchiveEntity.COLUMN_TITLE));
        detail.setArchiveCode(data.get(ArchiveEntity.COLUMN_ARCHIVE_CODE));
        detail.setVintages(data.get(ArchiveEntity.COLUMN_YEAR));
        detail.setSaveTerm(data.get(ArchiveEntity.COLUMN_SAVE_TERM));
        detail.setYW_HAVE(data.get(ArchiveEntity.COLUMN_YW_HAVE));
        return detail;
    }

    @Override
    public List<FilingDetail> queryArchiveDetail(String id, String type) {
        return commonMapper.selectByQuery(SqlQuery.from(FilingDetail.class).equal(FilingDetailInfo.FILINGID, id));
    }

    /**
     * 办结事项
     *
     * @param type       ”1“通过， ”0“驳回
     * @param id         taskId
     * @param suggestion 审核意见
     */
    @Override
    public void examine(String type, String id, String suggestion) {
    }

    /**
     * 根据HashMap构造档案查询参数
     *
     * @param params
     * @return
     */
    protected ArchiveDataQuery buildArchiveDataQuery(Map<String, Object> params) {
        ArchiveDataQuery query = new ArchiveDataQuery();
        try {
            BeanUtils.copyProperties(query, params);
        } catch (BeansException e) {
            e.printStackTrace();
        }
        query.parseQuery((String) params.get(ArchiveDataQuery.QUERY_KEY));
        return query;
    }

    /**
     * 待办任务跳转详情页面
     *
     * @param context
     * @return
     */
    @Override
    public String getFormUrl(ProcessContext context) {
        return "/archive/filing/filingRecord/detail";
    }

    @Override
    public String getType() {
        return "archive_filling";
    }

    @Override
    public String getName() {
        return "档案入库";
    }
}
