package com.dr.archive.fuzhou.configManager.bo.itemconfig;

import com.dr.archive.fuzhou.configManager.bo.AbstractConfigEntity;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 环节材料清单
 *
 * @author dr
 */
public class LinkRule {

    /**
     * 环节类型代码
     */
    private static final Map<String, String> LINK_CODE_NAME = new HashMap<>();
    private static final Map<String, String> MATERIAL_CODE_MAP = new HashMap<>();
    /**
     * 申请材料类型
     */
    public static final String APPROVE_FILE_CODE = "A";

    /**
     * 结果材料
     */
    public static final String RESULT_FILE_CODE_B99 = "C";

    static {
        LINK_CODE_NAME.put("1", "收件");
        LINK_CODE_NAME.put("2", "受理");
        LINK_CODE_NAME.put("3", "审核");
        LINK_CODE_NAME.put("4", "办结");
        LINK_CODE_NAME.put("11", "补齐补正");
        LINK_CODE_NAME.put("12", "挂起");
        LINK_CODE_NAME.put("99", "其他");

        MATERIAL_CODE_MAP.put("B1", "收件材料接收凭证");
        MATERIAL_CODE_MAP.put("B2", "即审即办信用承诺书");
        MATERIAL_CODE_MAP.put("B3", "备案未抽中结果告知书");
        MATERIAL_CODE_MAP.put("B4", "不予受理通知书");
        MATERIAL_CODE_MAP.put("B5", "特殊程序告知书");
        MATERIAL_CODE_MAP.put("B6", "容缺受理信用承诺书");
        MATERIAL_CODE_MAP.put("B7", "办理结果交接单");
        MATERIAL_CODE_MAP.put("B8", "受理承诺单");
        MATERIAL_CODE_MAP.put("B9", "确认补齐通知书");
        MATERIAL_CODE_MAP.put("B10", "审批替代信用承诺书");
        MATERIAL_CODE_MAP.put("B11", "建设局业务模版");
        MATERIAL_CODE_MAP.put("B12", "备案抽中结果告知书");
        MATERIAL_CODE_MAP.put("B13", "办结通知书");
        MATERIAL_CODE_MAP.put("B14", "不予许可通知书");
        MATERIAL_CODE_MAP.put("B15", "退件通知书");
        MATERIAL_CODE_MAP.put("B16", "收件通知书");
        MATERIAL_CODE_MAP.put("B17", "业务审批单");
        MATERIAL_CODE_MAP.put("B18", "补齐补正通知书");
        MATERIAL_CODE_MAP.put("B19", "送达回执");
        MATERIAL_CODE_MAP.put("B20", "受理材料接收凭证");
        MATERIAL_CODE_MAP.put("B21", "整改通知书");
        MATERIAL_CODE_MAP.put("B99", "其他");

    }

    private List<LinkRuleInfo> linkRuleInfo;

    /**
     * 执行环节材料清单检测
     *
     * @param materialCountMap
     * @param approveItemConfig
     * @return
     */
    public List<String> testLinkRule(Map<String, Integer> materialCountMap, ApproveItemConfig approveItemConfig) {
        List<String> results = new ArrayList<>();
        if (linkRuleInfo != null) {
            for (LinkRuleInfo ruleInfo : linkRuleInfo) {
                if (ruleInfo.ruleInfo != null) {
                    for (RuleInfo info : ruleInfo.ruleInfo) {
                        switch (info.classify) {
                            case "1":
                                //申报
                                int applyCount = materialCountMap.get(APPROVE_FILE_CODE);
                                int configCount = info.materialNum;
                                if (applyCount < configCount) {
                                    results.add(String.format("申请材料数量不对，规定数量至少为%s，实际数量为%s", configCount, applyCount));
                                }
                                break;
                            case "2":
                                //过程
                                if (ruleInfo.rulesRelatedInfo != null) {
                                    for (RulesRelatedInfo rulesRelatedInfo : ruleInfo.rulesRelatedInfo) {
                                        String[] materialTypes = rulesRelatedInfo.materialCodes.split(",");
                                        boolean result = true;
                                        switch (rulesRelatedInfo.related) {
                                            case "1":
                                                //或
                                                for (String materialType : materialTypes) {
                                                    result = result || materialCountMap.containsKey(materialType);
                                                }
                                                break;
                                            case "2":
                                                //且
                                                for (String materialType : materialTypes) {
                                                    result = result & materialCountMap.containsKey(materialType);
                                                }
                                                break;
                                            default:
                                                break;
                                        }
                                        if (!result) {
                                            results.add(
                                                    String.format(
                                                            "环节【%s】的材料清单与归档设置不匹配，设置内容为：%s",
                                                            LINK_CODE_NAME.get(rulesRelatedInfo.linkCode),
                                                            Arrays.stream(materialTypes).map(MATERIAL_CODE_MAP::get)
                                                                    .collect(Collectors.joining("1".equals(rulesRelatedInfo.related) ? "或" : "且"))
                                                    )
                                            );
                                        }
                                    }
                                }
                                break;
                            case "3":
                                //办结
                                Integer resultCount = materialCountMap.get(RESULT_FILE_CODE_B99);
                                int configResultCount = info.materialNum;
                                if (approveItemConfig.hasResultMaterial()) {
                                    if (resultCount < configResultCount) {
                                        results.add(String.format("办结材料数量不对，规定数量至少为%s，实际数量为%s", configResultCount, resultCount));
                                    }
                                }
                                break;
                            default:
                                //todo 不处理
                                break;
                        }
                    }
                }
            }
        }
        return results;
    }


    public static class LinkRuleInfo {
        /**
         * 环节类型配置
         */
        private List<RuleInfo> ruleInfo;
        /**
         * 环节对应材料信息
         */
        private List<RulesRelatedInfo> rulesRelatedInfo;
        /**
         * 环节类型事项配置(配置事项classify=2才有)
         */
        private RulesItemInfo rulesItemInfo;

        public List<RuleInfo> getRuleInfo() {
            return ruleInfo;
        }

        public void setRuleInfo(List<RuleInfo> ruleInfo) {
            this.ruleInfo = ruleInfo;
        }

        public RulesItemInfo getRulesItemInfo() {
            return rulesItemInfo;
        }

        public void setRulesItemInfo(RulesItemInfo rulesItemInfo) {
            this.rulesItemInfo = rulesItemInfo;
        }

        public List<RulesRelatedInfo> getRulesRelatedInfo() {
            return rulesRelatedInfo;
        }

        public void setRulesRelatedInfo(List<RulesRelatedInfo> rulesRelatedInfo) {
            this.rulesRelatedInfo = rulesRelatedInfo;
        }
    }

    /**
     * 环节类型配置
     */
    public static class RuleInfo extends AbstractConfigEntity {
        /**
         * 名称
         */
        private String name;
        /**
         * 配置类型(1默认 2区划 3单位 4事项)
         */
        private String type;
        /**
         * 分类配置(1申报 2过程 3办结)
         */
        private String classify;
        /**
         * 办结结果文件数量(classify=3)
         */
        private int fileNum;
        /**
         * 申报分类材料数量(classify=1)
         */
        private int materialNum;


        public String getClassify() {
            return classify;
        }

        public void setClassify(String classify) {
            this.classify = classify;
        }

        public int getFileNum() {
            return fileNum;
        }

        public void setFileNum(int fileNum) {
            this.fileNum = fileNum;
        }

        public int getMaterialNum() {
            return materialNum;
        }

        public void setMaterialNum(int materialNum) {
            this.materialNum = materialNum;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    /**
     * 环节类型事项配置(配置事项classify=2才有)
     */
    public static class RulesItemInfo extends AbstractConfigEntity {
        /**
         * ——分类配置(申报 2过程 3办结)
         */
        private String classify;
        /**
         * 事项编码
         */
        private String code;
        /**
         * 配置机构编码
         */
        private String configOrgCode;
        /**
         * 事项版本号
         */
        private String version;
        /**
         * 事项ID
         */
        private String itemId;
        /**
         * 规则ID
         */
        private String rulesId;
        /**
         * 材料名称
         */
        private String matName;

        public String getClassify() {
            return classify;
        }

        public void setClassify(String classify) {
            this.classify = classify;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getConfigOrgCode() {
            return configOrgCode;
        }

        public void setConfigOrgCode(String configOrgCode) {
            this.configOrgCode = configOrgCode;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getItemId() {
            return itemId;
        }

        public void setItemId(String itemId) {
            this.itemId = itemId;
        }

        public String getRulesId() {
            return rulesId;
        }

        public void setRulesId(String rulesId) {
            this.rulesId = rulesId;
        }

        public String getMatName() {
            return matName;
        }

        public void setMatName(String matName) {
            this.matName = matName;
        }
    }


    public static class RulesRelatedInfo extends AbstractConfigEntity {
        /**
         * 环节code(对应bsp数据字典 LinkType)
         */
        private String linkCode;
        /**
         * 关系（1或， 2且）
         */
        private String related;
        /**
         * 材料code组(对应bsp数据字典 MaterialType)
         */
        private String materialCodes;
        /**
         * 配置类型
         */
        private String type;
        /**
         * 规则id
         */
        private String rulesId;

        public String getLinkCode() {
            return linkCode;
        }

        public void setLinkCode(String linkCode) {
            this.linkCode = linkCode;
        }

        public String getRelated() {
            return related;
        }

        public void setRelated(String related) {
            this.related = related;
        }

        public String getMaterialCodes() {
            return materialCodes;
        }

        public void setMaterialCodes(String materialCodes) {
            this.materialCodes = materialCodes;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getRulesId() {
            return rulesId;
        }

        public void setRulesId(String rulesId) {
            this.rulesId = rulesId;
        }
    }

    public List<LinkRuleInfo> getLinkRuleInfo() {
        return linkRuleInfo;
    }

    public void setLinkRuleInfo(List<LinkRuleInfo> linkRuleInfo) {
        this.linkRuleInfo = linkRuleInfo;
    }
}
