package com.dr.archive.model.to;

/**
 * 描述：
 *
 * @author tuzl
 * @date 2020/6/23 14:52
 */
public class RetentionTo {
    /**
     * 定期十年 D10
     */
    private long tenYear;
    /**
     * 定期三十年 D30
     */
    private long thirtyYear;
    /**
     * 永久 Y
     */
    private long perpetual;
    /**
     * 长期 C
     */
    private long longTime;
    /**
     * 短期 D
     */
    private long shortTime;

    private String fondName;
    private String fondCode;

    public String getFondName() {
        return fondName;
    }

    public void setFondName(String fondName) {
        this.fondName = fondName;
    }

    public String getFondCode() {
        return fondCode;
    }

    public void setFondCode(String fondCode) {
        this.fondCode = fondCode;
    }

    public long getLongTime() {
        return longTime;
    }

    public void setLongTime(long longTime) {
        this.longTime = longTime;
    }

    public long getTenYear() {
        return tenYear;
    }

    public void setTenYear(long tenYear) {
        this.tenYear = tenYear;
    }

    public long getThirtyYear() {
        return thirtyYear;
    }

    public void setThirtyYear(long thirtyYear) {
        this.thirtyYear = thirtyYear;
    }

    public long getPerpetual() {
        return perpetual;
    }

    public void setPerpetual(long perpetual) {
        this.perpetual = perpetual;
    }

    public long getShortTime() {
        return shortTime;
    }

    public void setShortTime(long shortTime) {
        this.shortTime = shortTime;
    }
}
