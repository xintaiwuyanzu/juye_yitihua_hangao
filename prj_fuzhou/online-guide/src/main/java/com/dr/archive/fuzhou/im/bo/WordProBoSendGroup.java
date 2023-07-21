package com.dr.archive.fuzhou.im.bo;

/**
 * 发送群组消息普通版，链接显示会有异常，不用用formview
 */
public class WordProBoSendGroup {

    private String sender;

    private String group_id;

    private String text;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
