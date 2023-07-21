package com.dr.archive.fuzhou.approve.service;

import com.dr.archive.fuzhou.approve.entity.ArchivePackResult;
import com.dr.archive.fuzhou.approve.entity.ArchiveResultEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 审批中心接口客户端
 * <p>
 * 用来调用审批中心相关接口
 *
 * @author dr
 */
@FeignClient(url = "${fuzhou.approve.archiveresult-url}", name = "approveCenter")
public interface ApproveCenterClient {


    /**
     * 归档回执接口
     *
     * @return http://192.168.118.208:9091/fwapi/c/api.transfer/archivePack.archiveResult
     */
    @RequestMapping(value = "/StatMonitor/web/api/receive/archiveResult", method = RequestMethod.POST)
    ResponseEntity<ArchiveResultEntity> getEngineMesasge(@RequestBody ArchivePackResult archivePackResult);
}
