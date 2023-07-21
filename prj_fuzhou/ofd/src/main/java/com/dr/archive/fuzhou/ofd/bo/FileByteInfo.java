package com.dr.archive.fuzhou.ofd.bo;

import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Map;

/**
 * 文件流基本信息
 * 这个是接口请求参数
 *
 * @author dr
 */
public class FileByteInfo {
    /**
     * 是否
     * 返回转换后的附件下载地址
     */
    public static final String PARAM_KEY_DOWNLOAD = "isDownload";
    /**
     * 开启office2x可用; 0表示保持原样,1表示接受修订,默认为0
     */
    public static final String PARAM_OFFICE2X_KEY_ACCEPT_REV = "acceptRev";

    /**
     * 纸张类型 默认值 4
     * 0:A0, 1:A1, 2:A2, 3:A3, 4:A4, 5:A5, 6:A6, 7:A7, 8:A8, 9:A9, 10:B0,
     * 11:B1, 12:B2, 13:B3, 14:B4, 15:B5, 16:B6, 17:B7, 18:B8, 19:B9, 20:B10,
     * 21:C5E, 22:Comm10E, 23:DLE, 24:Executive, 25:Folio, 26:Ledger, 27:Legal, 28:Letter, 29:Tabloid, 30:Custom
     */
    public static final String PARAM_HTML_KEY_PAGE_TYPE = "pageType";
    /**
     * 默认值为 0
     */
    public static final String PARAM_HTML_KEY_WIDTH = "width";
    /**
     * 是否带邮件头, 默认false
     */
    public static final String PARAM_EMAIL_KEY_HIDE_HEADERS = "hideHeaders";
    /**
     * 是否提取附件, 默认false; 返回为zip类型文件包
     */
    public static final String PARAM_EMAIL_KEY_EXTRACT_ATTACHMENTS = "extractAttachments";
    /**
     * 这是只是辅助用的，真用媒体类型检测应该返回zip类型
     */
    public static final MediaType OFD = new MediaType("application", "ofd");
    /**
     * byte二进制
     */
    private byte[] bytes;
    /**
     * 转换文件格式
     */
    private String sourceType;
    /**
     * 生成文件格式
     */
    private String destType = OFD.getSubtype();
    /**
     * 设置分辨率, 可空,默认180
     */
    private int dpi;
    /**
     * 自定义参数,可空
     */
    private Map<String, Object> paramMap;

    /**
     * 从文件创建对象
     *
     * @param file
     * @return
     */
    public static FileByteInfo fromFile(File file) {
        FileByteInfo info = new FileByteInfo();
        info.sourceType = file.getName().split("\\.")[1];
        try (InputStream is = Files.newInputStream(file.toPath())) {
            info.bytes = StreamUtils.copyToByteArray(is);
        } catch (IOException e) {
            //ignore
        }
        return info;
    }

    /**
     * 根据流创建对象
     *
     * @param stream
     * @param sourceType
     * @return
     * @throws IOException
     */
    public static FileByteInfo fromInputStream(InputStream stream, String sourceType) throws IOException {
        FileByteInfo info = new FileByteInfo();
        info.sourceType = sourceType;
        info.bytes = StreamUtils.copyToByteArray(stream);
        if (stream != null) {
            stream.close();
        }
        return info;
    }


    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getDestType() {
        return destType;
    }

    public void setDestType(String destType) {
        this.destType = destType;
    }

    public int getDpi() {
        return dpi;
    }

    public void setDpi(int dpi) {
        this.dpi = dpi;
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
