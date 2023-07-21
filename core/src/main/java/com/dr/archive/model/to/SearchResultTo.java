package com.dr.archive.model.to;

import java.util.List;
import java.util.Map;

/**
 * 描述：
 *
 * @author tuzl
 * @date 2020/7/14 9:09
 */
public class SearchResultTo {
    private long start;
    private Integer size;
    private long total;
    private List<Map<String, Object>> data;
    private String indexQuery;
    private List<String> keyWord;

    public String getIndexQuery() {
        return indexQuery;
    }

    public void setIndexQuery(String indexQuery) {
        this.indexQuery = indexQuery;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }

    public List<String> getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(List<String> keyWord) {
        this.keyWord = keyWord;
    }
}
