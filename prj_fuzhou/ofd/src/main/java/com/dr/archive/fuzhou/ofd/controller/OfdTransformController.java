package com.dr.archive.fuzhou.ofd.controller;

import com.dr.archive.fuzhou.ofd.bo.OfdTransformRecord;
import com.dr.archive.fuzhou.ofd.bo.OfdTransformRecordInfo;
import com.dr.archive.fuzhou.ofd.service.OfdTransformService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: yang
 * @create: 2022-05-16 14:53
 **/

@RestController
@RequestMapping("/api/ofd_transform")
public class OfdTransformController extends BaseServiceController<OfdTransformService, OfdTransformRecord> {

    @Override
    protected SqlQuery<OfdTransformRecord> buildPageQuery(HttpServletRequest httpServletRequest, OfdTransformRecord ofdTransformRecord) {
        return SqlQuery.from(OfdTransformRecord.class)
                .like(OfdTransformRecordInfo.FILENAME, ofdTransformRecord.getFileName())
                .orderByDesc(OfdTransformRecordInfo.CREATEDATE);
    }

    /**
     * 保存文件
     */
    @RequestMapping("/upload")
    public ResultEntity upload(@RequestParam MultipartFile file) {
        service.upload(file);
        return ResultEntity.success();
    }

    /**
     * 根据id和类型（根据类型判断是源文件还是ofd文件）下载文件
     *
     * @param id   记录id
     * @param type 文件类型
     */
    @RequestMapping("/download")
    public void download(String id, String type, HttpServletResponse response) {
        service.download(id, type, response);
    }

    /**
     * 开始转换/重新转换
     */
    @RequestMapping("/transform")
    public ResultEntity transform(OfdTransformRecord record) {
        service.transform(record);
        return ResultEntity.success();
    }

}
