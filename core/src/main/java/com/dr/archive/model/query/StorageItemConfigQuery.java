package com.dr.archive.model.query;

/**
 * 描述：
 *
 * @author tuzl
 * @date 2020/6/18 9:10
 */
public class StorageItemConfigQuery {
    private String storageSchemeItemId;
    private String fieldCode;
    private String fieldName;

    public String getStorageSchemeItemId() {
        return storageSchemeItemId;
    }

    public void setStorageSchemeItemId(String storageSchemeItemId) {
        this.storageSchemeItemId = storageSchemeItemId;
    }

    public String getFieldCode() {
        return fieldCode;
    }

    public void setFieldCode(String fieldCode) {
        this.fieldCode = fieldCode;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}
