package com.dr.archive.appraisal.controller;

import com.dr.archive.appraisal.entity.AppraisalBasisInfo;
import com.dr.archive.appraisal.entity.AppraisalKeyWord;
import com.dr.archive.appraisal.entity.AppraisalKeyWordInfo;
import com.dr.archive.appraisal.entity.AppraisalRulesInfo;
import com.dr.archive.appraisal.service.AppraisalKeyWordService;
import com.dr.archive.appraisal.service.Archive4ToBeAppraisalService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 档案鉴定过滤词
 */
@RestController
@RequestMapping("api/appraisalKeyWord")
public class AppraisalKeyWordController extends BaseServiceController<AppraisalKeyWordService, AppraisalKeyWord> {

    @Autowired
    Archive4ToBeAppraisalService archive4ToBeAppraisalService;
    @Autowired
    CommonMapper commonMapper;

    @Override
    protected SqlQuery<AppraisalKeyWord> buildPageQuery(HttpServletRequest httpServletRequest, AppraisalKeyWord appraisalKeyWord) {
        SqlQuery sqlQuery = SqlQuery.from(AppraisalKeyWord.class)
                .like(AppraisalKeyWordInfo.KEYWORD, appraisalKeyWord.getKeyWord())
                .equal(AppraisalKeyWordInfo.RULESID, appraisalKeyWord.getRulesId());
        return sqlQuery;
    }

    @RequestMapping({"/page2"})
    public ResultEntity page2(HttpServletRequest request, AppraisalKeyWord entity, @RequestParam(defaultValue = "0") int pageIndex, @RequestParam(defaultValue = "15") int pageSize, @RequestParam(defaultValue = "true") boolean page) {
        SqlQuery<Map> sqlQuery = SqlQuery.from(AppraisalKeyWord.class)
                .leftOuterJoin(AppraisalKeyWordInfo.BASISID, AppraisalBasisInfo.ID)
                .leftOuterJoin(AppraisalKeyWordInfo.RULESID, AppraisalRulesInfo.ID)
                .column(AppraisalKeyWordInfo.ID)
                .column(AppraisalKeyWordInfo.KEYWORD)
                .column(AppraisalKeyWordInfo.CREATEDATE)
                .column(AppraisalKeyWordInfo.STATUS)
                .column(AppraisalBasisInfo.BASIS.alias("basisName"))
                .column(AppraisalRulesInfo.RULESNAME.alias("rulesName"))
                .like(AppraisalKeyWordInfo.KEYWORD, entity.getKeyWord())
                .equal(AppraisalKeyWordInfo.RULESID, entity.getRulesId())
                .orderBy(AppraisalKeyWordInfo.BASISID, AppraisalKeyWordInfo.RULESID)
                .orderByDesc(AppraisalKeyWordInfo.CREATEDATE)
                .setReturnClass(Map.class);
        Object result = page ? commonMapper.selectPageByQuery(sqlQuery, pageIndex * pageSize, (pageIndex + 1) * pageSize) : commonMapper.selectByQuery(sqlQuery);
        return ResultEntity.success(result);
    }

    @Override
    public ResultEntity<AppraisalKeyWord> insert(HttpServletRequest request, AppraisalKeyWord entity) {
        List<AppraisalKeyWord> oldKeywords = service.selectList(SqlQuery.from(AppraisalKeyWord.class)
                .equal(AppraisalKeyWordInfo.KEYWORD, entity.getKeyWord()));
        if (oldKeywords.size() > 0) {
            return ResultEntity.error("关键词已存在!");
        }
        entity.setOrgId(SecurityHolder.get().currentOrganise().getId());
        this.service.insert(entity);
        archive4ToBeAppraisalService.removeCache(entity.getOrgId());
        return ResultEntity.success(entity);
    }

    @Override
    public ResultEntity<AppraisalKeyWord> update(HttpServletRequest request, AppraisalKeyWord entity) {
        super.update(request, entity);
        archive4ToBeAppraisalService.removeCache(entity.getOrgId());
        return ResultEntity.success(entity);
    }

    @Override
    public ResultEntity<Boolean> delete(HttpServletRequest request, AppraisalKeyWord entity) {
        String[] ids = entity.getId().split(",");
        String orgId = "";
        for (String id : ids) {
            AppraisalKeyWord e = service.selectById(id);
            orgId = e.getOrgId();
            super.delete(request, e);
        }
        archive4ToBeAppraisalService.removeCache(orgId);
        return ResultEntity.success(true);
    }

    /*筛选重复关键词*/
    @RequestMapping({"/repeatKeyWord"})
    public ResultEntity repeatKeyWord() {
        SqlQuery<Map> sqlQuery = SqlQuery.from(AppraisalKeyWord.class)
                .column(AppraisalKeyWordInfo.KEYWORD)
                .column(AppraisalKeyWordInfo.ID.count("countNum"))
                .groupBy(AppraisalKeyWordInfo.KEYWORD)
                .orderByDesc(AppraisalKeyWordInfo.ID.count("countNum"))
                .setReturnClass(Map.class);
        List<Map> list = commonMapper.selectByQuery(sqlQuery);
        list = list.stream().filter(map -> !"1".equals(map.get("countNum").toString())).collect(Collectors.toList());
        return ResultEntity.success(list);
    }
}
