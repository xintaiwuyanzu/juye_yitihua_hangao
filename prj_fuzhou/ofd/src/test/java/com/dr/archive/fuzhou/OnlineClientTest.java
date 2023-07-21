package com.dr.archive.fuzhou;

import com.dr.archive.fuzhou.ofd.service.OfdOnlineService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public class OnlineClientTest {
    Logger logger = LoggerFactory.getLogger(OfdTest.class);
    @Autowired
    OfdOnlineService ofdOnlineService;

    @Test
    public void convertTest() {

        /*WaterMarkBo waterMarkBo = new WaterMarkBo();

        waterMarkBo.setFileUrl("http://localhost:8080/api/files/downLoad/9182c6c6-db9c-42c2-a48e-2df5cc8392bd?download=false");
        waterMarkBo.setIsExplicit("1");//是否添加显式水印 1为添加，0不添加
        waterMarkBo.setIsShowExplicit("1");//是否显示显式水印 1为显示，0不显示
        waterMarkBo.setIsPrintExplicit("1");//是否是文本水印 1为文本水印，0为非文本水印
        waterMarkBo.setIsText("1");//是否是文本水印 1为文本水印，0为非文本水印
        waterMarkBo.setContent("测试14点32分");//水印内容
        waterMarkBo.setFontStyle("宋体");//水印字体
        waterMarkBo.setFontSize(48);//水印字号
        waterMarkBo.setFontColor("#3300FF");//水印颜色
        waterMarkBo.setIsImage("1");//是否为图片水印
        //waterMarkBo.put("imageBase64",getImageStr(in));//图片base64编码值在JSON数据体中的索引
        waterMarkBo.setImageIndex("1");//图片base64编码值在JSON数据体中的索引
        waterMarkBo.setScale("2");//缩放比例,取值为0-5间的浮点型值,例如：1.5
        waterMarkBo.setRotation(360);//旋转角度,取值为0-360间的整数
        waterMarkBo.setDiaphaneity("1");//透明度,取值为0-1间的小数
        waterMarkBo.setPosition("222,333,333,4444");//水印位置,内容为x,y
        ofdOnlineClient.convertStream(waterMarkBo);*/
        Map<String, String> docParamObj = new HashMap<>();
        docParamObj.put("isExplicit", "1");//是否添加显式水印 1为添加，0不添加
        docParamObj.put("isShowExplicit", "1");//是否显示显式水印 1为显示，0不显示
        docParamObj.put("isPrintExplicit", "1");//是否是文本水印 1为文本水印，0为非文本水印
        docParamObj.put("isText", "1");//是否是文本水印 1为文本水印，0为非文本水印
        docParamObj.put("content", "1241124124");//水印内容
        docParamObj.put("fontStyle", "宋体");//水印字体
        docParamObj.put("fontSize", "48");//水印字号
        docParamObj.put("fontColor", "#3300FF");//水印颜色
        //docParamObj.put("isImage","");//是否为图片水印
        //docParamObj.put("imageIndex","");//片base64编码值在JSON数据体中的索引
        docParamObj.put("scale", "1");//缩放比例,取值为0-5间的浮点型值,例如：1.5
        docParamObj.put("rotation", "50");//旋转角度,取值为0-360间的整数
        docParamObj.put("diaphaneity", "1");//透明度,取值为0-1间的小数*//*
        //docParamObj.put("position","");//水印位置,内容为x,y
        //* docParam : 如果是单参数，那么docParam后直接跟着具体值 *//*
        //FileStreamResult fileStreamResult = ofdOnlineClient.convertStream(waterMarkBo);
/*        long start = System.currentTimeMillis();
        FileStreamResult result = ofdOnlineClient.convertStream(FileByteInfo.fromInputStream(resource.getInputStream(), "doc"));
        byte[] fileBytes = Base64Utils.decodeFromString(result.getBytes());
        long end = System.currentTimeMillis();

        File ofdFile = new File(new File(home.getDir(), "target"), UUID.randomUUID() + ".ofd");
        FileCopyUtils.copy(fileBytes, ofdFile);
        logger.info("转换成功，耗时{}毫秒", end - start);
        logger.info("原始文件大小：{}kb，生成ofd文件大小:{}kb", resource.contentLength() / 1000, ofdFile.length() / 1000);
        logger.info("生成ofd文件位置为：{}", ofdFile.getPath());*/
    }
}
