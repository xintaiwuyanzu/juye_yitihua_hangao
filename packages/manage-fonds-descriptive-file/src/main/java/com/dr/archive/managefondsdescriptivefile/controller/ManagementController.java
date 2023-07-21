package com.dr.archive.managefondsdescriptivefile.controller;

import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.managefondsdescriptivefile.entity.Management;
import com.dr.archive.managefondsdescriptivefile.entity.ManagementInfo;
import com.dr.archive.managefondsdescriptivefile.service.ManagementService;
import com.dr.archive.util.Constants;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import com.dr.framework.core.security.service.ResourceManager;
import com.dr.framework.core.web.annotations.Current;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping("api/management")
public class ManagementController extends BaseServiceController<ManagementService, Management> {
    @Autowired
    ResourceManager resourceManager;

    @Override
    protected SqlQuery<Management> buildPageQuery(HttpServletRequest request, Management entity) {
        SqlQuery sqlQuery = SqlQuery.from(Management.class)
                .equal(ManagementInfo.FONDID, entity.getFondId())//全宗id必填
                .like(ManagementInfo.TITLE, entity.getTitle()).like(ManagementInfo.MANAGEMENTTYPECODE, entity.getManagementTypeCode()).like(ManagementInfo.ARCHIVE_CODE, entity.getArchive_code()).like(ManagementInfo.FOND_CODE, entity.getFond_code()).like(ManagementInfo.RETENTIONPERIOD, entity.getRetentionPeriod()).like(ManagementInfo.TOTALNUMBEROFPAGES, entity.getTotalNumberOfPages()).orderByDesc(ManagementInfo.FILETIME).orderByDesc(ManagementInfo.CREATEDATE);
        //档案馆账户只能查看移交之后的数据,或者自己添加的数据(原则上档案馆不添加数据，档案局人员会添加数据，所以需要添加or语句）。状态为1，已移交
        if (Constants.ORG_TYPE_DAG.equals(SecurityHolder.get().currentOrganise().getOrganiseType())) {
            sqlQuery.andNew().equal(ManagementInfo.TRANSFERSTATUS, '1').or().equal(ManagementInfo.CREATEPERSON, SecurityHolder.get().currentPerson().getId());
        }
        return sqlQuery;
    }

    @RequestMapping("/getFondsList")
    public ResultEntity getFondsList(HttpServletRequest request, @Current Organise organise, @Current Person person) {
        return ResultEntity.success((List<Fond>) resourceManager.getPersonResources(person.getId(), "fond"));
    }

    /**
     * 全宗卷查看：根据全宗id查询全宗卷数量
     *
     * @return
     */
    @RequestMapping("/getFondsCountByYear")
    public ResultEntity getFondsCountByYear(HttpServletRequest request) {
        String fondId = request.getParameter("fondId");
        Assert.isTrue(!StringUtils.isEmpty(fondId), "全宗id不能为空！");
        return ResultEntity.success(service.getFondsCountByYear(fondId));
    }

    /**
     * 审核时保存
     * 说明：
     * 1、审批时直接在元数据中修改
     * 2、审批通过的保存：把新修改后的数据暂存到其他地方，修改通过之后，再显示新的内容项
     *
     * @param request
     * @param management
     * @param optType
     * @return
     */
    @RequestMapping("/updateByExamin")
    public ResultEntity updateByExamin(HttpServletRequest request, Management management, String optType) {
        String processInstanceId = request.getParameter("processInstanceId");
        String taskId = request.getParameter("taskId");
        return ResultEntity.success(service.updateByExamin(processInstanceId, taskId, optType, management));
    }
}