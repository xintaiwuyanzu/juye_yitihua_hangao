package com.dr.archive.kufang.entityfiles.controller;

import com.dr.archive.kufang.entityfiles.entity.PropertyValue;
import com.dr.archive.kufang.entityfiles.entity.PropertyValueInfo;
import com.dr.framework.common.controller.BaseController;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 属性值
 *
 * @author cuiyj
 * @Date 2021-09-02
 */
@RestController
@RequestMapping("api/propertyValue")
public class PropertyValueController extends BaseController<PropertyValue> {

    @Override
    protected void onBeforePageQuery(HttpServletRequest request, SqlQuery<PropertyValue> sqlQuery, PropertyValue entity) {
        sqlQuery.equal(PropertyValueInfo.ENTITYID, entity.getEntityId())
                .equal(PropertyValueInfo.PROPERTYID, entity.getPropertyId());
    }
}
