package com.dr.archive.manage.form.service.impl;

import com.dr.archive.manage.form.entity.RegisterWarehousing;
import com.dr.archive.manage.form.entity.RegisterWarehousingDetails;
import com.dr.archive.manage.form.service.RegisterWarehousingDetailsService;
import com.dr.archive.model.entity.ArchiveEntity;
import com.dr.archive.util.UUIDUtils;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.service.DefaultBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterWarehousingDetailsServiceImpl extends DefaultBaseService<RegisterWarehousingDetails> implements RegisterWarehousingDetailsService {

    @Autowired
    CommonMapper commonMapper;

    @Override
    public void insertFormData(FormData formData, String  bid) {
        RegisterWarehousingDetails registerWarehousingDetails = new RegisterWarehousingDetails();
        registerWarehousingDetails.setbId(bid);
        registerWarehousingDetails.setId(UUIDUtils.getUUID());
        registerWarehousingDetails.setCreateDate(System.currentTimeMillis());
        registerWarehousingDetails.setTIMING(formData.get(ArchiveEntity.COLUMN_TITLE));
        registerWarehousingDetails.setArchiveCode(formData.get(ArchiveEntity.COLUMN_ARCHIVE_CODE));
        registerWarehousingDetails.setND(formData.get(ArchiveEntity.COLUMN_YEAR));
        registerWarehousingDetails.setWJXCRQ(formData.get(ArchiveEntity.COLUMN_FILETIME));
        registerWarehousingDetails.setBGQX(formData.get(ArchiveEntity.COLUMN_SAVE_TERM));
        registerWarehousingDetails.setBZ(formData.get(ArchiveEntity.COLUMN_NOTE));
        registerWarehousingDetails.setFondCode(formData.get(ArchiveEntity.COLUMN_FOND_CODE));
        commonMapper.insert(registerWarehousingDetails);
    }

    @Override
    public void insertFormData(FormData formData, String  bid,String modelType) {
        RegisterWarehousingDetails registerWarehousingDetails = new RegisterWarehousingDetails();
        registerWarehousingDetails.setId(UUIDUtils.getUUID());
        registerWarehousingDetails.setbId(bid);
        registerWarehousingDetails.setTIMING(formData.get(ArchiveEntity.COLUMN_TITLE));
        registerWarehousingDetails.setArchiveCode(formData.get(ArchiveEntity.COLUMN_ARCHIVE_CODE));
        registerWarehousingDetails.setND(formData.get(ArchiveEntity.COLUMN_YEAR));
        registerWarehousingDetails.setWJXCRQ(formData.get(ArchiveEntity.COLUMN_FILETIME));
        registerWarehousingDetails.setBGQX(formData.get(ArchiveEntity.COLUMN_SAVE_TERM));
        registerWarehousingDetails.setBZ(formData.get(ArchiveEntity.COLUMN_NOTE));
        registerWarehousingDetails.setFondCode(formData.get(ArchiveEntity.COLUMN_FOND_CODE));
        registerWarehousingDetails.setArchiveType(modelType);
        if ("2".equals(modelType)){
            registerWarehousingDetails.setAJDH(formData.get(ArchiveEntity.COLUMN_AJDH));
        }
        commonMapper.insert(registerWarehousingDetails);
    }

    @Override
    public void enUpdateById(RegisterWarehousing insert,int size) {
        insert.setStateType("成功");
        insert.setQuantity(size);
        //出入库信息
        insert.setUpdateDate(System.currentTimeMillis());
        commonMapper.updateById(insert);
    }

    @Override
    public void enUpdateById(String insert, int size) {
        RegisterWarehousing registerWarehousing = commonMapper.selectById(RegisterWarehousing.class, insert);
        registerWarehousing.setStateType("成功");
        registerWarehousing.setQuantity(size);
        commonMapper.updateById(registerWarehousing);
    }
}
