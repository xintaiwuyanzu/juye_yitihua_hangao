package com.dr.archive.managearchivechange.service.impl;

import com.dr.archive.batch.query.BatchCreateQuery;
import com.dr.archive.event.ArchiveDataEditEvent;
import com.dr.archive.manage.archiveMetadata.entity.ArchiveMetadataRecord;
import com.dr.archive.manage.archiveMetadata.service.ArchiveMetadataRecordService;
import com.dr.archive.manage.category.service.CategoryService;
import com.dr.archive.manage.form.service.ArchiveDataManager;
import com.dr.archive.manage.form.service.impl.DefaultArchiveDataManager;
import com.dr.archive.managearchivechange.entity.ArchiveChangeRecord;
import com.dr.archive.managearchivechange.entity.ArchiveChangeRecordInfo;
import com.dr.archive.managearchivechange.entity.ErrorCorrection;
import com.dr.archive.managearchivechange.service.ArchiveChangeService;
import com.dr.archive.managearchivechange.service.ErrorCorrectionService;
import com.dr.archive.model.entity.ArchiveEntity;
import com.dr.archive.util.Constants;
import com.dr.archive.util.UUIDUtils;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.entity.StatusEntity;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.form.core.service.FormDefinitionService;
import com.dr.framework.common.form.engine.model.core.FieldModel;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.process.service.ProcessConstants;
import com.dr.framework.core.process.service.ProcessContext;
import com.dr.framework.core.process.service.ProcessTypeProvider;
import com.dr.framework.core.process.service.TaskContext;
import com.dr.framework.core.security.SecurityHolder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class ArchiveChangeServiceImpl extends DefaultBaseService<ArchiveChangeRecord> implements ArchiveChangeService, ProcessTypeProvider {
    public static final String DATE_PATTEN = "yyyy-MM-dd HH:mm:ss";
    @Autowired
    ArchiveDataManager archiveDataManager;
    @Autowired
    CommonMapper commonMapper;
    @Autowired
    ArchiveMetadataRecordService archiveMetadataRecordService;
    @Autowired
    DefaultArchiveDataManager defaultArchiveDataManager;
    @Autowired
    ErrorCorrectionService errorCorrectionService;
    @Autowired
    FormDefinitionService formDefinitionService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;
    @Override
    public void archiveChange(ArchiveChangeRecord changeRecord) {
        Person person = SecurityHolder.get().currentPerson();
        changeRecord.setId(UUIDUtils.getUUID());
        changeRecord.setCreateDate(System.currentTimeMillis());
        changeRecord.setUpdateDate(System.currentTimeMillis());
        changeRecord.setCreatePerson(person.getId());
        changeRecord.setUpdatePerson(person.getId());
        commonMapper.insert(changeRecord);
    }

    /**
     * 档案调整 多条 examine
     *
     * @param changeRecord
     */
    @Override
    public void archiveChangeMulti(BatchCreateQuery query, ArchiveChangeRecord changeRecord) {
        List<FormData> formDataList = defaultArchiveDataManager.findDataByQuery(query);
        formDataList.forEach(formData -> {
            //以下完全是复制审核通过的处理
            String[] fields = changeRecord.getFieldsCode().split(",");
            String[] newValues = changeRecord.getNewValues().split(",");
            FormData newFormData = (FormData) formData.clone();
            BeanUtils.copyProperties(formData, newFormData);
            for (int i = 0; i < fields.length; i++) {
                newFormData.put(fields[i], newValues[i]);
                //获取字段名称  TODO 应该先查找列表方案名称，再查找字段别名，最后查找字段名称
                FieldModel fieldModel = formDefinitionService.selectFieldByCode(newFormData.getFormDefinitionId(), fields[i]);
                //插入管理过程元数据
                archiveMetadataRecordService.insert(new ArchiveMetadataRecord(newFormData.getFormDefinitionId(), newFormData.getId(), newFormData.get(ArchiveEntity.COLUMN_FOND_CODE), newFormData.get(ArchiveEntity.COLUMN_CATEGORY_CODE), ArchiveMetadataRecordService.MANAGE_METADATA_TYPE_PILIANGXIUGAI, "档案室接收暂存库批量调整", "档案室接收暂存库批量调整", "档案室接收暂存库批量调整", fields[i], fieldModel.getLabel(), formData.get(fields[i]), newValues[i], null));
            }
            archiveDataManager.updateFormData(newFormData, query.getFondId(), query.getCategoryId());
//
        });
    }

    @Override
    public String getType() {
        return Constants.PROCESS_TYPE_ARCHIVECHANGE;
    }

    @Override
    public String getName() {
        return "档案调整";
    }

    @Override
    public void onBeforeStartProcess(ProcessContext context) {
        String businessId = StringUtils.isEmpty((String) context.getBusinessParams().get("businessId")) ? UUIDUtils.getUUID() : (String) context.getBusinessParams().get("businessId");
        context.setBusinessId(businessId);
        context.setProcessInstanceTitle(DateFormatUtils.format(new Date(), DATE_PATTEN) + "发起的" + getName() + "审核");
        context.addVar(ProcessConstants.PROCESS_BUSINESS_KEY, context.getBusinessId());
        context.addVar("formDefinitionId", context.getBusinessParams().get("formDefinitionId"));
        context.addVar("formDataId", context.getBusinessParams().get("formDataId"));
        context.addVar("fieldsCode", context.getBusinessParams().get("fieldsCode"));
        context.addVar("newValues", context.getBusinessParams().get("newValues"));
        context.addVar("oldValues", context.getBusinessParams().get("oldValues"));
        context.addVar("fieldNames", context.getBusinessParams().get("fieldNames"));
        context.addVar("errorDescription", context.getBusinessParams().get("errorDescription"));
        context.addVar("errorType", context.getBusinessParams().get("errorType"));//类型：1：自动发起 2：推送
        context.addVar("errorSource", context.getBusinessParams().get("errorSource"));
        context.addVar("status", context.getBusinessParams().get("status"));
        context.addVar("fondId", context.getBusinessParams().get("fondId"));
        context.addVar("categoryId", context.getBusinessParams().get("categoryId"));
        //设置流程查看详情跳转页面
        context.addVar(ProcessConstants.PROCESS_FORM_URL_KEY, "/archiveChange");
    }

    @Override
    public void onAfterStartProcess(ProcessContext context) {
        Person person = SecurityHolder.get().currentPerson();
        //修改纠错信息
        ErrorCorrection errorCorrectionOld = errorCorrectionService.selectById(context.getBusinessId());
        ErrorCorrection errorCorrection = null == errorCorrectionOld ? new ErrorCorrection() : errorCorrectionOld;
        initErrorCorrectionParams(errorCorrection, person, context);
        if (null == errorCorrectionOld) {
            errorCorrection.setCreateDate(System.currentTimeMillis());
            errorCorrection.setCreatePerson(person.getId());
            errorCorrection.setCreatePersonName(person.getUserName());
            //默认添加机构id
            if (StringUtils.isEmpty((String) context.getBusinessParams().get("orgId"))) {
                errorCorrection.setOrgId(SecurityHolder.get().currentOrganise().getId());
            }
            commonMapper.insert(errorCorrection);
        } else {
            commonMapper.updateIgnoreNullById(errorCorrection);
        }
        //保存要修改的字段
        insertChangeRecord(context, errorCorrection);
        ProcessTypeProvider.super.onAfterStartProcess(context);
    }

    @Override
    public void onAfterEndProcess(TaskContext context) {
        String businessId = (String) context.getBusinessParams().get("businessId");
        //如果通过怎修改元数据的值
        String isPass = (String) context.getBusinessParams().get("isPass");
        ErrorCorrection errorCorrection = errorCorrectionService.selectById(businessId);
        if ("2".equals(isPass)) {
            FormData formData = archiveDataManager.selectOneFormData(errorCorrection.getFormDefinitionId(), errorCorrection.getFormDataId());
            List<ArchiveChangeRecord> archiveChangeRecordList = detailByErrorCorrectionId(errorCorrection.getId());
            archiveChangeRecordList.forEach(archiveChangeRecord -> formData.put(archiveChangeRecord.getFieldsCode(), archiveChangeRecord.getNewValues()));
            archiveDataManager.updateFormData(formData, errorCorrection.getFondId(), errorCorrection.getCategoriesId());
            errorCorrection.setStatus(StatusEntity.STATUS_ENABLE_STR);//已纠错
            //TODO 插入管理过程元数据中
            for (ArchiveChangeRecord archiveChangeRecord : archiveChangeRecordList) {
                //获取字段名称 
                FieldModel fieldModel = formDefinitionService.selectFieldByCode(errorCorrection.getFormDefinitionId(), archiveChangeRecord.getFieldsCode());
                //插入管理过程元数据
                archiveMetadataRecordService.insert(new ArchiveMetadataRecord(errorCorrection.getFormDefinitionId(),
                        formData.getId(), formData.get(ArchiveEntity.COLUMN_FOND_CODE), formData.get(ArchiveEntity.COLUMN_CATEGORY_CODE),
                        ArchiveMetadataRecordService.MANAGE_METADATA_TYPE_TIAOZHENG, "档案调整", "档案调整", "档案调整", archiveChangeRecord.getFieldsCode(),
                        fieldModel.getLabel(), archiveChangeRecord.getOldValues(), archiveChangeRecord.getNewValues(), null));
            }
            //处理长期保存库的数据
            applicationEventPublisher.publishEvent(new ArchiveDataEditEvent(errorCorrection.getCategoriesId(), formData));
        } else {
            errorCorrection.setStatus("3");//未通过
        }
        errorCorrectionService.updateById(errorCorrection);
        ProcessTypeProvider.super.onAfterEndProcess(context);
    }


    /**
     * 保存要修改的字段
     *
     * @param context
     * @param errorCorrection
     */
    private void insertChangeRecord(ProcessContext context, ErrorCorrection errorCorrection) {
        String fieldsCode = (String) context.getBusinessParams().get("fieldsCode");
        String newValues = (String) context.getBusinessParams().get("newValues");
        String fieldNames = (String) context.getBusinessParams().get("fieldNames");
        String oldValues = (String) context.getBusinessParams().get("oldValues");

        String[] fieldsCodeArray = fieldsCode.split(",");
        String[] newValuesArray = newValues.split(",");
        String[] fieldNamesArray = fieldNames.split(",");
        String[] oldValuesArray = oldValues.split(",");

        for (int i = 0; i < fieldsCodeArray.length; i++) {
            ArchiveChangeRecord archiveChangeRecord = new ArchiveChangeRecord();
            archiveChangeRecord.setErrorCorrectionId(context.getBusinessId());
            archiveChangeRecord.setTitle(errorCorrection.getTitle());
            archiveChangeRecord.setArchiveCode(errorCorrection.getArchiveCode());
            archiveChangeRecord.setFormDefinitionId(errorCorrection.getFormDefinitionId());
            archiveChangeRecord.setFormDataId(errorCorrection.getFormDataId());

            archiveChangeRecord.setFieldNames(fieldNamesArray[i]);
            archiveChangeRecord.setFieldsCode(fieldsCodeArray[i]);
            archiveChangeRecord.setNewValues(newValuesArray[i]);
            archiveChangeRecord.setOldValues(oldValuesArray[i]);
            archiveChangeRecord.setRemarks(errorCorrection.getErrorDescription());
            archiveChangeRecord.setFondId(errorCorrection.getFondId());
            archiveChangeRecord.setCategoriesId(errorCorrection.getCategoriesId());
            insert(archiveChangeRecord);
        }
    }

    /**
     * 初始化
     *
     * @param errorCorrection
     * @param person
     * @param context
     */
    private void initErrorCorrectionParams(ErrorCorrection errorCorrection, Person person, ProcessContext context) {
        errorCorrection.setId(context.getBusinessId());
        errorCorrection.setStartPersonId(person.getId());//发起流程人id
        errorCorrection.setStartPersonName(person.getUserName());//发起流程人姓名
        errorCorrection.setErrorDescription((String) context.getBusinessParams().get("errorDescription"));
        errorCorrection.setFormDefinitionId((String) context.getBusinessParams().get("formDefinitionId"));
        errorCorrection.setFormDataId((String) context.getBusinessParams().get("formDataId"));
        errorCorrection.setStatus((String) context.getBusinessParams().get("status"));
        errorCorrection.setErrorSource((String) context.getBusinessParams().get("errorSource"));
        errorCorrection.setFondId((String) context.getBusinessParams().get("fondId"));
        errorCorrection.setCategoriesId((String) context.getBusinessParams().get("categoryId"));
        errorCorrection.setUpdateDate(System.currentTimeMillis());
        errorCorrection.setUpdatePerson(person.getId());
        //默认类型为2 推送
        if (StringUtils.isEmpty((String) context.getBusinessParams().get("errorType"))) {
            errorCorrection.setErrorType("2");
        } else {
            errorCorrection.setErrorType(StatusEntity.STATUS_ENABLE_STR);
        }
        //默认状态为0 待纠错
        if (StringUtils.isEmpty(errorCorrection.getStatus())) {
            errorCorrection.setStatus(StatusEntity.STATUS_DISABLE_STR);
        }
        //查询表单详情
        FormData formData = archiveDataManager.selectOneFormData(errorCorrection.getFormDefinitionId(), errorCorrection.getFormDataId());
        errorCorrection.setTitle(formData.get(ArchiveEntity.COLUMN_TITLE));
        errorCorrection.setArchiveCode(formData.get(ArchiveEntity.COLUMN_ARCHIVE_CODE));
    }

    @Override
    public List<ArchiveChangeRecord> detailByErrorCorrectionId(String detailByErrorCorrectionId) {
        return commonMapper.selectByQuery(SqlQuery.from(ArchiveChangeRecord.class).equal(ArchiveChangeRecordInfo.ERRORCORRECTIONID, detailByErrorCorrectionId));
    }
}
