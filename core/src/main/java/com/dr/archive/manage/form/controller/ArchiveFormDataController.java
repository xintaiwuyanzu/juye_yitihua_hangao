package com.dr.archive.manage.form.controller;

import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.fond.service.FondService;
import com.dr.archive.manage.form.service.ArchiveDataManager;
import com.dr.archive.manage.form.service.MoveGoRecordingService;
import com.dr.archive.manage.log.annotation.SysLog;
import com.dr.archive.model.query.ArchiveDataQuery;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.file.service.CommonFileService;
import com.dr.framework.common.form.annotations.Form;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.form.core.service.FormDataService;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.web.annotations.Current;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 档案数据控制类
 *
 * @author dr
 */
@RestController
@RequestMapping(value = "/api/manage/formData")
public class ArchiveFormDataController {
    @Autowired
    ArchiveDataManager dataManager;
    @Autowired
    FormDataService formDataService;
    @Autowired
    CommonFileService commonFileService;
    @Autowired
    FondService fondService;
    @Autowired
    MoveGoRecordingService moveGoRecordingService;

    /**
     * 查询表单数据，查询条件完全由前台控制
     *
     * @param request
     * @param index
     * @param size
     * @return
     */
    @RequestMapping(value = "/formDataPage")
    @SysLog("查询档案列表页数据")
    public ResultEntity findArchiveData(HttpServletRequest request,
                                        ArchiveDataQuery query,
                                        @RequestParam(name = ArchiveDataQuery.QUERY_KEY, required = false) String queryContent,
                                        @RequestParam(defaultValue = "0") Integer index,
                                        @RequestParam(defaultValue = "15") Integer size) {
        // TODO,可能需要根据数据权限判断登陆人信息
        query.parseQuery(queryContent);
        if (index > 0) {
            index = index - 1;
        }
        // TODO,查询条件中添加全宗号
        if (StringUtils.hasText(query.getFondId())) {
            Fond fond = fondService.selectById(query.getFondId());
            ArchiveDataQuery.QueryItem queryItem = new ArchiveDataQuery.QueryItem("FOND_CODE", fond.getCode(), ArchiveDataQuery.QueryType.EQUAL);
            List<ArchiveDataQuery.QueryItem> queryItems = query.getQueryItems();
            queryItems.add(queryItem);
            query.setQueryItems(queryItems);
        }

        return ResultEntity.success(dataManager.formDataPage(query, index, size));
    }

    /**
     * @param formData   表单数据
     * @param fondId     全宗Id
     * @param categoryId 门类分类Id
     * @return
     */
    @RequestMapping(value = "/insertFormData")
    public ResultEntity insertFormData(@Form FormData formData,
                                       String fondId,
                                       String categoryId) {
        return ResultEntity.success(dataManager.insertFormData(formData, fondId, categoryId));
    }

    @RequestMapping(value = "/updateFormData")
    public ResultEntity updateFormData(HttpServletRequest request,
                                       @Form FormData formData,
                                       String fondId,
                                       String categoryId) {
        return ResultEntity.success(dataManager.updateFormData(formData, fondId, categoryId));
    }

    @RequestMapping(value = "/updateStatus")
    @SysLog("档案状态信息修改")
    public ResultEntity updateStatus(String ids, String status, String formDefinitionId) {
        dataManager.updateStatus(ids, status, formDefinitionId);
        return ResultEntity.success();
    }

    @RequestMapping(value = "/deleteFormData")
    public ResultEntity deleteFormData(String formId, String categoryId, String id) {
        return ResultEntity.success(dataManager.deleteFormData(categoryId, formId, id));
    }

    /**
     * 查询详情
     *
     * @param formDefinitionId
     * @param formDataId
     * @return
     */
    @RequestMapping(value = "/detail")
    public ResultEntity<FormData> detail(String formDefinitionId, String formDataId) {
        return ResultEntity.success(dataManager.selectOneFormData(formDefinitionId, formDataId));
    }

    @RequestMapping(value = "/repeat")
    @SysLog("档案信息查重")
    public ResultEntity repeat(String fond, String category, String formId, String status) {
        return ResultEntity.success(dataManager.repeat(fond, category, formId, status, null));
    }

    @RequestMapping(value = "/continuity")
    @SysLog("档案信息查重")
    public ResultEntity continuity(String fond, String category, String formId, String status) {
        return ResultEntity.success(dataManager.continuity(fond, category, formId, status, null));
    }

    /**
     * 调整档案到其他分类
     *
     * @param request
     * @param ids
     * @param fondId
     * @param categoryId
     * @return
     */
    @RequestMapping(value = "/adjustFormData")
    public ResultEntity adjustFormData(HttpServletRequest request,
                                       String ids,
                                       String formId,
                                       String fondId,
                                       String categoryId) {
        dataManager.adjustFormData(ids, formId, fondId, categoryId);
        return ResultEntity.success();
    }

    @RequestMapping(value = "/updateStatusByQuery")
    @SysLog("档案状态信息修改")
    public ResultEntity updateStatusByQuery(@RequestParam(name = ArchiveDataQuery.QUERY_KEY, required = false) String queryContent,
                                            ArchiveDataQuery query,
                                            String status) {
        query.parseQuery(queryContent);
        dataManager.updateStatusByQuery(query, status);
        return ResultEntity.success();
    }

    @RequestMapping(value = "/updateHaveYuanwen")
    public ResultEntity updateHaveYuanwen(String categoryId, String id, String formId) {
        if (commonFileService.count(id, "archive") > 0) {
            dataManager.updateHaveYuanwen(categoryId, id, formId, "1");
            return ResultEntity.success("1");
        } else {
            dataManager.updateHaveYuanwen(categoryId, id, formId, "0");
        }
        return ResultEntity.success("0");
    }

    /**
     * 组卷方法
     *
     * @param ajFormDefinitionId 案卷表单id
     * @param wjFormDefinitionId 文件表单id
     * @param ajId               案卷id
     * @param wjIds              待组卷文件ids
     * @param fondId             全宗id
     * @param categoryId         门类id
     * @return
     */
    @RequestMapping("groupDocument")
    public ResultEntity groupDocument(String ajFormDefinitionId, String wjFormDefinitionId, String ajId, String wjIds, String fondId, String categoryId) {
        dataManager.groupDocument(ajFormDefinitionId, wjFormDefinitionId, ajId, wjIds, fondId, categoryId);
        return ResultEntity.success();
    }

    /**
     * 拆卷方法
     *
     * @param ajFormDefinitionId 案卷表单id
     * @param wjFormDefinitionId 文件表单id
     * @param ids                案卷ids
     * @param fondId             全宗id
     * @param categoryId         门类id
     * @return
     */
    @RequestMapping("disassemble")
    public ResultEntity disassemble(String ajFormDefinitionId, String wjFormDefinitionId, String ids, String fondId, String categoryId) {
        dataManager.disassemble(ajFormDefinitionId, wjFormDefinitionId, ids, fondId, categoryId);
        return ResultEntity.success();
    }

    /**
     * 插卷方法
     *
     * @param ajFormDefinitionId 案卷表单id
     * @param wjFormDefinitionId 文件表单id
     * @param ajArchiveCode      案卷档号
     * @param ids                文件ids
     * @param fondId             全宗id
     * @param categoryId         门类id
     * @return
     */
    @RequestMapping("insertFile")
    public ResultEntity insertFile(String ajFormDefinitionId, String wjFormDefinitionId, String ajArchiveCode, String ids, String fondId, String categoryId) {
        boolean flag = dataManager.insertFile(ajFormDefinitionId, wjFormDefinitionId, ajArchiveCode, ids, fondId, categoryId);
        if (flag) {
            return ResultEntity.success();
        }
        return ResultEntity.error("未找到对应档案信息");
    }

    /**
     * 移卷方法
     *
     * @param formDefinitionId 文件表单id
     * @param ids              案卷ids
     * @param fondId           全宗id
     * @param categoryId       门类id
     * @return
     */
    @RequestMapping("removeFile")
    public ResultEntity removeFile(String formDefinitionId, String ids, String fondId, String categoryId) {
        dataManager.removeFile(formDefinitionId, ids, fondId, categoryId);
        return ResultEntity.success();
    }

    /**
     * 全宗号获取全宗信息
     */
    @RequestMapping("getFondByCode")
    public ResultEntity getFondByCode(String fondCode) {
        return ResultEntity.success(fondService.findFondByCode(fondCode));
    }

    /**
     * 档案移动门类
     * @param queryContent
     * @param query
     * @param newCategoryId
     * @return
     */
    @RequestMapping(value = "/updateCategoryByQuery")
    public ResultEntity updateCategoryByQuery(@RequestParam(name = ArchiveDataQuery.QUERY_KEY, required = false) String queryContent,
                                              ArchiveDataQuery query,
                                              String newCategoryId, String oldCategoryId, @Current Person person) {
        query.parseQuery(queryContent);
        moveGoRecordingService.updateCategoryByQuery(query, newCategoryId, oldCategoryId,person);
        return ResultEntity.success();
    }
}
