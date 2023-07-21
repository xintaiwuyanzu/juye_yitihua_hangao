package com.dr.archive.controller;

import com.dr.archive.entity.*;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.service.FondRelationService;
import com.dr.archive.service.StructuredTextService;
import com.dr.archive.service.UnStructuredTextService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.organise.service.OrganisePersonService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import com.dr.framework.core.security.service.ResourceManager;
import org.camunda.feel.syntaxtree.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: yang
 * @create: 2022-06-03 15:25
 **/
@RestController
@RequestMapping("/api/unStructuredText")
public class UnStructuredTextController extends BaseServiceController<UnStructuredTextService, UnStructuredText> {
    private static final Integer UnWarehousing = 0; //未入库状态
    private static final Integer Warehousing = 1; //入库状态

    @Autowired
    private FondRelationService relationService;
    @Autowired
    ResourceManager resourceManager;

    @Override
    protected SqlQuery<UnStructuredText> buildPageQuery(HttpServletRequest httpServletRequest, UnStructuredText unStructuredText) {
        List<Fond> fonds = (List<Fond>) resourceManager.getPersonResources(SecurityHolder.get().currentPerson().getId(), "fond");
        List<String> fondCodeList = fonds.stream().map(Fond::getCode).collect(Collectors.toList());
        return SqlQuery.from(UnStructuredText.class)
                                .in(UnStructuredTextInfo.FONDCODE, fondCodeList)
                .equal(UnStructuredTextInfo.FONDID,unStructuredText.getFondId())
                .equal(UnStructuredTextInfo.STATUS, unStructuredText.getStatus())
                .equal(UnStructuredTextInfo.FONDCODE,unStructuredText.getFondCode())
                .like(UnStructuredTextInfo.TIMING,unStructuredText.getTIMING())
                .equal(UnStructuredTextInfo.CATEGORYCODE,unStructuredText.getCategoryCode())
                .orderBy(UnStructuredTextInfo.ARCHIVECODE)
                .orderByDesc(UnStructuredTextInfo.CREATEDATE);
    }

    @RequestMapping("flush")
    public ResultEntity flushData(){
        SqlQuery<UnStructuredText> query = SqlQuery.from(UnStructuredText.class)
                .equal(UnStructuredTextInfo.STATUS,UnWarehousing);
        for (UnStructuredText text : service.selectList(query)) {
            long count = 0;
            if (StringUtils.hasText(text.getFondId())){
                 count = relationService.count(SqlQuery.from(FondRelation.class).
                        equal(FondRelationInfo.FONDID, text.getFondId()));
            }
            text.setRelationNum(count+"");
            service.updateById(text);
        }
        return ResultEntity.success();
    }
}
