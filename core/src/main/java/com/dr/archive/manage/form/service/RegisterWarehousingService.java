package com.dr.archive.manage.form.service;

import com.dr.archive.manage.form.entity.RegisterWarehousing;
import com.dr.archive.manage.form.entity.RegisterWarehousingDetails;
import com.dr.framework.common.service.BaseService;
import com.itextpdf.text.DocumentException;


import java.io.IOException;
import java.util.List;

public interface RegisterWarehousingService extends BaseService<RegisterWarehousing> {

     RegisterWarehousing insertReg(String status);

     String processInformation(RegisterWarehousing registerWarehousing, List<RegisterWarehousingDetails> registerWarehousingDetails) throws IOException, DocumentException;
}
