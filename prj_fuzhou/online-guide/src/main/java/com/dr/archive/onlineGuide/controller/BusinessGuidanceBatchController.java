package com.dr.archive.onlineGuide.controller;

import com.dr.archive.onlineGuide.entity.BusinessGuidanceBatch;
import com.dr.archive.onlineGuide.entity.BusinessGuidanceBatchDetail;
import com.dr.archive.onlineGuide.entity.BusinessGuidanceBatchDetailInfo;
import com.dr.archive.onlineGuide.entity.BusinessGuidanceBatchInfo;
import com.dr.archive.onlineGuide.service.BusinessGuidanceBatchService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author: caor
 * @Date: 2022-06-10 15:27
 * @Description:
 */
@RestController
@RequestMapping("api/businessGuidanceBatch")
public class BusinessGuidanceBatchController extends BaseServiceController<BusinessGuidanceBatchService, BusinessGuidanceBatch> {
    @Autowired
    CommonMapper commonMapper;
    /**
     * 1、菜单需要配置角色编码
     * 2、当前添加人和接收者机构id，能够看到数据
     *
     * @param httpServletRequest
     * @param businessGuidanceBatch
     * @return
     */
    /*@Override
    protected SqlQuery<BusinessGuidanceBatch> buildPageQuery(HttpServletRequest httpServletRequest, BusinessGuidanceBatch businessGuidanceBatch) {
        Organise organise = SecurityHolder.get().currentOrganise();
        Person person = SecurityHolder.get().currentPerson();
        SqlQuery<BusinessGuidanceBatch> sqlQuery = SqlQuery.from(BusinessGuidanceBatch.class)
                .equal(BusinessGuidanceBatchInfo.CREATEPERSON, person.getId())
                .or()
                .equal(BusinessGuidanceBatchInfo.RECEIVEORGID, organise.getId())
                .andNew()
                .like(BusinessGuidanceBatchInfo.BATCHNAME, businessGuidanceBatch.getBatchName())
                .equal(BusinessGuidanceBatchInfo.STATUS, businessGuidanceBatch.getStatus())
                .orderByDesc(BusinessGuidanceBatchInfo.CREATEDATE);
        List<BusinessGuidanceBatch> businessGuidanceBatches = commonMapper.selectByQuery(sqlQuery);
        businessGuidanceBatches.forEach(item ->{
            List<BusinessGuidanceBatchDetail> businessGuidanceBatchDetails = commonMapper.selectByQuery(SqlQuery.from(BusinessGuidanceBatchDetail.class).equal(BusinessGuidanceBatchDetailInfo.BATCHID, item.getId()));
            item.setDetailNum(businessGuidanceBatchDetails.size());
            commonMapper.updateById(item);
        } );
        return sqlQuery;
    }*/

    @Override
    protected SqlQuery<BusinessGuidanceBatch> buildPageQuery(HttpServletRequest httpServletRequest, BusinessGuidanceBatch businessGuidanceBatch) {
        Organise organise = SecurityHolder.get().currentOrganise();
        SqlQuery<BusinessGuidanceBatch> sqlQuery = SqlQuery.from(BusinessGuidanceBatch.class)
                .equal(BusinessGuidanceBatchInfo.RECEIVEORGID,organise.getId())
                .equal(BusinessGuidanceBatchInfo.STATUS,businessGuidanceBatch.getStatus())
                .like(BusinessGuidanceBatchInfo.BATCHNAME,businessGuidanceBatch.getBatchName());
        return sqlQuery;
    }

    /**
     * 发起单条记录申请
     *
     * @param request
     */
    @RequestMapping({"/guidance"})
    public ResultEntity guidance(HttpServletRequest request) {
        return ResultEntity.success(service.guidance(request.getParameter("formDefinitionId"), request.getParameter("formDataId"), request.getParameter("description")));
    }

    /**
     * 发起指导
     * @param request
     * @return
     */
    @RequestMapping({"/guidanceProblem"})
    public ResultEntity guidanceProblem(HttpServletRequest request){
        return ResultEntity.success();
    }

    /**
     * 从业务指导类型档案发起申请
     *
     * @param request
     */
    @RequestMapping({"/batchApply"})
    public ResultEntity batchApply(HttpServletRequest request) {
        return ResultEntity.success(service.batchApply(request.getParameter("archiveCarId"), request.getParameter("description")));
    }
    /**
     * 查询当前档案是否存在为完成的指导批次
     */
    @RequestMapping("/judgeExistence")
    public ResultEntity judgeExistence(HttpServletRequest request){
        return ResultEntity.success(service.judgeExistence(request.getParameter("formDefinitionId"), request.getParameter("formDataId")));
    }

    /**
     *  总数
     * @return
     */
    @RequestMapping("/total")
    public ResultEntity total() {
        return ResultEntity.success(service.total());
    }



}
