package com.dr.archive.common.dzdacqbc.controller;

import com.dr.archive.common.dzdacqbc.entity.CqbcAlarm;
import com.dr.archive.common.dzdacqbc.entity.CqbcAlarmInfo;
import com.dr.archive.common.dzdacqbc.service.AlarmService;
import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.fond.service.FondService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 电子档案长期保存任务管理详情
 *
 * @author hyj
 */
@RestController
@RequestMapping("/api/cqbcAlarm")
public class AlarmController extends BaseServiceController<AlarmService, CqbcAlarm> {

    @Autowired
    CommonMapper commonMapper;
    @Autowired
    FondService fondService;

    @Override
    protected SqlQuery<CqbcAlarm> buildPageQuery(HttpServletRequest request, CqbcAlarm entity) {
        Organise organise = SecurityHolder.get().currentOrganise();
        Person person = SecurityHolder.get().currentPerson();
        SqlQuery<CqbcAlarm> sqlQuery = SqlQuery.from(CqbcAlarm.class);
        if (StringUtils.hasText(entity.getAlarmType())) {
            sqlQuery.equal(CqbcAlarmInfo.ALARMTYPE, entity.getAlarmType());
        }
        if (StringUtils.hasText(entity.getAlarmContent())) {
            sqlQuery.like(CqbcAlarmInfo.ALARMCONTENT, entity.getAlarmContent());
        }
        if (StringUtils.hasText(entity.getProcessState())) {
            sqlQuery.equal(CqbcAlarmInfo.PROCESSSTATE, entity.getProcessState());
        }
        if (StringUtils.hasText(entity.getArchiveId())) {
            sqlQuery.equal(CqbcAlarmInfo.ARCHIVEID, entity.getArchiveId());
        }
        if (!"dag".equals(organise.getOrganiseType()) && !"admin".equals(person.getUserCode())) {
            Fond fond = fondService.findFondByPartyId(organise.getId());
            sqlQuery.equal(CqbcAlarmInfo.FONDCODE, fond.getCode());
        }
        sqlQuery.orderBy(CqbcAlarmInfo.PROCESSSTATE).orderByDesc(CqbcAlarmInfo.CREATEDATE);
        return sqlQuery;
    }

    @RequestMapping("/selecArchivetList")
    public ResultEntity selecArchivetList(String archiveId) {
        SqlQuery<CqbcAlarm> equal = SqlQuery.from(CqbcAlarm.class).equal(CqbcAlarmInfo.ARCHIVEID, archiveId);
        List<CqbcAlarm> cqbcAlarms = commonMapper.selectLimitByQuery(equal, 0, 15);
        return ResultEntity.success(cqbcAlarms);
    }

    @Override
    public ResultEntity<CqbcAlarm> update(HttpServletRequest request, CqbcAlarm entity) {
        Person person = SecurityHolder.get().currentPerson();
        entity.setProcessUserId(person.getId());
        entity.setProcessUserName(person.getUserName());
        entity.setProcessDate(System.currentTimeMillis());
        return super.update(request, entity);
    }

    /**
     * 篡改检测恢复
     */
    @RequestMapping("/earchiveRestore")
    public ResultEntity earchiveRestore(String archiveId, String alarmId) {
        return service.earchiveRestore(archiveId, alarmId);
    }
}
