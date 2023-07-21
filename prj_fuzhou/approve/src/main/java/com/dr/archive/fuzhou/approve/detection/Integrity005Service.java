package com.dr.archive.fuzhou.approve.detection;

import com.dr.archive.detection.service.ItemDetectContext;
import com.dr.archive.detection.service.TestRecordService;
import com.dr.archive.receive.online.entity.ArchiveBatchDetailReceiveOnline;
import com.dr.archive.receive.online.entity.ArchiveBatchDetailReceiveOnlineInfo;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: qiuyf
 * 同一批次归档信息包的电子文件号判定是否连续、重复
 */
@Component
public class Integrity005Service extends AbstractApproveReceiveDetectService {
    @Autowired
    CommonMapper commonMapper;

    @Override
    public String code() {
        return "Integrity005";
    }

    @Override
    public String modeCode() {
        return "2-2-3";
    }

    @Override
    public void detection(ItemDetectContext context) {
        if (detectEnable(context)) {
            //1,判断同一批次的电子文件号是否连续  把电子文件号与上一个电子文件号进行对比 判断是否连续
            String testResult = "";
            boolean result = true;
            String lastArchivalCode = context.getAttribute("lastArchivalCode");
            String archiveCode = context.getFormData().get("ARCHIVE_CODE");
           /* if (!StringUtils.isEmpty(lastArchivalCode)) {
                //无上一个电子文件号,第一个 不对比直接通过

                //取当前档号和上一档号的 "-" 后的数值对比, 两数之前的绝对值为1则为判断为连续
                String str1 = lastArchivalCode.substring(lastArchivalCode.lastIndexOf("-") + 1);
                String str2 = archiveCode.substring(lastArchivalCode.lastIndexOf("-") + 1);
                int num1 = Integer.parseInt(str1);
                int num2 = Integer.parseInt(str2);
                int ret = num1 > num2 ? num1 - num2 : num2 - num1;
                if (ret != 1) {
                    result = false;
                    testResult += "电子文件号不连续";
                }
            }*/
            //2,判断同一批次的电子文件号是否重复
            String batchid = context.getAttribute(TestRecordService.CONTEXT_BUSINESS_ID_KEY);
            List<ArchiveBatchDetailReceiveOnline> detailReceiveOnlineList = commonMapper.selectByQuery(
                    SqlQuery.from(ArchiveBatchDetailReceiveOnline.class)
                            .equal(ArchiveBatchDetailReceiveOnlineInfo.BATCHID, batchid)
                            .equal(ArchiveBatchDetailReceiveOnlineInfo.ARCHIVECODE, archiveCode));
            if (detailReceiveOnlineList.size() > 1) {
                result = false;
                testResult += "同一批次的电子文件号重复!";
            }

            if (result) {
                context.addRecordItem(code(), modeCode());
            } else {
                context.addRecordItem(code(), modeCode(),
                        testResult,
                        TestRecordService.TEST_STATUS_FAIL,
                        "",
                        "电子文件号/档号",
                        archiveCode);
            }
        }
    }
}
