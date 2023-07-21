package com.dr.archive.manage.fond.controller;

import com.dr.archive.manage.fond.entity.FondOrganise;
import com.dr.archive.manage.fond.entity.FondOrganiseInfo;
import com.dr.archive.manage.fond.service.FondOrganiseService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author caor
 * @date 2021-11-12 10:07
 */
@RestController
@RequestMapping("/api/manage/fondorganise")
public class FondOrganiseController extends BaseServiceController<FondOrganiseService, FondOrganise> {
    @Override
    protected SqlQuery<FondOrganise> buildPageQuery(HttpServletRequest httpServletRequest, FondOrganise fondOrganise) {
        return SqlQuery.from(FondOrganise.class)
                .equal(FondOrganiseInfo.FONDID, fondOrganise.getFondId())
                .equal(FondOrganiseInfo.FONDCODE, fondOrganise.getFondCode())
                .equal(FondOrganiseInfo.ORGANISEID, fondOrganise.getOrganiseId())
                .equal(FondOrganiseInfo.ORGANISECODE, fondOrganise.getOrganiseCode());
    }
}
