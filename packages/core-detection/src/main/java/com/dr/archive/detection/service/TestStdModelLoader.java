package com.dr.archive.detection.service;

import com.dr.archive.detection.enums.LinkType;
import com.dr.archive.detection.model.TestItemInfo;
import com.dr.archive.detection.model.TestSubType;

/**
 * 四性检测标准相关数据加载类
 * <p>
 * 主要实现两类配置信息加载功能
 * 1、四性检测标准描述信息加载{@link TestSubType}
 * 2、四性检测标准描述信息加载{@link TestSubType}
 * <br>
 * 因为四性检测相关的业务基本上都是从国标上明确规定的，所以相关描述信息也是从国标上复制出来的
 * 标准相关描述信息在系统中只需要提供展示功能即可，并不需要提供编辑功能
 *
 * @author dr
 */
public interface TestStdModelLoader {
    /**
     * 资源路径
     */
    String RESOURCE_PATH = "com.dr.archive.detection/";

    /**
     * 根据小项编码查询小项详细描述信息
     *
     * @param code     编码
     * @param linkType 环节类型
     * @return
     */
    TestSubType getSubTypeByCode(String code, LinkType linkType);

    /**
     * 追加指定的小项描述信息，如果已经存在了该描述信息，则会直接报错
     * <p>
     * 系统中同一个环节的编码不能重复
     *
     * @param subType
     */
    default void addSubType(TestSubType subType) {
        addSubType(subType, false);
    }

    /**
     * 追加小项描述信息，可以选择覆盖默认的配置
     *
     * @param subType
     * @param override 是否覆盖默认的配置
     */
    void addSubType(TestSubType subType, boolean override);

    /**
     * 根据实现小项编码获取实现小项描述信息
     *
     * @param code     实现小项编码
     * @param linkType 环节编码
     * @return
     */
    TestItemInfo getTestItemInfo(String code, LinkType linkType);

    /**
     * 添加检测小项描述信息，如果指定的编码已经存在了，则会抛错
     *
     * @param itemInfo
     */
    default void addItemInfo(TestItemInfo itemInfo, String parentCode, LinkType linkType) {
        addItemInfo(itemInfo, parentCode, linkType, false);
    }

    /**
     * 添加检测小项描述信息
     *
     * @param itemInfo
     * @param override
     */
    void addItemInfo(TestItemInfo itemInfo, String parentCode, LinkType linkType, boolean override);

}
