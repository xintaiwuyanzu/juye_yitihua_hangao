package com.dr.archive.kufang.archives.vo;

/**
 * 统计类
 */
public class DataCount {

    private String name;
    private long count;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public DataCount(String name, long count) {
        this.name = name;
        this.count = count;
    }

    public DataCount() {
    }

    @Override
    public String toString() {
        return "DataCount{" +
                "name='" + name + '\'' +
                ", count=" + count +
                '}';
    }
}
