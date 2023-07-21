package com.dr.archive.manage.fond.service.impl;

import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.fond.entity.FondChangeHistory;
import com.dr.archive.manage.fond.service.FondChangeHistoryService;
import com.dr.archive.manage.fond.service.FondService;
import com.dr.archive.util.UUIDUtils;
import com.dr.framework.common.controller.BaseController;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.organise.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

/**
 * 全宗内部实现类
 *
 * @author caor
 * @author tuzl
 * @author dr
 */
@Service
public class FondChangeHistoryServiceImpl extends DefaultBaseService<FondChangeHistory> implements FondChangeHistoryService {

    @Autowired
    CommonMapper commonMapper;
    @Autowired
    FondService fondService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(Fond entity) {
        FondChangeHistory fondChangeHistory = new FondChangeHistory();
        fondChangeHistory.setId(UUIDUtils.getUUID());
        fondChangeHistory.setNewFondId(entity.getCode());
        fondChangeHistory.setNewFondName(entity.getName());
        fondChangeHistory.setNewOrder(entity.getOrder());
        fondChangeHistory.setCreatePerson(entity.getCreatePerson());
        fondChangeHistory.setCreateDate(entity.getCreateDate());
        fondChangeHistory.setNewCreatePerson(entity.getCreatePerson());
        fondChangeHistory.setNewCreateDate(entity.getCreateDate());
        fondChangeHistory.setStatus("新增");
        commonMapper.insertIgnoreNull(fondChangeHistory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateByChange(Fond oldFond, Fond entity) {
        FondChangeHistory fondChangeHistory = new FondChangeHistory();
        fondChangeHistory.setId(UUIDUtils.getUUID());
        //新数据
        fondChangeHistory.setNewFondId(entity.getCode());
        fondChangeHistory.setNewFondName(entity.getName());
        fondChangeHistory.setNewOrder(entity.getOrder());
        fondChangeHistory.setNewCreateDate(System.currentTimeMillis());
        //旧数据
        fondChangeHistory.setFondId(oldFond.getCode());
        fondChangeHistory.setFondName(oldFond.getName());
        fondChangeHistory.setOrder(oldFond.getOrder());
        fondChangeHistory.setCreatePerson(oldFond.getCreatePerson());
        fondChangeHistory.setCreateDate(oldFond.getCreateDate());
        fondChangeHistory.setUpdatePerson(oldFond.getCreatePerson());
        fondChangeHistory.setUpdateDate(oldFond.getCreateDate());
        fondChangeHistory.setStatus("更新");
        commonMapper.insertIgnoreNull(fondChangeHistory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteChange(HttpServletRequest request, Fond oldFond) {
        FondChangeHistory fondChangeHistory = new FondChangeHistory();
        fondChangeHistory.setId(UUIDUtils.getUUID());
        fondChangeHistory.setFondId(oldFond.getCode());
        fondChangeHistory.setFondName(oldFond.getName());
        fondChangeHistory.setOrder(oldFond.getOrder());
        fondChangeHistory.setCreatePerson(oldFond.getCreatePerson());
        fondChangeHistory.setCreateDate(oldFond.getCreateDate());
        Person userLogin = BaseController.getUserLogin(request);
        fondChangeHistory.setNewCreatePerson(userLogin.getId());
        fondChangeHistory.setNewCreateDate(System.currentTimeMillis());
        fondChangeHistory.setStatus("删除");
        commonMapper.insertIgnoreNull(fondChangeHistory);
    }

}
