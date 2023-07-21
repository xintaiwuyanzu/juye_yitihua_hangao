package com.dr.archive.utilization.search.controller;

import com.dr.archive.utilization.search.entity.EsTemplate;
import com.dr.archive.utilization.search.entity.EsTemplateInfo;
import com.dr.archive.utilization.search.service.EsTemplateService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author qiuyf
 */
@RestController
@RequestMapping("api/esTemplate")
public class EsTemplateController extends BaseServiceController<EsTemplateService, EsTemplate> {

    @Override
    protected SqlQuery<EsTemplate> buildPageQuery(HttpServletRequest request, EsTemplate entity) {
        SqlQuery<EsTemplate> sqlQuery = SqlQuery.from(EsTemplate.class)
                .orderBy(EsTemplateInfo.ORDERBY);
        return sqlQuery;
    }

    @RequestMapping("/rebuildTemplate")
    public ResultEntity rebuildTemplate(String id) {
        return ResultEntity.success(service.rebuildTemplate(id));
    }

    @RequestMapping("/deleteTemplate")
    public ResultEntity deleteTemplate(String id) {
        return ResultEntity.success(service.deleteTemplate(id));
    }

    /**
     * 元数据管理 重建es索引
     *
     * @param formDefinitionId
     * @param rebuildType
     * @return
     */
    @RequestMapping(value = "/rebuildIndex")
    public ResultEntity rebuildIndex(String formDefinitionId, String rebuildType) {
        if ("1".equalsIgnoreCase(rebuildType)) {
            //已存在索引则,进行复制,无索引,重新建
            service.rebuildIndex(formDefinitionId);
        } else { //直接删除,重建
            service.rebuildIndex2(formDefinitionId);
        }
        return ResultEntity.success("正在重建es中,请稍后查看结果!");
    }
}
