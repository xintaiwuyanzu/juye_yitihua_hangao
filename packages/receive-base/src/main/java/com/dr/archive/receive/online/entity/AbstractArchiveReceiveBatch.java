package com.dr.archive.receive.online.entity;

import com.dr.archive.batch.entity.AbstractArchiveBatch;
import com.dr.framework.core.orm.annotations.Column;

/**
 * 接收导出批次表抽象父类
 *
 * @author dr
 */
public class AbstractArchiveReceiveBatch extends AbstractArchiveBatch {
    /**
     * 导入使用
     */
    @Column(comment = "移交单位", length = 500)
    private String transferUnit;
    @Column(comment = "移交单位负责人", length = 100)
    private String transferUnitPerson;

/*    @Column(comment = "文件存储位置", length = 500)
    private String fileLocation;
    @Column(comment = "文件名称", length = 500)
    private String fileName;
    @Column(comment = "文件类型", length = 100)
    private String mineType;*/

    public String getTransferUnit() {
        return transferUnit;
    }

    public void setTransferUnit(String transferUnit) {
        this.transferUnit = transferUnit;
    }

    public String getTransferUnitPerson() {
        return transferUnitPerson;
    }

    public void setTransferUnitPerson(String transferUnitPerson) {
        this.transferUnitPerson = transferUnitPerson;
    }

/*    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMineType() {
        return mineType;
    }

    public void setMineType(String mineType) {
        this.mineType = mineType;
    }*/
}
