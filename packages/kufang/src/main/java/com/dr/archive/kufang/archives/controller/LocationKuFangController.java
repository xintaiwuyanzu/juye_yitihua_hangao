package com.dr.archive.kufang.archives.controller;

import com.dr.archive.kufang.archives.entity.LocationKuFang;
import com.dr.archive.kufang.archives.entity.LocationKuFangInfo;
import com.dr.archive.kufang.archives.service.LocationKuFangQuService;
import com.dr.archive.kufang.archives.service.LocationKuFangService;
import com.dr.archive.kufang.entityfiles.entity.EntityFiles;
import com.dr.archive.kufang.entityfiles.entity.EntityFilesInfo;
import com.dr.archive.kufang.entityfiles.service.EntityFilesService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import com.dr.framework.core.web.annotations.Current;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 档案室管理Controller
 *
 * @author dr
 */
@RestController
@RequestMapping("/api/locationkufang")
public class LocationKuFangController extends BaseServiceController<LocationKuFangService, LocationKuFang> {
    @Autowired
    LocationKuFangService locationKuFangService;
    @Autowired
    EntityFilesService entityFilesService;
    @Autowired
    LocationKuFangQuService locationKuFangQuService;

    @Override
    protected SqlQuery<LocationKuFang> buildPageQuery(HttpServletRequest httpServletRequest, LocationKuFang locationKuFang) {
        return SqlQuery.from(LocationKuFang.class)
                .like(LocationKuFangInfo.NAME, locationKuFang.getName())
                .equal(LocationKuFangInfo.ORGANISEID, SecurityHolder.get().currentOrganise().getId())
                .orderBy(LocationKuFangInfo.CREATEDATE);
    }

    @Override
    public ResultEntity<Boolean> delete(HttpServletRequest request, LocationKuFang entity) {
        List<EntityFiles> list = entityFilesService.selectList(SqlQuery.from(EntityFiles.class).equal(EntityFilesInfo.LOCID, entity.getId()));
        if(list.size() > 0){
            return ResultEntity.error("当前库房已存放档案，暂不能删除！");
        }
        //删除库房下的区
        locationKuFangQuService.delArea(entity.getId());
        return super.delete(request, entity);
    }

    /**
     * 查询当前系统所有档案室
     *
     * @return
     */
    @RequestMapping("/getLocations")
    public ResultEntity getLocations() {
        return ResultEntity.success(locationKuFangService.getLocations());
    }

    /**
     * 添加档案室
     *
     * @param location
     */
    @PostMapping("/addLocation")
    public ResultEntity addLocation(@Current Person person, LocationKuFang location) {
        locationKuFangService.addLocation(location, person.getId());
        return ResultEntity.success();
    }

    /**
     * 更新只是更新基本信息
     *
     * @param location
     */
    @RequestMapping("/updateLoc")
    public ResultEntity updateLocation(@Current Person person, HttpServletRequest request, LocationKuFang location) {
        locationKuFangService.updateLocation(location, person.getId());
        return ResultEntity.success();
    }

    /**
     * 更新档案室状态
     *
     * @param locId
     * @param enable
     */
    @RequestMapping("/updateLocStatus")
    public ResultEntity updateLocationStatus(@Current Person person, HttpServletRequest request, String locId, boolean enable) {
        LocationKuFang location = service.selectById(locId);
        locationKuFangService.updateLocationStatus(locId, person.getId(), enable);
        return ResultEntity.success();
    }

    /**
     * 根据id查馆藏室
     *
     * @param locId
     * @return
     */
    @RequestMapping("/getLocById")
    public ResultEntity getLocById(String locId) {
        return ResultEntity.success(locationKuFangService.getLocById(locId));
    }
}

