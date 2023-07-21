package com.dr.archive.manage.dict.service.impl;

import com.dr.archive.manage.dict.entity.ArchiveDictGroup;
import com.dr.archive.manage.dict.entity.ArchiveDictGroupInfo;
import com.dr.archive.manage.dict.entity.ArchiveDictItem;
import com.dr.archive.manage.dict.entity.ArchiveDictItemInfo;
import com.dr.archive.manage.dict.service.ArchiveDictItemService;
import com.dr.archive.model.query.DictItemQuery;
import com.dr.archive.model.query.DictQuery;
import com.dr.archive.service.impl.BaseCodeNameServiceImpl;
import com.dr.archive.util.Constants;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 描述：
 * TODO  好多逻辑没用的
 *
 * @author tuzl
 * @date 2020/6/2 17:39
 */
@Service
public class ArchiveDictItemServiceImpl extends BaseCodeNameServiceImpl<ArchiveDictItem> implements ArchiveDictItemService {

    protected ArchiveDictGroup findGroupByQuery(DictQuery query) {
        ArchiveDictGroup group = null;
        if (query.getYear() != null) {
            // 两头的数据判断
            group = commonMapper.selectOneByQuery(
                    SqlQuery.from(ArchiveDictGroup.class)
                            .equal(ArchiveDictGroupInfo.CODE, query.getType())
                            .lessThanEqual(ArchiveDictGroupInfo.STARTYEAR, query.getYear())
                            .greaterThanEqual(ArchiveDictGroupInfo.ENDYEAR, query.getYear())
            );
        }
        if (group == null) {
            group = commonMapper.selectOneByQuery(
                    SqlQuery.from(ArchiveDictGroup.class)
                            .equal(ArchiveDictGroupInfo.CODE, query.getType())
                            .equal(ArchiveDictGroupInfo.ISDEFAULT, Constants.YES)
            );
        }
        return group;
    }

    @Override
    public List<ArchiveDictItem> findDict(DictQuery dictQuery) {
        ArchiveDictGroup group = findGroupByQuery(dictQuery);
        return group == null ?
                Collections.emptyList() :
                commonMapper.selectByQuery(
                        SqlQuery.from(ArchiveDictItem.class)
                                .equal(ArchiveDictItemInfo.BUSINESSID, group.getId())
                );
    }


    @Override
    public ArchiveDictItem findDictItem(DictItemQuery dictItemQuery) {
        ArchiveDictGroup group = findGroupByQuery(dictItemQuery);
        ArchiveDictItem archiveDictItem = null;
        if (group != null) {
            archiveDictItem = commonMapper.selectOneByQuery(
                    SqlQuery.from(ArchiveDictItem.class)
                            .equal(ArchiveDictItemInfo.BUSINESSID, group.getId())
                            .equal(ArchiveDictItemInfo.CODE, dictItemQuery.getCode())
            );
        }
        return archiveDictItem;
    }
}
