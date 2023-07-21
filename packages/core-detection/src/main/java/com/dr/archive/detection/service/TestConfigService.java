package com.dr.archive.detection.service;

import com.dr.archive.detection.entity.TestConfig;
import com.dr.archive.detection.enums.LinkType;
import com.dr.archive.detection.model.TestItemInfo;
import com.dr.archive.manage.category.entity.Category;
import com.dr.framework.common.form.core.entity.FormDefinition;
import com.dr.framework.common.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * describe
 *
 * @author tzl
 * @date 2020/5/24 10:06
 */
@Service
public interface TestConfigService extends BaseService<TestConfig> {

    /**
     * 获取指定环节的系统实现的所有四性检测项目描述信息
     *
     * @param linkType
     * @return
     */
    List<TestItemInfo> getItemInfos(LinkType linkType);

    /**
     * 根据条件查询四性检测配置信息
     *
     * @param formDefinition
     * @param category
     * @param linkType
     * @return
     */
    TestConfig getConfig(FormDefinition formDefinition, Category category, LinkType linkType);
}
