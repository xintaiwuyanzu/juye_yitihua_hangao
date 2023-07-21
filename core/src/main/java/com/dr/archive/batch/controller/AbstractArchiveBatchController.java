package com.dr.archive.batch.controller;

import com.dr.archive.batch.entity.AbstractArchiveBatch;
import com.dr.archive.batch.query.BatchCreateQuery;
import com.dr.archive.batch.service.BaseArchiveBatchService;
import com.dr.archive.model.query.ArchiveDataQuery;
import com.dr.archive.util.UUIDUtils;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.file.FileInfoHandler;
import com.dr.framework.common.file.autoconfig.CommonFileConfig;
import com.dr.framework.common.file.resource.FileSystemFileResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

/**
 * 批次详情controller抽象父类
 *
 * @param <S> 批次service
 * @param <E> 实体类
 * @author dr
 */
public abstract class AbstractArchiveBatchController<S extends BaseArchiveBatchService<E>, E extends AbstractArchiveBatch> extends BaseServiceController<S, E> {
    @Autowired
    protected CommonFileConfig commonFileConfig;
    @Autowired
    protected FileInfoHandler fileInfoHandler;

    /**
     * 工具方法
     * 绑定前端传过来的表单查询条件
     *
     * @param request
     * @param query
     */
    protected void initQuery(HttpServletRequest request, ArchiveDataQuery query) {
        String queryContent = request.getParameter(ArchiveDataQuery.QUERY_KEY);
        if (StringUtils.hasText(queryContent)) {
            query.parseQuery(queryContent);
        }
    }

    /**
     * 工具方法，提供统一的文件上传保存处理逻辑
     *
     * @param query
     * @param file
     */
    protected void saveFile(BatchCreateQuery query, MultipartFile file) {
        if (file != null) {
            try (InputStream inputStream = file.getInputStream()) {
                //上传文件
                String filename = file.getOriginalFilename();
                //拼写文件名称
                filename = UUIDUtils.getUUID() + "-" + filename;
                //保存文件
                String filePath = commonFileConfig.uploadFile("batch", inputStream, filename, true);
                //获取文件媒体类型
                String mine = fileInfoHandler.fileMine(new FileSystemFileResource(filePath));
                //设置文件存储位置
                query.setFileLocation(filePath);
                query.setMineType(mine);
            } catch (IOException e) {
                throw new RuntimeException("保存上传文件失败！");
            }
        }
    }
}
