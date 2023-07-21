package com.dr.archive.detection.service.impl;

import com.dr.archive.detection.enums.DetectionType;
import com.dr.archive.detection.enums.LinkType;
import com.dr.archive.detection.model.TestItemInfo;
import com.dr.archive.detection.model.TestSubType;
import com.dr.archive.detection.service.TestStdModelLoader;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;


/**
 * 默认的配置加载类
 * TODO 将来数据量大了之后可以考虑把数据放到缓存中
 *
 * @author dr
 */
@Component
public class DefaultTestStdModelLoader implements TestStdModelLoader {
    final Object syncLock = new Object();
    final AtomicBoolean init = new AtomicBoolean(false);
    final static Logger logger = LoggerFactory.getLogger(DefaultTestStdModelLoader.class);

    @Autowired
    ObjectMapper objectMapper;

    /**
     * 所有检测小项集合
     */
    private Map<LinkType, Map<String, TestSubType>> subTypeMap = new HashMap<>(LinkType.values().length);
    /**
     * 所有检测小项实现方法描述信息的集合
     */
    private Map<LinkType, Map<String, TestItemInfo>> itemMap = new HashMap<>(LinkType.values().length);
    private Set<String> noContainsItemCodes = new HashSet<>();

    @Override
    public TestSubType getSubTypeByCode(String code, LinkType linkType) {
        checkInit();
        Map<String, TestSubType> stringTestSubTypeMap = subTypeMap.get(linkType);
        Map<String, TestSubType> defaultMap = subTypeMap.get(subTypeMap.get(LinkType.DEFAULT));
        if (stringTestSubTypeMap == null) {
            return defaultMap.get(code);
        } else {
            return stringTestSubTypeMap.getOrDefault(code, null);
        }
    }


    @Override
    public void addSubType(TestSubType subType, boolean override) {
        LinkType linkType = subType.getLinkType();
        if (linkType == null) {
            linkType = LinkType.DEFAULT;
            subType.setLinkType(LinkType.DEFAULT);
        }
        Assert.notNull(subType.getDetectionType(), "检测类型不能为空");
        Assert.isTrue(StringUtils.hasText(subType.getCode()), "检测编码不能为空！");
        synchronized (syncLock) {
            Map<String, TestSubType> stringTestSubTypeMap = subTypeMap.computeIfAbsent(linkType, k -> new HashMap<>());
            if (!override) {
                Assert.isTrue(!stringTestSubTypeMap.containsKey(subType.getCode()), "已经存在编码为" + subType.getCode() + "的检测小项配置");
            }
            stringTestSubTypeMap.put(subType.getCode(), subType);
        }
    }

    @Override
    public TestItemInfo getTestItemInfo(String code, LinkType linkType) {
        if (linkType == null) {
            linkType = LinkType.DEFAULT;
        }
        Map<String, TestItemInfo> itemInfoMap = itemMap.computeIfAbsent(linkType, k -> new HashMap<>());
        synchronized (syncLock) {
            if (itemInfoMap.containsKey(code)) {
                return itemInfoMap.get(code);
            } else if (noContainsItemCodes.contains(code)) {
                return null;
            } else {
                checkAndLoadItemInfo(code);
                return itemInfoMap.get(code);
            }
        }
    }


    @Override
    public void addItemInfo(TestItemInfo itemInfo, String parentCode, LinkType linkType, boolean override) {
        if (linkType == null) {
            linkType = LinkType.DEFAULT;
        }
        Assert.isTrue(StringUtils.hasText(parentCode), "检测方法父编码不能为空！");
        TestSubType subType = getSubTypeByCode(parentCode, linkType);
        Assert.notNull(subType, "未找到指定的检测小项父编码：" + parentCode);
        Assert.isTrue(StringUtils.hasText(itemInfo.getItemCode()), "检测方法编码不能为空！");
        itemInfo.setSubType(subType);
        synchronized (syncLock) {
            Map<String, TestItemInfo> itemInfoMap = itemMap.computeIfAbsent(linkType, k -> new HashMap<>());
            if (!override) {
                Assert.isTrue(!itemInfoMap.containsKey(itemInfo.getItemCode()), "已经存在编码为" + itemInfo.getItemCode() + "的检测方法描述");
            }
            itemInfoMap.put(itemInfo.getItemCode(), itemInfo);
        }
    }


    private void checkInit() {
        if (!init.get()) {
            synchronized (syncLock) {
                try {
                    Resource[] resources = loadResourceFromJarPath(RESOURCE_PATH + "testSubType.json");
                    for (Resource resource : resources) {
                        loadFromResource(resource, this::doLoadSubType);
                    }
                } catch (IOException e) {
                    logger.error("加载四性检测小项失败", e);
                }
                init.compareAndSet(false, true);
            }
        }
    }


    /**
     * 加载单个检测小项
     *
     * @param jsonNode
     */
    private void doLoadSubType(JsonNode jsonNode) {
        TestSubType subType = new TestSubType();
        if (jsonNode.has("code")) {
            subType.setCode(jsonNode.get("code").asText());
        }
        if (jsonNode.has("title")) {
            subType.setTitle(jsonNode.get("title").asText());
        }
        if (jsonNode.has("stdDescription")) {
            subType.setStdDescription(jsonNode.get("stdDescription").asText());
        }
        if (jsonNode.has("stdName")) {
            subType.setStdName(jsonNode.get("stdName").asText());
        }
        if (jsonNode.has("testRecordMethod")) {
            subType.setTestRecordMethod(jsonNode.get("testRecordMethod").asText());
        }
        if (jsonNode.has("linkType")) {
            subType.setLinkType(LinkType.fromCode(jsonNode.get("linkType").asText()));
        } else {
            subType.setLinkType(LinkType.DEFAULT);
        }
        subType.setDetectionType(DetectionType.fromCode(jsonNode.get("detectionType").asText()));
        if (jsonNode.has("comment")) {
            subType.setComment(jsonNode.get("comment").asText());
        }

        addSubType(subType);
    }

    private synchronized void checkAndLoadItemInfo(String code) {
        //之前没有加载过，尝试加载
        try {
            Resource[] resources = loadResourceFromJarPath(RESOURCE_PATH + code + ".json");
            if (resources.length == 0) {
                noContainsItemCodes.add(code);
            }
            for (Resource resource : resources) {
                loadFromResource(resource, this::doLoadItemInfo);
            }
        } catch (IOException e) {
            logger.error("加载四性检测小项失败", e);
        }
    }

    /**
     * 从配置文件中加载检测方法详情数据
     *
     * @param jsonNode
     */
    private void doLoadItemInfo(JsonNode jsonNode) {
        String parentCode = jsonNode.get("parentCode").asText();
        LinkType linkType = LinkType.DEFAULT;
        if (jsonNode.has("linkType")) {
            linkType = LinkType.fromCode(jsonNode.get("linkType").asText());
        }
        TestItemInfo itemInfo = new TestItemInfo();
        if (jsonNode.has("itemCode")) {
            itemInfo.setItemCode(jsonNode.get("itemCode").asText());
        }
        if (jsonNode.has("title")) {
            itemInfo.setTitle(jsonNode.get("title").asText());
        }
        if (jsonNode.has("purpose")) {
            itemInfo.setPurpose(jsonNode.get("purpose").asText());
        }
        if (jsonNode.has("target")) {
            itemInfo.setTarget(jsonNode.get("target").asText());
        }
        if (jsonNode.has("method")) {
            itemInfo.setMethod(jsonNode.get("method").asText());
        }
        if (jsonNode.has("comment")) {
            itemInfo.setComment(jsonNode.get("comment").asText());
        }
        addItemInfo(itemInfo, parentCode, linkType);
    }

    /**
     * 从json文件中加载检测小项
     *
     * @param resource
     * @throws IOException
     */
    private void loadFromResource(Resource resource, Consumer<JsonNode> consumer) throws IOException {
        objectMapper.readTree(resource.getInputStream()).elements().forEachRemaining(consumer);
    }

    private Resource[] loadResourceFromJarPath(String path) throws IOException {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        return resolver.getResources(ResourceLoader.CLASSPATH_URL_PREFIX + path);
    }

}
