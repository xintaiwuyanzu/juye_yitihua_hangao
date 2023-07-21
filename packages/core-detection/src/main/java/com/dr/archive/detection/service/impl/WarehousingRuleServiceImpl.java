package com.dr.archive.detection.service.impl;


import com.dr.archive.detection.entity.WarehousingRule;
import com.dr.archive.detection.service.WarehousingRuleService;
import com.dr.framework.common.service.DefaultBaseService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public class WarehousingRuleServiceImpl extends DefaultBaseService<WarehousingRule> implements WarehousingRuleService {
    @Override
    public String uploadExcel(MultipartFile file) throws Exception{

        if (file == null) {
            throw new Exception();
        }
        String filename = file.getOriginalFilename();
        if (filename == null) {
            throw new Exception();
        }
        String a = "";
        try {
            // 调用解析文件方法
            a = parseRowCell(filename, file.getInputStream());
            return a;
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * 解析文件中的数据
     */
    private String parseRowCell(String filename, InputStream is) {
        try {
            Workbook workbook = null;
            // 判断excel的后缀，不同的后缀用不同的对象去解析
            // xls是低版本的Excel文件
            if (filename.endsWith(".xls")) {

                workbook = new HSSFWorkbook(is);
            }
            // xlsx是高版本的Excel文件
            if (filename.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(is);
            }
            if (workbook == null) {
                throw new Exception();
            }

            // 取到excel 中的第一张工作表
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                throw new Exception();
            }

            // 工作表中第一行是表头，不获取，从第二行开始获取
            for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
                // 获取到这一行的数据
                Row row = sheet.getRow(rowNum);
                if (row == null) {
                    continue;
                }

                /**
                 * 以下的取数据，具体看你的Excel文件中有多少列数据，以此类推
                 */

                // 取到这一行的第一列数据
                String linkNature = "";
                if (row.getCell(0) != null) {
                    row.getCell(0).setCellType(CellType.STRING);
                    linkNature = row.getCell(0).getStringCellValue().trim();
                }

                // 取到这一行的第二列数据
                String testObject = "";
                if (row.getCell(1) != null) {
                    row.getCell(1).setCellType(CellType.STRING);
                    testObject = row.getCell(1).getStringCellValue().trim();
                }

                // 取到这一行的第三列数据
                String ruleNature = "";
                if (row.getCell(2) != null) {
                    row.getCell(2).setCellType(CellType.STRING);
                    ruleNature = row.getCell(2).getStringCellValue().trim();
                }

                // 取到这一行的第四列数据
                String testCode = "";
                if (row.getCell(3) != null) {
                    row.getCell(3).setCellType(CellType.STRING);
                    testCode = row.getCell(3).getStringCellValue().trim();
                }
                String testType = "";
                if (row.getCell(4) != null) {
                    row.getCell(4).setCellType(CellType.STRING);
                    testType = row.getCell(4).getStringCellValue().trim();
                }
                String testPurpose = "";
                if (row.getCell(5) != null) {
                    row.getCell(5).setCellType(CellType.STRING);
                    testPurpose = row.getCell(5).getStringCellValue().trim();
                }
                String testMethod= "";
                if (row.getCell(6) != null) {
                    row.getCell(6).setCellType(CellType.STRING);
                    testMethod = row.getCell(6).getStringCellValue().trim();
                }
                String project = "";
                if (row.getCell(7) != null) {
                    row.getCell(7).setCellType(CellType.STRING);
                    project = row.getCell(7).getStringCellValue().trim();
                }
                String sort = "";
                if (row.getCell(8) != null) {
                    row.getCell(8).setCellType(CellType.STRING);
                    sort = row.getCell(8).getStringCellValue().trim();
                }
                String remark = "";
                if (row.getCell(9) != null) {
                    row.getCell(9).setCellType(CellType.STRING);
                    remark = row.getCell(9).getStringCellValue().trim();
                }

                WarehousingRule entry = new WarehousingRule();
                entry.setLinkNature(linkNature);
                entry.setTestObject(testObject);
                entry.setRuleNature(ruleNature);
                entry.setTestCode(testCode);
                entry.setTestType(testType);
                entry.setTestPurpose(testPurpose);
                entry.setTestMethod(testMethod);
                entry.setProject(project);
                entry.setSort(sort);
                entry.setRemark(remark);

                // 将导入的数据插入数据库
               insert(entry);
            }
            return "true";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

}
