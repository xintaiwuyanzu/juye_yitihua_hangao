package com.dr.archive.model.query;

/**
 * 存储方案查询条件
 *
 * @author dr
 */
public class StorageConfigQuery extends BaseConfigQuery {
    private String storageSchemeName;
    private String storageSchemeCode;
    private String archiveId;
    private String id;

    public String getStorageSchemeName() {
        return storageSchemeName;
    }

    public void setStorageSchemeName(String storageSchemeName) {
        this.storageSchemeName = storageSchemeName;
    }

    public String getStorageSchemeCode() {
        return storageSchemeCode;
    }

    public void setStorageSchemeCode(String storageSchemeCode) {
        this.storageSchemeCode = storageSchemeCode;
    }

    public String getArchiveId() {
        return archiveId;
    }

    public void setArchiveId(String archiveId) {
        this.archiveId = archiveId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
