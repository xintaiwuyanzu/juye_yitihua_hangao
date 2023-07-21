package com.dr.archive.fuzhou.ofd.service;

import com.dr.archive.fuzhou.ofd.bo.FileByteInfo;
import com.dr.archive.fuzhou.ofd.bo.FileStreamResult;
import com.dr.framework.rpc.ResultMapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * ofd接口客户端
 *
 * @author dr
 */
@FeignClient(name = "ofdClient", url = "http://${fuzhou.ofd.base-ip}:${fuzhou.ofd.api-port:7777}/gsdk-service/")
@ResultMapper(codeKey = "returncode", messageKey = "returnmsg")
public interface OfdClient {

    /**
     * 转换统一接口
     *
     * @param fileByteInfo
     */
    @PostMapping("/v1/ofd/byteStream")
    FileStreamResult convertStream(@RequestBody FileByteInfo fileByteInfo);


}
