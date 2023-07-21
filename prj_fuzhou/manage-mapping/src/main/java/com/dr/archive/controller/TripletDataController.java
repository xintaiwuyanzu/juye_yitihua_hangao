package com.dr.archive.controller;

import com.dr.archive.entity.TripletData;
import com.dr.archive.entity.TripletDataInfo;
import com.dr.archive.service.TripletDataService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: yang
 * @create: 2022-06-09 11:49
 **/
@RestController
@RequestMapping("/api/tripletData")
public class TripletDataController extends BaseServiceController<TripletDataService, TripletData> {
    @Override
    protected SqlQuery<TripletData> buildPageQuery(HttpServletRequest httpServletRequest, TripletData tripletData) {
        return SqlQuery.from(TripletData.class, false)
                .like(TripletDataInfo.SOURCEFORMNAME, tripletData.getSourceFormName())
                .like(TripletDataInfo.TARGETFORMNAME, tripletData.getTargetFormName())
                .like(TripletDataInfo.RELATIONNAME, tripletData.getRelationName())
                .column(TripletDataInfo.SOURCEFORMNAME, TripletDataInfo.TARGETFORMNAME, TripletDataInfo.RELATIONNAME, TripletDataInfo.CREATEDATE, TripletDataInfo.STATUS, TripletDataInfo.SOURCEFORMNAME.count().alias("status"))
                .groupBy(TripletDataInfo.SOURCEFORMNAME, TripletDataInfo.TARGETFORMNAME, TripletDataInfo.RELATIONNAME);
    }

    @RequestMapping("/getAll")
    public ResultEntity getAll(TripletData tripletData) {
        SqlQuery<TripletData> sqlQuery = SqlQuery.from(TripletData.class)
                .like(TripletDataInfo.SOURCENAME, tripletData.getSourceName())
                .like(TripletDataInfo.TARGETNAME, tripletData.getTargetName())
                .like(TripletDataInfo.SOURCEFORMNAME, tripletData.getSourceFormName())
                .like(TripletDataInfo.TARGETFORMNAME, tripletData.getTargetFormName())
                .like(TripletDataInfo.RELATIONNAME, tripletData.getRelationName())
                .groupBy(TripletDataInfo.SOURCEFORMID, TripletDataInfo.TARGETFORMID, TripletDataInfo.RELATIONID);
        return ResultEntity.success(service.selectPage(sqlQuery, 0, tripletData.getOrder()));
    }

    @RequestMapping("/getMarkedRelations")
    public ResultEntity getMarkedRelations(TripletData tripletData) {
        SqlQuery<TripletData> sqlQuery = SqlQuery.from(TripletData.class)
                .equal(TripletDataInfo.BASEFORMID, tripletData.getBaseFormId());
        return ResultEntity.success(service.selectList(sqlQuery));
    }

}
