package com.dr.archive.tag;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 审批中心相关配置
 *
 * @author dr
 */
@Component
public class TagConfig {
    @Value(value = "${fuzhou.tag.ip}")
    String tagUrl;
    /**
     * 带磁性地址
     */
    private String tagword;
    /**
     * 关键词
     */
    private String tagkwords;

    public String getTagword() {
        return "http://" + tagUrl + "/aiplus/nlp/word/api/v1";
    }

    public String getTagkwords() {
        return "http://" + tagUrl + "/aiplus/nlp/keywords/api/v1";
    }

    public String getKeysentences() {
        return "http://" + tagUrl + "/aiplus/nlp/keysentences/api/v1";
    }

}
