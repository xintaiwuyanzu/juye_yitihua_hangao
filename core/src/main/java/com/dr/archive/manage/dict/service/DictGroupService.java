package com.dr.archive.manage.dict.service;

import com.dr.archive.manage.dict.entity.ArchiveDictGroup;
import com.dr.framework.common.service.BaseService;

/**
 * 描述：
 *
 * @author tuzl
 * @date 2020/6/2 17:37
 */
public interface DictGroupService extends BaseService<ArchiveDictGroup> {
    long deleteByIds(String ids);
}
