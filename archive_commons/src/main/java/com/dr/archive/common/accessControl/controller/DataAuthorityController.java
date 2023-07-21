package com.dr.archive.common.accessControl.controller;

import com.dr.archive.common.accessControl.entity.DataAuthority;
import com.dr.archive.common.accessControl.entity.DataAuthorityInfo;
import com.dr.archive.common.accessControl.service.DataAuthorityService;
import com.dr.archive.util.UUIDUtils;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Mr.Zhu
 * @date 2022/5/17 - 11:31
 */
@RestController
@RequestMapping("/api/data_authority")
public class DataAuthorityController extends BaseServiceController<DataAuthorityService,DataAuthority> {

    @Autowired
    DataAuthorityService dataAuthorityService;

    @Override
    protected SqlQuery<DataAuthority> buildPageQuery(HttpServletRequest httpServletRequest, DataAuthority dataAuthority) {
        SqlQuery query = SqlQuery.from(DataAuthority.class)
         .equal(DataAuthorityInfo.ACCESSCODE, dataAuthority.getAccessCode())
                .equal(DataAuthorityInfo.ACCESSLEVEL,dataAuthority.getAccessLevel())
                .equal(DataAuthorityInfo.LEVELCODE,dataAuthority.getLevelCode());
        return query;
    }


    @RequestMapping("getAllDataAuthority")
    public ResultEntity getAllDataAuthority(){
        return ResultEntity.success(dataAuthorityService.getAllDataAuthority());
    }

    @Override
    public ResultEntity<DataAuthority> insert(HttpServletRequest request, DataAuthority entity) {
        entity.setAccessCode(UUIDUtils.getUUID());
        if (dataAuthorityService.findDataLevelCode(entity.getAccessLevel(), entity.getLevelCode())) {
            return ResultEntity.error("添加失败，权限类别已存在");
        }
        return super.insert(request, entity);
    }

    @Override
    public ResultEntity<DataAuthority> update(HttpServletRequest request, DataAuthority entity) {
        if (dataAuthorityService.findDataLevelCode(entity.getAccessLevel(), entity.getLevelCode())) {
            return ResultEntity.error("更新失败，权限类别已存在");
        }
        return super.update(request, entity);
    }
}
