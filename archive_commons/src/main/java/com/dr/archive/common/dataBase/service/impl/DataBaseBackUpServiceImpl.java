package com.dr.archive.common.dataBase.service.impl;

import com.dr.archive.common.dataBase.config.MysqlConfig;
import com.dr.archive.common.dataBase.entity.DataBaseBackUp;
import com.dr.archive.common.dataBase.entity.DataBaseBackUpInfo;
import com.dr.archive.common.dataBase.service.DataBaseBackUpService;
import com.dr.archive.manage.log.annotation.SysLog;
import com.dr.archive.manage.log.service.SysLogService;
import com.dr.archive.util.DateTimeUtils;
import com.dr.archive.util.UUIDUtils;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * @author lirs
 * @date 2022/7/25 14:59
 */
@Service
public class DataBaseBackUpServiceImpl extends DefaultBaseService<DataBaseBackUp> implements DataBaseBackUpService {
    @Autowired
    CommonMapper commonMapper;
    @Autowired
    SysLogService sysLogService;
    @Autowired
    MysqlConfig mysqlConfig;

    private static boolean lock = false;

    private static final Logger log = LoggerFactory.getLogger(DataBaseBackUpServiceImpl.class);

    /**
     * 数据库备份
     *
     * @param request
     * @param person
     * @param dataBaseBackUp
     * @return
     */
    @Override
    @SysLog("备份数据库操作")
    public void dbBackUp(HttpServletRequest request, Person person, DataBaseBackUp dataBaseBackUp) {
        Assert.isTrue(!lock, "正在进行备份或恢复操作，请稍后重试！");
        //获取服务器ip
        String localAddr = request.getLocalAddr();
        //获取客户端ip
        String remoteHost = request.getRemoteHost();
        String fileName = mysqlConfig.getDataBaseName() + "_" + DateTimeUtils.dateToString(new Date(), "yyyy-MM-dd-HH-mm-ss") + ".sql";
        dataBaseBackUp.setFileName(fileName);
        //备份中
        saveBackUpRecord(localAddr, remoteHost, dataBaseBackUp, person, DataBaseBackUp.STATUS_BACKUPING);
        File saveFile = new File(savePath(dataBaseBackUp.getFileLocation()));
        if (!saveFile.exists()) {// 如果目录不存在
            saveFile.mkdirs();// 创建文件夹
        }
        String[] command = {"cmd", "/c", getBackUpCommand(dataBaseBackUp)};
        new Thread(() -> {
            lock = true;
            try {
                Process process = Runtime.getRuntime().exec(command);
                int i = process.waitFor();
                if (i == 0) {// 0 表示线程正常终止。
                    log.info("数据库备份成功");
                    SqlQuery<DataBaseBackUp> query = SqlQuery.from(DataBaseBackUp.class)
                            .set(DataBaseBackUpInfo.STATUS, DataBaseBackUp.STATUS_BACKUP)
                            .equal(DataBaseBackUpInfo.ID, dataBaseBackUp.getId());
                    commonMapper.updateByQuery(query);
                } else {
                    log.info("数据库备份失败");
                    SqlQuery<DataBaseBackUp> query = SqlQuery.from(DataBaseBackUp.class)
                            .set(DataBaseBackUpInfo.STATUS, DataBaseBackUp.STATUS_BACKUP_FAIL)
                            .equal(DataBaseBackUpInfo.ID, dataBaseBackUp.getId());
                    commonMapper.updateByQuery(query);
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                log.info("数据库备份失败");
                SqlQuery<DataBaseBackUp> query = SqlQuery.from(DataBaseBackUp.class)
                        .set(DataBaseBackUpInfo.STATUS, DataBaseBackUp.STATUS_BACKUP_FAIL)
                        .equal(DataBaseBackUpInfo.ID, dataBaseBackUp.getId());
                commonMapper.updateByQuery(query);
            } finally {
                lock = false;
            }
        }).start();
    }

    /**
     * 数据库恢复
     *
     * @param person
     * @param dataBaseBackUp
     * @return
     */
    @Override
    @SysLog("恢复数据库操作")
    public void dbRecover(Person person, DataBaseBackUp dataBaseBackUp) {
        Assert.isTrue(!lock, "正在进行备份或恢复操作，请稍后重试！");
        DataBaseBackUp dataBaseBackUp1 = commonMapper.selectById(DataBaseBackUp.class, dataBaseBackUp.getId());
        dataBaseBackUp1.setStatus(DataBaseBackUp.STATUS_RECOVERING);
        dataBaseBackUp1.setRecoverPersonName(person.getUserName());
        updateById(dataBaseBackUp1);
        String[] command = {"cmd", "/c", getRecoverCommand(dataBaseBackUp1)};
        new Thread(() -> {
            lock = true;
            try {
                Process process = Runtime.getRuntime().exec(command);
                int i = process.waitFor();
                if (i == 0) {// 0 表示线程正常终止。
                    log.info("数据库恢复成功");
                    dataBaseBackUp1.setStatus(DataBaseBackUp.STATUS_RECOVER);
                    commonMapper.updateById(dataBaseBackUp1);
                } else {
                    log.info("数据库恢复失败");
                    SqlQuery<DataBaseBackUp> query = SqlQuery.from(DataBaseBackUp.class)
                            .set(DataBaseBackUpInfo.STATUS, DataBaseBackUp.STATUS_RECOVER_FAIL)
                            .equal(DataBaseBackUpInfo.ID, dataBaseBackUp.getId());
                    commonMapper.updateByQuery(query);
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                log.info("数据库恢复失败");
                SqlQuery<DataBaseBackUp> query = SqlQuery.from(DataBaseBackUp.class)
                        .set(DataBaseBackUpInfo.STATUS, DataBaseBackUp.STATUS_RECOVER_FAIL)
                        .equal(DataBaseBackUpInfo.ID, dataBaseBackUp.getId());
                commonMapper.updateByQuery(query);
            } finally {
                lock = false;
            }
        }).start();
    }

    /**
     * 保存备份记录
     *
     * @param localAddr      服务器ip
     * @param remoteHost     客户端ip
     * @param dataBaseBackUp
     * @param person
     */
    private void saveBackUpRecord(String localAddr, String remoteHost, DataBaseBackUp dataBaseBackUp, Person person, String status) {
        dataBaseBackUp.setId(UUIDUtils.getUUID());
        dataBaseBackUp.setCreateDate(System.currentTimeMillis());
        dataBaseBackUp.setCreatePerson(person.getId());
        dataBaseBackUp.setPersonName(person.getUserName());
        dataBaseBackUp.setStatus(status);
        dataBaseBackUp.setServerIp(localAddr);
        dataBaseBackUp.setClientIp(remoteHost);
        dataBaseBackUp.setFileLocation(savePath(dataBaseBackUp.getFileLocation()));
        commonMapper.insert(dataBaseBackUp);
    }

    private String getBackUpCommand(DataBaseBackUp dataBaseBackUp) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(mysqlConfig.getCommandUrl()).append("\\").append("mysqldump ").append("--defaults-extra-file=")
                .append(mysqlConfig.getMyIniUrl()).append(" ").append(mysqlConfig.getDataBaseName()).append(" > ")
                .append(savePath(dataBaseBackUp.getFileLocation())).append("\\").append(dataBaseBackUp.getFileName());
        log.info(stringBuilder.toString());
        return stringBuilder.toString();
    }

    private String getRecoverCommand(DataBaseBackUp dataBaseBackUp) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(mysqlConfig.getCommandUrl()).append("\\").append("mysql ").append("--defaults-extra-file=")
                .append(mysqlConfig.getMyIniUrl()).append(" ").append(mysqlConfig.getDataBaseName()).append(" < ")
                .append(dataBaseBackUp.getFileLocation()).append("\\").append(dataBaseBackUp.getFileName());
        log.info(stringBuilder.toString());
        return stringBuilder.toString();
    }

    /**
     * 构建存储sql脚本的路径
     *
     * @return
     */
    private String savePath(String fileLocation) {
        String savePath;
        if (StringUtils.hasText(fileLocation)) {
            return fileLocation;
        }
        if (isWindows()) {
            savePath = mysqlConfig.getFileUrlWin();
        } else {
            savePath = mysqlConfig.getFileUrlLinux();
        }
        return savePath;
    }

    /**
     * 判断服务所在系统为什么操作系统
     *
     * @return
     */
    private boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }
}
