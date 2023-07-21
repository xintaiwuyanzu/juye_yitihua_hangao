package com.dr.archive.service;


import com.dr.archive.entity.ArchivesBatchAdd;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.service.BaseService;

public interface ArchiveBatchAddService extends BaseService<ArchivesBatchAdd> {
    //批量目录著录
    void newBatch(FormData formData);
}
