package com.dr.archive.impexp.service;

import com.dr.archive.impexp.entity.ImpExpScheme;
import com.dr.framework.common.service.BaseService;

/**
 * @author caor
 * @date 2020/7/31 19:00
 */
public interface ImpExpSchemeService extends BaseService<ImpExpScheme> {
    long insert(ImpExpScheme impExpScheme);
}
