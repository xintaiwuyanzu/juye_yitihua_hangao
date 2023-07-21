package com.dr.archive.fuzhou.ofd.vo;

import java.util.List;

/**
 * @Title: SimpleOFDSignatureInfo
 * @Tip 该声明为普通印章所需的参数-如后期骑缝章等等-则需要在本协议类中增加type等参数
 * @Description OFD文件签章协议类定义
 * @Author zhaoJing
 * @Date Create on 2020/11/18 16:41
 */
public class SimpleOFDStampInfo {

    /* 该文件的查询依据 */
    private String fileId;
    /* 印章内容 */
    private List<SimpleOFDStampInfoDetail> stamps;

    public SimpleOFDStampInfo() {
    }

    public SimpleOFDStampInfo(String fileId, List<SimpleOFDStampInfoDetail> stamps) {
        this.fileId = fileId;
        this.stamps = stamps;
    }

    @Override
    public String toString() {
        return "SimpleOFDStampInfo{" +
                "fileId='" + fileId + '\'' +
                ", stamps=" + stamps +
                '}';
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public List<SimpleOFDStampInfoDetail> getStamps() {
        return stamps;
    }

    public void setStamps(List<SimpleOFDStampInfoDetail> stamps) {
        this.stamps = stamps;
    }
}
