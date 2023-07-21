package com.dr.archive.manage.dict.service;

import com.dr.archive.manage.dict.entity.ArchiveDictItem;
import com.dr.archive.model.query.DictItemQuery;
import com.dr.archive.model.query.DictQuery;
import com.dr.framework.common.service.BaseService;

import java.util.List;

/**
 * 描述：
 *
 * @author tuzl
 * @date 2020/6/2 17:37
 */
public interface ArchiveDictItemService extends BaseService<ArchiveDictItem> {
    /**
     * 查询一组带有排序的字典
     *
     * @param dictQuery
     * @return
     */
    List<ArchiveDictItem> findDict(DictQuery dictQuery);

    /**
     * 根据类型，年度，编码查询一个字典
     *
     * @param dictItemQuery
     * @return
     */
    ArchiveDictItem findDictItem(DictItemQuery dictItemQuery);

}
