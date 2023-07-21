package com.dr.archive.service;

import com.dr.archive.manage.codingscheme.entity.CodingSchemeItem;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.common.service.BaseService;
import com.dr.archive.entity.FileNameSetting;

import java.util.List;

public interface FileNameSettingService extends BaseService<FileNameSetting> {

    ResultEntity startUp(FormData formData,String state);

    void insertFormBatchData(FormData formData, String fondId, String categoryId, int number);

    String CodeSetting(FormData formData, List<CodingSchemeItem> codingSchemeItems);

    String archiveCodeOrder(String index);
}
