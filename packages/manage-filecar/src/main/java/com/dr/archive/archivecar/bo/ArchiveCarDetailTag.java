package com.dr.archive.archivecar.bo;

/**
 * 档案车详情标记
 *
 * @author dr
 */
public class ArchiveCarDetailTag {
    /**
     * 档案车类型
     */
    private String carType;
    /**
     * 标记编码
     */
    private String code;
    /**
     * 标记名称
     */
    private String name;
    /**
     * 创建人Id
     */
    private String personId;

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }
}
