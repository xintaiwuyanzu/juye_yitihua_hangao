package com.dr.archive.fuzhou.ofd.vo;

import java.util.List;
import java.util.Map;

/**
 * @Title: ExplicitWaterMarkInfo
 * @Description 水印数据传输类根
 * @Author zhaoJing
 * @Date Create on 2021/8/9 9:58
 */
public class WebOFDExplicitWaterMarkInfo {
    /* 图片数据体, index --> image_base64_str */
    Map<String, String> imageContainer;

    /* pageIndex, 数据示例: 1-3,5,9 */
    String pageIndex;

    /* 水印详细信息,对象数组 */
    List<Detail> details;

    public WebOFDExplicitWaterMarkInfo() {
    }

    public WebOFDExplicitWaterMarkInfo(Map<String, String> imageContainer, String pageIndex, List<Detail> details) {
        this.imageContainer = imageContainer;
        this.pageIndex = pageIndex;
        this.details = details;
    }

    public Map<String, String> getImageContainer() {
        return imageContainer;
    }

    public void setImageContainer(Map<String, String> imageContainer) {
        this.imageContainer = imageContainer;
    }

    public String getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(String pageIndex) {
        this.pageIndex = pageIndex;
    }

    public List<Detail> getDetails() {
        return details;
    }

    public void setDetails(List<Detail> details) {
        this.details = details;
    }

    public static class Detail {

        /*是否显示水印 1为显示，0不显示*/
        private String isShowExplicit;
        /*是否打印水印 1为打印，0不打印*/
        private String isPrintExplicit;

        /*是否是文本水印 1为文本水印，0为非文本水印*/
        private String isText;
        /*水印内容 例如：福昕鲲鹏*/
        private String content;
        /*水印字体  例如：宋体*/
        private String fontStyle;
        /*水印字号  例如：48  单位：pt*/
        private Integer fontSize;
        /*水印颜色，取值为颜色16进制码*/
        private String fontColor;
        /* 是否为图片水印标记 */
        private String isImage;
        /*图片base64编码值在JSON数据体中的索引*/
        private String imageIndex;
        /*旋转角度,取值为0-360间的整数*/
        private Integer rotation;
        /*透明度,取值为0-1间的小数*/
        private String diaphaneity;
        /*水印位置,内容为x,y*/
        private String position;
        /* pageIndex, 数据示例: 1-3,5,9 */
        private String pageIndex;


        public Detail() {
        }


        public Detail(String isShowExplicit, String isPrintExplicit, String isText, String content, String fontStyle, Integer fontSize, String fontColor, String isImage, String imageIndex, Integer rotation, String diaphaneity, String position, String pageIndex) {
            this.isShowExplicit = isShowExplicit;
            this.isPrintExplicit = isPrintExplicit;
            this.isText = isText;
            this.content = content;
            this.fontStyle = fontStyle;
            this.fontSize = fontSize;
            this.fontColor = fontColor;
            this.isImage = isImage;
            this.imageIndex = imageIndex;
            this.rotation = rotation;
            this.diaphaneity = diaphaneity;
            this.position = position;
            this.pageIndex = pageIndex;
        }

        public String getPageIndex() {
            return pageIndex;
        }

        public void setPageIndex(String pageIndex) {
            this.pageIndex = pageIndex;
        }

        public String getIsShowExplicit() {
            return isShowExplicit;
        }

        public void setIsShowExplicit(String isShowExplicit) {
            this.isShowExplicit = isShowExplicit;
        }

        public String getIsPrintExplicit() {
            return isPrintExplicit;
        }

        public void setIsPrintExplicit(String isPrintExplicit) {
            this.isPrintExplicit = isPrintExplicit;
        }

        public String getIsText() {
            return isText;
        }

        public void setIsText(String isText) {
            this.isText = isText;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getFontStyle() {
            return fontStyle;
        }

        public void setFontStyle(String fontStyle) {
            this.fontStyle = fontStyle;
        }

        public Integer getFontSize() {
            return fontSize;
        }

        public void setFontSize(Integer fontSize) {
            this.fontSize = fontSize;
        }

        public String getFontColor() {
            return fontColor;
        }

        public void setFontColor(String fontColor) {
            this.fontColor = fontColor;
        }

        public String getIsImage() {
            return isImage;
        }

        public void setIsImage(String isImage) {
            this.isImage = isImage;
        }

        public String getImageIndex() {
            return imageIndex;
        }

        public void setImageIndex(String imageIndex) {
            this.imageIndex = imageIndex;
        }

        public Integer getRotation() {
            return rotation;
        }

        public void setRotation(Integer rotation) {
            this.rotation = rotation;
        }

        public String getDiaphaneity() {
            return diaphaneity;
        }

        public void setDiaphaneity(String diaphaneity) {
            this.diaphaneity = diaphaneity;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }
    }
}

