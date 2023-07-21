package com.dr.archive.fuzhou.ofd.watermark.entity;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * 水印管理
 */
@Table(name = Constants.TABLE_PREFIX + "WATERMARK", module = Constants.MODULE_NAME, comment = "水印管理")
public class WaterMark extends BaseStatusEntity<String> {
    @Column(comment = "机构编码", length = 50, order = 11)
    private String organiseCode;
    @Column(comment = "权限id", length = 50, order = 12)
    private String dataObjectId;
    @Column(comment = "水印名称", length = 500, order = 13)
    private String title;
    @Column(comment = "不透明度", length = 50, order = 14)
    private String colorGray;
    @Column(comment = "颜色", length = 50, order = 15)
    private String color;
    @Column(comment = "字体大小", length = 50, order = 16)
    private String fontSize;
    @Column(comment = "水印水平距离", length = 50, order = 17)
    private String widthX;
    @Column(comment = "水印垂直距离", length = 50, order = 18)
    private String heightY;
    @Column(comment = "水印水平距离初始值", length = 50, order = 19)
    private String width0;
    @Column(comment = "水印垂直距离初始值", length = 50, order = 20)
    private String height0;
    @Column(comment = "倾斜角度", length = 50, order = 21)
    private String tiltAngle;
    @Column(comment = "备注", length = 500, order = 22)
    private String remark;
    @Column(comment = "图片水印宽", length = 50, order = 23)
    private String photoWidth;
    @Column(comment = "图片水印高", length = 50, order = 24)
    private String photoHeight;
    @Column(comment = "图片水印水平距离初始值", length = 50, order = 25)
    private String width1;
    @Column(comment = "图片水印垂直距离初始值", length = 50, order = 26)
    private String height1;
    @Column(comment = "图片地址", length = 150, order = 27)
    private String photoUrl;
    @Column(comment = "地址", length = 150, order = 28)
    private String watermarkUrl;
    /*水印字体  例如：宋体*/
    @Column(comment = "水印字体", length = 200, order = 29)
    private String fontStyle;
    @Column(comment = "缩放比例", length = 200, order = 30)
    private String watermarkScale;

    public String getWatermarkScale() {
        return watermarkScale;
    }

    public void setWatermarkScale(String watermarkScale) {
        this.watermarkScale = watermarkScale;
    }

    public String getFontStyle() {
        return fontStyle;
    }

    public void setFontStyle(String fontStyle) {
        this.fontStyle = fontStyle;
    }

    public String getWatermarkUrl() {
        return watermarkUrl;
    }

    public void setWatermarkUrl(String watermarkUrl) {
        this.watermarkUrl = watermarkUrl;
    }

    public String getPhotoWidth() {
        return photoWidth;
    }

    public void setPhotoWidth(String photoWidth) {
        this.photoWidth = photoWidth;
    }

    public String getPhotoHeight() {
        return photoHeight;
    }

    public void setPhotoHeight(String photoHeight) {
        this.photoHeight = photoHeight;
    }

    public String getWidth1() {
        return width1;
    }

    public void setWidth1(String width1) {
        this.width1 = width1;
    }

    public String getHeight1() {
        return height1;
    }

    public void setHeight1(String height1) {
        this.height1 = height1;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getOrganiseCode() {
        return organiseCode;
    }

    public void setOrganiseCode(String organiseCode) {
        this.organiseCode = organiseCode;
    }

    public String getDataObjectId() {
        return dataObjectId;
    }

    public void setDataObjectId(String dataObjectId) {
        this.dataObjectId = dataObjectId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getColorGray() {
        return colorGray;
    }

    public void setColorGray(String colorGray) {
        this.colorGray = colorGray;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFontSize() {
        return fontSize;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    public String getWidthX() {
        return widthX;
    }

    public void setWidthX(String widthX) {
        this.widthX = widthX;
    }

    public String getHeightY() {
        return heightY;
    }

    public void setHeightY(String heightY) {
        this.heightY = heightY;
    }

    public String getWidth0() {
        return width0;
    }

    public void setWidth0(String width0) {
        this.width0 = width0;
    }

    public String getHeight0() {
        return height0;
    }

    public void setHeight0(String height0) {
        this.height0 = height0;
    }

    public String getTiltAngle() {
        return tiltAngle;
    }

    public void setTiltAngle(String tiltAngle) {
        this.tiltAngle = tiltAngle;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
