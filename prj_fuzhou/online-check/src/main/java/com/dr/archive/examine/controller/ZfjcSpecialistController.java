package com.dr.archive.examine.controller;

import com.dr.archive.examine.entity.ZfjcSpecialist;
import com.dr.archive.examine.entity.ZfjcSpecialistInfo;
import com.dr.archive.examine.service.ZfjcSpecialistService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.organise.query.PersonQuery;
import com.dr.framework.core.organise.service.OrganisePersonService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 专家管理
 */
@RestController
@RequestMapping("api/zfjcSpecialist")
public class ZfjcSpecialistController extends BaseServiceController<ZfjcSpecialistService, ZfjcSpecialist> {

    @Autowired
    OrganisePersonService organisePersonService;
    @Autowired
    ZfjcSpecialistService zfjcSpecialistService;

    @Override
    protected SqlQuery<ZfjcSpecialist> buildPageQuery(HttpServletRequest httpServletRequest, ZfjcSpecialist zfjcSpecialist) {
        return SqlQuery.from(ZfjcSpecialist.class).like(ZfjcSpecialistInfo.USERNAME, zfjcSpecialist.getUserName());
    }

    /**
     * 本单位人员
     *
     * @return
     */
    @RequestMapping({"/personPage"})
    public ResultEntity personPage() {

        String id = SecurityHolder.get().currentOrganise().getId();
        PersonQuery build = new PersonQuery.Builder().defaultOrganiseIdEqual(id).build();
        List<Person> personList = organisePersonService.getPersonList(build);
        return ResultEntity.success(personList);
    }

    /**
     * 本单位执法人员
     *
     * @return
     */
    @RequestMapping({"/specialistList"})
    public ResultEntity specialistList() {
        String id = SecurityHolder.get().currentOrganise().getId();
        List<ZfjcSpecialist> specialistList = zfjcSpecialistService.getZfjcSpecialistListByDefaultOrgId(id);
        return ResultEntity.success(specialistList);
    }

    @Override
    protected SqlQuery<ZfjcSpecialist> buildDeleteQuery(HttpServletRequest request, ZfjcSpecialist entity) {
        Assert.isTrue(StringUtils.hasText(entity.getId()), "要删除的数据不能为空！");
        return SqlQuery.from(ZfjcSpecialist.class).in(ZfjcSpecialistInfo.ID, entity.getId().split(","));
    }
}
