package com.dr.archive.common.filekeeping.service.impl;

import com.dr.archive.common.filekeeping.entity.BackupRecovery;
import com.dr.archive.common.filekeeping.service.BackupRecoveryService;
import com.dr.archive.common.utils.CommonTools;
import com.dr.framework.common.file.autoconfig.CommonFileConfig;
import com.dr.framework.common.form.core.service.FormDefinitionService;
import com.dr.framework.common.service.DataBaseService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.database.DataBaseMetaData;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import com.dr.framework.util.DateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

/**
 * @author caor
 * @Date 2020-11-04 9:51
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BackupRecoveryServiceSqlImpl extends DefaultBaseService<BackupRecovery> implements BackupRecoveryService {
    @Autowired
    FormDefinitionService formDefinitionService;
    @Autowired
    CommonFileConfig commonFileConfig;
    final Logger logger = LoggerFactory.getLogger(BackupRecoveryService.class);
    @Autowired
    DataBaseService dataBaseService;

    @Override
    @Async
    public BackupRecovery backup(BackupRecovery backupRecovery) {
        //可能有多个数据源！！！
        List<DataBaseMetaData> databaseMetaDataList = dataBaseService.getAllDatabases();
        DataBaseMetaData dataBaseMetaData;
        if (databaseMetaDataList.size() == 1) {
            dataBaseMetaData = databaseMetaDataList.get(0);
        } else {
            return null;
        }
        String table = DateTimeUtils.dateToString(new Date(), "yyyyMMddHHmmss");
        String exportPath = commonFileConfig.getUploadDirWithDate("databackup") + "/" + table + ".sql";
        String command;
        String showPath = "";
        if (CommonTools.isOSLinux()) {
            command = String.format(
                    "sh /c mysqldump -h%s -P%s -u%s -p%s %s > %s",
                    "192.168.1.140",
                    "3306",
                    dataBaseMetaData.getUserName(),
                    dataBaseMetaData.getPassword(),
                    dataBaseMetaData.getCatalog(),
                    exportPath);
        } else {
            command = String.format(
                    "cmd /c mysqldump -h%s -P%s -u%s -p%s %s > %s",
                    //TODO 需要处理host和port为空的问题
//                dataBaseMetaData.getHost(),
//                dataBaseMetaData.getPort(),
                    "localhost",
                    "3306",
                    dataBaseMetaData.getUserName(),
                    dataBaseMetaData.getPassword(),
                    dataBaseMetaData.getCatalog(),
                    exportPath
            );
        }
        logger.info("执行数据库备份命令：{}", command);
        //TODO 备份方式有问题
//        if (execCommand(command)) {
        execCommand(command);
        File file = new File(exportPath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        logger.info("数据库备份成功,备份路径为：" + exportPath);
        //添加到备份记录表
        backupRecovery.setBackupRecoveryPath(exportPath);
//        backupRecovery.setShowPath(exportPath.replace("/home/data/files/upload", ""));
        backupRecovery.setShowPath(commonFileConfig.getUploadDownLoadPath(exportPath));
        Person person = SecurityHolder.get().currentPerson();
        backupRecovery.setCreatePersonName(person.getUserName());
        insert(backupRecovery);
//        }
        return backupRecovery;
    }

    @Override
    @Async
    public void recovery(String id) {
        BackupRecovery backupRecovery = commonMapper.selectById(BackupRecovery.class, id);
        List<DataBaseMetaData> databaseMetaDataList = dataBaseService.getAllDatabases();
        DataBaseMetaData dataBaseMetaData;
        if (databaseMetaDataList.size() == 1) {
            dataBaseMetaData = databaseMetaDataList.get(0);
        } else {
            return;
        }
        String command;
        if (CommonTools.isOSLinux()) {
            command = String.format(
                    "sh /c mysql -h%s -P%s -u%s -p%s linshi < %s",
                    "192.168.1.140",
                    "3306",
                    dataBaseMetaData.getUserName(),
                    dataBaseMetaData.getPassword(),
                    backupRecovery.getBackupRecoveryPath());
        } else {
            command = String.format(
                    "cmd /c mysql -h%s -P%s -u%s -p%s linshi < %s",
                    //TODO 需要处理host和port为空的问题
//                dataBaseMetaData.getHost(),
//                dataBaseMetaData.getPort(),
                    "localhost",
                    "3306",
                    dataBaseMetaData.getUserName(),
                    dataBaseMetaData.getPassword(),
                    backupRecovery.getBackupRecoveryPath());
        }

        logger.info("数据库恢复命令：{}", command);
        execCommand(command);
    }

    protected boolean execCommand(String command) {
        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        try {
            process = runtime.exec(command);
            StringBuilder sb = new StringBuilder();
            InputStream in = process.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            in.close();
            return process.waitFor() == 0;
        } catch (Exception e) {
            logger.error("执行命令行失败：{}", command);
            e.printStackTrace();
            return false;
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
    }

    @Override
    public long delete(SqlQuery<BackupRecovery> sqlQuery) {
        BackupRecovery backupRecovery = selectOne(sqlQuery);
        backupRecovery = commonMapper.selectById(BackupRecovery.class, backupRecovery.getId());
        try {
            Files.delete(Paths.get(backupRecovery.getBackupRecoveryPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.delete(sqlQuery);
    }
}
