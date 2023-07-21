package com.dr.archive.service.internal;

import com.dr.archive.model.to.MetadataFormTo;

/**
 * 元数据具体业务逻辑控制类
 *
 * @author dr
 */
public interface MetaDataService {
    /**
     * 根据元数据模板创建表单
     *
     * @param templateId
     * @param createTable
     * @return
     */
    MetadataFormTo createForm(String templateId, boolean createTable);

    //保存表单数据

}
