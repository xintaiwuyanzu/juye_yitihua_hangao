package com.dr.archive.receive.online.controller;

import com.dr.archive.batch.query.BatchCreateQuery;
import com.dr.archive.manage.task.entity.ArchiveTask;
import com.dr.archive.model.query.ArchiveDataQuery;
import com.dr.archive.receive.online.entity.ExpBatch;
import com.dr.archive.receive.online.entity.ExpBatchDetail;
import com.dr.archive.receive.online.entity.ExpBatchDetailInfo;
import com.dr.archive.receive.online.entity.ExpBatchInfo;
import com.dr.archive.receive.online.service.ExpBatchDetailService;
import com.dr.archive.receive.online.service.ExpBatchService;
import com.dr.archive.util.UUIDUtils;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.file.FileInfoHandler;
import com.dr.framework.common.file.autoconfig.CommonFileConfig;
import com.dr.framework.common.file.resource.FileSystemFileResource;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.web.annotations.Current;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author: yang
 * @create: 2022-08-04 10:30
 **/
@RestController
@RequestMapping({"${common.api-path:/api}/expBatch"})
public class ExpBatchController extends BaseServiceController<ExpBatchService, ExpBatch> {

    @Autowired
    CommonFileConfig commonFileConfig;
    @Autowired
    FileInfoHandler fileInfoHandler;
    @Autowired
    ExpBatchDetailService expBatchDetailService;

    @Override
    protected SqlQuery<ExpBatch> buildPageQuery(HttpServletRequest httpServletRequest, ExpBatch expBatch) {
        return SqlQuery.from(ExpBatch.class).orderByDesc(ExpBatchInfo.CREATEDATE, ExpBatchInfo.UPDATEDATE);
    }

    @PostMapping("/newBatch")
    public ResultEntity<ArchiveTask> newBatch(HttpServletRequest request, BatchCreateQuery query, String type, MultipartFile file, @RequestParam(name = ArchiveDataQuery.QUERY_KEY, required = false) String queryContent, @Current Person person) {
        Assert.isTrue(!StringUtils.isEmpty(type), "批次类型不能为空！");
        query.parseQuery(queryContent);
        if (file != null) {
            try (InputStream inputStream = file.getInputStream()) {
                //上传文件
                String filename = file.getOriginalFilename();
                filename = UUIDUtils.getUUID() + filename;
                //保存文件
                String filePath = commonFileConfig.uploadFile("imp", inputStream, filename, true);
                String mine = fileInfoHandler.fileMine(new FileSystemFileResource(filePath));
                //设置文件存储位置
                query.setFileLocation(filePath);
                query.setMineType(mine);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        service.newBatch(query, person);
        return ResultEntity.success();
    }

    @Override
    public ResultEntity<Boolean> delete(HttpServletRequest request, ExpBatch entity) {
        expBatchDetailService.delete(SqlQuery.from(ExpBatchDetail.class).equal(ExpBatchDetailInfo.BATCHID, entity.getId()));
        return super.delete(request, entity);
    }
}
