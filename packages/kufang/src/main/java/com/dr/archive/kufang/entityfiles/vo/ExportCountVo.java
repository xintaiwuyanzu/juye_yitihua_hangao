package com.dr.archive.kufang.entityfiles.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * 二维码导出Vo类
 *
 * @author cuiyj
 * @Date 2021-09-02
 */
public class ExportCountVo {
    private String code1;
    private String name1;
    private Object image1;
    private String code2;
    private String name2;
    private Object image2;
    private String code3;
    private String name3;
    private Object image3;
    private String code4;
    private String name4;
    private Object image4;
    private List<ExportCountVo> children = new ArrayList<>();

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getName3() {
        return name3;
    }

    public void setName3(String name3) {
        this.name3 = name3;
    }

    public String getName4() {
        return name4;
    }

    public void setName4(String name4) {
        this.name4 = name4;
    }

    public String getCode1() {
        return code1;
    }

    public void setCode1(String code1) {
        this.code1 = code1;
    }

    public Object getImage1() {
        return image1;
    }

    public void setImage1(Object image1) {
        this.image1 = image1;
    }

    public String getCode2() {
        return code2;
    }

    public void setCode2(String code2) {
        this.code2 = code2;
    }

    public Object getImage2() {
        return image2;
    }

    public void setImage2(Object image2) {
        this.image2 = image2;
    }

    public String getCode3() {
        return code3;
    }

    public void setCode3(String code3) {
        this.code3 = code3;
    }

    public Object getImage3() {
        return image3;
    }

    public void setImage3(Object image3) {
        this.image3 = image3;
    }

    public String getCode4() {
        return code4;
    }

    public void setCode4(String code4) {
        this.code4 = code4;
    }

    public Object getImage4() {
        return image4;
    }

    public void setImage4(Object image4) {
        this.image4 = image4;
    }

    public List<ExportCountVo> getChildren() {
        return children;
    }

    public void setChildren(List<ExportCountVo> children) {
        this.children = children;
    }
}
