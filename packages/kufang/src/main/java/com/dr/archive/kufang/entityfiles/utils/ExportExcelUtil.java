package com.dr.archive.kufang.entityfiles.utils;


import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 导出EXCEL
 *
 * @author dr
 */
public class ExportExcelUtil<T> {

    /**
     * <p>
     * 导出带有头部标题行的Excel <br>
     * 时间格式默认：yyyy-MM-dd hh:mm:ss <br>
     * </p>
     *
     * @param title   表格标题
     * @param headers 头部标题集合
     * @param dataset 数据集合
     */
    public void exportExcel(HttpServletResponse response, String title, String[] headers, String[] codes, Collection<T> dataset, String name) {
        exportExcel2003(response, title, headers, codes, dataset, "yyyy-MM-dd HH:mm:ss", name);
    }

    /**
     * <p>
     * 导出带有头部标题行的Excel <br>
     * 时间格式默认：yyyy-MM-dd hh:mm:ss <br>
     * </p>
     *
     * @param response
     * @param title           表格标题名
     * @param headers         表格头部标题集合
     * @param dataset         需导出数据集
     * @param exportFieldName 实体类中需导出的字段，并不需要导出所有字段，只需将要导出的字段传入即可
     * @param convertFileds   需进行格式转换的字段，需要实现在实体类中编写get方法定义转换规则
     */
    public void exportExcelByFieldName(HttpServletResponse response, String title, String[] headers, Collection<T> dataset, String[] exportFieldName, String[] convertFileds) {
        exportExcel2003ByFieldName(response, title, headers, dataset, "yyyy-MM-dd HH:mm:ss", exportFieldName, convertFileds);
    }

    /**
     * <p>
     * 通用Excel导出方法,利用反射机制遍历对象的所有字段，将数据写入Excel文件中 <br>
     * 此方法生成2003版本的excel,文件名后缀：xls <br>
     * </p>
     *
     * @param title   表格标题名
     * @param headers 表格头部标题集合
     * @param dataset 需要显示的数据集合,集合中一定要放置符合JavaBean风格的类的对象。此方法支持的
     *                JavaBean属性的数据类型有基本数据类型及String,Date
     * @param pattern 如果有时间数据，设定输出格式。默认为"yyyy-MM-dd hh:mm:ss"
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void exportExcel2003(HttpServletResponse response, String title, String[] headers, String[] codes, Collection<T> dataset, String pattern, String name) {
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        //设置表格风格 居中对齐
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        //设置字体
        HSSFFont font = workbook.createFont();
        font.setFontName("楷体");

        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet(title);
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth(20);
        sheet.setDefaultRowHeightInPoints(18.0F);
        // 产生表格标题行
        HSSFRow row = sheet.createRow(0);
        HSSFCell cellHeader;

        for (int i = 0; i < headers.length; i++) {
            cellHeader = row.createCell(i);
            cellHeader.setCellValue(new HSSFRichTextString(headers[i]));
        }
        // 遍历集合数据，产生数据行
        Iterator<T> it = dataset.iterator();
        int index = 0;
        Pattern p = Pattern.compile("^//d+(//.//d+)?$");
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        while (it.hasNext()) {
            index++;
            row = sheet.createRow(index);
            T t = it.next();
            // 利用反射，根据JavaBean属性的先后顺序，动态调用getXxx()方法得到属性值
            for (int i = 0; i < codes.length; i++) {
                HSSFCell cell = row.createCell(i);
                String fieldName = codes[i];
                if (StringUtils.isEmpty(fieldName)) {
                    break;
                }
                try {
                    Object value = BeanUtils.getProperty(t, fieldName);
                    // 判断值的类型后进行强制类型转换
                    String textValue = null;
                    if (value instanceof Integer) {
                        cell.setCellValue((Integer) value);
                    } else if (value instanceof Float) {
                        textValue = String.valueOf((Float) value);
                        cell.setCellValue(textValue);
                    } else if (value instanceof Double) {
                        textValue = String.valueOf((Double) value);
                        cell.setCellValue(textValue);
                    } else if (value instanceof Long) {
                        cell.setCellValue((Long) value);
                    }
                    if (value instanceof Boolean) {
                        textValue = "是";
                        if (!(Boolean) value) {
                            textValue = "否";
                        }
                    } else if (value instanceof Date) {
                        textValue = sdf.format((Date) value);
                    } else {
                        // 其它数据类型都当作字符串简单处理
                        if (value != null) {
                            textValue = value.toString();
                        }
                    }
                    if("createDate".equals(fieldName)){
                        long l = Long.parseLong((String) value);
                        textValue = sdf.format(new Date(l));
                    }
                    if (textValue != null) {
                        Matcher matcher = p.matcher(textValue);
                        if (matcher.matches()) {
                            cell.setCellValue(Double.parseDouble(textValue));
                        } else {
                            HSSFRichTextString richString = new HSSFRichTextString(textValue);
                            cell.setCellValue(richString);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(name + ".xls", "UTF-8"));
            OutputStream outStream = response.getOutputStream();
            workbook.write(outStream);
            outStream.flush();
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>
     * 通用Excel导出方法,利用反射机制遍历对象的所有字段，将数据写入Excel文件中 <br>
     * 此方法生成2003版本的excel,文件名后缀：xls <br>
     *
     * @param response
     * @param title           表格标题名
     * @param headers         表格头部标题集合
     * @param dataset         需导出数据集
     * @param pattern         如果有时间数据，设定输出格式。默认为"yyyy-MM-dd hh:mm:ss"
     * @param exportFieldName 实体类中需导出的字段，并不需要导出所有字段，只需将要导出的字段传入即可
     * @param convertFileds   需进行格式转换的字段，需要实现在实体类中编写get方法定义转换规则
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void exportExcel2003ByFieldName(HttpServletResponse response, String title, String[] headers, Collection<T> dataset, String pattern, String[] exportFieldName, String[] convertFileds) {
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet(title);
        // 设置表格默认列宽度
        sheet.setDefaultColumnWidth(20);
        // 产生表格标题行
        HSSFRow row = sheet.createRow(0);
        HSSFCell cellHeader;
        for (int i = 0; i < headers.length; i++) {
            cellHeader = row.createCell(i);
            cellHeader.setCellValue(new HSSFRichTextString(headers[i]));
        }
        // 遍历集合数据，产生数据行
        Iterator<T> it = dataset.iterator();
        int index = 0;
        T t;
        HSSFRichTextString richString;
        Pattern p = Pattern.compile("^//d+(//.//d+)?$");
        Matcher matcher;
        String fieldName;
        String getMethodName;
        HSSFCell cell;
        Class tCls;
        Method getMethod;
        Object value;
        String textValue;
        long value2;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        boolean flag;
        while (it.hasNext()) {
            index++;
            row = sheet.createRow(index);
            t = (T) it.next();
            // 利用反射，根据JavaBean属性的先后顺序，动态调用getXxx()方法得到属性值
            for (int i = 0; i < exportFieldName.length; i++) {
                fieldName = exportFieldName[i];
                cell = row.createCell(i);
                getMethodName = "get" + fieldName.substring(0, 1).toUpperCase()
                        + fieldName.substring(1);
                flag = false;
                for (int j = 0; j < convertFileds.length; j++) {
                    if (fieldName.toUpperCase().equals(convertFileds[j].toUpperCase())) {
                        flag = true;
                        break;
                    }
                }
                try {
                    tCls = t.getClass();
                    getMethod = tCls.getMethod(getMethodName, new Class[]{});
                    value = getMethod.invoke(t, new Object[]{});
                    if (flag) {
                        //再反射一下，取出状态对应的值
                        getMethodName = getMethodName + "Status";
                        getMethod = tCls.getMethod(getMethodName, Object.class);
                        value = getMethod.invoke(t, value);
                    }
                    // 判断值的类型后进行强制类型转换
                    textValue = null;
                    if (value instanceof Integer) {
                        cell.setCellValue((Integer) value);
                    } else if (value instanceof Float) {
                        textValue = String.valueOf((Float) value);
                        cell.setCellValue(textValue);
                    } else if (value instanceof Double) {
                        textValue = String.valueOf((Double) value);
                        cell.setCellValue(textValue);
                    } else if (value instanceof Long) {
                        value2 = (Long) value;
                        if (value2 > 1000000000000L) {
                            textValue = DateTools.getDates(value2);
                            cell.setCellValue(textValue);
                        } else {
                            cell.setCellValue((Long) value);
                        }
                    } else if (value instanceof Boolean) {
                        textValue = "是";
                        if (!(Boolean) value) {
                            textValue = "否";
                        }
                    } else if (value instanceof Date) {
                        textValue = sdf.format((Date) value);
                    } else {
                        // 其它数据类型都当作字符串简单处理
                        if (value != null) {
                            textValue = value.toString();
                        }
                    }
                    if (textValue != null) {
                        matcher = p.matcher(textValue);
                        if (matcher.matches()) {
                            // 是数字当作double处理
                            cell.setCellValue(Double.parseDouble(textValue));
                        } else {
                            richString = new HSSFRichTextString(textValue);
                            cell.setCellValue(richString);
                        }
                    }
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } finally {
                }
            }
        }

        OutputStream outputStream = null;
        try {
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(title + ".xls", "UTF-8"));
            response.flushBuffer();
            outputStream = response.getOutputStream();
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
