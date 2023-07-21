package com.dr.archive.impexp.service;


import com.dr.archive.impexp.entity.ImpExpSchemeItem;
import com.dr.archive.impexp.entity.ImpExpSchemeItemInfo;
import com.dr.framework.common.service.BaseService;
import com.dr.framework.core.orm.sql.support.SqlQuery;

import java.util.List;

/**
 * @author caor
 * @date 2020/7/31 19:42
 */
public interface ImpExpSchemeItemService extends BaseService<ImpExpSchemeItem> {
    /**
     * 根据主键删除
     *
     * @param ids
     * @return
     */
    long delete(String ids);

    /**
     * 根据方案Id查询数据
     *
     * @param impSchemaId
     * @return
     */
    default List<ImpExpSchemeItem> selectBySchemaId(String impSchemaId) {
        return selectList(
                SqlQuery.from(ImpExpSchemeItem.class)
                        .equal(ImpExpSchemeItemInfo.BUSINESSID, impSchemaId)
                        .orderBy(ImpExpSchemeItemInfo.ORDERBY)
        );
    }

}
