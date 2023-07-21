package com.dr.archive.detection.model;

import com.dr.archive.detection.enums.DetectionType;
import com.dr.archive.detection.enums.LinkType;

/**
 * 四性检测小项
 *
 * @author dr
 */
public class TestSubType {
    /**
     * 四性检测类型
     */
    private DetectionType detectionType;
    /**
     * 检测环节
     */
    private LinkType linkType;

    /**
     * 小项检测名称
     */
    private String title;
    /**
     * 标准要求描述
     */
    private String stdDescription;
    /**
     * 标准依据名称
     */
    private String stdName;

    /*
     * 实现方式
     * */
    private String testRecordMethod;

    /**
     * 自定义类型编码，用来关联查询使用
     */
    private String code;

    /**
     * 自定义备注信息
     */
    private String comment;


    public DetectionType getDetectionType() {
        return detectionType;
    }

    public void setDetectionType(DetectionType detectionType) {
        this.detectionType = detectionType;
    }

    public LinkType getLinkType() {
        return linkType;
    }

    public void setLinkType(LinkType linkType) {
        this.linkType = linkType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStdDescription() {
        return stdDescription;
    }

    public void setStdDescription(String stdDescription) {
        this.stdDescription = stdDescription;
    }

    public String getStdName() {
        return stdName;
    }

    public void setStdName(String stdName) {
        this.stdName = stdName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTestRecordMethod() {
        return testRecordMethod;
    }

    public void setTestRecordMethod(String testRecordMethod) {
        this.testRecordMethod = testRecordMethod;
    }
}
