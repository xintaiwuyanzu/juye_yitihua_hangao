package com.dr.archive.impexp.service.impl;

import com.dr.archive.impexp.entity.ImpExpSchemeItem;
import com.dr.archive.impexp.entity.ImpExpSchemeItemInfo;
import com.dr.archive.impexp.service.ImpExpSchemeItemService;
import com.dr.archive.service.impl.BaseCodeNameServiceImpl;
import com.dr.framework.common.service.CommonService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * @author caor
 * @date 2020/7/31 19:42
 */
@Service
public class ImpExpSchemeItemServiceImpl extends BaseCodeNameServiceImpl<ImpExpSchemeItem> implements ImpExpSchemeItemService {

    @Autowired
    CommonService commonService;

    @Override
    public long delete(String ids) {
        /*
         * 防止ids为空，删除掉所有的数据。
         * */
        Assert.isTrue(StringUtils.hasText(ids),"数据有误,删除失败");
        SqlQuery<ImpExpSchemeItem> sqlQuery = SqlQuery.from(ImpExpSchemeItem.class);

        if (!StringUtils.isEmpty(ids)) {
            sqlQuery = sqlQuery.in(ImpExpSchemeItemInfo.ID, ids.split(","));
        }
        return delete(sqlQuery);
    }
}
