package com.dr.archive.appraisal.service.impl;

import com.dr.archive.appraisal.entity.*;
import com.dr.archive.appraisal.mapper.AppraisalArchiveMapper;
import com.dr.archive.appraisal.service.*;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.fond.service.FondService;
import com.dr.archive.managefile.entity.ManageFile;
import com.dr.archive.managefile.service.ManageFileService;
import com.dr.archive.managefondsdescriptivefile.entity.Management;
import com.dr.archive.managefondsdescriptivefile.service.ManagementService;
import com.dr.archive.util.UUIDUtils;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.file.autoconfig.CommonFileConfig;
import com.dr.framework.common.file.model.FileInfo;
import com.dr.framework.common.file.resource.FileSystemFileResource;
import com.dr.framework.common.file.service.CommonFileService;
import com.dr.framework.common.page.Page;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.organise.service.OrganisePersonService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AppraisalBatchServiceImpl extends DefaultBaseService<AppraisalBatch> implements AppraisalBatchService {

    @Autowired
    FondService fondService;
    @Autowired
    Archive4ToBeAppraisalService archive4ToBeAppraisalService;
    @Autowired
    AppraisalBatchPersonService appraisalBatchPersonService;
    @Autowired
    ArchiveAppraisalTaskService archiveAppraisalTaskService;
    @Autowired
    AppraisalRulesService appraisalWordGroupService;
    @Autowired
    AppraisalArchiveMapper appraisalArchiveMapper;
    @Autowired
    AppraisalOpenRangeService appraisalOpenRangeService;
    @Autowired
    ManagementService managementService;
    @Autowired
    OrganisePersonService organisePersonService;
    @Autowired
    CommonFileConfig commonFileConfig;
    @Autowired
    CommonFileService commonFileService;
    @Autowired
    ManageFileService manageFileService;


    @Override
    public Page getAppraisalBatchPageByPerson(String personId, int pageIndex, int pageSize, boolean page, AppraisalBatch batch) {
        SqlQuery sqlQuery = SqlQuery.from(AppraisalBatch.class)
                .equal(AppraisalBatchInfo.STATUS, batch.getStatus())
                .in(AppraisalBatchInfo.ID, SqlQuery.from(AppraisalBatchPerson.class, false)
                        .column(AppraisalBatchPersonInfo.BATCHID)
                        .equal(AppraisalBatchPersonInfo.PERSONID, personId))
                .orderByDesc(AppraisalBatchInfo.CREATEDATE);
        return selectPage(sqlQuery, pageIndex, pageSize);
    }

    @Override
    public ResultEntity getArchiveCountByBatch(String id) {
        Map resultMap = new HashMap();
        resultMap.put("zs", countByBatchID(id));
        resultMap.put("ylq", archiveAppraisalTaskService.countAllByBatchId(id));
        resultMap.put("ywc", archiveAppraisalTaskService.countFinishByBatchId(id));
        return ResultEntity.success(resultMap);
    }

    @Override
    public List<Map> getCategoryCountByBatch(String id) {
        List<Map> mapList = commonMapper.selectByQuery(SqlQuery.from(ArchiveAppraisalTask.class, false)
                .column(ArchiveAppraisalTaskInfo.CATEGORYNAME, ArchiveAppraisalTaskInfo.CATEGORYNAME.count("count"))
                .equal(ArchiveAppraisalTaskInfo.BATCHID, id)
                .groupBy(ArchiveAppraisalTaskInfo.CATEGORYNAME)
                .setReturnClass(Map.class));
        return mapList;
    }

    @Override
    public ResultEntity getArchiveCountByAuxiliary(String id) {
        List<CommonCount> commonCountList = groupCountByBatchIDAndAuxiliary(id);
        Map<String, String> map = new HashMap();
        Map<String, Map> transMap = new HashMap();
        for (CommonCount commonCount : commonCountList) {
            if (!map.containsKey(commonCount.getValue1())) {
                if ("null".equals(commonCount.getValue1())) {
                    map.put(commonCount.getValue1(), "无机器结果");
                } else {
                    AppraisalOpenRange appraisalOpenRange = appraisalOpenRangeService.selectById(commonCount.getValue1());
                    map.put(commonCount.getValue1(), ("kz".equals(appraisalOpenRange.getAuxiliaryResult()) ? "控制" : "开放") + "--" + appraisalOpenRange.getOpenRange());
                }
                Map tempMap = new HashMap();
                tempMap.put(commonCount.getValue2(), commonCount.getCount());
                transMap.put(commonCount.getValue1(), tempMap);
            } else {
                transMap.get(commonCount.getValue1()).put(commonCount.getValue2(), commonCount.getCount());
            }
        }
        String[] ywc = new String[transMap.keySet().size()];
        String[] ylq = new String[transMap.keySet().size()];
        String[] wlq = new String[transMap.keySet().size()];
        String[] wordGroup = new String[transMap.keySet().size()];
        int index = 0;
        for (String key : map.keySet()) {
            Map tempMap = transMap.get(key);
            if (tempMap.containsKey("0")) {
                wlq[index] = tempMap.get("0").toString();
            } else {
                wlq[index] = "0";
            }
            if (tempMap.containsKey("1")) {
                ylq[index] = tempMap.get("1").toString();
            } else {
                ylq[index] = "0";
            }
            if (tempMap.containsKey("2")) {
                ywc[index] = tempMap.get("2").toString();
            } else {
                ywc[index] = "0";
            }
            wordGroup[index] = map.get(key);
            index++;
        }

        List resultList = new ArrayList();
        resultList.add(wordGroup);
        resultList.add(ywc);
        resultList.add(ylq);
        resultList.add(wlq);
        return ResultEntity.success(resultList);
    }

    @Override
    public long insert(AppraisalBatch entity) {
        String[] fondIds = entity.getFondCodes().split(",");
        StringBuilder fondCodes = new StringBuilder();
        StringBuilder fondNames = new StringBuilder();
        for (String id : fondIds) {
            Fond fond = fondService.selectById(id);
            fondNames.append(fond.getName()).append(",");
            fondCodes.append(fond.getCode()).append(",");
        }
        fondNames = new StringBuilder(fondNames.substring(0, fondNames.length() - 1));
        fondCodes = new StringBuilder(fondCodes.substring(0, fondCodes.length() - 1));
        entity.setFondCodes(fondCodes.toString());
        entity.setFondNames(fondNames.toString());
        entity.setStatus("0");
        entity.setId(UUIDUtils.getUUID());
        entity.setArchiveQuantity(archive4ToBeAppraisalService.updateByAppraisalBatch(entity) + "");
        appraisalArchiveMapper.insertArchiveTaskOfToBeAppraisal(entity.getId());
        return getCommonService().insert(entity);
    }

    @Override
    public Map statisticsByBatchId(String batchId) {

        SqlQuery sqlQuery = SqlQuery.from(Archive4ToBeAppraisal.class, false)
                .column(Archive4ToBeAppraisalInfo.FONDCODE.alias("value1"))
                .column(Archive4ToBeAppraisalInfo.VINTAGES.alias("value2"))
                .column(Archive4ToBeAppraisalInfo.ID.count().alias("count"))
                .equal(Archive4ToBeAppraisalInfo.BATCHID, batchId)
                .groupBy(Archive4ToBeAppraisalInfo.FONDCODE, Archive4ToBeAppraisalInfo.VINTAGES)
                .orderBy(Archive4ToBeAppraisalInfo.FONDCODE)
                .orderBy(Archive4ToBeAppraisalInfo.VINTAGES)
                .setReturnClass(CommonCount.class);
        List<CommonCount> commonCountList = commonMapper.selectByQuery(sqlQuery);

        List filedList = new ArrayList();
        List<Map> resultList = new ArrayList();
        Map<String, Map> map = new HashMap();
        for (CommonCount commonCount : commonCountList) {
            String filed = commonCount.getValue2();
            if (StringUtils.isEmpty(filed)) {
                filed = "无年份";
            }
            filedList.add(filed);
            if (map.containsKey(commonCount.getValue1())) {
                map.get(commonCount.getValue1()).put(filed, commonCount.getCount());
            } else {
                Map mapOne = new HashMap();
                mapOne.put("fondCode", commonCount.getValue1());
                mapOne.put(filed, commonCount.getCount());
                map.put(commonCount.getValue1(), mapOne);
            }
        }

        for (String s : map.keySet()) {
            resultList.add(map.get(s));
        }

        Map resultMap = new HashMap();
        resultMap.put("filedList", filedList);
        resultMap.put("countList", resultList);

        return resultMap;
    }

    @Override
    public long countByBatchID(String id) {
        SqlQuery sqlQuery = SqlQuery.from(ArchiveAppraisalTask.class)
                .equal(ArchiveAppraisalTaskInfo.BATCHID, id);
        return commonMapper.countByQuery(sqlQuery);
    }

    @Override
    public List<CommonCount> groupCountByBatchIDAndAuxiliary(java.lang.String id) {
        SqlQuery sqlQuery = SqlQuery.from(ArchiveAppraisalTask.class, false)
                .count(ArchiveAppraisalTaskInfo.ID.alias("count"))
                .column(ArchiveAppraisalTaskInfo.AUXILIARYRESULT.alias("value1"))
                .column(ArchiveAppraisalTaskInfo.STATUS.alias("value2"))
                .equal(ArchiveAppraisalTaskInfo.BATCHID, id)
                .groupBy(ArchiveAppraisalTaskInfo.AUXILIARYRESULT, ArchiveAppraisalTaskInfo.STATUS)
                .setReturnClass(CommonCount.class);
        return commonMapper.selectByQuery(sqlQuery);
    }

    @Override
    public long releaseByAppraisalBatchId(String appraisalBatchId) {
        SqlQuery sqlQuery = SqlQuery.from(Archive4ToBeAppraisal.class)
                .equal(Archive4ToBeAppraisalInfo.BATCHID, appraisalBatchId)
                .set(Archive4ToBeAppraisalInfo.BATCHID, (Serializable) null)
                .set(Archive4ToBeAppraisalInfo.TASKID, (Serializable) null);
        return updateBySqlQuery(sqlQuery);
    }

    @Override
    public synchronized long updateAppraisalBatchByTask(AppraisalBatchTask appraisalBatchTask) {
        long rs;
        if ("KFJD".equals(appraisalBatchTask.getAppraisalType()) && !StringUtils.isEmpty(appraisalBatchTask.getAuxiliaryResult())) {
            if ("0".equals(appraisalBatchTask.getBatchTaskQuantity())) {  //数量选全部时
                rs = commonMapper.updateByQuery(SqlQuery.from(ArchiveAppraisalTask.class)
                        .set(ArchiveAppraisalTaskInfo.TASKID, appraisalBatchTask.getId())
                        .set(ArchiveAppraisalTaskInfo.STATUS, 0)
                        .isNull(ArchiveAppraisalTaskInfo.TASKID)
                        .equal(ArchiveAppraisalTaskInfo.BATCHID, appraisalBatchTask.getBatchId())
                        .equal(ArchiveAppraisalTaskInfo.AUXILIARYRESULT, appraisalBatchTask.getAuxiliaryResult()));
            } else {
                rs = appraisalArchiveMapper.updateAppraisalTaskIdOFOpen(
                        appraisalBatchTask.getId(),
                        Long.parseLong(appraisalBatchTask.getBatchTaskQuantity()),
                        appraisalBatchTask.getBatchId(),
                        appraisalBatchTask.getAuxiliaryResult());
            }

        } else {
            if ("0".equals(appraisalBatchTask.getBatchTaskQuantity())) { //数量选全部时
                rs = commonMapper.updateByQuery(SqlQuery.from(ArchiveAppraisalTask.class)
                        .set(ArchiveAppraisalTaskInfo.TASKID, appraisalBatchTask.getId())
                        .set(ArchiveAppraisalTaskInfo.STATUS, 0)
                        .isNull(ArchiveAppraisalTaskInfo.TASKID)
                        .equal(ArchiveAppraisalTaskInfo.BATCHID, appraisalBatchTask.getBatchId()));
            } else {
                rs = appraisalArchiveMapper.updateAppraisalTaskIdOFExpire(
                        appraisalBatchTask.getId(),
                        Long.parseLong(appraisalBatchTask.getBatchTaskQuantity()),
                        appraisalBatchTask.getBatchId());
            }
        }
        return rs;
    }

    @Override
    public void releaseByAppraisalTaskId(String taskId) {
        SqlQuery sqlQuery = SqlQuery.from(ArchiveAppraisalTask.class);
        sqlQuery.equal(ArchiveAppraisalTaskInfo.TASKID, taskId)
                .set(ArchiveAppraisalTaskInfo.STATUS, "0")
                .set(ArchiveAppraisalTaskInfo.TASKID, (Serializable) null);
        updateBySqlQuery(sqlQuery);
    }

    @Override
    public List statisticsFinishByBatchId(String batchId, String fondId) {
        Map tempMap = new HashMap();
        AppraisalBatch appraisalBatch = selectById(batchId);
        if ("KFJD".equals(appraisalBatch.getAppraisalType())) {
            List<AppraisalOpenRange> appraisalOpenRangeList = commonMapper.selectAll(AppraisalOpenRange.class);
            for (AppraisalOpenRange appraisalOpenRange : appraisalOpenRangeList) {
                tempMap.put(appraisalOpenRange.getId(), 0);
            }
        } else {
            tempMap.put("yq", 0);
            tempMap.put("xh", 0);
        }
        Fond fond = fondService.selectById(fondId);
        SqlQuery sqlQuery = SqlQuery.from(ArchiveAppraisalTask.class, false)
                .column(ArchiveAppraisalTaskInfo.ID.count().alias("count"))
                .column(ArchiveAppraisalTaskInfo.PERSONRESULT.alias("value1"))
                .column(ArchiveAppraisalTaskInfo.VINTAGES.alias("value2"))
                .groupBy(ArchiveAppraisalTaskInfo.PERSONRESULT)
                .groupBy(ArchiveAppraisalTaskInfo.VINTAGES)
                .equal(ArchiveAppraisalTaskInfo.BATCHID, batchId)
                .equal(ArchiveAppraisalTaskInfo.STATUS, "2")
                .setReturnClass(CommonCount.class);
        if (fond != null) {
            sqlQuery.equal(ArchiveAppraisalTaskInfo.FONDCODE, fond.getCode());
        }
        List<CommonCount> resultList = commonMapper.selectByQuery(sqlQuery);
        sqlQuery = SqlQuery.from(ArchiveAppraisalTask.class, false)
                .column(ArchiveAppraisalTaskInfo.ID.count().alias("count"))
                .column(ArchiveAppraisalTaskInfo.ISEQUAL.alias("value1"))
                .column(ArchiveAppraisalTaskInfo.VINTAGES.alias("value2"))
                .groupBy(ArchiveAppraisalTaskInfo.ISEQUAL)
                .groupBy(ArchiveAppraisalTaskInfo.VINTAGES)
                .equal(ArchiveAppraisalTaskInfo.BATCHID, batchId)
                .equal(ArchiveAppraisalTaskInfo.STATUS, "2")
                .setReturnClass(CommonCount.class);
        if (fond != null) {
            sqlQuery.equal(ArchiveAppraisalTaskInfo.FONDCODE, fond.getCode());
        }
        List<CommonCount> isEqualList = commonMapper.selectByQuery(sqlQuery);
        Map<String, Map> transMap = new HashMap();
        resultList.addAll(isEqualList);
        StringBuilder vintages = new StringBuilder();
        for (CommonCount commonCount : resultList) {
            if (!transMap.containsKey(commonCount.getValue2())) {
                vintages.append(commonCount.getValue2()).append(",");
                Map temp = new HashMap(tempMap);
                temp.put("vintages", commonCount.getValue2());
                temp.put(commonCount.getValue1(), commonCount.getCount());
                transMap.put(commonCount.getValue2(), temp);
            } else {
                transMap.get(commonCount.getValue2()).put(commonCount.getValue1(), commonCount.getCount());
            }
        }
        if (vintages.length() > 1) {
            vintages = new StringBuilder(vintages.substring(0, vintages.length() - 1));
        }
        String[] arrayVintages = vintages.toString().split(",");
        Arrays.sort(arrayVintages);
        List list = new ArrayList();
        for (String key : arrayVintages) {
            Map temp = transMap.get(key);
            temp.put("yz", temp.get("1"));
            temp.put("byz", temp.get("0"));
            list.add(temp);
        }
        return list;
    }

    @Override
    public List<Fond> getFondByBatchId(String batchId) {
        AppraisalBatch appraisalBatch = selectById(batchId);
        String[] arrayFondCodes = appraisalBatch.getFondCodes().split(",");
        List<Fond> fondList = new ArrayList();
        for (String fondCode : arrayFondCodes) {
            fondList.add(fondService.findFondByCode(fondCode));
        }
        return fondList;
    }

    @Override
    public void updateAppraisalBatchDestory(String batchId) {
        AppraisalBatch appraisalBatch = selectById(batchId);
        appraisalBatch.setIsDestory("1");
        updateById(appraisalBatch);
        List<ArchiveAppraisalTask> appraisalTaskList = archiveAppraisalTaskService.getXhArchiveByBatchId(batchId);
        for (ArchiveAppraisalTask archiveAppraisalTask : appraisalTaskList) {
            archiveAppraisalTask.setIsDel("1");
            commonMapper.updateById(archiveAppraisalTask);
        }
    }

    /*@Override
    public ResultEntity getCategoryCountByBatch(String id) {
        List<Map> mapList = commonMapper.selectByQuery(SqlQuery.from(ArchiveAppraisalTask.class, false)
                .column(ArchiveAppraisalTaskInfo.APPRAISALTYPE, ArchiveAppraisalTaskInfo.APPRAISALTYPE.count("count"))
                .equal(ArchiveAppraisalTaskInfo.BATCHID, id)
                .groupBy(ArchiveAppraisalTaskInfo.APPRAISALTYPE)
                .setReturnClass(Map.class));
        for (Map map : mapList) {
            if (map.get("appraisalType").equals("KFJD")){
                map.put("appraisalType","开放鉴定");
            }else if (map.get("appraisalType").equals("DQJD")){
                map.put("appraisalType","销毁鉴定");
            }else if (map.get("appraisalType").equals("MJJD")){
                map.put("appraisalType","密级鉴定");
            }else if (map.get("appraisalType").equals("BGQXJD")){
                map.put("appraisalType","保管期限鉴定");
            }else if (map.get("appraisalType").equals("JZJD")){
                map.put("appraisalType","价值鉴定");
            }
        }
        return ResultEntity.success(mapList);
    }*/

    @Override
    public ResultEntity endBatch(String batchId) throws IOException {
        SqlQuery sqlQuery = SqlQuery.from(ArchiveAppraisalTask.class)
                .equal(ArchiveAppraisalTaskInfo.BATCHID, batchId)
                .notEqual(ArchiveAppraisalTaskInfo.STATUS, 2);
        long w = commonMapper.countByQuery(sqlQuery);
        if (w > 0) {
            return ResultEntity.error("选中批次还有未完成的鉴定数据，请完成后在进行操作");
        }

        Map transMap = new HashMap();
        transMap.put("xh", "销毁");
        transMap.put("yq", "延期");
        String c = "<p style='text-align:center;'><span style='color:rgb(0,0,0);'>&nbsp; &nbsp;{{title}}</span></p><figure class='table'><table><tbody><tr><td colspan='4'>{{docName}}</td><td>{{year}}</td></tr><tr><td colspan='5'>{{content}}</td></tr><tr><td><p style='text-align:center;'><strong>{{assetsLists}}环节</strong></p></td><td><p style='text-align:center;'><strong>时间</strong></p></td><td><p style='text-align:center;'><strong>责任人</strong></p></td><td colspan='2'><p style='text-align:center;'><strong>意见</strong></p></td></tr><tr><td><p style='text-align:center;'>[hj]</p></td><td><p style='text-align:center;'>[time]</p></td><td><p style='text-align:center;'>[person]</p></td><td colspan='2'><p style='text-align:center;'>[opinion]</p></td></tr><tr><td colspan='5'><p style='text-align:center;'>{{result}}</p></td></tr></tbody></table></figure><p>&nbsp;</p>;";
        List<AppraisalOpenRange> appraisalOpenRangeList = commonMapper.selectAll(AppraisalOpenRange.class);
        for (AppraisalOpenRange appraisalOpenRange : appraisalOpenRangeList) {
            transMap.put(appraisalOpenRange.getId(), ("kz".equals(appraisalOpenRange.getAuxiliaryResult()) ? "控制" : "开放") + "--" + appraisalOpenRange.getOpenRange());
        }
        AppraisalBatch appraisalBatch = selectById(batchId);
        appraisalBatch.setStatus("2");
        updateById(appraisalBatch);
        String[] fondCodes = appraisalBatch.getFondCodes().split(",");
        for (String code : fondCodes) {
            String managementId = UUIDUtils.getUUID();
            Management management = new Management(managementId, code, ManagementService.SUB_TYPE_CODE_DAJDL_JDGZXCDBG, appraisalBatch.getBatchName() + "[" + code + "]", null, organisePersonService.getPersonById(appraisalBatch.getCreatePerson()).getUserName(), new SimpleDateFormat("yyyyMMdd").format(System.currentTimeMillis()), appraisalBatch.getStartVintages(), appraisalBatch.getEndVintages());
            List<ArchiveAppraisalTask> archiveAppraisalTaskList = commonMapper.selectByQuery(
                    SqlQuery.from(ArchiveAppraisalTask.class)
                            .equal(ArchiveAppraisalTaskInfo.BATCHID, batchId)
                            .equal(ArchiveAppraisalTaskInfo.FONDCODE, code)
            );

            c = c.replace("{{title}}", management.getTitle());

            Map<String, Integer> rsMap = new HashMap();
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
            cell.setCellValue("档号");

            cell = row.createCell(1);
            cell.setCellStyle(style);
            cell.setCellValue("题名");

            cell = row.createCell(2);
            cell.setCellStyle(style);
            cell.setCellValue("鉴定结果");

            String exp = commonFileConfig.getUploadDirWithDate("exp");
            File file = new File(exp);
            File file1 = fileCheck(file);

            FileOutputStream fileOutputStream = new FileOutputStream(file1);
            for (int i = 0; i < archiveAppraisalTaskList.size(); i++) {
                row = sheet.createRow(i + 1);

                cell = row.createCell(0);
                cell.setCellStyle(style);
                cell.setCellValue(archiveAppraisalTaskList.get(i).getArchiveCode());

                cell = row.createCell(1);
                cell.setCellStyle(style);
                cell.setCellValue(archiveAppraisalTaskList.get(i).getTitle());

                cell = row.createCell(2);
                cell.setCellStyle(style);
                cell.setCellValue("yq".equals(archiveAppraisalTaskList.get(i).getPersonResult()) ? "延期" + archiveAppraisalTaskList.get(0).getDelayYear() + "年" : transMap.get(archiveAppraisalTaskList.get(i).getPersonResult()).toString());
                if (rsMap.containsKey(archiveAppraisalTaskList.get(i).getPersonResult())) {
                    rsMap.put(archiveAppraisalTaskList.get(i).getPersonResult(), rsMap.get(archiveAppraisalTaskList.get(i).getPersonResult()) + 1);
                } else {
                    rsMap.put(archiveAppraisalTaskList.get(i).getPersonResult(), 1);
                }
            }
            StringBuilder content = new StringBuilder("本批次共鉴定档案" + archiveAppraisalTaskList.size() + "份，其中");
            for (String key : rsMap.keySet()) {
                content.append(transMap.get(key).toString()).append(rsMap.get(key)).append("份,");
            }
            content = new StringBuilder(content.substring(0, content.length() - 1));
            c = c.replace("{{content}}", content.toString());
            management.setCompilationContent(c);
            wb.write(fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            wb.close();
            FileInfo fileInfo = commonFileService.addFile(new FileSystemFileResource(file1), managementId);
            ManageFile fondManageFile = new ManageFile();
            fondManageFile.setBusinessId(managementId);
            fondManageFile.setFileInfoId(fileInfo.getId());
            managementService.insert(management);
            manageFileService.insert(fondManageFile);
        }
        return ResultEntity.success();
    }

    public File fileCheck(File file) throws IOException {
        if (!file.getPath().endsWith(".xls")) {
            if (!file.exists()) {
                file.mkdirs();
            }
            String fileName = "鉴定档案目录" + ".xls";
            file = new File(file.getPath(), fileName);
            file.createNewFile();
        } else if (!file.exists()) {
            //是文件且路径不对时直接返回异常信息
            throw new IllegalStateException("路径异常");
        }
        return file;
    }
}
