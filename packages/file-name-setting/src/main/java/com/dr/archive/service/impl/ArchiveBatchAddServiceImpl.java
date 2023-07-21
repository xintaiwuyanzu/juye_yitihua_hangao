package com.dr.archive.service.impl;

import com.dr.archive.entity.ArchivesBatchAdd;
import com.dr.archive.receive.offline.entity.ArchiveBatchReceiveOffline;
import com.dr.archive.service.ArchiveBatchAddService;
import com.dr.archive.service.ArchivesBatchAddDetailsService;
import com.dr.archive.util.SecurityHolderUtil;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.service.CommonService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.organise.service.OrganisePersonService;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.concurrent.Executor;

@Service
public class ArchiveBatchAddServiceImpl extends DefaultBaseService<ArchivesBatchAdd> implements ArchiveBatchAddService {
    @Autowired
    CommonService commonService;
    @Autowired
    ArchivesBatchAddDetailsService archivesBatchAddDetailsService;
    @Autowired
    @Qualifier("camundaTaskExecutor")
    protected Executor executor;
    @Autowired
    protected OrganisePersonService organisePersonService;

    /**
     * 批量目录著录
     *
     * @param formData
     */
    @Override
    public void newBatch(FormData formData) {
        Assert.isTrue(StringUtils.hasText(formData.getFormDefinitionId()), "请选择门类后再试");
        Person person = SecurityHolder.get().currentPerson();
        ArchiveBatchReceiveOffline archivesBatchAdd = new ArchiveBatchReceiveOffline();
        archivesBatchAdd.setBatchName(person.getUserName() + "提交的批量添加");
        archivesBatchAdd.setDetailNum(0);
        archivesBatchAdd.setStartDate(System.currentTimeMillis());
        archivesBatchAdd.setHookStatus("0");
        archivesBatchAdd.setHookFalseNum(0);
        archivesBatchAdd.setTypologic("BatchAdd");
        commonService.insert(archivesBatchAdd);
        //加线程
        String personId = SecurityHolder.get() == null ? "admin" : SecurityHolder.get().currentPerson().getId();
        SecurityHolder securityHolder = SecurityHolderUtil.checkSecurityHolder(organisePersonService, personId);
        executor.execute(() -> {
            SecurityHolder.set(securityHolder);
            archivesBatchAddDetailsService.newBatchDetails(formData, archivesBatchAdd, securityHolder.currentOrganise());
        });
    }
}
