package com.dr.archive.specialTag.service;

import com.dr.archive.specialTag.entity.SpecialTag;
import com.dr.archive.specialTag.entity.SpecialTagArchive;
import com.dr.framework.common.service.BaseService;
import com.dr.framework.core.orm.sql.support.SqlQuery;

/**
 * @author: qiuyf
 * @date: 2022/6/18 14:08
 */
public interface SpecialTagArchiveService extends BaseService<SpecialTagArchive> {
    long addSpecialTag(String tagId, String formDataId, String formDefinitionId);

    SqlQuery selectTagCount(SpecialTag specialTag);
}
