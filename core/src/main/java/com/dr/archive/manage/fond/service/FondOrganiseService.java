package com.dr.archive.manage.fond.service;

import com.dr.archive.manage.fond.entity.Fond;
import com.dr.archive.manage.fond.entity.FondOrganise;
import com.dr.framework.common.service.BaseService;
import com.dr.framework.core.organise.entity.Organise;

import java.util.List;

/**
 * @author caor
 * @date 2021-09-03 9:41
 */
public interface FondOrganiseService extends BaseService<FondOrganise> {

    /**
     * 根据组织机构id查询全宗列表
     *
     * @param organiseId
     * @return
     */
    List<Fond> getFondListByOrganiseId(String organiseId);

    /**
     * 根据组织机构编码查询全宗列表
     *
     * @param organiseCode
     * @return
     */
    List<Fond> getFondListByOrganiseCode(String organiseCode);

    /**
     * 根据全宗编码查询组织机构列表
     *
     * @param fondCode
     * @return
     */
    List<Organise> getOrganiseListByFondCode(String fondCode);

}
