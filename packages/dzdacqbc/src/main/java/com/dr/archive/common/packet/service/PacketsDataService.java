package com.dr.archive.common.packet.service;

import java.io.InputStream;

/**
 * @Author: caor
 * @Date: 2021-11-23 15:46
 * @Description:
 */
public interface PacketsDataService {
    String BASIC_INFORMATION_METADATA = "基本信息元数据.xml";
    String MANAGEMENT_PROCESS_METADATA = "管理过程元数据.xml";

    /**
     * 单条目录信息打包成zip
     *
     * @param formDefinitionId
     * @param formDataId
     * @param targetPath       打包路径，若为空则默认创建路径
     */
    void packet(String formDefinitionId, String formDataId, String targetPath);

    /**
     * 解析单个数据包
     *
     * @param inputStream
     */
    void parser(InputStream inputStream);
}
