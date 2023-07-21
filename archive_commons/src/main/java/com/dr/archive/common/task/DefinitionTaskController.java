package com.dr.archive.common.task;

import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.task.entity.TaskDefinition;
import com.dr.framework.common.task.entity.TaskDefinitionInfo;
import com.dr.framework.common.task.service.TaskDefinitionService;
import com.dr.framework.common.task.service.TaskTypeProvider;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.Column;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.service.SecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping({"${common.api-path:/api}/taskDefinition"})
public class DefinitionTaskController extends BaseServiceController<TaskDefinitionService, TaskDefinition> {
    @Autowired
    protected List<TaskTypeProvider> typeProviders;
    @Autowired
    SecurityManager securityManager;

    @Override
    protected SqlQuery<TaskDefinition> buildPageQuery(HttpServletRequest request, TaskDefinition entity) {
        Person person = this.getUserLogin(request);
        SqlQuery<TaskDefinition> taskInfoSqlQuery = SqlQuery.from(TaskDefinition.class);
        taskInfoSqlQuery.equal(TaskDefinitionInfo.TASKTYPE, entity.getTaskType()).like(TaskDefinitionInfo.TASKNAME, entity.getTaskName()).orderBy(new Column[]{TaskDefinitionInfo.ORDERBY}).orderByDesc(new Column[]{TaskDefinitionInfo.CREATEDATE});
        if (StringUtils.hasText(entity.getTaskType())) {
            taskInfoSqlQuery.equal(TaskDefinitionInfo.TASKTYPE, entity.getTaskType());
        } else {
            Set<String> roles = (Set) this.hasRoleTypes(person).map(TaskTypeProvider::type).collect(Collectors.toSet());
            taskInfoSqlQuery.in(TaskDefinitionInfo.TASKTYPE, new ArrayList(roles));
        }
        taskInfoSqlQuery.equal(TaskDefinitionInfo.CREATEPERSON, person.getId());
        return taskInfoSqlQuery;
    }

    protected Stream<TaskTypeProvider> hasRoleTypes(Person person) {
        return this.typeProviders.stream().filter((t) -> {
            return this.hasRole(t, person);
        });
    }

    protected boolean hasRole(TaskTypeProvider provider, Person person) {
        if (person == null) {
            return false;
        } else {
            String role = provider.getTaskRoleCode();
            return StringUtils.hasText(role) ? this.securityManager.hasRole(person.getId(), role.split(",")) : true;
        }
    }

}
