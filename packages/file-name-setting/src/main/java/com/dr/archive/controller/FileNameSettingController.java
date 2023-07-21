package com.dr.archive.controller;


import com.dr.archive.entity.FileNameSetting;
import com.dr.archive.service.FileNameSettingService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.form.annotations.Form;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/fileNameSetting")
public class FileNameSettingController extends BaseServiceController<FileNameSettingService, FileNameSetting> {

    @Override
    protected SqlQuery<FileNameSetting> buildPageQuery(HttpServletRequest httpServletRequest, FileNameSetting fileNameSetting) {
        return null;
    }

    @RequestMapping("startUp")
    public ResultEntity startUp(@Form FormData formData,String state){
        return service.startUp(formData,state);
    }
    /**
     * 批量添加
     */
    @RequestMapping(value = "/insertFormBatchData")
    public ResultEntity insertFormBatchData(
            @Form FormData formData,
            String fondId,
            String categoryId,
            Integer number_code) {
        service.insertFormBatchData(formData,fondId,categoryId,number_code);
        return ResultEntity.success();
    }
}
