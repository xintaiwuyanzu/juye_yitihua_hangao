package com.dr.archive.manage.dict.service.impl;

import com.dr.archive.manage.dict.entity.ArchiveDictGroup;
import com.dr.archive.manage.dict.entity.ArchiveDictItem;
import com.dr.archive.manage.dict.entity.ArchiveDictItemInfo;
import com.dr.archive.manage.dict.service.DictGroupService;
import com.dr.archive.service.impl.BaseYearServiceImpl;
import com.dr.framework.core.orm.sql.Column;
import org.springframework.stereotype.Service;

/**
 * 描述：字典组service
 *
 * @author dr
 * @author tuzl
 * @date 2020/6/2 17:39
 */
@Service
public class DictGroupServiceImpl extends BaseYearServiceImpl<ArchiveDictGroup> implements DictGroupService {

    @Override
    protected Class getSubTableClass() {
        return ArchiveDictItem.class;
    }

    @Override
    protected Column getRelateColumn() {
        return ArchiveDictItemInfo.BUSINESSID;
    }
}
