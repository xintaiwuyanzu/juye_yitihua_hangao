package com.dr.archive.controller;

import com.dr.archive.entity.StructuredText;
import com.dr.archive.entity.StructuredTextInfo;
import com.dr.archive.service.StructuredTextService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * @author: yang
 * @create: 2022-06-03 15:25
 **/
@RestController
@RequestMapping("/api/structuredText")
public class StructuredTextController extends BaseServiceController<StructuredTextService, StructuredText> {

    @Value("${struct.url}")
    String excelUrl;

    @Override
    protected SqlQuery<StructuredText> buildPageQuery(HttpServletRequest httpServletRequest, StructuredText structuredText) {
        return SqlQuery.from(StructuredText.class)
                .like(StructuredTextInfo.TITLE, structuredText.getTitle())
                .equal(StructuredTextInfo.STATUS, structuredText.getStatus());
    }

    /**
     * 上传excel文件
     */
    @RequestMapping("/upload")
    public ResultEntity upload(@RequestParam MultipartFile file) {
        return ResultEntity.success(service.upload(file));
    }

    /**
     * 获得excel文件数据
     */
    @RequestMapping("/excelTitle")
    public ResultEntity excelTitle(String id, int type) {
        return ResultEntity.success(service.excelTitle(id, type));
    }

    @RequestMapping("/findExcelText")
    public ResultEntity findExcelText(String result, String id, int type) {
        return ResultEntity.success(service.findExcelText(result, id, type));
    }

    @RequestMapping("/loadData")
    public ResultEntity loadData() {
        try {
            service.loadData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResultEntity.success();
    }

    /**
     * 保存关系结果
     */
    @RequestMapping("/saveRelationResult")
    public ResultEntity<Object> saveRelationResult(String result, int type) {
        return ResultEntity.success(service.saveRelationResult(result, type));
    }

    /**
     * 下载/预览 excel文件
     */
    @RequestMapping("/download")
    public ResultEntity download(String id, HttpServletResponse response) {
        service.download(id, response);
        return ResultEntity.success();
    }


    @RequestMapping("/getExcelFrom140")
    public ResultEntity getExcelFrom140() {
        File file = new File(excelUrl);
        File[] files = file.listFiles();
        int i = 0;
        for (File f : files) {
            StructuredText text = new StructuredText();
            text.setTitle("测试文件");
            text.setFilePath(f.getPath());
            text.setStatus("0");
            service.insert(text);
        }
        return ResultEntity.success();
    }

}
