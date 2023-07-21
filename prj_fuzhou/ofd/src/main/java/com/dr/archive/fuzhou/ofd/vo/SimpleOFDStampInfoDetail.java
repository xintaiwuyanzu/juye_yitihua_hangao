package com.dr.archive.fuzhou.ofd.vo;

/**
 * @Title: SimpleOFDStampInfoDetail
 * @Description 印章内容协议类
 * @Author zhaoJing
 * @Date Create on 2020/11/18 20:16
 */
public class SimpleOFDStampInfoDetail {

    /* 印章所在页的页索引 */
    private Integer page;
    /* x轴坐标 */
    private Float x;
    /* y轴坐标 */
    private Float y;

    public SimpleOFDStampInfoDetail(Integer page, Float x, Float y) {
        this.page = page;
        this.x = x;
        this.y = y;
    }

    public SimpleOFDStampInfoDetail() {
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public Float getY() {
        return y;
    }

    public void setY(Float y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "SimpleOFDStampInfoDetail{" +
                "page=" + page +
                ", x=" + x +
                ", y=" + y +
                '}';
    }

}
