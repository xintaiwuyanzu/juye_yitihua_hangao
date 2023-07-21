package com.dr.archive.model.to;

/**
 * 元数据字段基本信息
 *
 * @author dr
 */
public class MetadataTo extends BaseTo {

    private String templeId;

    private String metadataCode;

    private String metadataName;

    private String metadataType;

    private Integer metadataLength;

    public String getTempleId() {
        return templeId;
    }

    public void setTempleId(String templeId) {
        this.templeId = templeId;
    }

    public String getMetadataCode() {
        return metadataCode;
    }

    public void setMetadataCode(String metadataCode) {
        this.metadataCode = metadataCode;
    }

    public String getMetadataName() {
        return metadataName;
    }

    public void setMetadataName(String metadataName) {
        this.metadataName = metadataName;
    }

    public String getMetadataType() {
        return metadataType;
    }

    public void setMetadataType(String metadataType) {
        this.metadataType = metadataType;
    }

    public Integer getMetadataLength() {
        return metadataLength;
    }

    public void setMetadataLength(Integer metadataLength) {
        this.metadataLength = metadataLength;
    }
}
