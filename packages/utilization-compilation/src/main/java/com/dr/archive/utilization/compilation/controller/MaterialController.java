package com.dr.archive.utilization.compilation.controller;

import com.dr.archive.utilization.compilation.entity.Material;
import com.dr.archive.utilization.compilation.service.MaterialService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: caor
 * @Date: 2022-02-20 17:39
 * @Description:
 */
@RestController
@RequestMapping({"${common.api-path:/api}/material"})
public class MaterialController extends BaseServiceController<MaterialService, Material> {
    @Override
    protected SqlQuery<Material> buildPageQuery(HttpServletRequest httpServletRequest, Material material) {
        return SqlQuery.from(Material.class);
    }
}
