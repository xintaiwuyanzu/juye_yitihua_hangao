package com.dr.archive.fuzhou.ofd.bo;

/**
 * 数据流转换结果
 *
 * @author dr
 */
public class FileStreamResult {
    /**
     * byte base64编码之后的string
     */
    private String bytes;
    /**
     * 0 成功; -11:sourceType参数为空; -12:destType参数
     * 为空; -13:文件流为空; -1:转换文件失败; -9:不支持的转换
     */
    private String convertStatus;
    /**
     * 返回文件格式 ofd、zip、pdfa
     */
    private String type;

    public String getBytes() {
        return bytes;
    }

    public void setBytes(String bytes) {
        this.bytes = bytes;
    }

    public String getConvertStatus() {
        return convertStatus;
    }

    public void setConvertStatus(String convertStatus) {
        this.convertStatus = convertStatus;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
