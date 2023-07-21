package com.dr.archive.specialTag.controller;

import com.dr.archive.specialTag.entity.SpecialTag;
import com.dr.archive.specialTag.entity.SpecialTagInfo;
import com.dr.archive.specialTag.service.SpecialTagService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: qiuyf
 * @date: 2022/6/17 17:32
 */
@RestController
@RequestMapping("api/specialTag")
public class SpecialTagController extends BaseServiceController<SpecialTagService, SpecialTag> {
    @Override
    protected SqlQuery<SpecialTag> buildPageQuery(HttpServletRequest request, SpecialTag entity) {
        SqlQuery<SpecialTag> sqlQuery = SqlQuery.from(SpecialTag.class)
                .like(SpecialTagInfo.TAGNAME, entity.getTagName())
                .orderByDesc(SpecialTagInfo.CREATEDATE);
        if (entity.getLeaf() > 0) {
            sqlQuery.equal(SpecialTagInfo.LEAF, entity.getLeaf());
        }
        return sqlQuery;
    }

    @RequestMapping({"/allParent"})
    public ResultEntity allParent() {
        SqlQuery<SpecialTag> sqlQuery = SqlQuery.from(SpecialTag.class)
                .equal(SpecialTagInfo.LEAF, 0)
                .orderByDesc(SpecialTagInfo.CREATEDATE);
        return ResultEntity.success(service.selectList(sqlQuery));
    }

}
