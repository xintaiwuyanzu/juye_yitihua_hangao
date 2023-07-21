package com.dr.archive.examine.controller;

import com.dr.archive.examine.entity.JdzdWeight;
import com.dr.archive.examine.entity.JdzdWeightInfo;
import com.dr.archive.examine.service.JdzdWeightService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 监督指导  单位权重管理
 *
 * @author dr
 */
@RestController
@RequestMapping("api/jdzdWeight")
public class JdzdWeightController extends BaseServiceController<JdzdWeightService, JdzdWeight> {

    @Override
    protected SqlQuery<JdzdWeight> buildPageQuery(HttpServletRequest httpServletRequest, JdzdWeight jdzdWeight) {
        return SqlQuery.from(JdzdWeight.class).like(JdzdWeightInfo.ORGANISENAME, jdzdWeight.getOrganiseName())
                .equal(JdzdWeightInfo.ORGANISEID, jdzdWeight.getOrganiseId())
                .orderByDesc(JdzdWeightInfo.WEIGHT)
                .orderByDesc(JdzdWeightInfo.UPDATEDATE);
    }

    /**
     * 清空单位权重
     *
     * @return
     */
    @RequestMapping("clear")
    public ResultEntity clear() {
        service.clear();
        return ResultEntity.success();
    }

    /**
     * 一键新增机构权重
     *
     * @return
     */
    @RequestMapping("addAuto")
    public ResultEntity addAuto() {
        service.addAuto();
        return ResultEntity.success();
    }

    @Override
    protected SqlQuery<JdzdWeight> buildDeleteQuery(HttpServletRequest request, JdzdWeight entity) {
        Assert.isTrue(StringUtils.hasText(entity.getId()), "要删除的数据不能为空！");
        return SqlQuery.from(JdzdWeight.class).in(JdzdWeightInfo.ID, entity.getId().split(","));
    }

    /**
     * 随机num个单位  其实就是排序前多少个单位
     *
     * @return
     */
    @RequestMapping("random")
    public ResultEntity random(Integer num) {
        return ResultEntity.success(service.random(num));
    }
}
