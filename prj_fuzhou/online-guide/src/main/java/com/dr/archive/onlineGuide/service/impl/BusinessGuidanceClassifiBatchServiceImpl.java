package com.dr.archive.onlineGuide.service.impl;

import com.dr.archive.onlineGuide.entity.BusinessGuidanceClassifiBatch;
import com.dr.archive.onlineGuide.entity.BusinessGuidanceClassifiBatchInfo;
import com.dr.archive.onlineGuide.service.BusinessGuidanceClassifiBatchService;
import com.dr.archive.util.SecurityHolderUtil;
import com.dr.archive.util.UUIDUtils;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BusinessGuidanceClassifiBatchServiceImpl extends DefaultBaseService<BusinessGuidanceClassifiBatch> implements BusinessGuidanceClassifiBatchService {

    @Autowired
    CommonMapper commonMapper;

    @Override
    public String insertClass(String batchId , String dId) {
        Person person = SecurityHolder.get().currentPerson();
        BusinessGuidanceClassifiBatch businessGuidanceClassifiBatch = new BusinessGuidanceClassifiBatch();
        businessGuidanceClassifiBatch.setId(UUIDUtils.getUUID());
        businessGuidanceClassifiBatch.setBatchId(batchId);
        businessGuidanceClassifiBatch.setDid(dId);
         businessGuidanceClassifiBatch.setClassifiName(dId);
         businessGuidanceClassifiBatch.setCreateDate(System.currentTimeMillis());
         businessGuidanceClassifiBatch.setCreatePerson(person.getId());
        commonMapper.insert(businessGuidanceClassifiBatch);
        return businessGuidanceClassifiBatch.getId();
    }
}
