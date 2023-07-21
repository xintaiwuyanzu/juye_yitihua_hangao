package com.dr.archive.utilization.search.to;

/**
 * @author: lirs
 * @date: 2022/2/11 15:51
 */
public class Querys {
    private String keyWords;

    private String operato;
    private String condition;
    private boolean multiSearch;

    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }


    public String getOperato() {
        return operato;
    }

    public void setOperato(String operato) {
        this.operato = operato;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public boolean isMultiSearch() {
        return multiSearch;
    }

    public void setMultiSearch(boolean multiSearch) {
        this.multiSearch = multiSearch;
    }
}
