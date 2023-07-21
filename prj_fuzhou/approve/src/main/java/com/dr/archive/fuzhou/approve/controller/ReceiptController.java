package com.dr.archive.fuzhou.approve.controller;

import com.dr.archive.detection.entity.TestRecord;
import com.dr.archive.fuzhou.approve.bo.ArchiveReceiveBo;
import com.dr.archive.fuzhou.approve.exception.ReceiveException;
import com.dr.framework.common.entity.ResultEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 电子文件归档接口
 *
 * @author caor
 * @date 2021-08-17 17:09
 */
@RestController
@RequestMapping({"${common.api-path:/api}/receive"})
public class ReceiptController {

    static final Logger logger = LoggerFactory.getLogger(ReceiptController.class);
    /**
     * 用来左json解析
     */
    @Autowired
    ObjectMapper objectMapper;

    /**
     * 业务系统调用档案系统传入文件下载信息
     * <p>
     * 档案系统接收到消息后，异步下载参数中档案信息进行归档
     *
     * @return 返回消息接受成功失败信息
     */
    @PostMapping("/download")
    public ResultEntity<String> download(@RequestBody ArchiveReceiveBo receiveBo) {
        try {
            //调用归档服务
            // receiptService.receiveArchive(receiveBo);
            return ResultEntity.success("接收归档文件成功！");
        } catch (ReceiveException e) {
            logger.error("调用归档服务失败！", e);
            return ResultEntity.error(e.getMessage());
        } catch (Exception e) {
            logger.error("归档内部错误！", e);
            return ResultEntity.error("请求失败，请联系管理员！");
        }
    }


    /**
     * 查询回执失败记录
     *
     * @param testRecordList 系统编号\系统名称\业务主键
     * @return
     */
    @PostMapping("/archiveErrorResult")
    public ResultEntity<List<TestRecord>> archiveErrorResult(@RequestBody List<TestRecord> testRecordList) {
        //  return ResultEntity.success(receiptService.archiveErrorResult(testRecordList.get(0).getSystemNum(), testRecordList.get(0).getSystemName(), testRecordList.get(0).getBusinessId()));
        return null;
    }
}
