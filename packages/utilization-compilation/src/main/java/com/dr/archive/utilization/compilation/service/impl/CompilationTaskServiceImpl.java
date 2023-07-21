package com.dr.archive.utilization.compilation.service.impl;

import com.dr.archive.archivecar.entity.ArchiveCarBatch;
import com.dr.archive.archivecar.entity.ArchiveCarDetail;
import com.dr.archive.archivecar.entity.ArchiveCarDetailInfo;
import com.dr.archive.archivecar.service.ArchiveCarBatchService;
import com.dr.archive.archivecar.service.ArchiveCarDetailService;
import com.dr.archive.archivecar.service.ArchiveCarTypeProvider;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.fond.service.FondOrganiseService;
import com.dr.archive.manage.template.entity.CompilationTemplate;
import com.dr.archive.manage.template.service.CompilationTemplateService;
import com.dr.archive.managefile.entity.ManageFile;
import com.dr.archive.managefile.service.ManageFileService;
import com.dr.archive.managefondsdescriptivefile.entity.Management;
import com.dr.archive.managefondsdescriptivefile.entity.ManagementInfo;
import com.dr.archive.managefondsdescriptivefile.service.ManagementService;
import com.dr.archive.util.Constants;
import com.dr.archive.util.UUIDUtils;
import com.dr.archive.utilization.compilation.entity.*;
import com.dr.archive.utilization.compilation.entity.CompilationTaskDetailInfo;
import com.dr.archive.utilization.compilation.entity.CompilationTaskInfo;
import com.dr.archive.utilization.compilation.service.CompilationTaskDetailService;
import com.dr.archive.utilization.compilation.service.CompilationTaskHistoryService;
import com.dr.archive.utilization.compilation.service.CompilationTaskService;
import com.dr.archive.utilization.compilation.vo.ImageVo;
import com.dr.framework.common.file.model.FileInfo;
import com.dr.framework.common.file.service.CommonFileService;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.process.service.ProcessConstants;
import com.dr.framework.core.process.service.ProcessContext;
import com.dr.framework.core.process.service.ProcessTypeProvider;
import com.dr.framework.core.process.service.TaskContext;
import com.dr.framework.core.security.SecurityHolder;
import com.dr.framework.sys.service.DefaultOrganisePersonService;
import com.dr.framework.util.DateTimeUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.dr.archive.utilization.compilation.entity.CompilationTaskDetail.BUSINESS_TYPE_ARCHIVE;
import static com.dr.archive.utilization.compilation.service.CompilationTemplateConstants.TYPE_COMPILATION_TEMPLATE_PICTURE;


@Service
@Transactional(rollbackFor = Exception.class)
public class CompilationTaskServiceImpl extends DefaultBaseService<CompilationTask> implements CompilationTaskService, ArchiveCarTypeProvider, ProcessTypeProvider {
    public static final String DATE_PATTEN = "yyyy-MM-dd HH:mm:ss";
    static final Logger logger = LoggerFactory.getLogger(CompilationTaskService.class);
    static final String[] imgType = {"JPG", "JPEG", "PNG"};
    static final String[] templateType = {"DSJ", "QZJS", "ZZJGYG", "ZCSZHJ", "ZTHB"};
    @Autowired
    ArchiveCarBatchService carBatchService;
    @Autowired
    ArchiveCarDetailService carDetailService;
    @Autowired
    CompilationTaskHistoryService compilationTaskHistoryService;
    @Autowired
    CommonFileService commonFileService;
    @Autowired
    CompilationTemplateService compilationTemplateService;
    @Autowired
    ManageFileService manageFileService;
    @Autowired
    ManagementService managementService;
    @Autowired
    FondOrganiseService fondOrganiseService;
    @Autowired
    DefaultOrganisePersonService defaultOrganisePersonService;
    @Autowired
    CompilationTaskDetailService compilationTaskDetailService;
    @Autowired
    CompilationTaskService compilationTaskService;
    @Value("${common.api-path:/api}")
    String apiPath = "/api";
    @Value("${common.file.webPath:files}")
    String filesPath = "files";

    @Override
    public long insert(CompilationTask entity) {
        Organise organise = SecurityHolder.get().currentOrganise();
        entity.setPushed("0");
        entity.setOrganId(organise.getId());
        entity.setOrganCode(organise.getOrganiseCode());
        entity.setOrganName(organise.getOrganiseName());
        entity.setCreatPersonName(SecurityHolder.get().currentPerson().getUserName());
        super.insert(entity);

        //同步创建档案车
        ArchiveCarBatch carBatch = new ArchiveCarBatch();
        //档案车Id与档案查档批次相同
        carBatch.setId(entity.getId());
        carBatch.setBatchName("编研车【" + entity.getCompilationTitle() + "】");
        carBatch.setBatchType(getType());
        carBatchService.insert(carBatch);
        return 1;
    }

    /**
     * 删除 相关附件表、 档案车批次、历史记录
     */
    @Override
    public long delete(SqlQuery<CompilationTask> sqlQuery) {
        CompilationTask compilationTask = selectOne(sqlQuery);
        //删除档案车批次
        carBatchService.deleteById(compilationTask.getId());
        //删除编研历史记录
        compilationTaskHistoryService.deleteByTaskId(compilationTask.getId());
        //删除相关附件
        manageFileService.deleteByRefId(compilationTask.getId());
        return super.delete(sqlQuery);
    }

    /**
     * 启动流程前调用
     *
     * @param context
     */
    @Override
    public void onBeforeStartProcess(ProcessContext context) {
        //businessId:是任务id
        context.setBusinessId((String) context.getBusinessParams().get("businessId"));
        context.setProcessInstanceTitle(DateFormatUtils.format(new Date(), DATE_PATTEN) + "发起的" + getName() + "审核");
        //设置流程查看详情跳转页面
        if (TYPE_COMPILATION_TEMPLATE_PICTURE.equals(context.getBusinessParams().get("compilationType"))) {
            context.addVar(ProcessConstants.PROCESS_FORM_URL_KEY, "/compilation/pictureEdit");
        } else {
            context.addVar(ProcessConstants.PROCESS_FORM_URL_KEY, "/compilation/edit");
        }
        context.addVar(ProcessConstants.PROCESS_BUSINESS_KEY, context.getBusinessId());
    }

    /**
     * 启动流程后调用
     *
     * @param context
     */
    @Override
    public void onAfterStartProcess(ProcessContext context) {
        CompilationTask compilationTask = new CompilationTask();
        compilationTask.setId(context.getBusinessId());
        compilationTask.setStatus("1");
        commonMapper.updateIgnoreNullById(compilationTask);
        ProcessTypeProvider.super.onAfterStartProcess(context);
    }

    /**
     * 发送按钮完成后
     *
     * @param context
     */
    @Override
    public void onAfterCompleteTask(TaskContext context) {
        //TODO 先写死一些参数
        String processInstanceId = context.getTaskInstance().getProcessInstanceId();
        String taskId = context.getTaskInstance().getId();
        String businessId = (String) context.getBusinessParams().get("businessId");
        //添加历史记录
        addCompilationTaskHistory(processInstanceId, taskId, businessId, "add", "1", "");
        //TODO 修改正文内容
        CompilationTask compilationTask = new CompilationTask();
        compilationTask.setId((String) context.getBusinessParams().get("businessId"));
        if (!TYPE_COMPILATION_TEMPLATE_PICTURE.equals(context.getBusinessParams().get("compilationType"))) {
            compilationTask.setCompilationContent((String) context.getBusinessParams().get("compilationContent"));
            commonMapper.updateIgnoreNullById(compilationTask);
        }
        ProcessTypeProvider.super.onAfterCompleteTask(context);
    }

    /**
     * 办结完成后
     *
     * @param context
     */
    @Override
    public void onAfterEndProcess(TaskContext context) {
        //TODO 先写死一些参数
        String processInstanceId = context.getTaskInstance().getProcessInstanceId();
        String taskId = context.getTaskInstance().getId();
        String businessId = (String) context.getBusinessParams().get("businessId");
        String isPass = (String) context.getBusinessParams().get("isPass");
        CompilationTask compilationTask = selectById((String) context.getBusinessParams().get("businessId"));
        compilationTask.setId((String) context.getBusinessParams().get("businessId"));
        compilationTask.setStatus(isPass);
        if (!TYPE_COMPILATION_TEMPLATE_PICTURE.equals(context.getBusinessParams().get("compilationType"))) {
            //添加历史记录
            addCompilationTaskHistory(processInstanceId, taskId, businessId, "add", isPass, "");
            compilationTask.setCompilationContent((String) context.getBusinessParams().get("compilationContent"));
            //通过后生成pdf,不生成pdf了从编研成果中进行下载
//            if ("2".equals(isPass)) {
//                manageFileService.htmlToPdf(compilationTask.getId(), compilationTask.getCompilationTitle(), compilationTask.getCompilationContent());
//            }
        }
        commonMapper.updateIgnoreNullById(compilationTask);
        ProcessTypeProvider.super.onAfterEndProcess(context);
    }

    @Override
    public String getType() {
        return Constants.PROCESS_TYPE_COMPILATION;
    }

    @Override
    public String getName() {
        return "编研成果";
    }

    //编研类型的档案车添加数据的时候，编研详情同步添加数据
    @Override
    public void onInsertCar(String carType, ArchiveCarDetail carDetail, FormData formData) {
        //编研详情表中放数据
        CompilationTaskDetail compilationTaskDetail = new CompilationTaskDetail();
        copyCarDetailToCompilationTaskDetail(carDetail, compilationTaskDetail);
        compilationTaskDetailService.insert(compilationTaskDetail);
    }

    /**
     * 从档案车详情复制到编研详情
     *
     * @param carDetail
     * @param compilationTaskDetail
     */
    void copyCarDetailToCompilationTaskDetail(ArchiveCarDetail carDetail, CompilationTaskDetail compilationTaskDetail) {
        compilationTaskDetail.setBatchId(carDetail.getBatchId());
        compilationTaskDetail.setArchiveCode(carDetail.getArchiveCode());
        compilationTaskDetail.setTitle(carDetail.getTitle());
        compilationTaskDetail.setYear(carDetail.getYear());
        compilationTaskDetail.setFondCode(carDetail.getFondCode());
        compilationTaskDetail.setSourceType(BUSINESS_TYPE_ARCHIVE);
        compilationTaskDetail.setCategoryCode(carDetail.getCategoryCode());
        compilationTaskDetail.setFormDataId(carDetail.getFormDataId());
        compilationTaskDetail.setFormDefinitionId(carDetail.getFormDefinitionId());
        compilationTaskDetail.setBusinessType(getType());
    }

    @Override
    public void AISave(String materialCarBatchId, String compilationTemplateId) {
        //根据档案车查询档案
        List<ArchiveCarDetail> archiveCarDetailList = getArchiveCarDetailListByBatchId(materialCarBatchId);
        Assert.isTrue(archiveCarDetailList.size() > 0, "未查询到素材，无法自动编研");
        //根据模板id查询模板
        CompilationTemplate compilationTemplate = compilationTemplateService.selectById(compilationTemplateId);
        //需要根据不同模板code生成不同内容
        Map<String, Object> dataMap = new HashMap<>();
        CompilationTask compilationTask = selectById(materialCarBatchId);
        dataMap.put("compilationTitle", compilationTask.getCompilationTitle());
        dataMap.put("compilationAbstract", compilationTask.getCompilationAbstract());
        if (ArrayUtils.contains(templateType, compilationTemplate.getTemplateTypeCode().toUpperCase())) {
            dataMap.put("archiveCarDetailList", archiveCarDetailList);
            //更新内容
            compilationTask.setCompilationContent(compilationTemplateService.getHtml(dataMap, compilationTemplate.getCompilationContent()));
            updateById(compilationTask);
        }
        //TODO 此处改成从前台判断了，可以删掉
//        else if (TYPE_COMPILATION_TEMPLATE_PICTURE.equalsIgnoreCase(compilationTemplate.getTemplateTypeCode())) {
//            List<FileInfo> fileInfoList = new ArrayList<>();
//            for (CompilationTaskDetail archiveCarDetail : compilationTaskDetailList) {
//                fileInfoList.addAll(commonFileService.list(archiveCarDetail.getFormDataId(), "archive"));
//            }
//            dataMap.put("imgList", getImageVoList(fileInfoList));
//            //TODO 替换图片
//            //更新内容
//            compilationTask.setCompilationContent(compilationTemplateService.getHtml(dataMap, compilationTemplate.getCompilationContent()));
//            updateById(compilationTask);
//        }
    }

    /**
     * 把本地图片流，转换为base64
     *
     * @return
     */
    public static String LocalImgToBase64(InputStream inputStream) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            byte[] b = new byte[1024];
            int i = 0;
            while ((i = inputStream.read(b)) != -1) {
                out.write(b, 0, b.length);
            }
            inputStream.close();
            return Base64.getEncoder().encodeToString(out.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    List<ArchiveCarDetail> getArchiveCarDetailListByBatchId(String batchId) {
        return carDetailService.selectList(SqlQuery.from(ArchiveCarDetail.class).equal(ArchiveCarDetailInfo.BATCHID, batchId));
    }

    /**
     * 添加全宗卷历史表
     *
     * @param processInstanceId  流程实例
     * @param taskInstanceId     任务实例
     * @param compilationTaskId  全宗卷id
     * @param historyType        类型
     * @param isPass             是否通过
     * @param examinationOpinion 审批意见
     */
    private void addCompilationTaskHistory(String processInstanceId, String taskInstanceId, String compilationTaskId, String historyType, String isPass, String examinationOpinion) {
        CompilationTaskHistory compilationTaskHistory = new CompilationTaskHistory();
        BeanUtils.copyProperties(selectById(compilationTaskId), compilationTaskHistory);
        compilationTaskHistory.setId(UUIDUtils.getUUID());
        compilationTaskHistory.setProcessInstanceId(processInstanceId);
        compilationTaskHistory.setTaskInstanceId(taskInstanceId);
        compilationTaskHistory.setCompilationTaskId(compilationTaskId);
        compilationTaskHistory.setHistoryType(historyType);
        compilationTaskHistory.setIsPass(isPass);
        compilationTaskHistory.setExaminationOpinion(examinationOpinion);
        compilationTaskHistoryService.insert(compilationTaskHistory);
    }

    @Override
    public long push(String id, String fondCode, String managementTypeCode) {
        CompilationTask compilationTask = selectById(id);
        //如果推送过 就不再推送了 返回 0
        if (compilationTask.getPushed() == null || !compilationTask.getPushed().equals("push")) {
            compilationTask.setPushed("push");
            compilationTaskService.updateById(compilationTask);
            String managementId = UUIDUtils.getUUID();
            Person person = defaultOrganisePersonService.getPersonById(compilationTask.getCreatePerson());
            Management management = new Management(managementId, fondCode, managementTypeCode, compilationTask.getCompilationTitle(), compilationTask.getCompilationContent(), person.getUserName(), new SimpleDateFormat("yyyyMMdd").format(compilationTask.getCreateDate()), new SimpleDateFormat("yyyy").format(compilationTask.getCreateDate()), new SimpleDateFormat("yyyy").format(compilationTask.getCreateDate()));
            List<ManageFile> manageFileList = manageFileService.getManageFileListByBussinessId(compilationTask.getId());
            manageFileList.forEach(manageFile -> {
                ManageFile fondManageFile = new ManageFile();
                fondManageFile.setBusinessId(managementId);
                fondManageFile.setFileInfoId(manageFile.getFileInfoId());
                if (StringUtils.hasText(manageFile.getFileName())) {
                    manageFileService.insert(fondManageFile);
                }
            });
            return managementService.insert(management);
        } else {
            return 0;
        }

    }

    /**
     * 推送之前判断全宗是否唯一，是否配置相关模板
     *
     * @return
     */
    public String beforePush(String id) {
        CompilationTask compilationTask = selectById(id);
        List<Fond> fondList = fondOrganiseService.getFondListByOrganiseId(SecurityHolder.get().currentOrganise().getId());
        CompilationTemplate compilationTemplate = commonMapper.selectById(CompilationTemplate.class, compilationTask.getTemplateId());
        if (null == fondList) {
            //未查询到全宗
            return "false_1";
        } else if (fondList.size() > 0) {
            //查询到多条全宗
            return "false_2";
        } else if (StringUtils.isEmpty(compilationTemplate)) {
            //未查询到全宗卷模板
            return "false_3";
        } else {
            return "1";
        }
    }

    @Override
    public String getCompilationContent(String templateId, Management management) {
        Assert.isTrue(StringUtils.hasText(templateId), "模板编码不能为空！");
        Assert.isTrue(StringUtils.hasText(management.getVintagesStart()) && StringUtils.hasText(management.getVintagesEnd()), "起止年度不能为空！");
        //获取模板
        CompilationTemplate compilationTemplate = compilationTemplateService.selectById(templateId);


        Long sTime = DateTimeUtils.stringToMillis(management.getVintagesStart() + "-01-01 00:00:00", "yyyy-MM-dd hh:mm:ss");
        Long eTime = DateTimeUtils.stringToMillis(management.getVintagesEnd() + "-12-31 23:59:59", "yyyy-MM-dd hh:mm:ss");
        List<CompilationTask> compilationTaskList = getCompilationTaskList(sTime, eTime);
        compilationTaskList.forEach(i -> i.setPublishDate(Long.parseLong(DateTimeUtils.longToDate(i.getCreateDate(), "yyyy"))));
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("archiveCarDetailList", compilationTaskList);

        dataMap.put("vintagesStart", management.getVintagesStart());
        dataMap.put("vintagesEnd", management.getVintagesEnd());
        return compilationTemplateService.getHtml(dataMap, compilationTemplate.getCompilationContent());
    }

    public List<CompilationTask> getCompilationTaskList(long s, long e) {
        //查询本单位 审核通过的 本年度
        return commonMapper.selectByQuery(SqlQuery.from(CompilationTask.class)
                .lessThanEqual(CompilationTaskInfo.CREATEDATE, e)
                .greaterThanEqual(CompilationTaskInfo.CREATEDATE, s)
                .equal(CompilationTaskInfo.ORGANID, SecurityHolder.get().currentOrganise().getId())
                .equal(CompilationTaskInfo.STATUS, '2'));
    }

    @Override
    public List<ImageVo> getImgListByCarBatchId(String batchId) {
        //根据批次查询档案车详情
        List<ArchiveCarDetail> carDetailList = carDetailService.selectByBatchAndTag(batchId, null);
        List<FileInfo> fileInfos = new ArrayList<>();
        //查询所有文件
        for (ArchiveCarDetail archiveCarDetail : carDetailList) {
            fileInfos.addAll(commonFileService.list(archiveCarDetail.getFormDataId(), "archive"));
        }
        return getImageVoList(fileInfos);
    }

    @Override
    public String addCarToTask(String selectdetailIds, String taskId) {
        String[] carIdArray = selectdetailIds.split(",");
        int allCount = carIdArray.length;
        int success = 0;
        for (String carId : carIdArray) {
            ArchiveCarDetail archiveCarDetail = carDetailService.selectById(carId);
            SqlQuery<CompilationTaskDetail> sqlQuery = SqlQuery.from(CompilationTaskDetail.class)
                    .equal(CompilationTaskDetailInfo.FORMDATAID, archiveCarDetail.getFormDataId())
                    .equal(CompilationTaskDetailInfo.BATCHID, archiveCarDetail.getBatchId());
            boolean exists = commonMapper.existsByQuery(sqlQuery);
            if (exists) {
                continue;
            }
            CompilationTaskDetail compilationTaskDetail = new CompilationTaskDetail();
            compilationTaskDetail.setBatchId(taskId);
            compilationTaskDetail.setOrgCode(archiveCarDetail.getOrgCode());
            compilationTaskDetail.setFormDataId(archiveCarDetail.getFormDataId());
            compilationTaskDetail.setFormDefinitionId(archiveCarDetail.getFormDefinitionId());
            compilationTaskDetail.setCategoryCode(archiveCarDetail.getCategoryCode());
            compilationTaskDetail.setArchiveCode(archiveCarDetail.getArchiveCode());
            compilationTaskDetail.setFondCode(archiveCarDetail.getFondCode());
            compilationTaskDetail.setTitle(archiveCarDetail.getTitle());
            compilationTaskDetail.setKeyWords(archiveCarDetail.getKeyWords());
            compilationTaskDetail.setYear(archiveCarDetail.getYear());
            compilationTaskDetail.setSaveTerm(archiveCarDetail.getSaveTerm());
            compilationTaskDetail.setId(UUIDUtils.getUUID());
            compilationTaskDetail.setCreateDate(System.currentTimeMillis());
            compilationTaskDetail.setUpdateDate(System.currentTimeMillis());
            compilationTaskDetail.setStatus("");
            //直接保存
            commonMapper.insertIgnoreNull(compilationTaskDetail);
            success++;
        }
        return "总计推送" + allCount + "条,成功" + success + "条,失败" + (allCount - success) + "条";
    }

    /**
     * 把附件转imageVo
     *
     * @param fileInfos
     * @return
     */
    private List<ImageVo> getImageVoList(List<FileInfo> fileInfos) {
        List<ImageVo> imageVoList = new ArrayList<>();
        List<FileInfo> fileInfoList = fileInfos.stream().filter(f -> ArrayUtils.contains(imgType, f.getSuffix().toUpperCase())).collect(Collectors.toList());
        fileInfoList.forEach(fileInfo -> {
            ImageVo imageVo = new ImageVo();
            imageVo.setName(fileInfo.getName());
            imageVo.setDescription(fileInfo.getDescription());
            imageVo.setFileInfoId(fileInfo.getId());
            String imgPath = apiPath + "/" + filesPath + "/downLoad/" + fileInfo.getId();
            imageVo.setUrl(imgPath);
            imageVoList.add(imageVo);
        });
        return imageVoList;
    }

}