package com.dr.archive.examine.controller;


import com.dr.archive.examine.entity.PublicInformation;
import com.dr.archive.examine.entity.PublicInformationInfo;
import com.dr.archive.examine.service.PublicInformationService;
import com.dr.framework.common.controller.BaseController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 通知
 */
@RestController
@RequestMapping("api/publicInformation")
public class PublicInformationController extends BaseController<PublicInformation> {

    @Autowired
    PublicInformationService publicInformationService;

    @Override
    protected void onBeforePageQuery(HttpServletRequest request, SqlQuery<PublicInformation> sqlQuery, PublicInformation entity) {
        sqlQuery.orderByDesc(PublicInformationInfo.STATUS)
                .orderByDesc(PublicInformationInfo.UPDATEDATE);
    }

    @RequestMapping("fankui")
    public ResultEntity fankui(String id, String reply, String checkType) {
        publicInformationService.reply(id, reply, checkType);
        return ResultEntity.success();
    }

    /**
     * 当前登录人的消息  只查自己发出的和收到的
     *
     * @param request
     * @param entity
     * @param pageIndex
     * @param pageSize
     * @param page
     * @return
     */
    @RequestMapping("myInfo")
    public ResultEntity myInfo(HttpServletRequest request, PublicInformation entity, @RequestParam(defaultValue = "0") int pageIndex, @RequestParam(defaultValue = "15") int pageSize, @RequestParam(defaultValue = "true") boolean page) {
        SqlQuery<PublicInformation> sqlQuery = SqlQuery.from(PublicInformation.class, true);
        this.onBeforePageQuery(request, sqlQuery, entity);
        String personId = getUserLogin(request).getId();
        sqlQuery.equal(PublicInformationInfo.SENDID, personId).or().equal(PublicInformationInfo.TOID, personId);
        Object result;
        if (page) {
            result = this.commonService.selectPage(sqlQuery, pageIndex, pageSize);
        } else {
            result = this.commonService.selectList(sqlQuery);
        }

        return ResultEntity.success(result);
    }
}
