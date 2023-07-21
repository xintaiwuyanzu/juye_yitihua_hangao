package com.dr.archive.managefile.controller;

import com.dr.archive.managefile.entity.ManageFile;
import com.dr.archive.managefile.entity.ManageFileInfo;
import com.dr.archive.managefile.service.ManageFileService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: caor
 * @Date: 2022-03-31 17:08
 * @Description:
 */
@RestController
@RequestMapping("api/manageFile")
public class ManageFileController extends BaseServiceController<ManageFileService, ManageFile> {
    @Override
    protected SqlQuery<ManageFile> buildPageQuery(HttpServletRequest httpServletRequest, ManageFile manageFile) {
        return SqlQuery.from(ManageFile.class)
                .equal(ManageFileInfo.BUSINESSID, manageFile.getBusinessId())
                .equal(ManageFileInfo.FILEINFOID, manageFile.getFileInfoId())
                .like(ManageFileInfo.FILENAME, manageFile.getFileName())
                .orderByDesc(ManageFileInfo.SAVEDATE);
    }

    @RequestMapping({"/fileTree"})
    public ResultEntity fileTree(HttpServletRequest request) {
        return ResultEntity.success(service.getFileTree(request.getParameter("refId")));
    }
}
