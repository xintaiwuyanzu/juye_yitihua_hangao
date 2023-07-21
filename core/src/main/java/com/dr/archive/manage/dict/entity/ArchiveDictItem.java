package com.dr.archive.manage.dict.entity;

import com.dr.archive.model.entity.BaseBusinessIdEntity;
import com.dr.archive.util.Constants;
import com.dr.framework.core.orm.annotations.Table;

/**
 * 字典项
 *
 * @author caor
 * @date 2020/6/2 15:00
 */
@Table(name = Constants.TABLE_PREFIX + "ARCHIVE_DICT_ITEM", module = Constants.MODULE_NAME, comment = "档案字典项")
public class ArchiveDictItem extends BaseBusinessIdEntity {

}
