package com.dr.archive.fuzhou.approve.detection;

import com.dr.archive.detection.entity.TestRecord;
import com.dr.archive.detection.service.ItemDetectContext;
import com.dr.archive.detection.service.TestRecordService;
import com.dr.archive.model.entity.ArchiveEntity;

import com.dr.archive.receive.offline.entity.ArchiveBatchReceiveOfflineDetail;


import com.dr.archive.receive.offline.entity.ArchiveBatchReceiveOfflineDetailInfo;
import com.dr.archive.receive.online.entity.AbstractArchiveReceiveDetail;
import com.dr.archive.receive.online.entity.ArchiveBatchDetailReceiveOnline;
import com.dr.archive.receive.online.entity.ArchiveBatchDetailReceiveOnlineInfo;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 连续性元数据项检测
 *
 * @author caiwb
 * @date 2022/12/29
 */
@Component
public class Authenticity011Service extends AbstractApproveReceiveDetectService {

    @Autowired
    CommonMapper commonMapper;

    @Override
    public String modeCode() {
        return "GD-2-6";
    }

    @Override
    public String code() {
        return "Authenticity011";
    }

    @Override
    public void detection(ItemDetectContext context) {

        FormData formData = context.getFormData();

        TestRecord testRecord = context.getTestRecord();

        String gdType = context.getAttribute("gdType");

        String prefix = formData.getString(ArchiveEntity.COLUMN_ARCHIVE_CODE).substring(0, formData.getString(ArchiveEntity.COLUMN_ARCHIVE_CODE).lastIndexOf("-"));

        //判断是在线还是离线
        if ("Online".equals(gdType)) {

            List<ArchiveBatchDetailReceiveOnline> archiveBatchDetailReceiveOnlines = commonMapper.selectByQuery(SqlQuery.from(ArchiveBatchDetailReceiveOnline.class)
                    .equal(ArchiveBatchDetailReceiveOnlineInfo.BATCHID, testRecord.getBusinessId())
                    .like(ArchiveBatchDetailReceiveOnlineInfo.ARCHIVECODE, prefix)
                    .orderBy(ArchiveBatchDetailReceiveOnlineInfo.ARCHIVECODE));

            //只查询到一条直接通过
            if (archiveBatchDetailReceiveOnlines.size() == 1) {
                addRecordItem(context, true);
            } else {
                addRecordItem(context, continuityDetection(archiveBatchDetailReceiveOnlines));
            }

        } else {

            List<ArchiveBatchReceiveOfflineDetail> offlineDetails = commonMapper.selectByQuery(SqlQuery.from(ArchiveBatchReceiveOfflineDetail.class)
                    .equal(ArchiveBatchReceiveOfflineDetailInfo.BATCHID, testRecord.getBusinessId())
                    .like(ArchiveBatchReceiveOfflineDetailInfo.ARCHIVECODE, prefix)
                    .orderBy(ArchiveBatchReceiveOfflineDetailInfo.ARCHIVECODE));

            if (offlineDetails.size() == 1) {
                addRecordItem(context, true);
            } else {
                addRecordItem(context, continuityDetection(offlineDetails));
            }
        }

    }

    private boolean continuityDetection(List<? extends AbstractArchiveReceiveDetail> receiveDetails) {
        List<Integer> list = new ArrayList<>();
        for (AbstractArchiveReceiveDetail receiveDetail : receiveDetails) {
            try {
                //将档号最后一组转换为数字
                list.add(Integer.parseInt(receiveDetail.getArchiveCode().substring(receiveDetail.getArchiveCode().lastIndexOf("-") + 1)));

            } catch (Exception e) {
                //转换失败直接检测失败
                return false;
            }
        }

        boolean flag = true;

        for (int i = 0; i < list.size(); i++) {
            if (i > 0) {
                int num = list.get(i);
                int nextNum = list.get(i - 1) + 1;
                if (num != nextNum) {
                    flag = false;
                    break;
                }
            }
        }

        return flag;
    }

    private void addRecordItem(ItemDetectContext context, boolean flag) {
        FormData formData = context.getFormData();
        if (flag) {
            context.addRecordItem(code(), modeCode(), "【元数据连续性】校验", TestRecordService.TEST_STATUS_SUCCESS, ArchiveEntity.COLUMN_ARCHIVE_CODE,
                    "档号", formData.getString(ArchiveEntity.COLUMN_ARCHIVE_CODE));
        } else {
            context.addRecordItem(code(), modeCode(), "【元数据连续性】校验", TestRecordService.TEST_STATUS_FAIL, ArchiveEntity.COLUMN_ARCHIVE_CODE,
                    "档号", formData.getString(ArchiveEntity.COLUMN_ARCHIVE_CODE));
        }
    }

}
