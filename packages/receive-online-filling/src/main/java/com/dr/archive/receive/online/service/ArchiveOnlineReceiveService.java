package com.dr.archive.receive.online.service;

import com.dr.archive.batch.query.BatchCreateQuery;
import com.dr.archive.batch.service.BaseArchiveBatchService;
import com.dr.archive.manage.form.service.ArchiveDataManager;
import com.dr.archive.receive.online.bo.ArchiveReceiveBo;
import com.dr.archive.receive.online.entity.ArchiveBatchDetailReceiveOnline;
import com.dr.archive.receive.online.entity.ArchiveBatchReceiveOnline;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.organise.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Map;

/**
 * 在线接收service
 *
 * @author dr
 */
public interface ArchiveOnlineReceiveService extends BaseArchiveBatchService<ArchiveBatchReceiveOnline> {

    //在线接收批次状态
    //接收中
    String STATUS_RECEIVEING = "10";
    //接收失败
    String STATUS_FAIL = "20";
    //接收成功
    String STATUS_SUCCESS = "30";

    String RECEIVE_DIR = "onlineReceive";

    /**
     * 异步在线接收
     *
     * @param request
     * @param response
     * @return
     */
    Object receiveOnline(HttpServletRequest request, HttpServletResponse response, ArchiveReceiveBo receiveBo);


    /**
     * 根据批次对象创建
     * 在线接收批次交接单据文件
     *
     * @param batchReceiveOnline
     * @return
     */
    File buildBatchFile(ArchiveBatchReceiveOnline batchReceiveOnline);

    /**
     * 构建详情附件文件夹
     *
     * @param detailReceiveOnline
     * @return
     */
    File buildDetailDir(ArchiveBatchDetailReceiveOnline detailReceiveOnline);

    Map<String, Long> getReport(String batchId);
    void doCreateDetail(BatchCreateQuery query, Person person, Organise organise);
    void addArchives(String batchId,String categoryId,String fondId);
}
