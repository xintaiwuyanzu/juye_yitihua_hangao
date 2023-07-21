package com.dr.archive.kufang.archives.vo;

/**
 * 统计类1
 */
public class DataCountOne extends DataCount {

    private String info_one;
    private String info_two;
    private String info_three;

    public DataCountOne(String name, long count, String info_one, String info_two, String info_three) {
        super(name, count);
        this.info_one = info_one;
        this.info_two = info_two;
        this.info_three = info_three;
    }

    public DataCountOne(String info_one, String info_two, String info_three) {
        this.info_one = info_one;
        this.info_two = info_two;
        this.info_three = info_three;
    }

    public DataCountOne(String name, long count) {
        super(name, count);
    }

    public DataCountOne() {
    }

    public String getInfo_one() {
        return info_one;
    }

    public void setInfo_one(String info_one) {
        this.info_one = info_one;
    }

    public String getInfo_two() {
        return info_two;
    }

    public void setInfo_two(String info_two) {
        this.info_two = info_two;
    }

    public String getInfo_three() {
        return info_three;
    }

    public void setInfo_three(String info_three) {
        this.info_three = info_three;
    }


}
