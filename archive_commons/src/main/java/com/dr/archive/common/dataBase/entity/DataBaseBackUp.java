package com.dr.archive.common.dataBase.entity;

import com.dr.framework.common.entity.BaseStatusEntity;
import com.dr.framework.core.orm.annotations.Column;
import com.dr.framework.core.orm.annotations.Table;
import com.dr.framework.util.Constants;

/**
 * @author lirs
 * @date 2022/7/26 16:01
 */
@Table(name = Constants.COMMON_TABLE_PREFIX + "DATABASE_BACKUP", comment = "数据库备份表", module = Constants.COMMON_MODULE_NAME)
public class DataBaseBackUp extends BaseStatusEntity<String> {
    /**
     * 备份中
     */
    public static final String STATUS_BACKUPING = "0";
    /**
     * 备份成功
     */
    public static final String STATUS_BACKUP = "1";
    /**
     * 备份失败
     */
    public static final String STATUS_BACKUP_FAIL = "2";
    /**
     * 恢复中
     */
    public static final String STATUS_RECOVERING = "3";
    /**
     * 恢复成功
     */
    public static final String STATUS_RECOVER = "4";
    /**
     * 恢复失败
     */
    public static final String STATUS_RECOVER_FAIL = "5";

    @Column(comment = "备份名称")
    private String backUpName;
    @Column(comment = "备份人名称")
    private String personName;
    @Column(comment = "最后恢复人名称")
    private String recoverPersonName;
    @Column(comment = "服务器ip")
    private String serverIp;
    @Column(comment = "客户端ip")
    private String clientIp;
    @Column(comment = "备份文件地址")
    private String fileLocation;
    @Column(comment = "备份文件名称")
    private String fileName;

    public String getRecoverPersonName() {
        return recoverPersonName;
    }

    public void setRecoverPersonName(String recoverPersonName) {
        this.recoverPersonName = recoverPersonName;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getBackUpName() {
        return backUpName;
    }

    public void setBackUpName(String backUpName) {
        this.backUpName = backUpName;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getFileLocation() {
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
}
