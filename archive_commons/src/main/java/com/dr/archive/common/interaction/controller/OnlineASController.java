package com.dr.archive.common.interaction.controller;

import com.dr.archive.common.interaction.entity.OnlineAS;
import com.dr.archive.common.interaction.entity.OnlineASInfo;
import com.dr.archive.common.interaction.service.OnlineASService;
import com.dr.framework.common.controller.BaseController;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import com.dr.framework.core.web.annotations.Current;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/onlineas/")
@ResponseBody
public class OnlineASController extends BaseServiceController<OnlineASService, OnlineAS> {

    @Override
    protected SqlQuery<OnlineAS> buildPageQuery(HttpServletRequest request, OnlineAS entity) {
        SqlQuery<OnlineAS> sqlQuery = SqlQuery.from(OnlineAS.class);
        String name = request.getParameter("name");
        if (!StringUtils.isEmpty(name)) {
            sqlQuery.like(OnlineASInfo.NIANJIANNAME, name);
        }
        if (!StringUtils.isEmpty(entity.getNian())) {
            sqlQuery.equal(OnlineASInfo.NIAN, entity.getNian());
        }
        Person userLogin = BaseController.getUserLogin(request);
        Organise organise = SecurityHolder.get().currentOrganise();
        if (!StringUtils.isEmpty(entity.getStatus())) {
            sqlQuery.notEqual(OnlineASInfo.STATUS, entity.getStatus());
            if (!organise.getOrganiseType().equals("dag")){
                sqlQuery.equal(OnlineASInfo.ORGANISEID,organise.getId());
            }
        }else {
            if (!organise.getOrganiseType().equals("dag")){
                sqlQuery.equal(OnlineASInfo.ORGANISEID,organise.getId());
            }
        }
        sqlQuery.orderByDesc(OnlineASInfo.CREATEDATE);
        return sqlQuery;
    }

    @Override
    public ResultEntity<OnlineAS> insert(HttpServletRequest request, OnlineAS entity) {
        Organise organise = BaseController.getOrganise(request);
        entity.setOrganiseId(organise.getId());
        entity.setOrganiseName(organise.getConcatName());
        entity.setOrganiseCode(organise.getOrganiseCode());
        entity.setStatus("未审核");
        return super.insert(request, entity);
    }

    @Override
    public ResultEntity<OnlineAS> update(HttpServletRequest request, OnlineAS entity) {
        entity.setStatus("未审核");
        return super.update(request, entity);
    }

    @Override
    protected SqlQuery<OnlineAS> buildDeleteQuery(HttpServletRequest request, OnlineAS entity) {
        String[] ids = request.getParameter("aIds").split(",");
        for (String id : ids) {
            service.delete(SqlQuery.from(OnlineAS.class).equal(OnlineASInfo.ID, id));
        }
        return super.buildDeleteQuery(request, entity);
    }

    @RequestMapping("/checks")
    public ResultEntity checks(String aIds, @Current Person person) {
        String[] ids = aIds.split(",");
        for (String id : ids) {
            OnlineAS onlineAS = service.selectOne(SqlQuery.from(OnlineAS.class).equal(OnlineASInfo.ID, id));
            service.checkById(onlineAS, person);
        }
        return ResultEntity.success();
    }

    @RequestMapping("/tongguo")
    public ResultEntity tongguo(OnlineAS onlineAS) {
        service.updateById(onlineAS);
        return ResultEntity.success();
    }

    @RequestMapping("/findById")
    public ResultEntity findById(String id) {
        return ResultEntity.success(service.selectOne(SqlQuery.from(OnlineAS.class).equal(OnlineASInfo.ID, id)));
    }
    @RequestMapping("/deletes")
    public ResultEntity deletes(String aIds) {
        String[] split = aIds.split(",");
        for (String id : split) {
            service.deleteById(id);
        }
        return ResultEntity.success();
    }


}
