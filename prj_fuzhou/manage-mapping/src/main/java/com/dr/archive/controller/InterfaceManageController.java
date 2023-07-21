package com.dr.archive.controller;

import com.dr.archive.entity.InterfaceManage;
import com.dr.archive.entity.InterfaceManageInfo;
import com.dr.archive.service.InterfaceManageService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: yang
 * @create: 2022-06-03 11:33
 **/
@RequestMapping("/api/interface")
@RestController
public class InterfaceManageController extends BaseServiceController<InterfaceManageService, InterfaceManage> {
    @Override
    protected SqlQuery<InterfaceManage> buildPageQuery(HttpServletRequest httpServletRequest, InterfaceManage interfaceManage) {
        return SqlQuery.from(InterfaceManage.class).like(InterfaceManageInfo.INTERFACENAME, interfaceManage.getInterfaceName());
    }
}
