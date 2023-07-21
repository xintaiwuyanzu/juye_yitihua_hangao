package com.dr.archive.receive.offline.controller;

import com.dr.archive.batch.controller.AbstractArchiveBatchController;
import com.dr.archive.batch.query.BatchCreateQuery;
import com.dr.archive.manage.form.service.ArchiveDataManager;
import com.dr.archive.manage.form.service.ArchiveFormDefinitionService;
import com.dr.archive.manage.task.entity.ArchiveTask;
import com.dr.archive.model.entity.ArchiveEntity;
import com.dr.archive.receive.offline.entity.ArchiveBatchReceiveOffline;
import com.dr.archive.receive.offline.entity.ArchiveBatchReceiveOfflineDetail;
import com.dr.archive.receive.offline.entity.ArchiveBatchReceiveOfflineDetailInfo;
import com.dr.archive.receive.offline.entity.ArchiveBatchReceiveOfflineInfo;
import com.dr.archive.receive.offline.service.ArchiveBatchOfflineDetailService;
import com.dr.archive.receive.offline.service.ArchiveBatchOfflineService;
import com.dr.archive.util.UUIDUtils;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.form.core.service.FormDataService;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.web.annotations.Current;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * 离线接收批次controller
 *
 * @Author: caor
 * @Date: 2021-12-17 12:57
 * @Description:
 */
@RestController
@RequestMapping("${common.api-path:/api}/receive/offline")
public class ArchiveBatchOfflineController extends AbstractArchiveBatchController<ArchiveBatchOfflineService, ArchiveBatchReceiveOffline> {
    @Autowired
    ArchiveBatchOfflineDetailService offlineDetailService;
    @Autowired
    ArchiveDataManager archiveDataManager;
    @Autowired
    FormDataService formDataService;

    /**
     * 根据上传目录创建离线导入目录批次
     *
     * @param request
     * @param query
     * @param file
     * @param person
     * @return
     */
    @PostMapping("/newBatch")
    public ResultEntity<ArchiveTask> newBatch(HttpServletRequest request, BatchCreateQuery query, MultipartFile file, @Current Person person) {
        //初始化查询条件
        initQuery(request, query);
        //保存上传文件
        saveFile(query, file);
        //执行service创建批次方法
        service.newBatch(query, person);
        return ResultEntity.success();
    }

    /**
     * 执行挂接操作
     *
     * @param request
     * @param isDeleteFile
     * @return
     */
    @RequestMapping("/startHook")
    public ResultEntity<String> startHook(HttpServletRequest request, @RequestParam(defaultValue = "false") boolean isDeleteFile) {
        service.hookByBatchId(request.getParameter("type"), request.getParameter("fileLocations"), request.getParameter("filePath"), request.getParameter("impBatchId"), request.getParameter("clientBatchId"), isDeleteFile, request.getParameter("coverOrAdd"));
        return ResultEntity.success("正在挂接中，请稍后刷新数据查看挂接结果");
    }

    /**
     * 查询方法
     *
     * @param request
     * @param batch
     * @return
     */
    @Override
    protected SqlQuery<ArchiveBatchReceiveOffline> buildPageQuery(HttpServletRequest request, ArchiveBatchReceiveOffline batch) {
        SqlQuery<ArchiveBatchReceiveOffline> sqlQuery = SqlQuery.from(ArchiveBatchReceiveOffline.class);
        if (!StringUtils.isEmpty(batch.getBatchName())) {
            sqlQuery.like(ArchiveBatchReceiveOfflineInfo.BATCHNAME, batch.getBatchName());
        }
        Person person = getUserLogin(request);
        if (!"admin".equals(person.getUserCode())) {
            sqlQuery.equal(ArchiveBatchReceiveOfflineInfo.CREATEPERSON, person.getId());
        }
        if (StringUtils.hasText(batch.getTypologic())) {
            sqlQuery.equal(ArchiveBatchReceiveOfflineInfo.TYPOLOGIC, batch.getTypologic());
        }
        sqlQuery.equal(ArchiveBatchReceiveOfflineInfo.BATCHTYPE, batch.getBatchType()).orderByDesc(ArchiveBatchReceiveOfflineInfo.CREATEDATE);
        return sqlQuery;
    }

    /*重新进行四性检测*/
    @RequestMapping("/doTestOffline")
    public ResultEntity doTestOffline(String batchId) {
        service.doTestOffline(batchId);
        return ResultEntity.success("正在重新检测中，请稍后刷新数据查看检测结果!");
    }

    /**
     * 下载
     *
     * @param id
     * @return
     */
    @RequestMapping("/getUploadDownLoadPath")
    public void getUploadDownLoadPath(HttpServletResponse response, String id) {
        ArchiveBatchReceiveOffline archiveBatchReceiveOffline = service.selectById(id);
        try (InputStream in = new FileInputStream(archiveBatchReceiveOffline.getFileLocation())) {
            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(UUIDUtils.getUUID() + archiveBatchReceiveOffline.getFileLocation().substring(archiveBatchReceiveOffline.getFileLocation().lastIndexOf(".")), "UTF-8"));
            StreamUtils.copy(in, response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取接收报告
     */
    @RequestMapping("/getReport")
    public ResultEntity getReport(String batchId) {
        return ResultEntity.success(service.getReport(batchId));
    }

    @RequestMapping({"/delete"})
    public ResultEntity delete(HttpServletRequest request, ArchiveBatchReceiveOffline entity) {
        service. extracted(entity);
        return  ResultEntity.success("请求成功，请刷新后查看！");
    }
    @RequestMapping(value = "/deleteForm")
    public ResultEntity deleteForm(String formId, String categoryId, String id) {
        service.deleteFormData(categoryId, formId, id);
        return ResultEntity.success();
    }
    /**
     * 获取错误接收报告
     */
    @RequestMapping("/getError")
    public ResultEntity getError(String batchId) {
        return ResultEntity.success(service.getError(batchId));
    }

    /**
     * 获取配置文件中数据维护客户端地址
     *
     * @return
     */
    @RequestMapping("/getClientUrl")
    public ResultEntity getClientUrl() {
        return ResultEntity.success(service.getClientUrl());
    }
}
