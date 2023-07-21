package com.dr.archive.common.ipWhiteList.controller;

import com.dr.archive.common.ipWhiteList.entity.IpWhiteList;
import com.dr.archive.common.ipWhiteList.entity.IpWhiteListInfo;
import com.dr.archive.common.ipWhiteList.service.IpWhiteListService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * ip白名单
 */
@RestController
@RequestMapping("/api/IpWhiteList")
public class IpWhiteListController extends BaseServiceController<IpWhiteListService,IpWhiteList> {

    @Override
    protected SqlQuery<IpWhiteList> buildPageQuery(HttpServletRequest httpServletRequest, IpWhiteList ipWhiteList) {
        SqlQuery<IpWhiteList> sqlQuery = SqlQuery.from(IpWhiteList.class).equal(IpWhiteListInfo.IP,ipWhiteList.getIp())
            .equal(IpWhiteListInfo.MAC,ipWhiteList.getMac()).like(IpWhiteListInfo.CREATEPERSON,ipWhiteList.getCreatePerson())
            .equal(IpWhiteListInfo.STATUS,ipWhiteList.getStatus()).equal(IpWhiteListInfo.USERNAME,ipWhiteList.getUsername())
                .orderByDesc(IpWhiteListInfo.CREATEPERSON);
        return sqlQuery;
    }




}
