package com.dr.archive.appraisal.controller;

import com.dr.archive.appraisal.entity.AppraisalBatchPerson;
import com.dr.archive.appraisal.entity.AppraisalBatchPersonInfo;
import com.dr.archive.appraisal.service.AppraisalBatchPersonService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.organise.query.PersonQuery;
import com.dr.framework.core.organise.service.OrganisePersonService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.web.annotations.Current;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 鉴定批次人员
 */
@RestController
@RequestMapping("api/appraisalBatchPerson")
public class AppraisalBatchPersonController extends BaseServiceController<AppraisalBatchPersonService, AppraisalBatchPerson> {
    @Autowired
    OrganisePersonService organisePersonService;

    @Override
    protected SqlQuery<AppraisalBatchPerson> buildPageQuery(HttpServletRequest httpServletRequest, AppraisalBatchPerson appraisalBatchPerson) {
        SqlQuery sqlQuery = SqlQuery.from(AppraisalBatchPerson.class);
        sqlQuery.equal(AppraisalBatchPersonInfo.BATCHID, appraisalBatchPerson.getBatchId());
        return sqlQuery;
    }

    /*
     * 获取本机构下的所有人员*/
    @PostMapping({"/currentOrganisePersonsPage"})
    public ResultEntity currentOrganisePersonsPage(Person person, @Current Organise organise, @RequestParam(defaultValue = "0") int pageIndex, @RequestParam(defaultValue = "15") int pageSize, @RequestParam(defaultValue = "true") boolean page) {
        PersonQuery personQuery = (PersonQuery) (new PersonQuery.Builder()).nameLike(person.getUserName()).typeLike(person.getPersonType()).userCodeLike(person.getUserCode()).statusEqual(new String[]{(String) person.getStatus()}).defaultOrganiseIdEqual(new String[]{organise.getId()}).build();
        return page ? ResultEntity.success(this.organisePersonService.getPersonPage(personQuery, pageSize * pageIndex, (pageIndex + 1) * pageSize)) : ResultEntity.success(this.organisePersonService.getPersonList(personQuery));
    }
}
