package com.dr.archive.kufang.archives.service.impl;

import com.dr.archive.kufang.archives.entity.LocationKuFang;
import com.dr.archive.kufang.archives.entity.LocationKuFangInfo;
import com.dr.archive.kufang.archives.service.LocationKuFangService;
import com.dr.archive.util.UUIDUtils;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.entity.StatusEntity;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.organise.service.OrganisePersonService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 图书馆管理实现类
 *
 * @author dr
 */
@Service
public class LocationKuFangServiceImpl extends DefaultBaseService<LocationKuFang> implements LocationKuFangService {
    @Autowired
    CommonMapper commonMapper;
    @Autowired
    OrganisePersonService organisePersonService;

    @Override
    public long insert(LocationKuFang entity) {
       entity.setOrganiseId(SecurityHolder.get().currentOrganise().getId());
        return super.insert(entity);
    }

    @Override
    public long delete(SqlQuery<LocationKuFang> sqlQuery) {
        LocationKuFang kf = selectOne(sqlQuery);

        return super.delete(sqlQuery);
    }

    /**
     * 查询所有馆藏室
     *
     * @return
     */
    @Override
    public List<LocationKuFang> getLocations() {
        SqlQuery<LocationKuFang> sqlQuery = SqlQuery.from(LocationKuFang.class);
        return commonMapper.selectByQuery(sqlQuery);
    }

    /**
     * 添加馆藏室
     * 1、判断类型是否支持
     * 3、逻辑判断编号是否重复
     * 4、执行添加
     *
     * @param location
     * @param personId 经手人
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addLocation(LocationKuFang location, String personId) {
        Assert.isTrue(!StringUtils.isEmpty(location.getLocType()), "档案室类型不能为空");
        Assert.isTrue(!StringUtils.isEmpty(location.getName()), "档案室名字不能为空");
        location.setId(UUIDUtils.getUUID());
        location.setCreatePerson(personId);
        location.setCreateDate(System.currentTimeMillis());
        location.setUpdateDate(System.currentTimeMillis());
        location.setUpdatePerson(personId);
        location.setStatus(StatusEntity.STATUS_ENABLE_STR);
        location.setOrder(0);
        //add
        commonMapper.insertIgnoreNull(location);
    }

    /**
     * 更新馆藏地
     *
     * @param location
     * @param personId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateLocation(LocationKuFang location, String personId) {
        //判断馆藏室id是否存在
        Assert.isTrue(commonMapper.existsByQuery(SqlQuery.from(LocationKuFang.class)
                .equal(LocationKuFangInfo.ID, location.getId())), "您更新的档案室不存在");
        //设置更新人、更新时间
        location.setUpdatePerson(personId);
        location.setUpdateDate(System.currentTimeMillis());
        //update
        commonMapper.updateIgnoreNullById(location);
    }

    /**
     * 更新档案室状态
     *
     * @param locId    判断locId是否存在
     * @param personId
     * @param enable   状态
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateLocationStatus(String locId, String personId, boolean enable) {
        //判断档案室id是否存在
        Assert.isTrue(!StringUtils.isEmpty(locId) && commonMapper.exists(LocationKuFang.class, locId), "该档案室不存在");
        //根据id修改状态
        commonMapper.updateIgnoreNullByQuery(SqlQuery.from(LocationKuFang.class)
                .set(LocationKuFangInfo.UPDATEDATE, System.currentTimeMillis())
                .set(LocationKuFangInfo.UPDATEPERSON, personId)
                .set(LocationKuFangInfo.STATUS, booleanToString(enable))
                .equal(LocationKuFangInfo.ID, locId));
    }

    /**
     * boolean转String true->"1",false->"0"
     *
     * @param status
     * @return
     */
    public static String booleanToString(boolean status) {
        return status ? StatusEntity.STATUS_ENABLE_STR : StatusEntity.STATUS_DISABLE_STR;
    }

    /**
     * 根据id查馆藏室
     *
     * @param locId
     * @return
     */
    @Override
    public LocationKuFang getLocById(String locId) {
        return commonMapper.selectById(LocationKuFang.class, locId);
    }

}

