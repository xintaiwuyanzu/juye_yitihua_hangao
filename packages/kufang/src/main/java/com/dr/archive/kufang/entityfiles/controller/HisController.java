package com.dr.archive.kufang.entityfiles.controller;


import com.dr.archive.kufang.entityfiles.entity.ArchiveType;
import com.dr.archive.kufang.entityfiles.entity.ArchiveTypeInfo;
import com.dr.archive.kufang.entityfiles.entity.InAndOutRecords;
import com.dr.archive.kufang.entityfiles.entity.InAndOutRecordsInfo;
import com.dr.archive.kufang.entityfiles.utils.DateTools;
import com.dr.framework.common.controller.BaseController;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 进出库记录
 */
@RestController
@RequestMapping("api/his")
public class HisController extends BaseController<InAndOutRecords> {

    @Override
    protected void onBeforePageQuery(HttpServletRequest request, SqlQuery<InAndOutRecords> sqlQuery, InAndOutRecords entity) {
        sqlQuery.equal(InAndOutRecordsInfo.DOTYPE, entity.getDoType())
                .greaterThanEqual(InAndOutRecordsInfo.CREATEDATE, entity.getCreateDate())
                .lessThanEqual(InAndOutRecordsInfo.CREATEDATE, entity.getUpdateDate())
                .equal(InAndOutRecordsInfo.ARCHIVECODE, entity.getArchiveCode())
                .equal(InAndOutRecordsInfo.ARCHIVEID, entity.getArchiveId())
                .equal(InAndOutRecordsInfo.ORGANISEID, SecurityHolder.get().currentOrganise().getId())
                .orderByDesc(InAndOutRecordsInfo.CREATEDATE);
    }

    /**
     * 进出库记录导出EXCEL
     *
     * @return
     */
    @RequestMapping("/exp")
    public void getBookCountEpx(HttpServletResponse response, String startTime, String endTime, String doType, String archiveCode) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SqlQuery<InAndOutRecords> sqlQuery = SqlQuery.from(InAndOutRecords.class);

        if (!StringUtils.isBlank(doType)) {
            sqlQuery.equal(InAndOutRecordsInfo.DOTYPE, doType);
        }
        if (!StringUtils.isBlank(archiveCode)) {
            sqlQuery.equal(InAndOutRecordsInfo.ARCHIVECODE, archiveCode);
        }

        if (!com.mysql.cj.util.StringUtils.isNullOrEmpty(startTime) &&
                !com.mysql.cj.util.StringUtils.isNullOrEmpty(endTime) &&
                !"undefined".equals(startTime) && !"undefined".equals(endTime)) {
            try {
                long start = sdf.parse(startTime).getTime();
                long end = sdf.parse(endTime).getTime();
                sqlQuery.between(InAndOutRecordsInfo.CREATEDATE, start, end);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        List<InAndOutRecords> list = commonService.selectList(sqlQuery);

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("进出库记录");
        sheet.setDefaultColumnWidth(30);
        sheet.setDefaultRowHeightInPoints(30);
        HSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short) 10);
        font.setFontName("微软雅黑");

        HSSFCellStyle style = wb.createCellStyle();
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER); // 居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);//居中
        HSSFCell cell;
        HSSFRow row = sheet.createRow(0);
        row = sheet.createRow(0);

        cell = row.createCell(0);
        cell.setCellStyle(style);
        cell.setCellValue("档号");

        cell = row.createCell(1);
        cell.setCellStyle(style);
        cell.setCellValue("档案名称");

        cell = row.createCell(2);
        cell.setCellStyle(style);
        cell.setCellValue("档案类型");

        cell = row.createCell(3);
        cell.setCellStyle(style);
        cell.setCellValue("操作类型");

        cell = row.createCell(4);
        cell.setCellStyle(style);
        cell.setCellValue("操作时间");

        for (int i = 0; i < list.size(); i++) {
            InAndOutRecords records = list.get(i);
            row = sheet.createRow(i + 1);
            cell = row.createCell(0);
            cell.setCellStyle(style);
            cell.setCellValue(records.getArchiveCode());

            cell = row.createCell(1);
            cell.setCellStyle(style);
            cell.setCellValue(records.getTitle());

            cell = row.createCell(2);
            cell.setCellStyle(style);
            cell.setCellValue(commonService.selectOne(SqlQuery.from(ArchiveType.class).equal(ArchiveTypeInfo.ID, records.getArchiveType())).getArchiveTypeName());

            cell = row.createCell(3);
            cell.setCellStyle(style);
            cell.setCellValue("1".equals(records.getDoType()) ? "入库" : "出库");

            cell = row.createCell(4);
            cell.setCellStyle(style);
            cell.setCellValue(DateTools.getDates(records.getCreateDate()));
        }
        ByteArrayOutputStream outBtye = new ByteArrayOutputStream();
        try {
            wb.write(outBtye);
            byte[] byteArray = outBtye.toByteArray();
            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode("进出库记录.xls", "UTF-8"));
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(byteArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
