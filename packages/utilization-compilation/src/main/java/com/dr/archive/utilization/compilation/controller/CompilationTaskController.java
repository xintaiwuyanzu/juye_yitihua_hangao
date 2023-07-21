package com.dr.archive.utilization.compilation.controller;

import com.dr.archive.managefile.service.ManageFileService;
import com.dr.archive.managefondsdescriptivefile.entity.Management;
import com.dr.archive.util.UUIDUtils;
import com.dr.archive.utilization.compilation.entity.CompilationTask;
import com.dr.archive.utilization.compilation.entity.CompilationTaskInfo;
import com.dr.archive.utilization.compilation.service.CompilationTaskService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.file.autoconfig.CommonFileConfig;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/compilationtask")
public class CompilationTaskController extends BaseServiceController<CompilationTaskService, CompilationTask> {
    @Autowired
    CommonFileConfig commonFileConfig;
    @Autowired
    ManageFileService manageFileService;

    /**
     * @param request
     * @param entity
     * @return
     */
    @Override
    protected SqlQuery<CompilationTask> buildPageQuery(HttpServletRequest request, CompilationTask entity) {
        Person person = SecurityHolder.get().currentPerson();
        Organise organise = SecurityHolder.get().currentOrganise();
        SqlQuery sqlQuery = SqlQuery.from(CompilationTask.class);
        String type = request.getParameter("type");
        String status = request.getParameter("status");
        //result 为成果展示，发布后的数据本单位都可以查看，否则只能查看自己权限的数据
        if (!StringUtils.isEmpty(type) && "preview".equals(type)) {
            sqlQuery.equal(CompilationTaskInfo.ORGANID, organise.getId());
        } else if (!StringUtils.isEmpty(type) && "task".equals(type)) {//任务管理查询 除通过状态之外的数据
            sqlQuery.equal(CompilationTaskInfo.CREATEPERSON, person.getId());
            sqlQuery.notEqual(CompilationTaskInfo.STATUS, status);
        } else if (!StringUtils.isEmpty(type) && "result".equals(type)) {//成果管理查询 状态为1的数据
            sqlQuery.equal(CompilationTaskInfo.CREATEPERSON, person.getId());
            sqlQuery.equal(CompilationTaskInfo.STATUS, status);
        }

        return sqlQuery.like(CompilationTaskInfo.COMPILATIONTITLE, entity.getCompilationTitle()).equal(CompilationTaskInfo.TASKTYPE, entity.getTaskType()).equal(CompilationTaskInfo.PUBLISHSTATUS, entity.getPublishStatus()).orderByDesc(CompilationTaskInfo.CREATEDATE);
    }

    /**
     * 发布下线相关
     *
     * @param id
     * @param publishStatus
     * @return
     */
    @RequestMapping("/publish")
    public ResultEntity publish(String id, String publishStatus) {
        CompilationTask compilationTask = service.selectById(id);
        compilationTask.setPublishStatus(publishStatus);
        compilationTask.setPublishDate(System.currentTimeMillis());
        return ResultEntity.success(service.updateById(compilationTask));
    }

    /**
     * 上传ckeditor编辑器中的图片
     * TODO 删除需要处理，需要分文件夹存储上传图片
     *
     * @param file
     * @return
     */
    @RequestMapping("/uploadImg")
    public Map uploadImg(@RequestParam(value = "upload", required = false) MultipartFile file) {
        Map<String, Object> map = new HashMap<>();
        String fileFullPath = null;
        try {
            fileFullPath = commonFileConfig.uploadFile("img", file.getInputStream(), UUIDUtils.getUUID() + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        map.put("uploaded", true);
        map.put("url", commonFileConfig.getUploadDownLoadPath(fileFullPath));
        return map;
    }


    /**
     * 把本地图片地址转换为base64
     *
     * @param filePath
     * @return
     */
    public static String LocalImgToBase64(String filePath) {
        System.out.println("filePath:" + filePath);
        if (filePath == null) {
            return null;
        }
        File file = new File(filePath);
        ByteArrayOutputStream out = null;
        try {
            FileInputStream in = new FileInputStream(file);
            out = new ByteArrayOutputStream();
            byte[] b = new byte[2048];
            int i = 0;
            while ((i = in.read(b)) != -1) {
                out.write(b, 0, b.length);
            }
            out.close();
            in.close();
            // java.util.Base64.getEncoder().encodeToString ==》对字节数组Base64编码
            return Base64.getEncoder().encodeToString(out.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/htmlToWords")
    public void htmlToWords(HttpServletResponse response, HttpServletRequest request, String id, boolean onlyBody) throws Exception {
        Assert.isTrue(StringUtils.hasText(id), "id不能为空！");
        CompilationTask compilationTask = service.selectById(id);
        try {
            manageFileService.htmlToWords(compilationTask.getCompilationTitle(), compilationTask.getCompilationContent(), response, request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/htmlToPdf")
    public void htmlToPdf(HttpServletResponse response, String id, boolean onlyBody) {
        Assert.isTrue(!StringUtils.isEmpty(id), "id不能为空！");
        CompilationTask compilationTask = service.selectById(id);
        manageFileService.htmlToPdf(compilationTask.getCompilationTitle(), compilationTask.getCompilationContent(), false, response);
    }

    @RequestMapping("/AISave")
    public ResultEntity AISave(HttpServletRequest request) {
        //素材库id
        String materialCarBatchId = request.getParameter("batchId");
        //编研类型
        String compilationTemplateId = request.getParameter("compilationTemplateId");
        Assert.isTrue(!StringUtils.isEmpty(materialCarBatchId), "素材库id不能为空！");
        Assert.isTrue(!StringUtils.isEmpty(compilationTemplateId), "未选择编研模板！");
        service.AISave(materialCarBatchId, compilationTemplateId);
        return ResultEntity.success();
    }

    /**
     * 推送至全宗卷
     *
     * @param request
     * @return
     */
    @RequestMapping("/push")
    public ResultEntity push(HttpServletRequest request) {
        String id = request.getParameter("id");
        String fondCode = request.getParameter("fondCode");
        String managementTypeCode = request.getParameter("managementTypeCode");
        Assert.isTrue(StringUtils.hasText(id), "id不能为空！");
        Assert.isTrue(StringUtils.hasText(fondCode), "全宗号不能为空！");
        Assert.isTrue(StringUtils.hasText(managementTypeCode), "分类编码不能为空！");
        return ResultEntity.success(service.push(id, fondCode, managementTypeCode));
    }

    @RequestMapping("/getCompilationTaskList")
    public ResultEntity getCompilationTaskList(HttpServletRequest request, Management management) {
        return ResultEntity.success(service.getCompilationContent(request.getParameter("templateId"),management));
    }
}
