package com.dr.archive.fuzhou.ofd.watermark.service;


import com.dr.archive.fuzhou.ofd.OfdConfig;
import com.dr.archive.fuzhou.ofd.bo.WaterMarkBo;
import com.dr.archive.fuzhou.ofd.watermark.entity.WaterMark;
import com.dr.archive.fuzhou.ofd.watermark.entity.WaterMarkInfo;
import com.dr.archive.fuzhou.ofd.watermark.utill.URLGeneratorForExtension;
import com.dr.framework.common.file.autoconfig.CommonFileConfig;
import com.dr.framework.common.file.model.FileInfo;
import com.dr.framework.common.file.service.CommonFileService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 水印管理
 *
 * @author cuiyj
 * @Date 2021-08-17
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WaterMarkServiceImpl extends DefaultBaseService<WaterMark> implements WaterMarkService {
    Logger logger = LoggerFactory.getLogger(WaterMarkServiceImpl.class);
    @Autowired
    CommonFileService commonFileService;
    @Autowired
    CommonFileConfig commonFileConfig;
    @Autowired
    OfdConfig ofdConfig;

    @Override
    public String showViewForMob(String mobId, String fileId, String keyWord, String tools) {
        return showViewForMob(mobId, fileId, keyWord, tools, "");
    }

    @Override
    public String showViewForMob(String mobId, String fileId, String keyWord, String tools, String fileUrl) {
        return getPathURl(mobId, fileId, keyWord, tools, fileUrl);
    }

    @Override
    public WaterMark getWaterMarkByMobId(String mobId) {
        return commonMapper.selectById(WaterMark.class, mobId);
    }

    @Override
    public List<WaterMark> getWaterMarkByOrganiseId(String organiseId, String moren) {
        if ("1".equals(moren)) {
            return commonMapper.selectByQuery(SqlQuery.from(WaterMark.class).equal(com.dr.archive.fuzhou.ofd.watermark.entity.WaterMarkInfo.ORGANISECODE, organiseId).equal(com.dr.archive.fuzhou.ofd.watermark.entity.WaterMarkInfo.STATUS, moren));
        } else {
            return commonMapper.selectByQuery(SqlQuery.from(WaterMark.class).equal(com.dr.archive.fuzhou.ofd.watermark.entity.WaterMarkInfo.ORGANISECODE, organiseId));
        }

    }

    /**
     * 多参接口   restfulmultiparam
     *
     * @param mobId
     * @param fileId
     * @param keyWord
     * @return
     */
    private String getPathURl(String mobId, String fileId, String keyWord, String tools) {
        return getPathURl(mobId, fileId, keyWord, tools, "");
    }

    /**
     * 多参接口   restfulmultiparam
     *
     * @param mobId
     * @param fileId
     * @param keyWord
     * @return
     */
    private String getPathURl(String mobId, String fileId, String keyWord, String tools, String filePath) {
        /* 云阅读根页面地址 */
        String ofdServerUrl = UriComponentsBuilder.newInstance().scheme("http").host(ofdConfig.getBaseIp()).port(ofdConfig.getViewPort()).path("/viewer/pc/index.html").toUriString();
        /* docParam : 如果是JSON多参数加密的逻辑，那么就应该先封装对象 */
        Map<String, String> docParamObj = new HashMap<>();
        docParamObj.put("fileId", fileId);
        docParamObj.put("userId", mobId);
        docParamObj.put("version", tools);
        docParamObj.put("fileUrl", filePath);
        /* docParam : 如果是单参数，那么docParam后直接跟着具体值 */
        String docParam = gson.toJson(docParamObj);
        logger.debug("请求参数：【{}】", docParam);
        String ofdUrl = URLGeneratorForExtension.generateWebOFDUrl(ofdServerUrl, docParam, URLGeneratorForExtension.WebOFDBizTypeEnum.restfulmultiparam, ofdConfig.getExtensionApiMode());
        logger.debug("云阅读路径：【{}】", ofdUrl);
        if (StringUtils.isBlank(keyWord)) {
            return ofdUrl;
        }
        return ofdUrl + "&keyword=" + keyWord;
    }

    public String showView(String fondId) {
        List<WaterMark> waterMarks = selectList(SqlQuery.from(WaterMark.class).equal(WaterMarkInfo.ORGANISECODE, fondId));
        return waterMarks.get(0).getId();
    }

    /**
     * 通过全宗id获取水印配置
     *
     * @param fondId
     * @return
     */
//    public WaterMarkBo showWatermark(String fondId) {
//        List<WaterMark> waterMarks = selectList(SqlQuery.from(WaterMark.class).equal(WaterMarkInfo.ORGANISECODE, fondId).equal(WaterMarkInfo.STATUS, "1"));
//        if (waterMarks.size() > 0) {
//            return getWaterMark(waterMarks.get(0).getId());
//        } else {
//            return null;
//        }
//    }

    private static Gson gson = new Gson();

    @Override
    public WaterMark updateStatus(WaterMark waterMark) {
        List<WaterMark> waterMarks = selectList(SqlQuery.from(WaterMark.class).equal(WaterMarkInfo.ORGANISECODE, waterMark.getOrganiseCode()).equal(WaterMarkInfo.STATUS, "1"));
        if (waterMarks.size() > 0) {
            return null;
        } else {
            updateById(waterMark);
            return waterMark;
        }

    }

    @Override
    public WaterMarkBo getWaterMark(String mobId) {
        WaterMark waterMark = checkWaterMark(mobId);
        FileInfo fileInfo = commonFileService.firstFile(waterMark.getPhotoUrl());
        InputStream in = null;
        try {
            in = commonFileService.fileStream(fileInfo.getId());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //String s = WatermarkUtil.showPreview(person, waterMark, file,in);
        WaterMarkBo waterMarkBo = new WaterMarkBo();
//         waterMarkBo.setIsExplicit("1");//是否添加显式水印 1为添加，0不添加
//         waterMarkBo.setIsShowExplicit("1");//是否显示显式水印 1为显示，0不显示
//         waterMarkBo.setIsPrintExplicit("1");//是否是文本水印 1为文本水印，0为非文本水印
//         waterMarkBo.setIsText("1");//是否是文本水印 1为文本水印，0为非文本水印
//         waterMarkBo.setContent(waterMark.getTitle());//水印内容
//         waterMarkBo.setFontStyle("宋体");//水印字体
//         waterMarkBo.setFontSize(Integer.parseInt(waterMark.getFontSize()));//水印字号
//         waterMarkBo.setFontColor(waterMark.getColor());//水印颜色
//         waterMarkBo.setIsImage("1");//是否为图片水印
//         waterMarkBo.setImageBase64(WatermarkUtil.getImageStr(in));//图片base64编码值
//         waterMarkBo.setImageIndex("1");//图片base64编码值在JSON数据体中的索引
//         waterMarkBo.setScale(waterMark.getWatermarkScale());//缩放比例,取值为0-5间的浮点型值,例如：1.5
//         waterMarkBo.setRotation(Integer.parseInt(waterMark.getTiltAngle()));//旋转角度,取值为0-360间的整数
//         waterMarkBo.setDiaphaneity(waterMark.getColorGray());//透明度,取值为0-1间的小数
//         waterMarkBo.setPosition(waterMark.getHeight0() + "," + waterMark.getWidth0());//水印位置,内容为x,y*/
        return waterMarkBo;
    }


    /**
     * 档案文件加水印
     *
     * @param refId    文件外键id
     * @param mobId    模板id
     * @param person
     * @param response
     * @throws Exception
     */
    @Override
    public void showViewForFile(String refId, String mobId, Person person, String organiseId, HttpServletResponse response) throws Exception {
        FileInfo fileInfo = commonFileService.fileInfo(refId);
        response.reset();
        response.setContentType("application/pdf");
        if (org.apache.commons.lang3.StringUtils.isBlank(mobId)) {
            //如果模板未选择,使用默认模板
            SqlQuery<WaterMark> sqlQuery = SqlQuery.from(WaterMark.class).equal(WaterMarkInfo.ORGANISECODE, organiseId).equal(WaterMarkInfo.STATUS, "1");//“1”代表默认模板的状态值
            List<WaterMark> list = selectList(sqlQuery);
            Assert.isTrue(list.size() > 0, "无默认水印模板可以使用");
            mobId = list.get(0).getId();
        }
        WaterMark waterMark = checkWaterMark(mobId);
        //        PdfHelper.waterMark(commonFileService.fileStream(fileInfo.getId()), waterMark.getTitle(), Float.parseFloat(waterMark.getColorGray()), waterMark.getColor(), Float.parseFloat(waterMark.getFontSize()), Integer.parseInt(waterMark.getHeightY()), Integer.parseInt(waterMark.getWidthX()), Integer.parseInt(waterMark.getHeight0()), Integer.parseInt(waterMark.getWidth0()), response.getOutputStream());
        //setWatermark(response.getOutputStream(), commonFileService.fileStream(fileInfo.getId()), waterMark);

    }

    /**
     * 校验模板值，设置默认值
     *
     * @param mobId
     * @return
     */
    private WaterMark checkWaterMark(String mobId) {
        WaterMark waterMark = selectById(mobId);

        //不透明度
        String colorGray = waterMark.getColorGray();
        if (org.apache.commons.lang3.StringUtils.isBlank(colorGray)) {
            waterMark.setColorGray("0.5");
        }

        //颜色
        String color = waterMark.getColor();
        if (org.apache.commons.lang3.StringUtils.isBlank(color)) {
            waterMark.setColor("0");//灰色
        }

        String size = waterMark.getFontSize();
        if (org.apache.commons.lang3.StringUtils.isBlank(size)) {
            waterMark.setFontSize("22");
        }

        String heightY = waterMark.getHeightY();
        if (org.apache.commons.lang3.StringUtils.isBlank(heightY)) {
            waterMark.setHeightY("90");
        }

        String widthX = waterMark.getWidthX();
        if (org.apache.commons.lang3.StringUtils.isBlank(widthX)) {
            waterMark.setWidthX("120");
        }

        String height0 = waterMark.getHeight0();
        if (org.apache.commons.lang3.StringUtils.isBlank(height0)) {
            waterMark.setHeight0("20");
        }

        String width0 = waterMark.getWidth0();
        if (org.apache.commons.lang3.StringUtils.isBlank(width0)) {
            waterMark.setWidth0("20");
        }

        String tiltAngle = waterMark.getTiltAngle();
        if (org.apache.commons.lang3.StringUtils.isBlank(tiltAngle)) {
            waterMark.setTiltAngle("45");
        }

        return waterMark;
    }
}
