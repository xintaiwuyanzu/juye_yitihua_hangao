package com.dr.archive.fuzhou.ofd.service;

import com.dr.archive.fuzhou.ofd.bo.OfdTransformRecord;
import com.dr.framework.common.service.BaseService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @author: yang
 * @create: 2022-05-16 17:46
 **/
public interface OfdTransformService extends BaseService<OfdTransformRecord> {
    void upload(MultipartFile file);
    void download(String id, String type, HttpServletResponse response);
    void transform(OfdTransformRecord record);
}
