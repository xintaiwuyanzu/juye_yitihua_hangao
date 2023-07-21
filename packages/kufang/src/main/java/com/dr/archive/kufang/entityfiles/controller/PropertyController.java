package com.dr.archive.kufang.entityfiles.controller;

import com.dr.archive.kufang.entityfiles.entity.Property;
import com.dr.archive.kufang.entityfiles.entity.PropertyInfo;
import com.dr.archive.kufang.entityfiles.service.PropertyService;
import com.dr.framework.common.controller.BaseController;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/**
 * 属性配置表
 *
 * @author cuiyj
 * @Date 2021-09-02
 */
@RestController
@RequestMapping("/api/property")
public class PropertyController extends BaseController<Property> {
    @Autowired
    PropertyService propertyService;

    @Override
    protected void onBeforePageQuery(HttpServletRequest request, SqlQuery<Property> sqlQuery, Property entity) {
        sqlQuery.like(PropertyInfo.PROPERTYNAME, entity.getPropertyName())
                .equal(PropertyInfo.ARCHIVETYPEID, entity.getArchiveTypeId())
                .orderByDesc(PropertyInfo.PROPERTYNAME);
    }


}
