package com.dr.archive.common.statistics.service.impl;

import com.dr.archive.common.statistics.entity.ExportEntity;
import com.dr.archive.common.statistics.entity.ReportForm;
import com.dr.archive.common.statistics.service.ExportService;
import com.dr.archive.common.statistics.service.StatisticsService;
import com.dr.archive.util.JsonUtils;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.file.autoconfig.CommonFileConfig;
import com.dr.framework.common.service.CommonService;
import com.dr.framework.core.security.SecurityHolder;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Service("fltj")
public class ExportFltjerviceImpl implements ExportService {

    @Autowired
    StatisticsService statisticsService;

    @Autowired
    CommonFileConfig commonFileConfig;

    @Autowired
    CommonMapper commonMapper;

    @Override
    public String exportByType(ExportEntity exportEntity) {


        List<Map> maps = null;
        try {
            maps = JsonUtils.jsonToList(exportEntity.getData(), Map.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        List<Map> list = ergodicMap(maps);

        String path = "";

        try {
            path = exportExcel(list);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ReportForm reportForm = new ReportForm();

        reportForm.setType("fltj");
        reportForm.setOrgId(SecurityHolder.get().currentOrganise().getId());
        reportForm.setVintages(Calendar.getInstance().get(Calendar.YEAR) + "");
        reportForm.setExportUser(SecurityHolder.get().currentPerson().getUserName());
        reportForm.setDownloadPath(path);
        CommonService.bindCreateInfo(reportForm);
        commonMapper.insert(reportForm);

        return path;
    }


    public List<Map> ergodicMap(List<Map> maps) {

        List<Map> list = new ArrayList<>();

        for (Map map : maps) {
            list.add(map);
            if (map.containsKey("children")) {
                list.addAll(ergodicMap((List<Map>) map.get("children")));
            }
        }

        return list;
    }

    public String exportExcel(List<Map> resource) throws IOException {

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();

        //头字体样式
        HSSFFont headFont = workbook.createFont();
        headFont.setFontName("宋体");
        headFont.setFontHeightInPoints((short) 16);

        //列头
        HSSFCellStyle headStyle = workbook.createCellStyle();
        headStyle.setFont(headFont);
        //headStyle.setFillBackgroundColor(IndexedColors.YELLOW.getIndex());
        //新版本FillPatternType
        headStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headStyle.setAlignment(HorizontalAlignment.CENTER);
        headStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        //数字字体样式
        HSSFFont dataFont = workbook.createFont();
        dataFont.setFontName("宋体");
        dataFont.setFontHeightInPoints((short) 16);

        //数据样式
        HSSFCellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setFont(dataFont);
        dataStyle.setAlignment(HorizontalAlignment.LEFT);
        dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        HSSFRow headRow = null;

        String[] headName = {"序号", "分类", "文件数量", "案卷数量", "原文数量"};

        //===========表头操作=====================
        for (int i = 0; i < headName.length; i++) {
            //设置宽度
            sheet.setColumnWidth(i, 4000);
            if (i == 0) {
                //创建一行表头
                headRow = sheet.createRow(i);
                headRow.setHeight((short) 800);
            }
            //创建单元格数据
            HSSFCell cell = headRow.createCell(i);
            //给单元格赋值
            cell.setCellValue(headName[i]);
        }


        String exp = commonFileConfig.getUploadDirWithDate("exp");

        File file = new File(exp);
        File file1 = fileCheck(file);
        FileOutputStream fileOutputStream = new FileOutputStream(file1);

        for (int i = 0; i < resource.size(); i++) {
            HSSFRow row = sheet.createRow(i + 1);

            Map map = resource.get(i);

            HSSFCell cell0 = row.createCell(0);
            cell0.setCellStyle(dataStyle);
            cell0.setCellValue(i + 1);

            HSSFCell cell = row.createCell(1);
            cell.setCellStyle(dataStyle);
            cell.setCellValue(map.get("name").toString());

            HSSFCell cell2 = row.createCell(2);
            cell2.setCellStyle(dataStyle);
            cell2.setCellValue(map.get("wj").toString());

            HSSFCell cell3 = row.createCell(3);
            cell3.setCellStyle(dataStyle);
            cell3.setCellValue(map.get("aj").toString());

            HSSFCell cell4 = row.createCell(4);
            cell4.setCellStyle(dataStyle);
            cell4.setCellValue(map.get("yw").toString());

        }
        workbook.write(fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
        workbook.close();

        return commonFileConfig.getUploadDownLoadPath(file1.getPath());
    }

    public File fileCheck(File file) throws IOException {
        if (!file.getPath().endsWith(".xls")) {
            if (!file.exists()) {
                file.mkdirs();
            }
            String fileName = "分类统计—" + System.currentTimeMillis() + ".xls";
            file = new File(file.getPath(), fileName);
            file.createNewFile();
        } else if (!file.exists()) {
            //是文件且路径不对时直接返回异常信息
            throw new IllegalStateException("路径异常");
        }
        return file;
    }
}
