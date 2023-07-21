package com.dr.archive.fuzhou.approve.fileReturn.controller;

import com.dr.archive.batch.query.BatchCreateQuery;
import com.dr.archive.common.dzdacqbc.service.EArchiveService;
import com.dr.archive.fuzhou.approve.fileReturn.service.ReturnService;
import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.category.service.CategoryService;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.fond.service.FondService;
import com.dr.archive.manage.form.service.ArchiveDataManager;
import com.dr.archive.model.query.ArchiveDataQuery;
import com.dr.archive.receive.offline.service.TempToFormalBatchDetailService;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * @Author: caor
 * @Date: 2021-12-01 16:01
 * @Description: 福州档案档案室在线接收的退回功能
 */
@RestController
@RequestMapping("api/return")
public class ReturnController {
    @Autowired
    ReturnService returnService;
    @Autowired
    ArchiveDataManager archiveDataManager;
    @Autowired
    EArchiveService eArchiveService;
    /**
     * 用来执行异步操作
     */
    @Autowired
    protected Executor executor;
    @Autowired
    FondService fondService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    TempToFormalBatchDetailService tempToFormalBatchDetailService;

    /**
     * 退回
     *
     * @param request
     * @param queryContent
     * @param query
     * @return
     */
    @RequestMapping("/fileReturn")
    public ResultEntity fileReturn(HttpServletRequest request, @RequestParam(name = ArchiveDataQuery.QUERY_KEY, required = false) String queryContent, BatchCreateQuery query) {
        query.parseQuery(queryContent);
        String formDefinitionId = request.getParameter("formDefinitionId");
        String returnReason = request.getParameter("returnReason");
        Assert.isTrue(!StringUtils.isEmpty(formDefinitionId), "表单ID不能为空！");
        return ResultEntity.success(returnService.fileReturn(query, returnReason));
    }

    @RequestMapping("/newFileReturn")
    public ResultEntity newFileReturn(BatchCreateQuery query, String receiptIds) {

        returnService.newFileReturn(query, receiptIds);

        return ResultEntity.success();
    }

    /**
     * 退回到接收库
     *
     * @param request
     * @param queryContent
     * @param query
     * @return
     */
    @RequestMapping("/fileReturnRec")
    public ResultEntity fileReturnRec(HttpServletRequest request, @RequestParam(name = ArchiveDataQuery.QUERY_KEY, required = false) String queryContent, BatchCreateQuery query) {
        query.parseQuery(queryContent);
        String formDefinitionId = request.getParameter("formDefinitionId");
        String status = request.getParameter("status");
        Assert.isTrue(!StringUtils.isEmpty(formDefinitionId), "表单ID不能为空！");
        Fond fond = fondService.selectById(query.getFondId());
        query.setFondCode(fond.getCode());
        ArchiveDataQuery.QueryItem queryItem = new ArchiveDataQuery.QueryItem("FOND_CODE", fond.getCode(), ArchiveDataQuery.QueryType.EQUAL);
        List<ArchiveDataQuery.QueryItem> queryItems = query.getQueryItems();
        queryItems.add(queryItem);
        query.setQueryItems(queryItems);
        Category category = categoryService.selectById(query.getCategoryId());
        query.setCategoryCode(category.getCode());
//        returnService.fileReturnRec(query,status);
        SecurityHolder securityHolder = SecurityHolder.get();
        executor.execute(() -> {
            SecurityHolder.set(securityHolder);
            //异步执行
            archiveDataManager.updateStatusByQueryPlus(query, status);
            //档案退回至临时库，删除长期保存的数据
            eArchiveService.backArchives(query);
            // 档案退回至临时库(退回全部/退会选中)，删除实体档案的数据  正式库案卷/卷内的id 对应 实体档案案卷/卷内的dataid
            tempToFormalBatchDetailService.deleteEntityFiles(query);
        });
        return ResultEntity.success();
    }


    /**
     * 退回校验
     *
     * @param request
     * @param queryContent
     * @param query
     * @return
     */
    @RequestMapping("/fileReturnCheck")
    public ResultEntity fileReturnCheck(HttpServletRequest request, @RequestParam(name = ArchiveDataQuery.QUERY_KEY, required = false) String queryContent, BatchCreateQuery query) {
        query.parseQuery(queryContent);
        String formDefinitionId = request.getParameter("formDefinitionId");
        Assert.isTrue(!StringUtils.isEmpty(formDefinitionId), "表单ID不能为空！");
        return returnService.fileReturnCheck(query);
    }
}
