package com.dr.archive.common.filereading.service.impl;

import com.dr.archive.common.filereading.entity.BackupRecovery;
import com.dr.archive.common.filereading.entity.BackupRecoveryInfo;
import com.dr.archive.common.filereading.mapper.SelectMapper;
import com.dr.archive.common.filereading.service.BackupRecoveryService;
import com.dr.archive.manage.form.service.ArchiveDataManager;
import com.dr.archive.manage.form.service.ArchiveFormDefinitionService;
import com.dr.archive.model.query.ArchiveDataQuery;
import com.dr.framework.common.file.autoconfig.CommonFileConfig;
import com.dr.framework.common.file.model.FileInfo;
import com.dr.framework.common.file.service.CommonFileService;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.form.core.query.FormDefinitionQuery;
import com.dr.framework.common.form.engine.model.core.FormModel;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import com.dr.framework.util.DateTimeUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * 备份
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BackupRecoveryServiceImpl extends DefaultBaseService<BackupRecovery> implements BackupRecoveryService {
    @Autowired
    SelectMapper selectMapper;
    @Autowired
    ArchiveDataManager archiveDataManager;
    @Autowired
    ArchiveFormDefinitionService archiveFormService;
    @Autowired
    CommonFileConfig commonFileConfig;
    @Autowired
    CommonFileService commonFileService;
    final Logger logger = LoggerFactory.getLogger(BackupRecoveryService.class);
    @Value("${sys.name}")
    private String sysName;


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

    /**
     * 发送备份
     *
     * @param table
     * @return
     */
    @Override
    @Async
    public BackupRecovery backup(String table) {

        //        //获取目前最大版本号
        SqlQuery<BackupRecovery> max = SqlQuery.from(BackupRecovery.class, false)
                .max(BackupRecoveryInfo.VERSIONNUM)
                .equal(BackupRecoveryInfo.DOTYPE, BackupRecovery.DOTYPE_SEND);
        long count = 0;
        try {
            count = count(max);
        } catch (Exception ignored) {
        }

        String fileName = DateTimeUtils.dateToString(new Date(), "yyyyMMddHHmmss") + "-" + (count + 1) + ".xlsx";
        String banPath = commonFileConfig.getFullDirPath("upload", "receive-box-" + sysName, null);
        String exportPath = banPath + File.separator + fileName;

        String tableName = "";

        String[] split = table.split(",");
        for (String s : split) {
            //todo 暂时写死，因为代码可能弃用
            if ("shuiyin".equals(s)) {
                tableName = "WaterMark";
                SqlQuery<BackupRecovery> maxTime = SqlQuery.from(BackupRecovery.class, false)
                        .max(BackupRecoveryInfo.CREATEDATE)
                        .equal(BackupRecoveryInfo.TABLENAME, tableName);
                BackupRecovery one = selectOne(maxTime);
            } else if ("guanliku".equals(s)) {
                //todo 这里咋拼sql啊，实现增量备份
                ArchiveDataQuery queryItem = new ArchiveDataQuery();
                //todo 设置全宗id 先写死
                queryItem.setFondId("c3a18928-be44-4cbd-89aa-68ec3858d19e");
                List<FormData> list = archiveDataManager.findDataByQuery(queryItem);
                try {
                    listtoExcel(Arrays.asList(list.toArray()), banPath, fileName, tableName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {

            }
        }


        BackupRecovery backupRecovery = new BackupRecovery();
        //添加到备份记录表
        backupRecovery.setBackupRecoveryPath(exportPath);
//        backupRecovery.setShowPath(exportPath.replace("/home/data/files/upload", ""));
        backupRecovery.setShowPath(banPath);
        Person person = SecurityHolder.get().currentPerson();
        backupRecovery.setCreatePersonName(person.getUserName());
        backupRecovery.setSysName(sysName);
        backupRecovery.setDoType(BackupRecovery.DOTYPE_SEND);
        backupRecovery.setVersionNum(count + 1);
        backupRecovery.setTableName(tableName);
        insert(backupRecovery);
        return backupRecovery;
    }

    @Override
    @Async
    public BackupRecovery recovery(String filePath, String showPath, String versionNum, String sysName) {

        String tableName = excelToList(filePath);

        BackupRecovery backupRecovery = new BackupRecovery();
        //添加到备份记录表
        backupRecovery.setBackupRecoveryPath(filePath);
        backupRecovery.setShowPath(showPath);
        Person person = SecurityHolder.get().currentPerson();
        backupRecovery.setCreatePersonName(person.getUserName());
        backupRecovery.setSysName(sysName);
        backupRecovery.setDoType(BackupRecovery.DOTYPE_RECEIVE);
        backupRecovery.setVersionNum(Long.parseLong(versionNum));
        backupRecovery.setTableName(tableName);
        insert(backupRecovery);
        return backupRecovery;
    }


    @Override
    public List<BackupRecovery> check(String sysName) {
        String dir = commonFileConfig.getFullDirPath("upload", "receive-box-" + sysName, null);
        List<BackupRecovery> file = getFile(dir, sysName);
        return file;
    }


    @Override
    public void wjbackup() {

        //1.现在是智网的长期保存备份  首先把长期保存库导出的zip文件备份到 同步交换区的智网文件夹下
        String tempPath = commonFileConfig.getFullDirPath("upload", "temp", null);
        final File file = new File(tempPath);
        final List<String> allZip = getAllZip(file);
        FormDefinitionQuery formDefinitionQuery = new FormDefinitionQuery();
        List<? extends FormModel> formList = archiveFormService.findFormList(formDefinitionQuery);
        for (FormModel formModel : formList) {
            List<Map> list = selectMapper.getIdByTableName("f_" + formModel.getFormCode());
            for (Map map : list) {
                String id = (String) map.get("id");
                System.out.println(id);

                try {
                    FileInfo fileInfo = commonFileService.firstFile(id, "archive");

                    if (fileInfo != null) {
                        InputStream in = commonFileService.fileStream(fileInfo.getId());
                        String banPath = commonFileConfig.getFullDirPath("upload", "receive-file-" + sysName, null);
                        addFileToPath(in, banPath + "/receive-file-" + sysName + "/" + fileInfo.getName());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void addFileToPath(InputStream in, String namepath) throws IOException {
        Files.copy(in, Paths.get(namepath));
    }


    /*
     * 函数名：getFile
     * 作用：使用递归，输出指定文件夹内的所有文件
     * 参数：path：文件夹路径   deep：表示文件的层次深度，控制前置空格的个数
     * 前置空格缩进，显示文件层次结构
     */
    private static List<BackupRecovery> getFile(String path, String sysName) {
        List<BackupRecovery> strings = new ArrayList<>();
        // 获得指定文件对象
        File file = new File(path);
        // 获得该文件夹内的所有文件
        File[] array = file.listFiles();

        for (File value : array) {
            if (value.isFile())//如果是文件
            {// 只输出文件名字
                BackupRecovery recovery = new BackupRecovery();
                String name = value.getName();
                String v = name.split("-")[1].split("\\.")[0];
                recovery.setSysName(sysName);
                recovery.setBackupRecoveryPath(path + "\\" + name);
                recovery.setShowPath(path);
                recovery.setVersionNum(Long.parseLong(v));
                recovery.setCreateDate(value.lastModified());

                strings.add(recovery);
            }
        }
        return strings;
    }

    /**
     * @param stuList 从数据库中查询需要导入excel文件的信息列表
     * @return 返回生成的excel文件的路径
     * @throws Exception
     */
    public String listtoExcel(List<Object> stuList, String folderPath, String name, String tableName) throws Exception {

        Workbook wb = new XSSFWorkbook();

        Field[] fields = stuList.get(0).getClass().getDeclaredFields();
        String[] title = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            title[i] = fields[i].getName();
        }


        //标题行抽出字段
//        String[] title = {"序号", "学号", "姓名", "性别", "入学时间", "住址", "手机号", "其他信息"};
        //设置sheet名称，并创建新的sheet对象
        Sheet stuSheet = wb.createSheet(tableName);
        //获取表头行
        Row titleRow = stuSheet.createRow(0);
        //创建单元格，设置style居中，字体，单元格大小等
        CellStyle style = wb.createCellStyle();
        Cell cell = null;
        //把已经写好的标题行写入excel文件中
        for (int i = 0; i < title.length; i++) {
            cell = titleRow.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(style);
        }
        //把从数据库中取得的数据一一写入excel文件中
        Row row = null;
        for (int i = 0; i < stuList.size(); i++) {
            row = stuSheet.createRow(i + 1);
            Field[] fields1 = stuList.get(i).getClass().getDeclaredFields();
            for (int j = 0; j < fields1.length; j++) {
                fields1[j].setAccessible(true);
                Object o = fields1[j].get(stuList.get(i));
                if (o == null) {
                    row.createCell(j).setCellValue("");
                } else {
                    row.createCell(j).setCellValue(o.toString());
                }

            }


        }
        //设置单元格宽度自适应，在此基础上把宽度调至1.5倍
        for (int i = 0; i < title.length; i++) {
            stuSheet.autoSizeColumn(i, true);
            stuSheet.setColumnWidth(i, stuSheet.getColumnWidth(i) * 15 / 10);
        }

        //创建上传文件目录
        File folder = new File(folderPath);
        //如果文件夹不存在创建对应的文件夹
        if (!folder.exists()) {
            folder.mkdirs();
        }
        //设置文件名
        String savePath = folderPath + File.separator + name;

        OutputStream fileOut = Files.newOutputStream(Paths.get(savePath));
        wb.write(fileOut);
        fileOut.close();
        //返回文件保存全路径
        return savePath;
    }

    public String excelToList(String filePath) {

        File file = new File(filePath);
        XSSFWorkbook wb = null;
        try {
            wb = new XSSFWorkbook(Files.newInputStream(file.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        XSSFSheet sheet = wb.getSheetAt(0);

        String sheetName = sheet.getSheetName();

        String tabelName = "";

        if ("WaterMark".equals(sheetName)) {
            tabelName = "WaterMark";

            //循环Row
            for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
                Row row = sheet.getRow(rowNum);
                if (row == null) {
                    continue;
                }
                //TODO 这里是啥逻辑？
              /*  //水印表
                WaterMark waterMark = new WaterMark();
                //1
                Cell cell1 = row.getCell(0);
                if (cell1 == null) {
                    continue;
                }
                waterMark.setOrganiseCode(cell1.getStringCellValue());
                //2
                Cell cell2 = row.getCell(1);
                if (cell2 == null) {
                    continue;
                }
                waterMark.setDataObjectId(cell2.getStringCellValue());
                //3
                Cell cell3 = row.getCell(2);
                if (cell3 == null) {
                    continue;
                }
                waterMark.setTitle(cell3.getStringCellValue());
                // 4
                Cell cell4 = row.getCell(3);
                if (cell4 == null) {
                    continue;
                }
                waterMark.setColorGray(cell4.getStringCellValue());

                //5
                Cell cell5 = row.getCell(4);
                if (cell5 == null) {
                    continue;
                }
                waterMark.setColor(cell5.getStringCellValue());

                //6
                Cell cell6 = row.getCell(5);
                if (cell6 == null) {
                    continue;
                }
                waterMark.setFontSize(cell6.getStringCellValue());

                waterMark.setId(CommonTools.getUUID());
                //水印新增 这里需要
                waterMarkService.insert(waterMark);*/
            }


        }
        return tabelName;
    }


    List<String> zipFileList = new ArrayList<>();

    public List<String> getAllZip(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                String fileName = file.getName().toUpperCase();
                // 只检测图片
                if (fileName.contains(".ZIP")) {
                    zipFileList.add(file.getAbsolutePath());
                }
            } else {
                File[] list = file.listFiles();
                if (list.length != 0) {
                    for (File value : list) {
                        getAllZip(value);
                    }
                }
            }
        } else {
            System.out.println("文件不存在！");
        }
        return zipFileList;
    }


}
