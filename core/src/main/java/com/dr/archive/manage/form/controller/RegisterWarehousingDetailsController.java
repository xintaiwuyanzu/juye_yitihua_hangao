package com.dr.archive.manage.form.controller;

import com.dr.archive.manage.form.entity.RegisterWarehousingDetails;
import com.dr.archive.manage.form.entity.RegisterWarehousingDetailsInfo;
import com.dr.archive.manage.form.service.RegisterWarehousingDetailsService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("api/registerWarehousingDetails")
public class RegisterWarehousingDetailsController extends BaseServiceController<RegisterWarehousingDetailsService,RegisterWarehousingDetails> {

    @Autowired
    CommonMapper commonMapper;


    @Override
    protected SqlQuery<RegisterWarehousingDetails> buildPageQuery(HttpServletRequest request, RegisterWarehousingDetails entity) {

        if (entity.getbId()!=null){
            SqlQuery<RegisterWarehousingDetails> sqlQuery = SqlQuery.from(RegisterWarehousingDetails.class);
            sqlQuery.equal(RegisterWarehousingDetailsInfo.BID,entity.getbId());
            if (entity.getTIMING()!=null){
                sqlQuery.like(RegisterWarehousingDetailsInfo.TIMING,entity.getTIMING());
            }
            if (entity.getArchiveCode()!=null){
                sqlQuery.equal(RegisterWarehousingDetailsInfo.ARCHIVECODE,entity.getArchiveCode());
            }
            sqlQuery.orderByDesc(RegisterWarehousingDetailsInfo.CREATEDATE);
            return sqlQuery;
        }
        return null;
    }
}
