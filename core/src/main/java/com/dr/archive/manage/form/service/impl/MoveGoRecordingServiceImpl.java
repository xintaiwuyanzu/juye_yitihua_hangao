package com.dr.archive.manage.form.service.impl;

import com.dr.archive.event.ArchiveDataEditEvent;
import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.category.entity.CategoryConfig;
import com.dr.archive.manage.category.entity.CategoryConfigInfo;
import com.dr.archive.manage.category.service.CategoryConfigService;
import com.dr.archive.manage.category.service.CategoryService;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.fond.service.FondService;
import com.dr.archive.manage.form.entity.MoveGoRecording;
import com.dr.archive.manage.form.service.ArchiveDataManager;
import com.dr.archive.manage.form.service.MoveGoRecordingService;
import com.dr.archive.model.entity.ArchiveEntity;
import com.dr.archive.model.entity.ArchiveOrder;
import com.dr.archive.model.entity.ArchiveOrderInfo;
import com.dr.archive.model.query.ArchiveDataQuery;
import com.dr.archive.util.SecurityHolderUtil;
import com.dr.framework.common.file.model.FileInfo;
import com.dr.framework.common.file.service.CommonFileService;
import com.dr.framework.common.form.core.entity.FormDefinition;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.form.core.service.FormDataService;
import com.dr.framework.common.form.core.service.FormDefinitionService;
import com.dr.framework.common.form.core.service.SqlBuilder;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.organise.service.OrganisePersonService;
import com.dr.framework.core.orm.jdbc.Column;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import com.dr.framework.core.web.annotations.Current;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MoveGoRecordingServiceImpl extends DefaultBaseService<MoveGoRecording> implements MoveGoRecordingService {
    @Autowired
    FormDataService formDataService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    CategoryConfigService categoryConfigService;
    @Autowired
    FormDefinitionService formDefinitionService;
    @Autowired
    CommonFileService commonFileService;
    @Autowired
    ArchiveDataManager dataManager;
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    FondService fondService;
    @Autowired
    OrganisePersonService organisePersonService;
    @Async
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCategoryByQuery(ArchiveDataQuery query, String newCategoryId, String oldCategoryId,Person person) {
        Category category = categoryService.selectById(newCategoryId);
        Category category1 = categoryService.selectById(category.getParentId());
        CategoryConfig categoryConfig = categoryConfigService.selectOneByCategoryId(newCategoryId);
        List<FormData> formDataList = formDataService.selectFormData(query.getFormDefinitionId(), newBuilder(query));
        //通过表单id查到其表单数据
        FormDefinition formDefinition = (FormDefinition)formDefinitionService.selectFormDefinitionById(query.getFormDefinitionId());
        for (FormData formData : formDataList) {
            String formDataId = formData.getId();
            MoveGoRecording moveGoRecording = new MoveGoRecording();
            moveGoRecording.setOrganName(formData.get("ORG_CODE"));
            moveGoRecording.setWJLX(formData.get("WJLX"));
            //因为下面要删除表单数据，如果是案卷类型表单，查到它对应的卷内表单数据
            List<FormData> wjs = null;
            if (!org.apache.commons.lang3.StringUtils.isEmpty(formData.get(ArchiveEntity.COLUMN_ARCHIVE_CODE))){
                wjs = getWJS(formData);
            }
            String id= UUID.randomUUID().toString();
            //TODO 新增时有可能获取不到人员
            String personId = SecurityHolder.get() == null ? "admin" : SecurityHolder.get().currentPerson().getId();
            SecurityHolder securityHolder = SecurityHolderUtil.checkSecurityHolder(organisePersonService, personId);
            SecurityHolder.set(securityHolder);
            //查到旧档案的原文，给档案一个新的id并添加原文
            List<FileInfo> fileInfos = commonFileService.list(formData.getId(), "archive", "default");
            fileInfos.forEach(fileInfo -> {
                commonFileService.addFile(fileInfo.getFileHash(), id, "archive");
            });
            //删除旧档案及其原文
            dataManager.deleteFormData(query.getCategoryId(), formData.getFormDefinitionId(), formDataId);
            formData.setId(id);
            formData.put("CATE_GORY_CODE", category.getCode());
            formData.put("WJLX", category.getName());
            //判断旧档案所在表单是否为案卷表单
            if ("1".equals(formDefinition.getFormType())){
                //设置表单id为新表单的案卷类型
                formData.setFormDefinitionId(categoryConfig.getArcFormId());
                CategoryConfig cateConfig = categoryConfigService.selectOneByCategoryId(query.getCategoryId());
                //如果有卷内文件，也作相应移动（同案卷）
                if ((wjs != null ? wjs.size() : 0) >0){
                    for (FormData wj : wjs) {
                        String id1=UUID.randomUUID().toString();
                        List<FileInfo> fileInfo = commonFileService.list(wj.getId(), "archive", "default");
                        fileInfo.forEach(file -> {
                            commonFileService.addFile(file.getFileHash(), id1, "archive");
                        });
                        dataManager.deleteFormData(query.getCategoryId(), wj.getFormDefinitionId(), wj.getId());
                        wj.setId(id1);
                        wj.put("CATE_GORY_CODE", category.getCode());
                        wj.put("WJLX", category.getName());
                        wj.setFormDefinitionId(cateConfig.getFileFormId());
                        dataManager.insertFormData(wj, query.getFondId(),newCategoryId);
                    }
                }
            }else {
                formData.setFormDefinitionId(categoryConfig.getFileFormId());
            }
            if (!StringUtils.isEmpty(category1)) {
                formData.put("ORG_CODE", category1.getName());
            }
            moveGoRecording.setOrganNameNew(category.getName());
            moveGoRecording.setWJLXNew(category.getName());
            //添加档案到新的表单中
            dataManager.insertFormData(formData, query.getFondId(), newCategoryId);
            ArchiveDataEditEvent event = new ArchiveDataEditEvent(query.getCategoryId(), formData, true);
            applicationEventPublisher.publishEvent(event);
            moveGoRecording.setId(UUID.randomUUID().toString());
            moveGoRecording.setCreatePerson(person.getId());
            moveGoRecording.setUpdatePerson(person.getId());
            moveGoRecording.setCreateDate(System.currentTimeMillis());
            moveGoRecording.setUpdateDate(System.currentTimeMillis());
            commonMapper.insert(moveGoRecording);
        }
    }
    public List<FormData> getWJS(FormData formData){
        Fond fond = fondService.findFondByCode(formData.get(ArchiveEntity.COLUMN_FOND_CODE));
        Category category = categoryService.findCategoryByCode(formData.get(ArchiveEntity.COLUMN_CATEGORY_CODE), fond.getId());
        if("1".equals(category.getArchiveType())){//案卷类型
            CategoryConfig categoryConfig = commonMapper.selectOneByQuery(SqlQuery.from(CategoryConfig.class)
                    .equal(CategoryConfigInfo.BUSINESSID, category.getId()));
            ArchiveDataQuery dataQuery = new ArchiveDataQuery();
            List<ArchiveDataQuery.QueryItem> items = new ArrayList<>();
            items.add(new ArchiveDataQuery.QueryItem(ArchiveEntity.COLUMN_AJDH, formData.get(ArchiveEntity.COLUMN_ARCHIVE_CODE), ArchiveDataQuery.QueryType.EQUAL));
            dataQuery.setFormDefinitionId(categoryConfig.getFileFormId());
            dataQuery.setQueryItems(items);
            List<FormData> wjs = formDataService.selectFormData(dataQuery.getFormDefinitionId(), newBuilder(dataQuery));
            return wjs;
        }
        return null;
    }
    /**
     * 根据查询条件查询表单数据
     *
     * @param query
     * @return
     */
    private SqlBuilder newBuilder(ArchiveDataQuery query) {
        return ((sqlQuery, wrapper) -> {
            for (ArchiveDataQuery.QueryItem item : query.getQueryItems()) {
                Column column = wrapper.getColumn(item.getKey());
                if (column == null) {
                    continue;
                }
                switch (item.getType()) {
                    case IN:
                        String[] data = item.getValue().split(",");
                        sqlQuery.in(column, data);
                        break;
                    case LIKE:
                        sqlQuery.like(column, item.getValue());
                        break;
                    case EQUAL:
                        sqlQuery.equal(column, item.getValue());
                        break;
                    case END_WITH:
                        sqlQuery.endingWith(column, item.getValue());
                        break;
                    case START_WITH:
                        sqlQuery.startingWith(column, item.getValue());
                        break;
                    default:
                        break;
                }
            }
            // System.out.println(SecurityHolder.get().currentPerson());
            //需要根据机构id判断权限 TODO 在线接收时需要处理，估计得根据机构code查询机构id
            /*Organise organise = SecurityHolder.get().currentOrganise();
            if (null != organise) {
                if (null != wrapper.getColumn(ArchiveEntity.COLUMN_ORGANISEID) && "dag".equals(organise.getOrganiseType())) {
                    sqlQuery.equal(wrapper.getColumn(ArchiveEntity.COLUMN_ORGANISEID), organisePersonService.getPersonDefaultOrganise(SecurityHolder.get().currentPerson().getId()).getId());
                }
            }*/
            String status = "";

            for (ArchiveDataQuery.QueryItem item : query.getQueryItems()) {
                if (item.getKey().equals(ArchiveEntity.STATUS_COLUMN_KEY)) {
                    status = item.getValue();
                }
            }
            List<ArchiveOrder> archiveOrders = commonMapper.selectByQuery(SqlQuery.from(ArchiveOrder.class)
                    .equal(ArchiveOrderInfo.CATEGORYID, query.getCategoryId())
                    .equal(ArchiveOrderInfo.CODE, status)
                    .equal(ArchiveOrderInfo.ARCHIVETYPE, query.getArchiveType())
                    .orderBy(ArchiveOrderInfo.ORDERBY));
            if (archiveOrders.size() > 0) {
                for (ArchiveOrder order : archiveOrders) {
                    if (order.getOrderType().equals("asc")) {
                        sqlQuery.orderBy(wrapper.getColumn(order.getFieldOrder()));
                    } else {
                        sqlQuery.orderByDesc(wrapper.getColumn(order.getFieldOrder()));
                    }
                }
            } else {
                sqlQuery.orderBy(wrapper.getColumn(ArchiveEntity.COLUMN_YEAR));
                sqlQuery.orderBy(wrapper.getColumn(ArchiveEntity.COLUMN_FILETIME));
                sqlQuery.orderBy(wrapper.getColumn(ArchiveEntity.COLUMN_ARCHIVE_CODE));
            }
            /*if ("ascending".equals(query.getOrderType())) {
                sqlQuery.orderBy(wrapper.getColumn(query.getOrderKey()));
            } else if ("dscending".equals(query.getOrderType())) {
                sqlQuery.orderByDesc(wrapper.getColumn(query.getOrderKey()));
            } else {
                sqlQuery.orderBy(wrapper.getColumn(ArchiveEntity.COLUMN_ARCHIVE_CODE));
            }*/
        });
    }
}
