package com.dr.archive.fuzhou.ofd.controller;

import com.dr.archive.fuzhou.ofd.bo.CommonExtensionUtils;
import com.dr.archive.fuzhou.ofd.enums.WebExtensionResponseDefinitionEnum;
import com.dr.archive.fuzhou.ofd.service.OfdService;
import com.dr.archive.fuzhou.ofd.vo.WaterMarkObjectInfo;
import com.dr.archive.fuzhou.ofd.vo.WebOFDExplicitWaterMarkInfo;
import com.dr.archive.fuzhou.ofd.watermark.entity.WaterMark;
import com.dr.archive.fuzhou.ofd.watermark.service.WaterMarkService;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.file.model.FileInfo;
import com.dr.framework.common.file.service.CommonFileService;
import com.dr.framework.core.organise.service.OrganisePersonService;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 云阅读水印添加
 *
 * @author cuiyj
 * @date 2021-12-26
 */
@RestController
@RequestMapping("/api/ofd")
public class OFDWaterMarkController {

    @Autowired
    OfdService ofdService;
    @Autowired
    OrganisePersonService organisePersonService;
    @Autowired
    CommonFileService commonFileService;
    @Autowired
    WaterMarkService waterMarkService;

    /**
     * 原来的代码  没有调用方
     * * @param fileId
     *
     * @return
     */
    @RequestMapping("/fileToOfd")
    public ResultEntity fileToOfd(String fileId) {
        ofdService.fileToOfd(fileId);
        return ResultEntity.success("正在执行转换操作，请稍后刷新原文列表查看转换结果！");
    }


    /**
     * 本接口提供显式水印信息
     *
     * @param userId todo 这里目前存放 水印id  后面可能会调整（如果调整的话需要 1.请求的地方  2.Linux系统配置文件 3.这里的接收参数  都得加）
     * @param fileId 文件网络地址
     */
    @ResponseBody
    @RequestMapping(value = "/getExplicitWatermarkInfo", method = RequestMethod.GET)
    public Map getExplicitWatermarkInfo(@RequestParam("userId") String userId, @RequestParam("fileId") String fileId) {

        //这里的userId 作为水印对象的 id ，fileId 是文件地址
        System.out.println(userId);
        System.out.println(fileId);
        /* 组织接口返回信息 */
        Map<String, Object> resultMap = new ConcurrentHashMap<>();

        //todo 这里是想获取ofd的页数
//        File file = new File(fileId);
//        PdfReader pdfReader = new PdfReader(new FileInputStream(file));
//        int number = pdfReader.getNumberOfPages();

        //如果没有水印模板信息 就不加水印了
        if ("".equals(userId)) {
            resultMap.put("ret", WebExtensionResponseDefinitionEnum.SUCCESS.getRet());
            resultMap.put("data", "");
            resultMap.put("message", "success");
            return resultMap;
        }
        WaterMark waterMark = waterMarkService.getWaterMarkByMobId(userId);
        resultMap.put("ret", WebExtensionResponseDefinitionEnum.SUCCESS.getRet());
        //这里的页码上限是 1000 写死的 假如ofd页码大于了1000 那么后面的页码将没有 水印
        resultMap.put("data", getWaterMarkIfno(waterMark, 1000));
        resultMap.put("message", "success");
        return resultMap;
    }

    private WebOFDExplicitWaterMarkInfo getWaterMarkIfno(WaterMark waterMark, int number) {
        WebOFDExplicitWaterMarkInfo explicitWaterMarkInfo = new WebOFDExplicitWaterMarkInfo();
//        explicitWaterMarkInfo.setPageIndex("1-" + number);
//        explicitWaterMarkInfo.setPageIndex("all");
        ArrayList<WebOFDExplicitWaterMarkInfo.Detail> details = new ArrayList<>();
        WebOFDExplicitWaterMarkInfo.Detail detail = new WebOFDExplicitWaterMarkInfo.Detail();

        /* 水印对象,实际渲染效果以‘页’为单位 FIXME 水印对象1 */
        detail.setPageIndex("all");
        detail.setIsText("1");
        detail.setIsShowExplicit("1");
        detail.setIsPrintExplicit("1");

        /* CONTENT DATA */
        detail.setContent(waterMark.getTitle());
        detail.setFontColor(waterMark.getColor());
//        detail.setFontStyle(waterMark.getFontStyle());
        detail.setFontStyle("宋体");
        detail.setFontSize(Integer.parseInt(waterMark.getFontSize()));
        detail.setDiaphaneity(waterMark.getColorGray());
        detail.setPosition(waterMark.getWidth0() + "," + waterMark.getHeight0());
        //位置信息 文字与图片共用
        detail.setRotation(0);
        if (org.springframework.util.StringUtils.hasText(waterMark.getTiltAngle())) {
            detail.setRotation(Integer.parseInt(waterMark.getTiltAngle()));// TODO 有问题
        }
        //下方是图片相关的配置
        detail.setIsImage("0");
        if (StringUtils.isNotBlank(waterMark.getPhotoUrl())) {

            Map<String, String> imageContainer = new HashMap<>();
            //当图片路径在  图片实际没有的时候  就不加图片了
            String base64 = getWatermarkImageBase64(waterMark.getPhotoUrl());
            if (!"".equals(base64)) {
                detail.setIsImage("1");
                /*图片base64编码值在JSON数据体中的索引*/
                detail.setImageIndex("1");
                imageContainer.put("1", base64);
                explicitWaterMarkInfo.setImageContainer(imageContainer);
            }
        }

        //目前实现的是单个水印    如果是需要加多个水印  new detail 并且调整位置信息(x,y）即可
        details.add(detail);
        explicitWaterMarkInfo.setDetails(details);
        return explicitWaterMarkInfo;
    }

    /* 从默认根路径获取指定的测试图片base64 */
    private String getWatermarkImageBase64(String pngName) {

        String fileStr = "";
        InputStream fileStream = null;
        try {
            FileInfo fileInfo = commonFileService.lastFile(pngName);
            if (fileInfo == null) {
                return "";
            }
            fileStream = commonFileService.fileStream(fileInfo.getId());
        } catch (IOException e) {
            return "";
        }
        fileStr = CommonExtensionUtils.getFileStr(fileStream);
        return fileStr;
    }


    /* 模拟三方接口组织水印数据.... */
//    private WebOFDExplicitWaterMarkInfo getWatermarkData() {
//        /* INFO ROOT */
//        WebOFDExplicitWaterMarkInfo explicitWaterMarkInfo = new WebOFDExplicitWaterMarkInfo();
//        /* IMAGE BASE64 - 图片的base64值 - 考虑到图片base64数据量大,需要作复用处理,故在此声明为Map,key值作为水印对象中引用索引 */
////		Map<String,String> imageContainer = new HashMap<>();
////		imageContainer.put("1", getWatermarkImageBase64("testWatermark.png"));
////		imageContainer.put("2", getWatermarkImageBase64("child.png"));
////		explicitWaterMarkInfo.setImageContainer(imageContainer);
//
//        /* PAGE INDEX - 设置水印对象要显示在哪些页 - 页坐标示例如下 */
//        explicitWaterMarkInfo.setPageIndex("1-8");
//
//        /* 水印对象,实际渲染效果以‘页’为单位 FIXME 水印对象1 */
//        WebOFDExplicitWaterMarkInfo.Detail detail = new WebOFDExplicitWaterMarkInfo.Detail();
//        /* BASIC DATA */
//        detail.setIsExplicit("1");
//        detail.setIsText("1");
//        detail.setIsShowExplicit("1");
//        detail.setIsPrintExplicit("0");
//        /* CONTENT DATA */
//        detail.setContent("广州市中级人民法院1");
//        detail.setFontStyle("宋体");
//        detail.setFontSize(60);
//        detail.setFontColor("#808080");
//        /* IMAGE DATA */
//        detail.setIsImage("0");
//        if ("1".equals(detail.getIsImage())) {
//            detail.setImageIndex("1");
//        }
////		detail.setScale("2");
//        detail.setRotation(335);
//        detail.setDiaphaneity("0.1");
//        /* POSITION */
//        detail.setPosition("105,88");
//
//        WebOFDExplicitWaterMarkInfo.Detail detail2 = new WebOFDExplicitWaterMarkInfo.Detail();
//        /* BASIC DATA */
//        detail2.setIsExplicit("1");
//        detail2.setIsText("1");
//        detail2.setIsShowExplicit("1");
//        detail2.setIsPrintExplicit("1");
//        /* CONTENT DATA */
//        detail2.setContent("广州市中级人民法院2");
//        detail2.setFontStyle("宋体");
//        detail2.setFontSize(60);
//        detail2.setFontColor("#808080");
//        /* IMAGE DATA */
//        detail2.setIsImage("0");
//        if ("1".equals(detail2.getIsImage())) {
//            detail2.setImageIndex("1");
//        }
////		detail2.setScale("2");
//        detail2.setRotation(335);
//        detail2.setDiaphaneity("0.3");
//        detail2.setPosition("105,148");
//
//        WebOFDExplicitWaterMarkInfo.Detail detail3 = new WebOFDExplicitWaterMarkInfo.Detail();
//        /* BASIC DATA */
//        detail3.setIsExplicit("1");
//        detail3.setIsText("0");
//        detail3.setIsShowExplicit("1");
//        detail3.setIsPrintExplicit("1");
//        /* CONTENT DATA */
//        detail3.setContent("广州市中级人民法院3");
//        detail3.setFontStyle("宋体");
//        detail3.setFontSize(60);
//        detail3.setFontColor("#000000");
//        /* IMAGE DATA */
//        detail3.setIsImage("1");
//        if ("1".equals(detail3.getIsImage())) {
//            detail3.setImageIndex("1");
//        }
//        detail3.setScale("2");
//        detail3.setRotation(335);
//        detail3.setDiaphaneity("0.7");
//        detail3.setPosition("105,188");
//
//
//        /* 多个水印对象 */
//        explicitWaterMarkInfo.setDetails(Arrays.asList(detail, detail2, detail3));
//        return explicitWaterMarkInfo;
//    }


    /* 直接读取准备好的数据体JSON文件 */
    private WebOFDExplicitWaterMarkInfo getWatermarkDataMk2() {
        String targetDir = System.getProperty("os.name").toLowerCase().contains("win") ? BaseController.DEFAULT_TEMP_DIR_WIN : BaseController.DEFAULT_TEMP_DIR_LINUX;
        String json = CommonExtensionUtils.readJsonFile(targetDir + "testWatermarkData.json");
        return new Gson().fromJson(json, WebOFDExplicitWaterMarkInfo.class);
    }


    /**
     * 本接口提供隐式水印信息   暂时没用到这里  隐式水印是什么鬼
     */
    @ResponseBody
    @RequestMapping(value = "/getImplicitWatermarkInfo", method = RequestMethod.GET)
    public Map getImplicitWatermarkInfo(@RequestParam String userId, @RequestParam String fileId) {
        /* WaterMarkController.class.getClassLoader().getResource("/extensionApi.conf").getPath() */
        WaterMarkObjectInfo watermark = new WaterMarkObjectInfo();
        watermark.setObjectId("00001");
        watermark.setUsername("张三");
        watermark.setGender(1);
        watermark.setEmail("test@foxitjj.com");
        watermark.setPhone("12345678900");
        watermark.setDeviceId("pc001");
        watermark.setImei("863329031789562");
        watermark.setCpuId("E1DC1202C893F15C967B19BA172BBEA5");
        watermark.setDeviceSysVersion("Windows10");

        /* 组织接口返回信息 */
        Map<String, Object> resultMap = new ConcurrentHashMap<>();
        resultMap.put("ret", WebExtensionResponseDefinitionEnum.SUCCESS.getRet());
        resultMap.put("data", watermark);
        resultMap.put("message", "success");
        return resultMap;
    }

    private WaterMarkObjectInfo getImplicitWatermarkData() {
        String targetDir = System.getProperty("os.name").toLowerCase().contains("win") ? BaseController.DEFAULT_TEMP_DIR_WIN : BaseController.DEFAULT_TEMP_DIR_LINUX;
        String json = CommonExtensionUtils.readJsonFile(targetDir + "testImplicitWatermarkData.json");
        return new Gson().fromJson(json, WaterMarkObjectInfo.class);
    }


}
