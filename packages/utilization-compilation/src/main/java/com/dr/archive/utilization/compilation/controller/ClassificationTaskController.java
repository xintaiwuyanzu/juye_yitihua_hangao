package com.dr.archive.utilization.compilation.controller;

import com.dr.archive.utilization.compilation.entity.ClassificationTask;
import com.dr.archive.utilization.compilation.entity.ClassificationTaskInfo;
import com.dr.archive.utilization.compilation.service.ClassificationTaskService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/ClassificationTask")
public class ClassificationTaskController extends BaseServiceController<ClassificationTaskService, ClassificationTask> {


    @Override
    protected SqlQuery<ClassificationTask> buildPageQuery(HttpServletRequest request, ClassificationTask entity) {
        SqlQuery<ClassificationTask> sqlQuery = SqlQuery.from(ClassificationTask.class)
                .like(ClassificationTaskInfo.CLASSIFCATIONNAME, entity.getClassIfCationName())
                .orderByDesc(ClassificationTaskInfo.CREATEDATE);
        return sqlQuery;
    }
}
