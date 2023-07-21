package com.dr.archive.controller;

import com.dr.archive.entity.Realm;
import com.dr.archive.entity.RealmInfo;
import com.dr.archive.service.RealmService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: yang
 * @create: 2022-05-25 11:25
 **/
@RestController
@RequestMapping("/api/realm")
public class RealmController extends BaseServiceController<RealmService, Realm> {

    @Override
    protected SqlQuery<Realm> buildPageQuery(HttpServletRequest httpServletRequest, Realm atlas) {
        return SqlQuery.from(Realm.class);
    }

    @RequestMapping("/getRealms")
    public ResultEntity getRealms(String realmName) {
        return ResultEntity.success(service.selectList(SqlQuery.from(Realm.class).equal(RealmInfo.REALMNAME, realmName)));
    }

    @RequestMapping("/getClassNum")
    public ResultEntity getClassNum() {
        return ResultEntity.success(service.getClassNum());
    }
}
