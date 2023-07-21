package com.dr.archive.fuzhou.configManager.bo.itemconfig;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 审批事项配置详情
 *
 * @author dr
 * TODO 这里面太复杂，需要沟通
 */
public class ApproveItemConfig {

    /**
     * 归档环节
     */
    public static final String HUAN_JIE_GUI_DANG = "1";
    /**
     * 移交环节
     */
    public static final String HUAN_JIE_YI_JIAO = "2";
    /**
     * 长期保存环节
     */
    public static final String HUAN_JIE_BAO_CUN = "3";


    private String agentCode;
    private String agentName;
    /**
     * 档案门类ID
     */
    private String arcId;
    /**
     * 事项编码
     */
    private String code;
    /**
     * 配置所属单位
     */
    private String configOrgCode;
    /**
     * 配置单位所属区划
     */
    private String configRegionCode;
    /**
     * 扩展属性
     */
    private String extend;
    private String isCaseSetting;
    /**
     * 事项ID
     */
    private String itemId;
    /**
     * 事项类型
     */
    private String matId;
    /**
     * 配置名称
     */
    private String matName;
    /**
     * 牵头单位编码
     */
    private String orgCode;


    private String regionCode;
    private String regionName;

    /**
     * 申请材料
     */
    private List<MaterialRule.ApplyMaterialRule> materials;
    /**
     * 过程材料
     */
    private List<MaterialRule.PrintMaterialRule> printmodels;
    /**
     * 结果材料
     */
    private List<MaterialRule.ResultMaterialRule> results;

    /**
     * 数据包检测规则
     */
    private List<FileRule> testItems;
    /**
     * 元数据检测规则
     */
    private List<MetaRule> rules;
    /**
     * 环节材料配置
     */
    private LinkRule linkRule;

    /**
     * 根据归档、移交、保存环节判断是否检测数据包
     *
     * @param huanjie
     * @return
     */
    public boolean testFile(String huanjie) {
        if (testItems == null) {
            return false;
        } else {
            //指定环节有配置文件检测，则返回true
            return !testItems.stream()
                    .filter(r -> r.getLinkCode().equals(huanjie))
                    .collect(Collectors.toSet())
                    .isEmpty();
        }
    }

    /**
     * 获取元数据检测规则
     *
     * @param huanjie
     * @return
     */
    public List<MetaRule> getMetaRules(String huanjie) {
        if (rules == null) {
            return Collections.EMPTY_LIST;
        } else {
            return rules.stream()
                    .filter(r -> r.getLinkCode().equals(huanjie))
                    .collect(Collectors.toList());
        }
    }

    /**
     * 获取材料清单系统
     *
     * @param situationCode 情形
     * @return
     */
    public Set<String> getStdFileList(String situationCode) {
        Set<String> resultSet = new HashSet<>();
        //申请材料
        if (materials != null) {
            for (MaterialRule.ApplyMaterialRule material : materials) {
                //TODO 过滤情形
                resultSet.add("申报文件/" + material.getName());
            }
        }
        //过程材料
        if (printmodels != null) {
            for (MaterialRule.PrintMaterialRule printmodel : printmodels) {
                resultSet.add("过程文件/" + printmodel.getName());
            }
        }
        //结果材料
        if (results != null) {
            for (MaterialRule.ResultMaterialRule resultMaterial : results) {
                resultSet.add("结果文件/" + resultMaterial.getName());
            }
        }
        return resultSet;
    }

    /**
     * 判断材料清单中是否有结果材料
     *
     * @return
     */
    public boolean hasResultMaterial() {
        return false;
    }

    public String getAgentCode() {
        return agentCode;
    }

    public void setAgentCode(String agentCode) {
        this.agentCode = agentCode;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getArcId() {
        return arcId;
    }

    public void setArcId(String arcId) {
        this.arcId = arcId;
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

    public String getConfigRegionCode() {
        return configRegionCode;
    }

    public void setConfigRegionCode(String configRegionCode) {
        this.configRegionCode = configRegionCode;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public String getIsCaseSetting() {
        return isCaseSetting;
    }

    public void setIsCaseSetting(String isCaseSetting) {
        this.isCaseSetting = isCaseSetting;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getMatId() {
        return matId;
    }

    public void setMatId(String matId) {
        this.matId = matId;
    }

    public String getMatName() {
        return matName;
    }

    public void setMatName(String matName) {
        this.matName = matName;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public List<FileRule> getTestItems() {
        return testItems;
    }

    public void setTestItems(List<FileRule> testItems) {
        this.testItems = testItems;
    }

    public List<MetaRule> getRules() {
        return rules;
    }

    public void setRules(List<MetaRule> rules) {
        this.rules = rules;
    }

    public List<MaterialRule.ApplyMaterialRule> getMaterials() {
        return materials;
    }

    public void setMaterials(List<MaterialRule.ApplyMaterialRule> materials) {
        this.materials = materials;
    }

    public List<MaterialRule.PrintMaterialRule> getPrintmodels() {
        return printmodels;
    }

    public void setPrintmodels(List<MaterialRule.PrintMaterialRule> printmodels) {
        this.printmodels = printmodels;
    }

    public List<MaterialRule.ResultMaterialRule> getResults() {
        return results;
    }

    public void setResults(List<MaterialRule.ResultMaterialRule> results) {
        this.results = results;
    }

    public LinkRule getLinkRule() {
        return linkRule;
    }

    public void setLinkRule(LinkRule linkRule) {
        this.linkRule = linkRule;
    }


}
