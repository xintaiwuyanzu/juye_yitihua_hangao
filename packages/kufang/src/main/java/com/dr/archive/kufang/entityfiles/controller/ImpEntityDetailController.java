package com.dr.archive.kufang.entityfiles.controller;


import com.dr.archive.kufang.entityfiles.entity.ImpEntityDetail;
import com.dr.archive.kufang.entityfiles.entity.ImpEntityDetailInfo;
import com.dr.archive.kufang.entityfiles.service.ImpEntityDetailService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.page.Page;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 实体档案导入详情
 */
@RestController
@RequestMapping("api/impEntityDetail")
public class ImpEntityDetailController extends BaseServiceController<ImpEntityDetailService, ImpEntityDetail> {


    @Override
    protected SqlQuery<ImpEntityDetail> buildPageQuery(HttpServletRequest request, ImpEntityDetail entity) {
        SqlQuery<ImpEntityDetail> sqlQuery = SqlQuery.from(ImpEntityDetail.class);
        if(!StringUtils.isEmpty(entity.getBatchId())){
            sqlQuery.equal(ImpEntityDetailInfo.BATCHID, entity.getBatchId());
        }
        if(!StringUtils.isEmpty(entity.getTitle())){
            sqlQuery.like(ImpEntityDetailInfo.TITLE, entity.getTitle());
        }
        if(!StringUtils.isEmpty(entity.getArchiveCode())){
            sqlQuery.equal(ImpEntityDetailInfo.ARCHIVECODE, entity.getArchiveCode());
        }
        return sqlQuery;
    }
    /**
     * 获得实体档案导入详情
     */
    @RequestMapping("/getDetail/page")
    public ResultEntity getDetailPage(String batchId, String title, String archiveCode, String archiveType,
                                      @RequestParam(defaultValue = "0") int pageIndex,
                                      @RequestParam(defaultValue = Page.DEFAULT_PAGE_SIZE_STR) int pageSize){
        return ResultEntity.success(service.getDetailPage(batchId, title, archiveCode, archiveType, pageIndex, pageSize));
    }
}
