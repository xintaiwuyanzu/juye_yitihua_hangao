package com.dr.archive.fuzhou.ofd.bo;

import com.dr.archive.util.Constants;
import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;

/**
 * @author: yang
 * @create: 2022-05-17 15:14
 **/
@Table(name = Constants.TABLE_PREFIX + "OFD_Transform_Record", module = Constants.MODULE_NAME, comment = "Ofd文件转换记录")
public class OfdTransformRecord extends BaseStatusEntity<String> {
    @Column(comment = "文件fileUid")
    private String fileUid;
    @Column(comment = "文件名称")
    private String fileName;
    @Column(name = "fileSize", comment = "文件大小")
    private long fileSize;
    @Column(comment = "源文件类型")
    private String fromType;
    @Column(comment = "转换后文件类型")
    private String toType = "ofd";

    public OfdTransformRecord() {
    }

    public OfdTransformRecord(String fileUid, String fileName, long fileSize, String fromType) {
        this.fileUid = fileUid;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fromType = fromType;
    }

    public String getFileUid() {
        return fileUid;
    }

    public void setFileUid(String fileUid) {
        this.fileUid = fileUid;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFromType() {
        return fromType;
    }

    public void setFromType(String fromType) {
        this.fromType = fromType;
    }

    public String getToType() {
        return toType;
    }

    public void setToType(String toType) {
        this.toType = toType;
    }

}
