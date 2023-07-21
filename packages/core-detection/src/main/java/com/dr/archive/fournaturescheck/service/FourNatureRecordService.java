package com.dr.archive.fournaturescheck.service;

import com.dr.archive.detection.entity.TestRecord;
import com.dr.archive.fournaturescheck.entity.FourNatureRecord;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.service.BaseService;

/**
 * @Author: caor
 * @Date: 2022-09-02 16:45
 * @Description:
 */
public interface FourNatureRecordService extends BaseService<FourNatureRecord> {
    TestRecord startTest(FormData formData,String businessId,String checklink);
}
