package com.dr.archive.appraisal.service.impl;


import com.dr.archive.appraisal.entity.AppraisalBatchPerson;
import com.dr.archive.appraisal.entity.AppraisalBatchPersonInfo;
import com.dr.archive.appraisal.service.AppraisalBatchPersonService;
import com.dr.framework.common.page.Page;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.organise.service.OrganisePersonService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppraisalBatchPersonServiceImpl extends DefaultBaseService<AppraisalBatchPerson> implements AppraisalBatchPersonService {

    @Autowired
    OrganisePersonService organisePersonService;

    @Override
    public void deleteBatchPersonByBatchId(String batchId) {
        SqlQuery sqlQuery = SqlQuery.from(AppraisalBatchPerson.class)
                                    .equal(AppraisalBatchPersonInfo.BATCHID,batchId);
        delete(sqlQuery);
    }



    @Override
    public List<AppraisalBatchPerson> selectList(SqlQuery<AppraisalBatchPerson> sqlQuery) {
        List<AppraisalBatchPerson> list = getCommonService().selectList(sqlQuery);
        for(AppraisalBatchPerson appraisalBatchPerson:list){
            Person person = organisePersonService.getPersonById(appraisalBatchPerson.getPersonId());
            appraisalBatchPerson.setPersonName(person.getUserName());
            appraisalBatchPerson.setPersonPhone(person.getPhone());
            appraisalBatchPerson.setPersonCode(person.getUserCode());
        }
        return list;
    }




    @Override
    public Page<AppraisalBatchPerson> selectPage(SqlQuery<AppraisalBatchPerson> sqlQuery, int pageIndex, int pageSize) {
        Page<AppraisalBatchPerson> personPage = super.selectPage(sqlQuery, pageIndex, pageSize);
        for(AppraisalBatchPerson appraisalBatchPerson:personPage.getData()){
            Person person = organisePersonService.getPersonById(appraisalBatchPerson.getPersonId());
            appraisalBatchPerson.setPersonName(person.getUserName());
            appraisalBatchPerson.setPersonPhone(person.getPhone());
            appraisalBatchPerson.setPersonCode(person.getUserCode());
        }
        return personPage;
    }
}
