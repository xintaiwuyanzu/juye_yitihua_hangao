package com.dr.archive.common.exportUser;

import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.organise.query.PersonQuery;
import com.dr.framework.core.organise.service.OrganisePersonService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/api/impexptable")
public class ExportUserController extends HttpServlet {

    @Autowired
    OrganisePersonService organisePersonService;


    /**
     * 导出人员信息
     */
    @RequestMapping("/exportpersonnel")
    public void expAll(HttpServletResponse response, Person person) {
        PersonQuery personQuery = (PersonQuery) (new PersonQuery.Builder()).nameLike(person.getUserName()).typeLike(person.getPersonType()).userCodeLike(person.getUserCode()).statusEqual(new String[]{(String) person.getStatus()}).organiseIdEqual(new String[]{person.getDefaultOrganiseId()}).build();
        List<Person> personList = organisePersonService.getPersonList(personQuery);
        HSSFWorkbook wb = new HSSFWorkbook();

        HSSFSheet sheet = wb.createSheet("导出人员");
        sheet.setDefaultColumnWidth(30);
        sheet.setDefaultRowHeightInPoints(30);

        // 设置表头样式
        // 创建字体对象并设置字体名称和粗细
        HSSFFont fontHeader = wb.createFont();
        fontHeader.setFontName("微软雅黑");
        fontHeader.setBold(true); // 将字体加粗
        // 创建单元格样式并将字体对象应用到样式中
        HSSFCellStyle styleHeader = wb.createCellStyle();
        styleHeader.setFont(fontHeader);
        styleHeader.setAlignment(HorizontalAlignment.CENTER);
        styleHeader.setVerticalAlignment(VerticalAlignment.CENTER);
        // 设置填充模式和前景色为浅蓝色
        styleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleHeader.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());

        //设置内容样式
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
        cell.setCellStyle(styleHeader);
        cell.setCellValue("用户姓名");
        cell = row.createCell(1);
        cell.setCellStyle(styleHeader);
        cell.setCellValue("用户编号");
        cell = row.createCell(2);
        cell.setCellStyle(styleHeader);
        cell.setCellValue("手机号");
        cell = row.createCell(3);
        cell.setCellStyle(styleHeader);
        cell.setCellValue("邮箱");
        cell = row.createCell(4);
        cell.setCellStyle(styleHeader);
        cell.setCellValue("性别");

        for (int i = 0; i < personList.size(); i++) {
            if ("1".equals(personList.get(i).getStatus())) {
                row = sheet.createRow(i + 1);
                cell = row.createCell(0);
                cell.setCellStyle(style);
                cell.setCellValue(personList.get(i).getUserName());
                cell = row.createCell(1);
                cell.setCellStyle(style);
                cell.setCellValue(personList.get(i).getUserCode());
                cell = row.createCell(2);
                cell.setCellStyle(style);
                cell.setCellValue(personList.get(i).getMobile() == null ? "" : String.valueOf(personList.get(i).getMobile()));
                cell = row.createCell(3);
                cell.setCellStyle(style);
                cell.setCellValue(personList.get(i).getEmail() == null ? "" : String.valueOf(personList.get(i).getEmail()));
                cell = row.createCell(4);
                cell.setCellStyle(style);
                cell.setCellValue((personList.get(i).getSex() != null) ?
                        (personList.get(i).getSex() == 1 ?
                                "男"
                                :
                                (personList.get(i).getSex() == 0 ?
                                        "女"
                                        :
                                        String.valueOf(personList.get(i).getSex())))
                        :
                        "");
            }
        }
        ByteArrayOutputStream outBtye = new ByteArrayOutputStream();
        try {
            wb.write(outBtye);
            byte[] byteArray = outBtye.toByteArray();
            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode("导出人员.xls", "UTF-8"));
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(byteArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
