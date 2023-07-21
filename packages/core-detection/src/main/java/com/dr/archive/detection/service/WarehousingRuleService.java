package com.dr.archive.detection.service;


import com.dr.archive.detection.entity.WarehousingRule;
import com.dr.framework.common.service.BaseService;
import org.springframework.web.multipart.MultipartFile;

public interface WarehousingRuleService extends BaseService<WarehousingRule> {

    public String uploadExcel(MultipartFile file) throws Exception;
}
