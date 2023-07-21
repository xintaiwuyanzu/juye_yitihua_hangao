package com.dr.archive.manage.task.controller;

import com.dr.archive.manage.task.entity.BatchDetails;
import com.dr.archive.manage.task.service.ArchiveBatchDetailsService;
import com.dr.framework.common.controller.BaseController;
import com.dr.framework.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/details")
public class ArchiveBatchDetailsController extends BaseController<BatchDetails>{


    @Autowired
    ArchiveBatchDetailsService archiveBatchDetailsService;

    @RequestMapping("insertBatchDetails")
    public ResultEntity BatchDetails(HttpServletRequest request,String taskId,String flag){

        List<Map> list = archiveBatchDetailsService.getBatchId(taskId);

        for (Map map : list){
            Map appraisal = archiveBatchDetailsService.getSourceValue(map.get("appraisalid").toString());
            BatchDetails batchDetails = new BatchDetails();
            batchDetails.setBatchid(map.get("batchid").toString());
            batchDetails.setAppraisalid(map.get("appraisalid").toString());
            batchDetails.setTaskid(taskId);
           if(appraisal!=null){
               if(appraisal.containsKey("sourceCode") && !StringUtils.isEmpty(appraisal.get("sourceCode").toString())){
                   batchDetails.setSourceCode(appraisal.get("sourceCode").toString());
               }

               if(appraisal.containsKey("sourceName") && !StringUtils.isEmpty(appraisal.get("sourceName").toString())){
                   batchDetails.setSourceName(appraisal.get("sourceName").toString());
               }
               if(appraisal.containsKey("targetValue") && !StringUtils.isEmpty(appraisal.get("targetValue").toString())){
                   batchDetails.setTargetValue(appraisal.get("targetValue").toString());
               }

               if(appraisal.containsKey("sourceValue") && !StringUtils.isEmpty(appraisal.get("sourceValue").toString())){
                   batchDetails.setSourceValue(appraisal.get("sourceValue").toString());
               }
           }
           if(!StringUtils.isEmpty(flag)){
               batchDetails.setFlag(flag);
           }else {
               batchDetails.setFlag("1");
           }
            batchDetails.setStatus("0");
            super.insert(request,batchDetails);
        }
        return ResultEntity.success();
    }


    @RequestMapping("/getBatchDetailsCount")
    public ResultEntity getBatchDetailsCount(String taskId,String batchId){

        return ResultEntity.success(archiveBatchDetailsService.getBatchDetailsCount(taskId,batchId));
    }

    @RequestMapping("/updateBatchDetails")
    public ResultEntity updateBatchDetails(String taskId, String status,
                                           String batchId,String appraisalid,String advice,String targetValue){

          archiveBatchDetailsService.updateBatchDetails(taskId,status,batchId,appraisalid,advice,targetValue);
        return ResultEntity.success();
    }

    @RequestMapping("/getloadPending")
    public ResultEntity getloadPending(String taskId,String batchId){

        return ResultEntity.success(archiveBatchDetailsService.getloadPending(taskId,batchId));
    }

    @RequestMapping("/getAppraisalByid")
    public ResultEntity getAppraisalByid(String id){
        return ResultEntity.success(archiveBatchDetailsService.getAppraisalByid(id));
    }

    @RequestMapping("/getBatchDetailsFlag")
    public ResultEntity getBatchDetailsFlag(String taskId,String batchId){

        return ResultEntity.success(archiveBatchDetailsService.getBatchDetailsFlag(taskId,batchId));
    }

    @RequestMapping("/getTaskStatus")
    public ResultEntity getTaskStatus(String taskId){

        return ResultEntity.success(archiveBatchDetailsService.getTaskStatus(taskId));
    }
}
