package com.dr.archive.fuzhou.ofd.watermark.controller;


import com.dr.archive.fuzhou.ofd.bo.WaterMarkBo;
import com.dr.archive.fuzhou.ofd.watermark.entity.WaterMarkInfo;
import com.dr.archive.fuzhou.ofd.watermark.service.WaterMarkService;
import com.dr.archive.fuzhou.ofd.watermark.entity.WaterMark;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import com.dr.framework.core.web.annotations.Current;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * 水印管理
 *
 * @author cuiyj
 * @Date 2021-08-17
 */
@RestController
@RequestMapping({"${common.api-path:/api}/watermark"})
public class WaterMarkController extends BaseServiceController<WaterMarkService, WaterMark> {

    @Override
    public ResultEntity<WaterMark> insert(HttpServletRequest request, WaterMark entity) {
        List<WaterMark> list = service.getWaterMarkByOrganiseId(entity.getOrganiseCode());
        if (list.size() > 0) {
            entity.setStatus("0");
        } else {
            entity.setStatus("1");//添加如果没有默认的 就默认
        }
        return super.insert(request, entity);
    }

    @Override
    protected SqlQuery<WaterMark> buildPageQuery(HttpServletRequest httpServletRequest, WaterMark waterMark) {
        SqlQuery<WaterMark> sqlQuery = SqlQuery.from(WaterMark.class);
        if (!StringUtils.isEmpty(waterMark.getTitle())) {
            sqlQuery.like(WaterMarkInfo.TITLE, waterMark.getTitle());
        }
        String organiseId = waterMark.getOrganiseCode();
        if (StringUtils.isEmpty(waterMark.getOrganiseCode())) {
            organiseId = SecurityHolder.get().currentOrganise().getId();
        }
        sqlQuery.like(WaterMarkInfo.ORGANISECODE, organiseId);
        sqlQuery.orderByDesc(WaterMarkInfo.CREATEDATE);
        return sqlQuery;
    }

    /**
     * 预览水印
     *
     * @throws Exception
     */
    @RequestMapping(value = "/showViewForMob")
    public ResultEntity showViewForMob(String mobId) {
        Assert.isTrue(StringUtils.hasText(mobId), "id不能为空");
        String ofdViewUrl = service.showViewForMob(mobId);
        return ResultEntity.success(ofdViewUrl);
    }

    @RequestMapping(value = "/updateStatus")
    public ResultEntity updateStatus(HttpServletRequest request, WaterMark waterMark) {
        return ResultEntity.success(service.updateStatus(waterMark));
    }
    /*    *//**
     * 预览水印
     *
     * @param request
     * @param person
     * @param response
     * @throws Exception
     *//*
    @RequestMapping(value = "/showPreview")
    public void showPreview(HttpServletRequest request,
                               @Current Person person, HttpServletResponse response) throws Exception {
        Assert.isTrue(!StringUtils.isEmpty(request.getParameter("mobId")), "id不能为空");
        String mobId = request.getParameter("mobId");
        service.showViewForMob(mobId, person, response);
    }*/

    /**
     * 添加水印为档案文件  模板可选  不选时采用默认模板
     *
     * @param request
     * @param person
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/showViewForFile")
    public void showViewForFile(HttpServletRequest request,
                                @Current Person person, @Current Organise organise, HttpServletResponse response) throws Exception {
        Assert.isTrue(!StringUtils.isEmpty(request.getParameter("refId")), "id不能为空");
        Assert.isTrue(!StringUtils.isEmpty(request.getParameter("mobId")), "模板id不能为空");

        String refId = request.getParameter("refId");
        String mobId = request.getParameter("mobId");
        service.showViewForFile(refId, mobId, person, organise.getId(), response);
    }

    /**
     * 福昕阅读获取水印配置接口
     *
     * @param request
     * @param waterMarkId
     * @return
     */
    @RequestMapping(value = "/getWaterMark")
    public WaterMarkBo getWaterMark(HttpServletRequest request, @RequestParam String waterMarkId) {
        //String[] split = waterMarkId.split(":");
        //WaterMarkBo waterMark = service.getWaterMark(split[1]);
        WaterMarkBo waterMark = service.getWaterMark(waterMarkId);
        return waterMark;
    }

    //默认预览模板
    @RequestMapping(value = "/getTemplate")
    public void getTemplate1(HttpServletRequest request, HttpServletResponse response) {
        String path = this.getClass().getResource("/word").getPath();
        File file = new File(path + "/mb.ofd");
        //InputStream resourceAsStream = this.getClass().getResourceAsStream("/word/ofdmob.ofd");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        response.setHeader("Content-Length", String.valueOf(file.length()));
        MediaType mediaType = MediaType.parseMediaType("application/zip");
        response.setContentType(mediaType.toString());
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            StreamUtils.copy(fileInputStream, response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //return this.getClass().getResourceAsStream("/word/ofdmob.ofd");
    }
}
