package com.dr.archive.controller;


import com.dr.archive.entity.ArchivesBatchAdd;
import com.dr.archive.entity.ArchivesBatchAddInfo;
import com.dr.archive.service.ArchiveBatchAddService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.form.annotations.Form;
import com.dr.framework.common.form.core.model.FormData;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/ArchiveBatchAdd")
public class ArchiveBatchAddController extends BaseServiceController<ArchiveBatchAddService, ArchivesBatchAdd> {

    @Override
    protected SqlQuery<ArchivesBatchAdd> buildPageQuery(HttpServletRequest request, ArchivesBatchAdd entity) {
        return SqlQuery.from(ArchivesBatchAdd.class).orderByDesc(ArchivesBatchAddInfo.CREATEDATE);
    }

    /**
     * 批量目录著录
     *
     * @param formData
     * @return
     */
    @RequestMapping("/newBatch")
    public ResultEntity newBatch(@Form FormData formData) {
        service.newBatch(formData);
        return ResultEntity.success("正在添加中请稍后");
    }

}
