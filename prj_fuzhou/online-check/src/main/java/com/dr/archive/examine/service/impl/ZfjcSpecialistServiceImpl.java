package com.dr.archive.examine.service.impl;

import com.dr.archive.examine.entity.ZfjcSpecialist;
import com.dr.archive.examine.entity.ZfjcSpecialistInfo;
import com.dr.archive.examine.service.ZfjcSpecialistService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.sys.service.DefaultOrganisePersonService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class ZfjcSpecialistServiceImpl extends DefaultBaseService<ZfjcSpecialist> implements ZfjcSpecialistService {
    @Autowired
    DefaultOrganisePersonService defaultOrganisePersonService;

    @Override
    public List<ZfjcSpecialist> getZfjcSpecialistListByDefaultOrgId(String orgId) {
        return getCommonService().selectList(SqlQuery.from(ZfjcSpecialist.class).equal(ZfjcSpecialistInfo.STATUS, "1").equal(ZfjcSpecialistInfo.DEFAULTORGANISEID, orgId));
    }

    @Override
    public long insert(ZfjcSpecialist entity) {
        if (StringUtils.isBlank(entity.getStatus())) {
            entity.setStatus("1");
        }
        Assert.isTrue(!StringUtils.isEmpty(entity.getUserId()), "未选中用户！");
        Assert.isTrue(!selectZfjcSpecialistByUserId(entity.getUserId()), "该用户已存在！");
//        String[] userIdArray = entity.getUserId().split(",");
//        String userId = userIdArray.length > 0 ? userIdArray[userIdArray.length - 1] : entity.getUserId();
        entity.setUserName(defaultOrganisePersonService.getPersonById(entity.getUserId()).getUserName());
        Organise organise = defaultOrganisePersonService.getPersonDefaultOrganise(entity.getUserId());
        entity.setDefaultOrganiseId(organise.getId());
        entity.setDefaultOrganiseName(organise.getOrganiseName());
        return super.insert(entity);
    }

    boolean selectZfjcSpecialistByUserId(String userId) {
        return commonMapper.countByQuery(SqlQuery.from(ZfjcSpecialist.class).equal(ZfjcSpecialistInfo.USERID, userId)) > 0;
    }
}
