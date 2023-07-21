package com.dr.archive.utilization.search.service;

import com.dr.archive.utilization.search.entity.EsTemplate;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.common.service.BaseService;

/**
 * es索引相关服务
 *
 * @author qyf
 */
public interface EsTemplateService extends BaseService<EsTemplate> {
    //索引模板
    String TEMPLATE_TYPE_INDEX = "1";
    //搜索模板
    String TEMPLATE_TYPE_SEARCH = "2";

    /**
     * 根据模板Id重建模板
     *
     * @param id
     * @return
     */
    ResultEntity rebuildTemplate(String id);

    /**
     * 删除指定的模板
     *
     * @param id
     * @return
     */
    boolean deleteTemplate(String id);

    /**
     * 已存在索引则,进行复制,无索引,重新建 暴力方法，删除索引重建，重新同步数据
     *
     * @param formDefinitionId
     * @return
     */
    void rebuildIndex(String formDefinitionId);

    /**
     * 网上推荐方法1，重新建一个索引，将旧索引上的数据复制到新索引上
     *
     * @param formDefinitionId
     * @return
     */
    void rebuildIndex2(String formDefinitionId);
}
