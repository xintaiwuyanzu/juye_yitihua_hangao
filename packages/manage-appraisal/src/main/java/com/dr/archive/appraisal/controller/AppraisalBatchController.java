package com.dr.archive.appraisal.controller;

import com.dr.archive.appraisal.entity.*;
import com.dr.archive.appraisal.service.*;
import com.dr.archive.util.DateTimeUtils;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.service.CommonService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * 鉴定批次管理
 */
@RestController
@RequestMapping("api/appraisalBatch")
public class AppraisalBatchController extends BaseServiceController<AppraisalBatchService, AppraisalBatch> {

    @Autowired
    Archive4ToBeAppraisalService archive4ToBeAppraisalService;
    @Autowired
    AppraisalBatchPersonService appraisalBatchPersonService;
    @Autowired
    AppraisalBatchTaskService appraisalBatchTaskService;
    @Autowired
    ArchiveAppraisalTaskService archiveAppraisalTaskService;
    @Autowired
    CommonService commonService;
    @Autowired
    ArchiveAppraisalMessageService archiveAppraisalMessageService;


    @Override
    protected SqlQuery<AppraisalBatch> buildPageQuery(HttpServletRequest httpServletRequest, AppraisalBatch appraisalBatch) {
        SqlQuery sqlQuery = SqlQuery.from(AppraisalBatch.class);
        sqlQuery.equal(AppraisalBatchInfo.APPRAISALTYPE,appraisalBatch.getAppraisalType());
        sqlQuery.like(AppraisalBatchInfo.BATCHNAME,appraisalBatch.getBatchName());
        sqlQuery.orderByDesc(AppraisalBatchInfo.CREATEDATE);
        return sqlQuery;
    }

    @Override
    public ResultEntity<AppraisalBatch> insert(HttpServletRequest request, AppraisalBatch entity) {
        entity.setCreatePersonName(getUserLogin(request).getUserName());
        this.service.insert(entity);
        return ResultEntity.success(entity);
    }

    @Override
    public ResultEntity<Boolean> delete(HttpServletRequest request, AppraisalBatch entity) {
        String [] ids = entity.getId().split(",");
        for(String id:ids){
            AppraisalBatch e = new AppraisalBatch();
            e.setId(id);
            service.releaseByAppraisalBatchId(e.getId());
            appraisalBatchPersonService.deleteBatchPersonByBatchId(e.getId());
            appraisalBatchTaskService.deleteBatchTaskByBatchId(e.getId());
            archiveAppraisalTaskService.deleteBatchTaskArchiveByBatchId(e.getId());
            super.delete(request,e);
            archiveAppraisalMessageService.deleteByBatchId(id);
        }
        return ResultEntity.success(true);
    }

    @RequestMapping({"/getAppraisalBatchByPerson"})
    public ResultEntity getAppraisalBatchByPerson(HttpServletRequest request, @RequestParam(defaultValue = "0") int pageIndex, @RequestParam(defaultValue = "15") int pageSize, @RequestParam(defaultValue = "true") boolean page,AppraisalBatch batch) {
        return ResultEntity.success(service.getAppraisalBatchPageByPerson(getUserLogin(request).getId(),pageIndex,pageSize,page,batch));
    }

    @RequestMapping({"/getArchiveCountByBatch"})
    public ResultEntity getArchiveCountByBatch(String id) {
        return ResultEntity.success(service.getArchiveCountByBatch(id));
    }

    @RequestMapping({"/getCategoryCountByBatch"})
    public ResultEntity getCategoryCountByBatch(String id) {
        return ResultEntity.success(service.getCategoryCountByBatch(id));
    }

    @RequestMapping({"/getArchiveCountByAuxiliary"})
    public ResultEntity getArchiveCountByAuxiliary(String id) {
        return ResultEntity.success(service.getArchiveCountByAuxiliary(id));
    }

    @RequestMapping("/statisticsByBatchId")
    public ResultEntity statisticsByBatchId(String batchId) {
        return ResultEntity.success(service.statisticsByBatchId(batchId));
    }

    @RequestMapping("/getFondByBatchId")
    public ResultEntity getFondByBatchId(String batchId) {
        return ResultEntity.success(service.getFondByBatchId(batchId));
    }
    @RequestMapping("/statisticsFinishByBatchId")
    public ResultEntity statisticsFinishByBatchId(String batchId,String fondId) {
        return ResultEntity.success(service.statisticsFinishByBatchId(batchId,fondId));
    }

    @RequestMapping("/endBatch")
    public ResultEntity endBatch(String id) throws IOException {
        return service.endBatch(id);
    }
    @PostMapping("/isDel")
    public String isDel(String batchId) {
        List<ArchiveAppraisalTask> list = commonService.selectList(SqlQuery.from(ArchiveAppraisalTask.class)
                .equal(ArchiveAppraisalTaskInfo.BATCHID, batchId)
                .equal(ArchiveAppraisalTaskInfo.PERSONRESULT, "xh")
                .notEqual(ArchiveAppraisalTaskInfo.ISDEL, "1")
                .equal(ArchiveAppraisalTaskInfo.STATUS, "2"));
        if (list.size()>0){
            AppraisalBatch appraisalBatch = service.selectById(batchId);
            appraisalBatch.setIsDestory("0");
            commonService.update(appraisalBatch);
        }else {
            return "0";
        }
        return "1";
    }
    @GetMapping("/expDestoryArchive")
    public void expDestoryArchive(HttpServletResponse response, String batchId) {
        List<ArchiveAppraisalTask> list = archiveAppraisalTaskService.getXhArchiveByBatchId(batchId);
        AppraisalBatch appraisalBatch = service.selectById(batchId);
        if(!"1".equals(appraisalBatch.getIsDestory())){
            service.updateAppraisalBatchDestory(batchId);
            DestoryHistory destoryHistory = new DestoryHistory();
            destoryHistory.setBatchId(batchId);
            destoryHistory.setDestoryQuantity(list.size()+"");
            destoryHistory.setCreatePersonName(SecurityHolder.get().currentPerson().getUserName());
            destoryHistory.setOrgId(SecurityHolder.get().currentOrganise().getId());
            List<DestoryHistory> destoryHistories = commonService.selectList(SqlQuery.from(DestoryHistory.class).equal(DestoryHistoryInfo.BATCHID, batchId));
            if (destoryHistories.size()>0){
                destoryHistory.setId(destoryHistories.get(0).getId());
                commonService.update(destoryHistory);
            }else {
                commonService.insert(destoryHistory);
            }
        }
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("销毁清单");
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

        cell = row.createCell(0);
        cell.setCellStyle(style);
        cell.setCellValue("全宗");

        cell = row.createCell(1);
        cell.setCellStyle(style);
        cell.setCellValue("档号");

        cell = row.createCell(2);
        cell.setCellStyle(style);
        cell.setCellValue("题名");

        cell = row.createCell(3);
        cell.setCellStyle(style);
        cell.setCellValue("销毁时间");
        for (int i = 0; i < list.size(); i++) {
            ArchiveAppraisalTask archiveAppraisalTask = list.get(i);
            row = sheet.createRow(i + 1);
            cell = row.createCell(0);
            cell.setCellStyle(style);

            cell.setCellValue(archiveAppraisalTask.getFondCode());
            cell = row.createCell(1);
            cell.setCellStyle(style);
            cell.setCellValue(archiveAppraisalTask.getArchiveCode());

            cell = row.createCell(2);
            cell.setCellStyle(style);
            cell.setCellValue(archiveAppraisalTask.getTitle());

            cell = row.createCell(3);
            cell.setCellStyle(style);
            String date = DateTimeUtils.longToDate(archiveAppraisalTask.getUpdateDate(), "yyyy-MM-dd HH:mm:ss");
            cell.setCellValue(date);
        }
        ByteArrayOutputStream outBtye = new ByteArrayOutputStream();
        try {
            wb.write(outBtye);
            byte[] byteArray = outBtye.toByteArray();
            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode("销毁清册.xls", "UTF-8"));
            //InputStream in = null;
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(byteArray);
        } catch (IOException e) {
            e.printStackTrace();
        }




    }

}
