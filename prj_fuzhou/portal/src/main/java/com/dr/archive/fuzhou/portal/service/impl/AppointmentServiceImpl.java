package com.dr.archive.fuzhou.portal.service.impl;

import com.dr.archive.fuzhou.portal.service.AppointmentService;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.core.organise.service.OrganisePersonService;
import com.dr.framework.core.security.service.ResourceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    CommonMapper commonMapper;

    @Autowired
    OrganisePersonService organisePersonService;

    @Autowired
    ResourceManager resourceManager;

   /* @Override
    public void addConsult(ConsultApply consultApply, String targetPersonId) {

        Person person = SecurityHolder.get().currentPerson();

        ArchiveBatch archiveBatch = new ArchiveBatch();
        //TODO 先设置上所查机构，后面添加上机构审核人员
        if (!StringUtils.isEmpty(consultApply.getToSendOrgId())) {

            Organise organise = organisePersonService.getOrganise(new OrganiseQuery.Builder().idEqual(consultApply.getToSendOrgId()).getQuery());

            consultApply.setToSendOrgName(organise.getOrganiseName());
        }
        archiveBatch.setBatchName("预约查档");
        archiveBatch.setStatus(StatusEntity.STATUS_DISABLE_STR);
        archiveBatch.setBatchType("CONSULT");
        archiveBatch.setStartDate(System.currentTimeMillis());
        CommonService.bindCreateInfo(archiveBatch);
        commonMapper.insert(archiveBatch);

        ArchiveTask archiveTask = new ArchiveTask();
        archiveTask.setTaskName("预约查档");
        archiveTask.setBatchId(archiveBatch.getId());
        archiveTask.setSourcePersonId(person.getId());
        archiveTask.setSourcePersonName(person.getUserName());
        archiveTask.setStatus(StatusEntity.STATUS_DISABLE_STR);
        archiveTask.setTaskType("CONSULT");

        //Person personById = organisePersonService.getPersonById(targetPersonId);
        //TODO 需要设置成查档单位默认的审核接收人，暂时写成系统管理员
        archiveTask.setTargetPersonId("admin");
        archiveTask.setTargetPersonName("系统管理员");

        archiveTask.setSourceDate(System.currentTimeMillis());
        CommonService.bindCreateInfo(archiveTask);
        commonMapper.insert(archiveTask);
        List<Fond> fondList = (List<Fond>) resourceManager.getPersonResources(SecurityHolder.get().currentPerson().getId(), "fond");
        if(fondList != null && fondList.size() != 0){
            consultApply.setDocOrgId(fondList.get(0).getId());
            consultApply.setDocOrgName(fondList.get(0).getName());
        }
        consultApply.setBatchId(archiveBatch.getId());
        CommonService.bindCreateInfo(consultApply);
        commonMapper.insert(consultApply);
    }*/
}
