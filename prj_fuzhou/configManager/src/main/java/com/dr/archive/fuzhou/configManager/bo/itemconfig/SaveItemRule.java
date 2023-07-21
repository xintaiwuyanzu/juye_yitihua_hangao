package com.dr.archive.fuzhou.configManager.bo.itemconfig;

import java.util.ArrayList;
import java.util.List;

/**
 * 保管期限规则
 * 从智能归档配置系统中获取
 *
 * @author hyj
 */
public class SaveItemRule {
    /**
     * 保管期限
     */
    private String period;
    /**
     * 优先级
     */
    private Integer priority;
    private List<MetadataItem> metadata = new ArrayList<>();
    private String id;
    private String code;

    public List<MetadataItem> getMetadata() {
        return metadata;
    }

    public void setMetadata(List<MetadataItem> metadata) {
        this.metadata = metadata;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static class MetadataItem {
        private String periodId;
        private String metadataId;
        private String creator;
        private String crTime;
        private Integer flag;
        /**
         * 元数据项代码
         */
        private String eName;
        private String modifier;
        private String name;
        private String id;
        private String moTime;
        private String content;

        public String getPeriodId() {
            return periodId;
        }

        public void setPeriodId(String periodId) {
            this.periodId = periodId;
        }

        public String getMetadataId() {
            return metadataId;
        }

        public void setMetadataId(String metadataId) {
            this.metadataId = metadataId;
        }

        public String getCreator() {
            return creator;
        }

        public void setCreator(String creator) {
            this.creator = creator;
        }

        public String getCrTime() {
            return crTime;
        }

        public void setCrTime(String crTime) {
            this.crTime = crTime;
        }

        public Integer getFlag() {
            return flag;
        }

        public void setFlag(Integer flag) {
            this.flag = flag;
        }

        public String geteName() {
            return eName;
        }

        public void seteName(String eName) {
            this.eName = eName;
        }

        public String getModifier() {
            return modifier;
        }

        public void setModifier(String modifier) {
            this.modifier = modifier;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMoTime() {
            return moTime;
        }

        public void setMoTime(String moTime) {
            this.moTime = moTime;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    @Override
    public String toString() {
        return "SaveItemRule{" +
                "period='" + period + '\'' +
                ", priority=" + priority +
                '}';
    }
}
