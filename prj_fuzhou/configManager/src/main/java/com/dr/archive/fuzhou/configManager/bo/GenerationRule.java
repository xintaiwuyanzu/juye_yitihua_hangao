package com.dr.archive.fuzhou.configManager.bo;

/**
 * 具体的档号生成规则
 *
 * @author dr
 */
public class GenerationRule {
    private String id;
    /**
     * 中文名称
     */
    private String name;
    /**
     * 间隔符
     */
    private String symbol;
    /**
     * 英文字段名称
     */
    private String eName;
    /**
     * 编号长度
     */
    private Integer digit;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String geteName() {
        return eName;
    }

    public void seteName(String eName) {
        this.eName = eName;
    }

    public Integer getDigit() {
        return digit;
    }

    public void setDigit(Integer digit) {
        this.digit = digit;
    }
}
