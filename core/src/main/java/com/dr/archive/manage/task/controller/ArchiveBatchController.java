package com.dr.archive.manage.task.controller;

import com.dr.archive.batch.entity.AbstractArchiveBatch;
import com.dr.archive.batch.entity.AbstractArchiveBatchInfo;
import com.dr.archive.batch.entity.AbstractBatchDetailEntity;
import com.dr.archive.batch.query.BatchCreateQuery;
import com.dr.archive.batch.service.ArchiveBatchService;
import com.dr.archive.batch.service.BaseArchiveBatchDetailService;
import com.dr.archive.batch.vo.BatchCount;
import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.manage.category.entity.CategoryConfigInfo;
import com.dr.archive.manage.category.entity.CategoryInfo;
import com.dr.archive.manage.form.service.ArchiveDataManager;
import com.dr.archive.manage.task.entity.AppraisalBatchDetail;
import com.dr.archive.manage.task.entity.ArchiveTask;
import com.dr.archive.manage.task.service.ArchiveBatchDetailsService;
import com.dr.archive.model.query.ArchiveDataQuery;
import com.dr.archive.util.UUIDUtils;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.file.FileInfoHandler;
import com.dr.framework.common.file.autoconfig.CommonFileConfig;
import com.dr.framework.common.file.resource.FileSystemFileResource;
import com.dr.framework.common.page.Page;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.web.annotations.Current;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * 具体项
 *
 * @author dr
 */
//@RestController
//@RequestMapping({"${common.api-path:/api}/batch"})
public class ArchiveBatchController extends BaseServiceController<ArchiveBatchService, AbstractArchiveBatch> {
    @Autowired
    CommonFileConfig commonFileConfig;
    @Autowired
    FileInfoHandler fileInfoHandler;
    @Autowired
    ArchiveDataManager dataManager;
    @Autowired
    Map<String, BaseArchiveBatchDetailService> baseBatchServiceMap;
    @Autowired
    ArchiveBatchDetailsService archiveBatchDetailsService;
    @Autowired
    CommonMapper commonMapper;

    @Override
    protected SqlQuery<AbstractArchiveBatch> buildPageQuery(HttpServletRequest httpServletRequest, AbstractArchiveBatch batch) {
        SqlQuery<AbstractArchiveBatch> sqlQuery = SqlQuery.from(AbstractArchiveBatch.class);
        if (!StringUtils.isEmpty(batch.getBatchName())) {
            sqlQuery.like(AbstractArchiveBatchInfo.BATCHNAME, batch.getBatchName());
        }
        Person person = getUserLogin(httpServletRequest);
        if (!"admin".equals(person.getUserCode())) {
            sqlQuery.equal(AbstractArchiveBatchInfo.CREATEPERSON, person.getId());
        }
        sqlQuery.equal(AbstractArchiveBatchInfo.BATCHTYPE, batch.getBatchType()).orderByDesc(AbstractArchiveBatchInfo.CREATEDATE);
        return sqlQuery;
    }

    @Override
    public ResultEntity<Boolean> delete(HttpServletRequest request, AbstractArchiveBatch entity) {
        ResultEntity<Boolean> delete = null;
        try {
            delete = super.delete(request, entity);
            String type = request.getParameter("type");
            String id = request.getParameter("id");
            BaseArchiveBatchDetailService service = baseBatchServiceMap.get("impBatchDetailServiceImpl");
            service.deleteByBatchId(entity.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return delete;
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

    /**
     * 查询批次统计数量
     *
     * @param batchId
     * @return
     */
    @PostMapping("/batchCount")
    public ResultEntity<BatchCount> batchCount(String type, String batchId) {
        return ResultEntity.success(service.count(type, batchId));
    }

    /**
     * 更新批次状态
     *
     * @param type
     * @param detailId
     * @param status
     * @param advice
     * @return
     */
    @RequestMapping("/changeStatus")
    public ResultEntity changeStatus(String type, String detailId, String status, String advice, String targetValue) {
        return ResultEntity.success(service.changeStatus(type, detailId, status, advice, targetValue));
    }

    /**
     * 查询batchdetail列表
     *
     * @param request
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Deprecated
    @RequestMapping("/batchDetailPage")
    public ResultEntity batchDetailPage(HttpServletRequest request, BatchCreateQuery query, @RequestParam(defaultValue = "0") int pageIndex, @RequestParam(defaultValue = Page.DEFAULT_PAGE_SIZE_STR) int pageSize, String taskId, AbstractArchiveBatch batch) {
        Assert.isTrue(!StringUtils.isEmpty(batch.getBatchType()), "业务类型不能为空！");
//        Assert.isTrue(!StringUtils.isEmpty(batch.getId()), "批次id为空！");
        Page<AppraisalBatchDetail> page = service.selectPage(batch, query, pageIndex, pageSize);
        if (!StringUtils.isEmpty(taskId)) {
            for (int i = 0; i < page.getData().size(); i++) {
                String status = archiveBatchDetailsService.getAppraisalStatusByid(page.getData().get(i).getId(), taskId);
                if (!StringUtils.isEmpty(status)) {
                    page.getData().get(i).setStatus(status);
                }
            }
        }
        return ResultEntity.success(page);
    }

    @RequestMapping("/batchDetail")
    public ResultEntity batchDetail(String id, String type) {
        return ResultEntity.success(service.detail(id, type));
    }

    @RequestMapping({"/doFinish"})
    public ResultEntity update(HttpServletRequest request, AbstractBatchDetailEntity entity, String taskId, boolean finish) {
        archiveBatchDetailsService.updateDetailsStatus(entity.getBatchId(), taskId, entity.getStatus());
        service.doFinish(entity, finish);
        return ResultEntity.success(entity);
    }

    @RequestMapping("/regect")
    public ResultEntity regect(AbstractBatchDetailEntity entity, String taskId) {
        archiveBatchDetailsService.regect(entity.getBatchId(), taskId, entity.getStatus());
        return ResultEntity.success();
    }


    @RequestMapping("/getCategory")
    public ResultEntity getCategory(String formDefinitionId) {
        return ResultEntity.success(commonMapper.selectOneByQuery(SqlQuery.from(Category.class).leftOuterJoin(CategoryInfo.ID, CategoryConfigInfo.BUSINESSID).equal(CategoryConfigInfo.ARCFORMID, formDefinitionId).or().equal(CategoryConfigInfo.FILEFORMID, formDefinitionId).and().equal(CategoryInfo.CATEGORYTYPE, "Document")));
    }

}
