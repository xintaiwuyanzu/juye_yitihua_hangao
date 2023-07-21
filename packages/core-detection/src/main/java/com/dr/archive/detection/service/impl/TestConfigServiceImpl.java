package com.dr.archive.detection.service.impl;

import com.dr.archive.detection.entity.TestConfig;
import com.dr.archive.detection.enums.LinkType;
import com.dr.archive.detection.model.TestItemInfo;
import com.dr.archive.detection.service.ItemDetectionService;
import com.dr.archive.detection.service.TestConfigService;
import com.dr.archive.detection.service.TestStdModelLoader;
import com.dr.archive.manage.category.entity.Category;
import com.dr.archive.service.impl.BaseYearServiceImpl;
import com.dr.framework.common.form.core.entity.FormDefinition;
import com.dr.framework.core.orm.sql.Column;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * describe
 *
 * @author tzl
 * @date 2020/5/24 10:07
 */
@Service
public class TestConfigServiceImpl extends BaseYearServiceImpl<TestConfig> implements TestConfigService {
    @Autowired
    TestStdModelLoader testStdModelLoader;
    @Autowired
    List<ItemDetectionService> itemDetectionServices;

    @Override
    public List<TestItemInfo> getItemInfos(LinkType linkType) {
        return itemDetectionServices.stream().filter(i -> i.configAble(linkType, null, null) == ItemDetectionService.ConfigType.DETECT_WITH_CONFIG_CODE)
                .map(i -> testStdModelLoader.getTestItemInfo(i.code(), linkType))
                .collect(Collectors.toList());
    }

    @Override
    public TestConfig getConfig(FormDefinition formDefinition, Category category, LinkType linkType) {
        return null;
    }

    @Override
    protected Class getSubTableClass() {
        return TestConfig.class;
    }

    @Override
    protected Column getRelateColumn() {
        return null;
    }


}
