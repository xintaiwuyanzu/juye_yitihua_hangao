package com.dr.archive.manage.log.controller;

import com.dr.archive.manage.log.entity.SysLogEntity;
import com.dr.archive.manage.log.entity.SysLogEntityInfo;
import com.dr.archive.manage.log.service.SysLogService;
import com.dr.archive.util.DateTimeUtils;
import com.dr.framework.common.controller.BaseController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.page.Page;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.organise.service.OrganisePersonService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 描述：
 *
 * @author tuzl
 * @date 2020/8/12 17:38
 */
@RequestMapping(value = "/api/fzlog")
@RestController
public class SysLogController extends BaseController<SysLogEntity> {


    @Autowired
    OrganisePersonService organisePersonService;
    @Autowired
    SysLogService sysLogService;

    @RequestMapping({"/page"})
    @Override
    public ResultEntity page(HttpServletRequest request, SysLogEntity entity, @RequestParam(defaultValue = "0") int pageIndex, @RequestParam(defaultValue = "15") int pageSize, @RequestParam(defaultValue = "true") boolean page) {
        SqlQuery<SysLogEntity> sqlQuery = SqlQuery.from(SysLogEntity.class);
        if (entity.getCreateDate() != null && entity.getCreateDate() != 0) {
            sqlQuery.greaterThanEqual(SysLogEntityInfo.CREATEDATE, entity.getCreateDate())
                    .lessThanEqual(SysLogEntityInfo.CREATEDATE, entity.getUpdateDate() + (24 * 3600 - 1) * 1000);
        }
        Organise organise = SecurityHolder.get().currentOrganise();
        if (!organise.getId().equals("root")){
            sqlQuery.equal(SysLogEntityInfo.ORGANISEID,organise.getId());
        }
        sqlQuery.like(SysLogEntityInfo.OPERATION, entity.getOperation())
                .orderByDesc(SysLogEntityInfo.CREATEDATE);
        final ArrayList<SysLogEntity> list = new ArrayList<>();
        if (StringUtils.hasText(entity.getCreatePerson())) {
            sqlQuery.equal(SysLogEntityInfo.CREATEPERSON, entity.getCreatePerson());
            List<SysLogEntity> selectList = this.commonService.selectList(sqlQuery);
            list.addAll(selectList);
        } else {
            Person userLogin = getUserLogin(request);
            if (!"admin".equals(userLogin.getUserCode())) {
                List<Person> personList = organisePersonService.getOrganiseDefaultPersons(getOrganise(request).getId());
                for (Person person : personList) {
                    sqlQuery.equal(SysLogEntityInfo.CREATEPERSON, person.getUserCode());
                    List<SysLogEntity> selectList = this.commonService.selectList(sqlQuery);
                    list.addAll(selectList);
                }
            }else {
                List<SysLogEntity> selectList = this.commonService.selectList(sqlQuery);
                list.addAll(selectList);
            }
        }

        Object result;
        if (page) {
            List<SysLogEntity> arrayList = new ArrayList<>();
            for (int i = pageIndex * pageSize; i < list.size() && i < pageSize * (pageIndex + 1); i++) {
                arrayList.add(list.get(i));
            }
            result = new Page<>((long) pageIndex * pageSize, pageSize, list.size(), arrayList);
        } else {
            result = this.commonService.selectList(sqlQuery);
        }

        return ResultEntity.success(result);
    }

    @PostMapping("/deleteAll")
    public ResultEntity deleteAll(String ids, String no) {
        String s = new String(Base64Utils.decodeFromString(ids));
        s = s.substring(0, s.length() - 6);
        try {
            sysLogService.deleteEntity(s);
        } catch (Exception e) {
            return ResultEntity.error("删除出错");
        }
        return ResultEntity.success("删除成功");
    }


    /**
     * 导出日志
     *
     * @param response
     */
    @GetMapping("/expLog")
    public void expLog(HttpServletResponse response, String operation, String createPerson, long createDate, long updateDate) {
        String date1 = DateTimeUtils.getDates(createDate);
        long time = updateDate + (24 * 3600 - 1) * 1000;
        String date2 = DateTimeUtils.getDates(time);
        SqlQuery<SysLogEntity> sqlQuery = SqlQuery.from(SysLogEntity.class, false)
                .column(SysLogEntityInfo.CREATEPERSON)
                .column(SysLogEntityInfo.OPERATION)
                .column(SysLogEntityInfo.IP)
                .column(SysLogEntityInfo.UPDATEDATE)
                .column(SysLogEntityInfo.CREATEDATE)
                .like(SysLogEntityInfo.OPERATION, operation)
                .greaterThanEqual(SysLogEntityInfo.CREATEDATE, createDate)
                .lessThanEqual(SysLogEntityInfo.CREATEDATE, time);
        final ArrayList<SysLogEntity> list = new ArrayList<>();
        if (StringUtils.hasText(createPerson)) {
            sqlQuery.equal(SysLogEntityInfo.CREATEPERSON, createPerson);
            List<SysLogEntity> selectList = this.commonService.selectList(sqlQuery);
            list.addAll(selectList);
        } else {
            Person userLogin = SecurityHolder.get().currentPerson();
            Organise organise = SecurityHolder.get().currentOrganise();
            if (!"admin".equals(userLogin.getUserCode())) {
                List<Person> personList = organisePersonService.getOrganiseDefaultPersons(organise.getId());
                for (Person person : personList) {
                    sqlQuery.equal(SysLogEntityInfo.CREATEPERSON, person.getUserCode());
                    List<SysLogEntity> selectList = this.commonService.selectList(sqlQuery);
                    list.addAll(selectList);
                }
            }else {
                List<SysLogEntity> selectList = this.commonService.selectList(sqlQuery);
                list.addAll(selectList);
            }
        }
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("日志数据");
        sheet.setDefaultColumnWidth(30);
        sheet.setDefaultRowHeightInPoints(30);
        HSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short) 10);
        font.setFontName("微软雅黑");


        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER); // 居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFont(font);
        HSSFCell cell;
        HSSFRow row = sheet.createRow(0);
        row = sheet.createRow(0);

        cell = row.createCell(0);
        cell.setCellStyle(style);
        cell.setCellValue("操作人");

        cell = row.createCell(1);
        cell.setCellStyle(style);
        cell.setCellValue("操作");

        cell = row.createCell(2);
        cell.setCellStyle(style);
        cell.setCellValue("IP");


        cell = row.createCell(3);
        cell.setCellStyle(style);
        cell.setCellValue("创建时间");


        for (int i = 0; i < list.size(); i++) {
            SysLogEntity log = list.get(i);
            row = sheet.createRow(i + 1);

            cell = row.createCell(0);
            cell.setCellStyle(style);
            try {
                Person person = organisePersonService.getPersonByUserCodeEqual(log.getCreatePerson());
                cell.setCellValue(person.getUserName());
            } catch (Exception e) {
                cell.setCellValue(log.getCreatePerson());
            }
            cell = row.createCell(1);
            cell.setCellStyle(style);
            cell.setCellValue(log.getOperation());

            cell = row.createCell(2);
            cell.setCellStyle(style);
            cell.setCellValue(log.getIp());

            cell = row.createCell(3);
            cell.setCellStyle(style);
            cell.setCellValue(DateTimeUtils.getDates(log.getCreateDate()));
        }
        ByteArrayOutputStream outBtye = new ByteArrayOutputStream();
        try {
            wb.write(outBtye);
            byte[] byteArray = outBtye.toByteArray();
            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode("日志(" + date1 + "至" + date2 + ").xls", "UTF-8"));
            //InputStream in = null;
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(byteArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 统计 新增 修改 添加 删除 查询数量
     */
    @RequestMapping("/operation")
    public ResultEntity operation() {
        Map<String, Long> map = sysLogService.operation();
        return ResultEntity.success(map);
    }
    /**
     * 统计 新增 修改 添加 删除 查询数量
     */
    @RequestMapping("/operationToday")
    public ResultEntity operationToday(){
        Map<String,Long> map= sysLogService.operationToday();
        return ResultEntity.success(map);
    }

}
