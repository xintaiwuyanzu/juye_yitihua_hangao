package com.dr.archive.common.dzdacqbc.controller;

import com.dr.archive.common.dzdacqbc.entity.ESaveOffLine;

import com.dr.archive.common.dzdacqbc.entity.ESaveOffLineInfo;
import com.dr.archive.common.dzdacqbc.service.ESaveBackBatchDetailService;
import com.dr.archive.common.dzdacqbc.service.ESaveOffLineService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 离线备份
 */

@RestController
@RequestMapping("/api/esave/offLine")
public class ESaveOffLineController extends BaseServiceController<ESaveOffLineService, ESaveOffLine> {

    @Autowired
    ESaveBackBatchDetailService detailService;

    @Override
    protected SqlQuery<ESaveOffLine> buildPageQuery(HttpServletRequest httpServletRequest, ESaveOffLine eSaveOffLine) {
        SqlQuery<ESaveOffLine> sqlQuery = SqlQuery.from(ESaveOffLine.class);
        if(StringUtils.hasText(eSaveOffLine.getStrategyName())){
            sqlQuery.like(ESaveOffLineInfo.STRATEGYNAME, eSaveOffLine.getStrategyName());
        }
        if(StringUtils.hasText(eSaveOffLine.getFondId())){
            sqlQuery.equal(ESaveOffLineInfo.FONDID, eSaveOffLine.getFondId());
        }
        if(StringUtils.hasText(eSaveOffLine.getClassId())){
            sqlQuery.equal(ESaveOffLineInfo.CLASSID, eSaveOffLine.getClassId());
        }
        sqlQuery.equal(ESaveOffLineInfo.ORGID, SecurityHolder.get().currentOrganise().getId());
        sqlQuery.orderByDesc(ESaveOffLineInfo.CREATEDATE);
        return sqlQuery;
    }

    @Override
    public ResultEntity<ESaveOffLine> insert(HttpServletRequest request, ESaveOffLine entity) {
        entity = service.addBefore(entity);
        entity.setStatus("0");
        entity.setOrgId(SecurityHolder.get().currentOrganise().getId());
        super.insert(request, entity);
        //添加备份详情
        detailService.addDetailLX(entity.getId());
        return ResultEntity.success(entity);
    }

    @Override
    public ResultEntity<ESaveOffLine> update(HttpServletRequest request, ESaveOffLine entity) {
        entity = service.changeBefore(entity);
        return super.update(request, entity);
    }
    /**
     * 将离线上传的数据，提交到客户端
     */
    @RequestMapping("/sendDataToClient")
    public ResultEntity sendDataToClient(String offLineId){
        return ResultEntity.success(service.sendDataToClient(offLineId));
    }
}
