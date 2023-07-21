package com.dr.archive.impexp.service.impl;

import com.dr.archive.formMap.bo.FormKeyMap;
import com.dr.archive.formMap.bo.FormKeyMapGroup;
import com.dr.archive.formMap.service.FormKeyMapService;
import com.dr.archive.impexp.entity.ImpExpScheme;
import com.dr.archive.impexp.entity.ImpExpSchemeInfo;
import com.dr.archive.impexp.entity.ImpExpSchemeItem;
import com.dr.archive.impexp.entity.ImpExpSchemeItemInfo;
import com.dr.archive.impexp.service.ImpExpSchemeItemService;
import com.dr.archive.impexp.service.ImpExpSchemeService;
import com.dr.archive.service.impl.BaseYearServiceImpl;
import com.dr.framework.common.service.CommonService;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.orm.sql.Column;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author caor
 * @date 2020/7/31 19:01
 */
@Service
public class ImpExpSchemeServiceImpl extends BaseYearServiceImpl<ImpExpScheme> implements ImpExpSchemeService, FormKeyMapService {
    @Autowired
    ImpExpSchemeItemService impExpSchemeItemService;
    @Autowired
    CommonService commonService;

    @Override
    protected Class getSubTableClass() {
        return ImpExpSchemeItem.class;
    }

    @Override
    protected Column getRelateColumn() {
        return ImpExpSchemeItemInfo.BUSINESSID;
    }

    @Override
    public List<FormKeyMapGroup> getFormKeyMapGroup(String fondCode, String cateGoryCode, String year, String businessCode) {
        SqlQuery<ImpExpScheme> schemeSqlQuery = SqlQuery.from(ImpExpScheme.class);
/*        //新增只能获取 同机构下的 方案配置。
        Organise organise = SecurityHolder.get().currentOrganise();
        if (StringUtils.hasText(businessCode)) {
            schemeSqlQuery.equal(ImpExpSchemeInfo.SCHEMETYPE, businessCode);
        }
        if (organise != null) {
            schemeSqlQuery.equal(ImpExpSchemeInfo.ORGANISEID, organise.getId());
        }*/
        return selectList(schemeSqlQuery)
                .stream()
                .map(this::newGroup)
                .sorted()
                .collect(Collectors.toList());

    }


    @Override
    public List<FormKeyMap> getFormKeyMap(String formGroupId) {
        return impExpSchemeItemService.selectBySchemaId(formGroupId)
                .stream()
                .map(this::newMap)
                .collect(Collectors.toList());
    }

    /**
     * 根据配置信息创建分组对象
     *
     * @param scheme
     * @return
     */

    private FormKeyMapGroup newGroup(ImpExpScheme scheme) {
        FormKeyMapGroup group = new FormKeyMapGroup();
        group.setGroupId(scheme.getId());
        group.setGroupName(scheme.getName());
        group.setGroupDescription(scheme.getDescription());
        return group;
    }


    private FormKeyMap newMap(ImpExpSchemeItem schemeItem) {
        FormKeyMap formKeyMap = new FormKeyMap();
        formKeyMap.setTargetCode(schemeItem.getHashKey());
        formKeyMap.setFieldName(schemeItem.getName());
        formKeyMap.setFieldCode(schemeItem.getCode());
        return formKeyMap;
    }

    @Override
    public long insert(ImpExpScheme impExpScheme) {
        //添加组织机构
        Organise organise = SecurityHolder.get().currentOrganise();
        impExpScheme.setOrganiseId(organise.getId());
        impExpScheme.setOrganiseName(organise.getOrganiseName());
        return commonService.insert(impExpScheme);
    }

}
