package com.dr.archive.manage.task.controller;

import com.dr.archive.batch.service.ArchiveBatchService;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.file.autoconfig.CommonFileConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author caor
 * @date 2020/12/7 23:41
 */
@RestController
@RequestMapping({"${common.api-path:/api}/download"})
public class DownloadController {
    @Autowired
    CommonFileConfig fileConfig;
    @Autowired
    ArchiveBatchService archiveBatchService;

    /**
     * 导出导出下载
     *
     * @param id
     * @param type
     * @param fileFullPath
     * @return
     */
    @RequestMapping("/getUploadDownLoadPath")
    public ResultEntity getUploadDownLoadPath(String id, String type, String fileFullPath) {
//        ArchiveBatch archiveBatch = archiveBatchService.selectById(id);
        return ResultEntity.success(fileConfig.getUploadDownLoadPath(fileFullPath));
    }
}
