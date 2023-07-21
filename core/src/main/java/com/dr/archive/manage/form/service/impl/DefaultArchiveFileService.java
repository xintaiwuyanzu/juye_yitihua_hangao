package com.dr.archive.manage.form.service.impl;

import com.dr.archive.manage.form.service.ArchiveDataContext;
import com.dr.archive.manage.form.service.ArchiveDataPlugin;
import com.dr.archive.manage.form.service.ArchiveFileService;
import com.dr.framework.common.file.service.CommonFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 档案原文相关service
 *
 * @author dr
 */
@Service
public class DefaultArchiveFileService implements ArchiveFileService, ArchiveDataPlugin {
    @Autowired
    CommonFileService fileService;

    /**
     * 档案删除同步删除附件信息
     *
     * @param archiveIds
     * @param context
     * @return
     */
    @Override
    public Long afterDelete(String archiveIds, ArchiveDataContext context) {
        String[] ids = archiveIds.split(",");
        long result = 0;
        for (String id : ids) {
            //TODO 删除跟档案相关的附件archive为档案原文类型
            result += fileService.removeFileByRef(id, "archive");
        }
        return result;
    }
}