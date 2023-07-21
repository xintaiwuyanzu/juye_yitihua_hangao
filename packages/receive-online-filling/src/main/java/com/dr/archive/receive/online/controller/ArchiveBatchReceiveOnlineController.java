package com.dr.archive.receive.online.controller;

import com.dr.archive.batch.query.BatchCreateQuery;
import com.dr.archive.model.query.ArchiveDataQuery;
import com.dr.archive.receive.online.bo.ArchiveReceiveBo;
import com.dr.archive.receive.online.entity.ArchiveBatchReceiveOnline;
import com.dr.archive.receive.online.entity.ArchiveBatchReceiveOnlineInfo;
import com.dr.archive.receive.online.service.ArchiveOnlineReceiveService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.web.annotations.Current;
import org.ofdrw.core.basicStructure.res.Res;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 在线接收controller
 *
 * @author dr
 */
@RestController
@RequestMapping("${common.api-path:/api}/receive/online")
public class ArchiveBatchReceiveOnlineController extends BaseServiceController<ArchiveOnlineReceiveService, ArchiveBatchReceiveOnline> {
    /*@Autowired
    ArchiveOnlineReceiveService archiveOnlineReceiveService;*/
    static final Logger logger = LoggerFactory.getLogger(ArchiveBatchReceiveOnlineController.class);

    @Override
    protected SqlQuery<ArchiveBatchReceiveOnline> buildPageQuery(HttpServletRequest request, ArchiveBatchReceiveOnline entity) {
        SqlQuery<ArchiveBatchReceiveOnline> sqlQuery = SqlQuery.from(ArchiveBatchReceiveOnline.class);
        sqlQuery.like(ArchiveBatchReceiveOnlineInfo.BATCHNAME, entity.getBatchName());
        sqlQuery.orderByDesc(ArchiveBatchReceiveOnlineInfo.CREATEDATE);
        return sqlQuery;
    }

    @PostMapping("/receiveOnline")
    public ResultEntity<String> receiveOnline(HttpServletRequest request, HttpServletResponse response, @RequestBody ArchiveReceiveBo receiveBo) {
        try {
            //调用归档服务
            service.receiveOnline(request, response, receiveBo);
            return ResultEntity.success("接收归档文件成功！");
        } catch (Exception e) {
            logger.error("归档内部错误！", e);
            return ResultEntity.error("请求失败，请联系管理员！");
        }
    }

    /**
     * 获取接收报告
     */
    @RequestMapping("/getReport")
    public ResultEntity getReport(String batchId) {
        return ResultEntity.success(service.getReport(batchId));
    }

    /*批量退回*/
    @RequestMapping("/batch")
    public ResultEntity batch(String id) {
        ArchiveBatchReceiveOnline archiveBatchReceiveOnline = service.selectById(id);
        if ("3".equals(archiveBatchReceiveOnline.getStatus())) {
            return ResultEntity.error("当前状态已为退回!");
        } else {
            archiveBatchReceiveOnline.setStatus("3");
            service.updateById(archiveBatchReceiveOnline);
            return ResultEntity.success("退回成功!");
        }
    }

    @RequestMapping("/handover")
    public ResultEntity handover(@Current Person person,
                                 @Current Organise organise,
                                 @RequestParam(name = ArchiveDataQuery.QUERY_KEY, required = false) String queryContent,
                                 BatchCreateQuery query){
        query.parseQuery(queryContent);
        service.doCreateDetail(query,person,organise);
        return ResultEntity.success();
    }
    @RequestMapping("/addArchives")
    public ResultEntity handover(@Current Person person,
                                 @Current Organise organise,
                                 String batchId,String categoryId,String fondId){

        service.addArchives(batchId,categoryId,fondId);
        return ResultEntity.success("入库成功");
    }

}
