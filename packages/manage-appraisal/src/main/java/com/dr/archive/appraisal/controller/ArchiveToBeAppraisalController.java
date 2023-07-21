package com.dr.archive.appraisal.controller;

import com.dr.archive.appraisal.entity.Archive4ToBeAppraisal;
import com.dr.archive.appraisal.entity.Archive4ToBeAppraisalInfo;
import com.dr.archive.appraisal.service.Archive4ToBeAppraisalService;
import com.dr.archive.manage.category.service.CategoryService;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.fond.service.FondService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.file.service.CommonFileService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 待鉴定档案管理
 */
@RestController
@RequestMapping("api/archiveToBeAppraisal")
public class ArchiveToBeAppraisalController extends BaseServiceController<Archive4ToBeAppraisalService, Archive4ToBeAppraisal> {
    @Autowired
    CommonMapper commonMapper;
    @Autowired
    CategoryService categoryService;
    @Autowired
    FondService fondService;
    @Autowired
    CommonFileService fileService;

    @Override
    protected SqlQuery<Archive4ToBeAppraisal> buildPageQuery(HttpServletRequest httpServletRequest, Archive4ToBeAppraisal archive4ToBeAppraisal) {
        SqlQuery sqlQuery = SqlQuery.from(Archive4ToBeAppraisal.class);

        if (StringUtils.isEmpty(archive4ToBeAppraisal.getCategoryCode()) && !StringUtils.isEmpty(archive4ToBeAppraisal.getFondCode())) {
            sqlQuery.in(Archive4ToBeAppraisalInfo.CATEGORYCODE, categoryService.getCategoryCodeByFondId(fondService.findFondByCode(archive4ToBeAppraisal.getFondCode()).getId()));
        } else if (!StringUtils.isEmpty(archive4ToBeAppraisal.getCategoryCode()) &&
                !StringUtils.isEmpty(archive4ToBeAppraisal.getFondCode())) {
            sqlQuery.in(Archive4ToBeAppraisalInfo.CATEGORYCODE, categoryService.getCategoryCodeByParentCode(fondService.findFondByCode(archive4ToBeAppraisal.getFondCode()).getId(), archive4ToBeAppraisal.getCategoryCode()));
        }
        sqlQuery.equal(Archive4ToBeAppraisalInfo.VINTAGES, archive4ToBeAppraisal.getVintages());
        sqlQuery.like(Archive4ToBeAppraisalInfo.TITLE, archive4ToBeAppraisal.getTitle());
        sqlQuery.like(Archive4ToBeAppraisalInfo.ARCHIVECODE, archive4ToBeAppraisal.getArchiveCode());
        if (SecurityHolder.get().currentOrganise().getOrganiseType().equals("dag")){
            sqlQuery.equal(Archive4ToBeAppraisalInfo.ORGANISEID, SecurityHolder.get().currentOrganise().getId());
        }
        sqlQuery.equal(Archive4ToBeAppraisalInfo.BATCHID, archive4ToBeAppraisal.getBatchId());
        sqlQuery.equal(Archive4ToBeAppraisalInfo.APPRAISALTYPE, archive4ToBeAppraisal.getAppraisalType());
        sqlQuery.equal(Archive4ToBeAppraisalInfo.TASKID, archive4ToBeAppraisal.getTaskId());
        sqlQuery.equal(Archive4ToBeAppraisalInfo.MATCHINGRULES, archive4ToBeAppraisal.getMatchingRules());
        sqlQuery.equal(Archive4ToBeAppraisalInfo.AUXILIARYRESULT, archive4ToBeAppraisal.getAuxiliaryResult());
        sqlQuery.isNotNull(Archive4ToBeAppraisalInfo.APPRAISALTYPE);
        sqlQuery.orderByDesc(Archive4ToBeAppraisalInfo.FONDCODE);
        //根据登录人查询全宗信息 用于判断刷新鉴定的数据
        if (StringUtils.isEmpty(archive4ToBeAppraisal.getFondCode())) {
            Fond fond = fondService.findFondByPartyId(SecurityHolder.get().currentOrganise().getId());
            sqlQuery.equal(Archive4ToBeAppraisalInfo.FONDCODE, fond.getCode());
        } else {
            sqlQuery.equal(Archive4ToBeAppraisalInfo.FONDCODE, archive4ToBeAppraisal.getFondCode());
        }
        return sqlQuery;
    }

    @RequestMapping("/startScanArchive")
    public ResultEntity startScanArchive(HttpServletRequest httpServletRequest) throws InterruptedException {
        service.startScanArchive(SecurityHolder.get());
        return ResultEntity.success();
    }

    @RequestMapping("/statistics")
    public ResultEntity statistics(String fondCodes, String appraisalType, String startVintages, String endVintages) {
        return ResultEntity.success(service.statistics(fondCodes, appraisalType, startVintages, endVintages));
    }

}
