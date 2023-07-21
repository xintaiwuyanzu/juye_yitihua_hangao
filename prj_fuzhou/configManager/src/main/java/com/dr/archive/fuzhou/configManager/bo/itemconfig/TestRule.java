package com.dr.archive.fuzhou.configManager.bo.itemconfig;

import java.util.List;

/**
 * @author: qiuyf
 * @date: 2022/5/26 14:44
 */
public class TestRule {
    /*规则类型
     * MaxMinVal 最大值最小值, codomain 值域, MaxMinLen 最大长度最小长度, disByte 禁用字符, type 数据类型*/
    private String type;
    private String val;
    private String maxVal;
    private String minVal;
    /*
     * 包含,不包含,等于,大于,小于,不为空*/
    private String condition;
    private List<RuleCondition> conditions;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getMaxVal() {
        return maxVal;
    }

    public void setMaxVal(String maxVal) {
        this.maxVal = maxVal;
    }

    public String getMinVal() {
        return minVal;
    }

    public void setMinVal(String minVal) {
        this.minVal = minVal;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public List<RuleCondition> getConditions() {
        return conditions;
    }

    public void setConditions(List<RuleCondition> conditions) {
        this.conditions = conditions;
    }

    public class RuleCondition {
        private String val;
        /*检测元数据字段*/
        private String metadata;
        private String condition;

        public String getVal() {
            return val;
        }

        public void setVal(String val) {
            this.val = val;
        }

        public String getMetadata() {
            return metadata;
        }

        public void setMetadata(String metadata) {
            this.metadata = metadata;
        }

        public String getCondition() {
            return condition;
        }

        public void setCondition(String condition) {
            this.condition = condition;
        }
    }
}
