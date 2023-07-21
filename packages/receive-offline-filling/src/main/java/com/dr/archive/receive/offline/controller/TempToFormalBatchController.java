package com.dr.archive.receive.offline.controller;

import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.fond.service.FondService;
import com.dr.archive.model.query.ArchiveDataQuery;
import com.dr.archive.receive.offline.entity.TempToFormalBatch;
import com.dr.archive.receive.offline.entity.TempToFormalBatchInfo;
import com.dr.archive.receive.offline.service.TempToFormalBatchService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 临时库到正式库前的四性检测批次controller
 */
@RestController
@RequestMapping("${common.api-path:/api}/tempToFormal/batch")
public class TempToFormalBatchController extends BaseServiceController<TempToFormalBatchService, TempToFormalBatch> {
    @Autowired
    FondService fondService;
    /**
     * 创建批次
     */
    @PostMapping("/newBatch")
    public ResultEntity newBatch(@RequestParam(name = ArchiveDataQuery.QUERY_KEY, required = false) String queryContent,
                                 ArchiveDataQuery query, String status, String archiveType) {
        //初始化查询条件
        query.parseQuery(queryContent);
        if (StringUtils.hasText(query.getFondId())) {
            Fond fond = fondService.selectById(query.getFondId());
            ArchiveDataQuery.QueryItem queryItem = new ArchiveDataQuery.QueryItem("FOND_CODE", fond.getCode(), ArchiveDataQuery.QueryType.EQUAL);
            List<ArchiveDataQuery.QueryItem> queryItems = query.getQueryItems();
            queryItems.add(queryItem);
            query.setQueryItems(queryItems);
        }
        service.newBatch(query, status, archiveType);
        return ResultEntity.success();
    }

    /**
     * 查询方法
     */
    @Override
    protected SqlQuery<TempToFormalBatch> buildPageQuery(HttpServletRequest httpServletRequest, TempToFormalBatch batch) {
        SqlQuery<TempToFormalBatch> sqlQuery = SqlQuery.from(TempToFormalBatch.class);
        if (!StringUtils.isEmpty(batch.getBatchName())) {
            sqlQuery.like(TempToFormalBatchInfo.BATCHNAME, batch.getBatchName());
        }
        Person person = getUserLogin(httpServletRequest);
        if (!"admin".equals(person.getUserCode())) {
            sqlQuery.equal(TempToFormalBatchInfo.CREATEPERSON, person.getId());
        }
        sqlQuery.equal(TempToFormalBatchInfo.BATCHTYPE, batch.getBatchType()).orderByDesc(TempToFormalBatchInfo.CREATEDATE);
        return sqlQuery;

    }

    /**
     * 获取入库检测接收报告
     */
    @RequestMapping("/getReport")
    public ResultEntity getReport(String batchId) {
        return ResultEntity.success(service.getReport(batchId));
    }

}
