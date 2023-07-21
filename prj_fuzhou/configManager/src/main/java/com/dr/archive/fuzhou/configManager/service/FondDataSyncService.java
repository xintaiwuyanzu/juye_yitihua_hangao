package com.dr.archive.fuzhou.configManager.service;

import java.util.Map;
import java.util.Set;

/**
 * @author caor
 * @date 2021-09-09 17:23
 */
public interface FondDataSyncService {
    //需要在字典管理中配置 key为智能归档配置系统中元数据，value为档案系统中ArchiveEntity中定义的元数据 archive.metadata.ws·zw.archivalcategorycode
    String DICT_ARCHIVE_METADATA = "archive.metadata.";
    String DICT_ARCHIVE_METADATA_DEFAULT = "archive.metadata.default";

    /**
     * 根据机构编码同步全宗门类数据
     *
     * @param orgCode
     * @return
     */
    long dataSyncFondByOrgCode(String orgCode, Map<String, String> formDefinitionMap, Set<String> permissionSet);
}
