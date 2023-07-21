package com.dr.archive.common.dzdacqbc.controller;

import com.dr.archive.common.dzdacqbc.entity.ESaveMedium;
import com.dr.archive.common.dzdacqbc.entity.ESaveMediumInfo;
import com.dr.archive.common.dzdacqbc.service.ESaveMediumService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 介质管理
 */

@RestController
@RequestMapping("/api/eSaveMedium")
public class ESaveMediumController extends BaseServiceController<ESaveMediumService, ESaveMedium> {

    @Override
    protected SqlQuery<ESaveMedium> buildPageQuery(HttpServletRequest httpServletRequest, ESaveMedium eSaveMedium) {
        SqlQuery<ESaveMedium> sqlQuery =  SqlQuery.from(ESaveMedium.class);
        if(StringUtils.hasText(eSaveMedium.getMediumName())){
            sqlQuery.like(ESaveMediumInfo.MEDIUMNAME, eSaveMedium.getMediumName());
        }
        sqlQuery.equal(ESaveMediumInfo.ORGID, SecurityHolder.get().currentOrganise().getId());
        return sqlQuery;
    }
}
