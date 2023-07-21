package com.dr.archive.fuzhou.approve.fileReturn.service.impl;

import com.dr.archive.fuzhou.approve.entity.ArchivePackResult;
import com.dr.archive.fuzhou.approve.entity.Receipt;
import com.dr.archive.fuzhou.approve.entity.ReceiptInfo;
import com.dr.archive.fuzhou.approve.fileReturn.service.ReturnService;
import com.dr.archive.fuzhou.approve.service.ApproveCenterClient;
import com.dr.archive.fuzhou.approve.service.ArchiveReceiptService;
import com.dr.archive.fuzhou.approve.service.ReceiptService;
import com.dr.archive.manage.category.service.CategoryService;
import com.dr.archive.manage.fond.service.FondService;
import com.dr.archive.manage.form.service.ArchiveDataManager;
import com.dr.archive.manage.form.service.impl.DefaultArchiveDataManager;
import com.dr.archive.batch.query.BatchCreateQuery;
import com.dr.archive.model.entity.ArchiveEntity;
import com.dr.archive.receive.online.entity.ArchiveBatchDetailReceiveOnline;
import com.dr.archive.receive.online.entity.ArchiveBatchDetailReceiveOnlineInfo;
import com.dr.archive.receive.online.service.ArchiveBatchDetailReceiveOnlineService;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.form.core.service.FormDataService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: caor
 * @Date: 2021-12-01 16:04
 * @Description:
 */
@Service
public class ReturnServiceImpl implements ReturnService {
    @Autowired
    ArchiveReceiptService archiveReceiptService;
    @Autowired
    ArchiveDataManager archiveDataManager;
    @Autowired
    ArchiveBatchDetailReceiveOnlineService archiveBatchDetailReceiveOnlineService;
    @Autowired
    ApproveCenterClient approveCenterClient;
    @Autowired
    DefaultArchiveDataManager defaultArchiveDataManager;
    @Autowired
    CommonMapper commonMapper;
    @Autowired
    FormDataService formDataService;
    @Autowired
    protected FondService fondService;
    @Autowired
    protected CategoryService categoryService;

    @Override
    public ResultEntity fileReturnCheck(BatchCreateQuery query) {
        List<FormData> formDataList = defaultArchiveDataManager.findDataByQuery(query);
        List<String> list = new ArrayList<>();
        if (formDataList.size() > 0) {
            for (FormData formData : formDataList) {
                String org = formData.get(ArchiveEntity.COLUMN_ORGANISEID);
                if (!StringUtils.isEmpty(org)) {
                    if (!SecurityHolder.get().currentOrganise().getId().equals(formData.get(ArchiveEntity.COLUMN_ORGANISEID))) {
                        list.add("<p>" + formData.get(ArchiveEntity.COLUMN_TITLE) + " " + formData.get(ArchiveEntity.COLUMN_ARCHIVE_CODE) + "</p>");
                    }
                }
            }
            if (list.size() > 0) {
                return ResultEntity.error("已移交的档案不能退回,条数为：" + list.size() + " 档号及题名分别是: " + String.join("", list));
            }
        } else {
            return ResultEntity.error("未查询到档案信息！");
        }
        return ResultEntity.success();
    }

    @Override
    public void newFileReturn(BatchCreateQuery query, String receiptIds) {
        List<ArchiveBatchDetailReceiveOnline> archiveBatchDetailReceiveOnlines = new ArrayList<>();
        if ("all".equals(query.getSendType())) {

            archiveBatchDetailReceiveOnlines.addAll(commonMapper.selectByQuery(SqlQuery.from(ArchiveBatchDetailReceiveOnline.class)
                    .equal(ArchiveBatchDetailReceiveOnlineInfo.BATCHID, query.getBatchId())));

        } else {

            archiveBatchDetailReceiveOnlines.addAll(commonMapper.selectByQuery(SqlQuery.from(ArchiveBatchDetailReceiveOnline.class)
                    .in(ArchiveBatchDetailReceiveOnlineInfo.ID, receiptIds.split(","))));
        }

        for (ArchiveBatchDetailReceiveOnline archiveBatchDetailReceiveOnline : archiveBatchDetailReceiveOnlines) {
            //TODO 暂时还没有formDefinitionId和formDataId 所以只修改ReceiptBatchDetail表中数据
            //FormData formData = archiveDataManager.selectOneFormData(receiptBatchDetail.getFormDefinitionId(), receiptBatchDetail.getFormDataId());
            //if (formData.getString(ArchiveEntity.STATUS_COLUMN_KEY).equals(ArchiveDataManager.STATUS_RECEIVE)) {
            //formDataService.removeFormData(receiptBatchDetail.getFormDefinitionId(), receiptBatchDetail.getFormDataId());
            //approveCenterClient.getEngineMesasge(new ArchivePackResult(formData.getId(), "3", "2", "人工退回：" + query.getReturnReason()));
            commonMapper.updateByQuery(SqlQuery.from(ArchiveBatchDetailReceiveOnline.class)
                    .set(ArchiveBatchDetailReceiveOnlineInfo.STATUS, "4")
                    .equal(ArchiveBatchDetailReceiveOnlineInfo.ID, archiveBatchDetailReceiveOnline.getId()));
            // }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String fileReturn(BatchCreateQuery query, String returnReason) {
        List<FormData> formDataList = defaultArchiveDataManager.findDataByQuery(query);
        long onlineCounts = 0;
        for (FormData formData : formDataList) {
            if (ReceiptService.SOURCE_TYPE_ONLINE.equals(formData.get(ArchiveEntity.COLUMN_SOURCE_TYPE))) {
                //从回执信息中获取业务系统信息
                List<Receipt> receiptList = archiveReceiptService.selectList(SqlQuery.from(Receipt.class).equal(ReceiptInfo.BUSINESSID, formData.getId()));
                Assert.isTrue(receiptList.size() > 0, "为查询到回执信息，退回失败！");
                Receipt receipt = receiptList.get(0);
                //删除目录信息，删除原文
                archiveDataManager.deleteFormData(query.getCategoryId(), query.getFormDefinitionId(), formData.getId());
                //修改批次详情信息状态为 归档失败
                ArchiveBatchDetailReceiveOnline archiveBatchDetailReceiveOnline = archiveBatchDetailReceiveOnlineService.selectById(formData.getId());
                Assert.isTrue(null != archiveBatchDetailReceiveOnline, "为查询到归档详情信息，退回失败");
                archiveBatchDetailReceiveOnline.setStatus("4");
                archiveBatchDetailReceiveOnlineService.updateById(archiveBatchDetailReceiveOnline);
                //TODO 需要讨论预归档信息包是否删除
                //插入四性检测检查记录信息，说明退回原因
                //通知业务系统 该信息已退回 TODO 目前只对接政务系统，固定往审批系统推送回执信息，将来得修改
                approveCenterClient.getEngineMesasge(new ArchivePackResult(formData.getId(), "3", "2", "人工退回：" + returnReason));
                //TODO 记录管理过程元数据信息
                onlineCounts++;
            }
        }
        return "本次退回在线归档数据：【" + onlineCounts + "】条；其中有【" + (formDataList.size() - onlineCounts) + "】条离线接收数据！";
    }
}
