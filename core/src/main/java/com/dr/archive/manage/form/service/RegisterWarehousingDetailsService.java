package com.dr.archive.manage.form.service;

import com.dr.archive.manage.form.entity.RegisterWarehousing;
import com.dr.archive.manage.form.entity.RegisterWarehousingDetails;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.service.BaseService;

public interface RegisterWarehousingDetailsService extends BaseService<RegisterWarehousingDetails> {

     void insertFormData(FormData formData,String  bid);

     void enUpdateById(RegisterWarehousing insert,int size);


     void insertFormData(FormData formData,String  bid, String modelType);

     void enUpdateById(String insert,int size);
}
