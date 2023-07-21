package com.dr.archive.fuzhou.approve.detection;

import com.dr.archive.detection.service.ItemDetectContext;
import com.dr.archive.detection.service.TestRecordService;
import com.dr.archive.fuzhou.approve.service.impl.ApprovalOnlineReceiveProvider;
import com.dr.archive.manage.form.service.ArchiveDataManager;
import com.dr.archive.model.entity.ArchiveEntity;
import com.dr.archive.model.query.ArchiveDataQuery;
import com.dr.archive.transfer.bo.TransferInfo;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.form.core.model.FormData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

/**
 * 1-2-2 通过对比接收到归档信息包的(电子文件号/档号)与系统内该全宗门类的电子文件号是否重复
 *
 * @author: qiuyf
 * @date: 2022/4/20 14:21
 */
@Component
public class Authenticity003Service extends AbstractApproveReceiveDetectService {
    @Autowired
    CommonMapper commonMapper;
    @Autowired
    ArchiveDataManager archiveDataManager;

    @Override
    public String code() {
        return "Authenticity003";
    }

    @Override
    public String modeCode() {
        return "1-2-2";
    }

    @Override
    public void detection(ItemDetectContext context) {
        String gdType = context.getAttribute("gdType");
        //判断归档类型是在线接收还是离线接收
        if ("Online".equals(gdType)) {
            //取归档信息包中的电子文件号或档号 审批系统中取电子文件号, 数字化取档号
            TransferInfo.TransferDirectory directory = context.getAttribute(ApprovalOnlineReceiveProvider.DIRECTORY_KEY);
            Assert.notNull(directory, "交接单据未找到");
            String recordCode = Optional.ofNullable(directory.getDocumentNumber()).orElse(directory.getArchivalCode());
            //系统内的电子文件号/档号
            ArchiveDataQuery query = new ArchiveDataQuery();
            ArchiveDataQuery.QueryItem queryItem = new ArchiveDataQuery.QueryItem(ArchiveEntity.COLUMN_ARCHIVE_CODE, recordCode, ArchiveDataQuery.QueryType.EQUAL);
            List<ArchiveDataQuery.QueryItem> queryItems = query.getQueryItems();
            queryItems.add(queryItem);
            query.setFormDefinitionId(context.getFormData().getFormDefinitionId());
            query.setQueryItems(queryItems);
            List<FormData> formData = archiveDataManager.findDataByQuery(query);
            if (formData.size() > 1) {
                context.addRecordItem(code(), modeCode(),
                        "电子文件号/档号在系统中已存在!",
                        TestRecordService.TEST_STATUS_FAIL,
                        "archiveCode",
                        "电子文件号/档号",
                        recordCode);
            } else {
                context.addRecordItem(code(), modeCode());
            }
        } else {
            //离线接收
            String archiveCode = context.getFormData().get(ArchiveEntity.COLUMN_ARCHIVE_CODE);
            ArchiveDataQuery query = new ArchiveDataQuery();
            ArchiveDataQuery.QueryItem queryItem = new ArchiveDataQuery.QueryItem(ArchiveEntity.COLUMN_ARCHIVE_CODE, archiveCode, ArchiveDataQuery.QueryType.EQUAL);
            List<ArchiveDataQuery.QueryItem> queryItems = query.getQueryItems();
            queryItems.add(queryItem);
            query.setFormDefinitionId(context.getFormData().getFormDefinitionId());
            query.setQueryItems(queryItems);
            List<FormData> formData = archiveDataManager.findDataByQuery(query);
            if (formData.size() > 1) {
                context.addRecordItem(code(), modeCode(),
                        "电子文件号/档号在系统中已存在!",
                        TestRecordService.TEST_STATUS_FAIL,
                        "archiveCode",
                        "电子文件号/档号",
                        archiveCode);
            } else {
                context.addRecordItem(code(), modeCode());
            }
        }
    }

}
