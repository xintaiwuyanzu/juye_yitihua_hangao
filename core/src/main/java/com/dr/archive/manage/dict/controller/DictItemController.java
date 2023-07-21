package com.dr.archive.manage.dict.controller;

import com.dr.archive.manage.dict.entity.ArchiveDictItem;
import com.dr.archive.manage.dict.entity.ArchiveDictItemInfo;
import com.dr.archive.manage.dict.service.ArchiveDictItemService;
import com.dr.framework.common.controller.BaseServiceController;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 描述：
 *
 * @author tuzl
 * @date 2020/6/2 17:41
 */
@RestController
@RequestMapping("api/manage/dictitem")
public class DictItemController extends BaseServiceController<ArchiveDictItemService, ArchiveDictItem> {
    @Override
    protected SqlQuery<ArchiveDictItem> buildPageQuery(HttpServletRequest httpServletRequest, ArchiveDictItem entity) {
        SqlQuery<ArchiveDictItem> sqlQuery = SqlQuery.from(ArchiveDictItem.class);
        if (!StringUtils.isEmpty(entity.getName())) {
            sqlQuery.like(ArchiveDictItemInfo.NAME, entity.getName());
        }
        sqlQuery.equal(ArchiveDictItemInfo.BUSINESSID, entity.getBusinessId());
        return sqlQuery;
    }

}
