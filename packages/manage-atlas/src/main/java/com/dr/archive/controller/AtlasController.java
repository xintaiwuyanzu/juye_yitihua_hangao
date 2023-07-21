package com.dr.archive.controller;

import com.dr.archive.entity.Atlas;
import com.dr.archive.entity.AtlasInfo;
import com.dr.archive.service.AtlasService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: yang
 * @create: 2022-05-25 11:25
 **/
@RestController
@RequestMapping("/api/atlas")
public class AtlasController extends BaseServiceController<AtlasService, Atlas> {

    @Override
    protected SqlQuery<Atlas> buildPageQuery(HttpServletRequest httpServletRequest, Atlas atlas) {
        return SqlQuery.from(Atlas.class)
                .like(AtlasInfo.ATLASNAME, atlas.getAtlasName())
                .like(AtlasInfo.ATLASTYPE, atlas.getAtlasType())
                .orderByDesc(AtlasInfo.CREATEDATE);
    }

    @RequestMapping("/getImgArchives")
    public ResultEntity getImgArchives(HttpServletRequest request, String atlasId, @RequestParam(defaultValue = "0") int pageIndex, @RequestParam(defaultValue = "15") int pageSize, @RequestParam(defaultValue = "true") boolean page) {
        return ResultEntity.success(service.getImgArchives(atlasId, pageIndex, pageSize));
    }
}
