package com.dr.archive.kufang.archives.controller;

import com.dr.archive.kufang.archives.entity.LocationKuFangQu;
import com.dr.archive.kufang.archives.entity.LocationKuFangQuInfo;
import com.dr.archive.kufang.archives.service.LocationKuFangQuService;
import com.dr.archive.kufang.archives.service.LocationKuFangService;
import com.dr.archive.kufang.archives.service.PositionService;
import com.dr.archive.kufang.entityfiles.entity.EntityFiles;
import com.dr.archive.kufang.entityfiles.entity.EntityFilesInfo;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 库房分区Controller
 *
 * @author dr
 */
@RestController
@RequestMapping("/api/kufangArea")
public class LocationKuFangQuController extends BaseServiceController<LocationKuFangQuService, LocationKuFangQu> {
    @Autowired
    LocationKuFangService locationKuFangService;
    @Autowired
    CommonMapper commonMapper;
    @Autowired
    PositionService positionService;

    @Override
    protected SqlQuery<LocationKuFangQu> buildPageQuery(HttpServletRequest request, LocationKuFangQu entity) {
        SqlQuery<LocationKuFangQu> sql = SqlQuery.from(LocationKuFangQu.class);
        if(StringUtils.hasText(entity.getAreaName())){
            sql.like(LocationKuFangQuInfo.AREANAME, entity.getAreaName());
        }
        if(StringUtils.hasText(entity.getKufangId())){
            sql.equal(LocationKuFangQuInfo.KUFANGID, entity.getKufangId());
        }
        sql.equal(LocationKuFangQuInfo.ORGID, SecurityHolder.get().currentOrganise().getId())
                .orderBy(LocationKuFangQuInfo.CREATEDATE);
        return sql;
    }

    @Override
    public ResultEntity<Boolean> delete(HttpServletRequest request, LocationKuFangQu entity) {
        List<EntityFiles> list = commonMapper.selectByQuery(SqlQuery.from(EntityFiles.class).equal(EntityFilesInfo.AREAID, entity.getId()));
        if(list.size() > 0){
            return ResultEntity.error("当前密集下有档案，暂不能删除");
        }
        positionService.delPosition(entity.getId());
        return super.delete(request, entity);
    }
}

