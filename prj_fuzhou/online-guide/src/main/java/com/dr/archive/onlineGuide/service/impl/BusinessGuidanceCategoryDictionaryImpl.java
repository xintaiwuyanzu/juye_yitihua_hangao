package com.dr.archive.onlineGuide.service.impl;

import com.dr.archive.onlineGuide.entity.BusinessGuidanceBatch;
import com.dr.archive.onlineGuide.entity.BusinessGuidanceBatchInfo;
import com.dr.archive.onlineGuide.entity.BusinessGuidanceCategoryDictionary;
import com.dr.archive.onlineGuide.entity.BusinessGuidanceCategoryDictionaryInfo;
import com.dr.archive.onlineGuide.service.BusinessGuidanceClassDictionaryService;
import com.dr.archive.util.UUIDUtils;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class BusinessGuidanceCategoryDictionaryImpl extends DefaultBaseService<BusinessGuidanceCategoryDictionary> implements BusinessGuidanceClassDictionaryService {


    @Override
    public ResultEntity insertClass(String cType, String cProblem, String cResult) {
        Assert.isTrue(!StringUtils.isEmpty(cType), "归类类型不能为空！");
        Assert.isTrue(!StringUtils.isEmpty(cProblem), "问题不能为空！");
        Assert.isTrue(!StringUtils.isEmpty(cResult), "结果不能为空！");
        long l = commonMapper.countByQuery(SqlQuery.from(BusinessGuidanceCategoryDictionary.class)
                .equal(BusinessGuidanceCategoryDictionaryInfo.CTYPE, cType)
                .equal(BusinessGuidanceCategoryDictionaryInfo.CPROBLEM, cProblem));
        if(l != 0 ){
            return ResultEntity.error("当前类型中该问题已存在");
        }

        Person person = SecurityHolder.get().currentPerson();
        Organise organise = SecurityHolder.get().currentOrganise();
        BusinessGuidanceCategoryDictionary businessGuidanceCategoryDictionary = new BusinessGuidanceCategoryDictionary();
        businessGuidanceCategoryDictionary.setcType(cType);
        businessGuidanceCategoryDictionary.setcProblem(cProblem);
        businessGuidanceCategoryDictionary.setcResult(cResult);

        businessGuidanceCategoryDictionary.setId(UUIDUtils.getUUID());
        businessGuidanceCategoryDictionary.setCreateUserId(person.getId());
        businessGuidanceCategoryDictionary.setCreatUserName(person.getUserName());
        businessGuidanceCategoryDictionary.setCreateOrgId(organise.getId());
        businessGuidanceCategoryDictionary.setCreateOrgName(organise.getOrganiseName());

        businessGuidanceCategoryDictionary.setCreateOrgId(organise.getId());
        businessGuidanceCategoryDictionary.setCreateOrgName(organise.getOrganiseName());
        businessGuidanceCategoryDictionary.setId(UUIDUtils.getUUID());
        businessGuidanceCategoryDictionary.setCreateDate(System.currentTimeMillis());
        businessGuidanceCategoryDictionary.setOrgId(organise.getId());
        businessGuidanceCategoryDictionary.setCreatePerson(person.getId());

        commonMapper.insert(businessGuidanceCategoryDictionary);
        return ResultEntity.success(businessGuidanceCategoryDictionary.getId());
    }

    @Override
    public ResultEntity queryType(int type) {
        SqlQuery<Map> mapSqlQuery = SqlQuery.from(BusinessGuidanceCategoryDictionary.class, false)
                .column(BusinessGuidanceCategoryDictionaryInfo.CTYPE.alias("cType"))
                .groupBy(BusinessGuidanceCategoryDictionaryInfo.CTYPE)
                .setReturnClass(Map.class);
        List<Map> maps = commonMapper.selectByQuery(mapSqlQuery);
        if(type == 0){
            return ResultEntity.success(maps);
        }
        ArrayList list = new ArrayList();
        maps.forEach(i->{
            list.add(i.get("cType"));
        });
        return ResultEntity.success(list);
    }

    @Override
    public ResultEntity queryProblem(String type) {

        List<BusinessGuidanceCategoryDictionary> businessGuidanceCategoryDictionaries =
                commonMapper.selectByQuery(SqlQuery.from(BusinessGuidanceCategoryDictionary.class)
                .equal(BusinessGuidanceCategoryDictionaryInfo.CTYPE, type));
        return ResultEntity.success(businessGuidanceCategoryDictionaries);

    }

    @Override
    public ResultEntity classifyBatch(String batchId, String classifyId,String classifyType) {
        Person person = SecurityHolder.get().currentPerson();
        BusinessGuidanceBatch businessGuidanceBatch = commonMapper.selectOneByQuery(SqlQuery.from(BusinessGuidanceBatch.class).equal(BusinessGuidanceBatchInfo.ID, batchId));
        businessGuidanceBatch.setId(batchId);
        businessGuidanceBatch.setClassifyId(classifyId);
        businessGuidanceBatch.setReceiveUserName(person.getUserName());
        businessGuidanceBatch.setReceiveUserId(person.getId());
        businessGuidanceBatch.setClassifyType(classifyType);
        commonMapper.updateIgnoreNullById(businessGuidanceBatch);
        return ResultEntity.success("成功");
    }
}

