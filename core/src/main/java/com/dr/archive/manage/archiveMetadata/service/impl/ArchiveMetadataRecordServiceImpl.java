package com.dr.archive.manage.archiveMetadata.service.impl;

import com.dr.archive.manage.archiveMetadata.entity.ArchiveMetadataRecord;
import com.dr.archive.manage.archiveMetadata.service.ArchiveMetadataRecordService;
import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.category.service.CategoryService;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.fond.service.FondService;
import com.dr.archive.manage.form.service.ArchiveDataPlugin;
import com.dr.archive.model.entity.ArchiveEntity;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.form.core.service.FormDataService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.organise.service.OrganisePersonService;
import com.dr.framework.core.security.SecurityHolder;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ArchiveMetadataRecordServiceImpl extends DefaultBaseService<ArchiveMetadataRecord> implements ArchiveMetadataRecordService, ArchiveDataPlugin {

    @Autowired
    FondService fondService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    FormDataService formDataService;
    @Autowired
    OrganisePersonService organisePersonService;

    /**
     * @param entity
     * @return
     */
    @Override
    public long insert(ArchiveMetadataRecord entity) {
        Assert.isTrue(!StringUtils.isEmpty(entity.getYWXW()), "业务行为不能为空！");
        Assert.isTrue(!StringUtils.isEmpty(entity.getFondCode()), "全宗号不能为空！");
        Assert.isTrue(!StringUtils.isEmpty(entity.getCategoryCode()), "分类号不能为空！");
        bindArchiveMetadataRecordInfo(entity);
        return super.insert(entity);
    }

    /**
     * 绑定基本信息
     *
     * @param entity
     */
    void bindArchiveMetadataRecordInfo(ArchiveMetadataRecord entity) {
        //绑定操作人信息
        Person person = SecurityHolder.get().currentPerson();
        entity.setCreatePerson(Optional.ofNullable(entity.getCreatePerson()).orElse(person.getId()));
        entity.setChangePersonName(organisePersonService.getPersonById(entity.getCreatePerson()).getUserName());
        Organise organise = organisePersonService.getPersonDefaultOrganise(entity.getCreatePerson());
        entity.setQXGL(organise.getOrganiseType());

        //默认添加时间
        entity.setCreateDate(System.currentTimeMillis());

        //绑定全宗信息
        Fond fond = fondService.findFondByCode(entity.getFondCode());
        entity.setFondId(fond.getId());
        entity.setFondName(fond.getName());

        //绑定门类信息
        Category category = categoryService.findCategoryByCode(entity.getCategoryCode(), fond.getId());
        entity.setCategoryId(category.getId());
        entity.setCategoryName(category.getName());

        String formDefinitionId = entity.getFormDefinitionId();
        String formDataId = entity.getFormDataId();

        //绑定档案信息
        FormData formData = formDataService.selectOneFormData(formDefinitionId, formDataId);
        if (ObjectUtils.isNotEmpty(formData)) {
            //题名
            entity.setTIMING(formData.get(ArchiveEntity.COLUMN_TITLE));
            //档号
            entity.setArchiveCode(formData.get(ArchiveEntity.COLUMN_ARCHIVE_CODE));
            //保管期限
            entity.setBGQX(formData.get(ArchiveEntity.COLUMN_SAVE_TERM));
            //立档单位名称
            entity.setLDDWMC(formData.get(ArchiveEntity.COLUMN_CRE_ORG_NAME));
            //年度
            entity.setND(formData.get(ArchiveEntity.COLUMN_YEAR));
            //保存期限
            entity.setBGQX(formData.get(ArchiveEntity.COLUMN_SAVE_TERM));
            //机构或问题
            entity.setJGHWT(formData.get(ArchiveEntity.COLUMN_ORG_CODE));
            //责任者
            entity.setZRZ(formData.get(ArchiveEntity.COLUMN_DUTY_PERSON));
        }
    }

   /* @Override
    public FormData afterInsert(FormData data, ArchiveDataContext context) {
        //TODO 默认添加数据后，传入的管理过程元数据的行为人id为当前登录用户id
        insert(new ArchiveMetadataRecord(MANAGE_METADATA_TYPE_ADD, data.getFormDefinitionId(), data.getId(), context.getFond().getId(), context.getCategory().getId(), context.getPerson().getId()));
        return ArchiveDataPlugin.super.afterInsert(data, context);
    }*/

//    @Override
//    public FormData afterUpdate(FormData data, ArchiveDataContext context) {
    //TODO 默认修改数据后，传入的管理过程元数据的行为人id为当前登录用户id
//        insert(new ArchiveMetadataRecord(MANAGE_METADATA_TYPE_EDIT, data.getFormDefinitionId(), data.getId(), context.getFond().getId(), context.getCategory().getId(), context.getPerson().getId()));
//        return ArchiveDataPlugin.super.afterUpdate(data, context);
//    }

//    @Override
//    public Long beforeDelete(String archiveIds, ArchiveDataContext context) {
//        //TODO 默认彻底删除数据后，传入的管理过程元数据的行为人id为当前登录用户id
//        String[] ids = archiveIds.split(",");
//        for (String id : ids) {
//            FormData data = formDataService.selectOneFormData(context.getFormModel().getId(), id);
////            insert(new ArchiveMetadataRecord(MANAGE_METADATA_TYPE_DELETE, data.getFormDefinitionId(), data.getId(), context.getFond().getId(), context.getCategory().getId(), context.getPerson().getId()));
//        }
//        return ArchiveDataPlugin.super.beforeDelete(archiveIds, context);
//    }
}
