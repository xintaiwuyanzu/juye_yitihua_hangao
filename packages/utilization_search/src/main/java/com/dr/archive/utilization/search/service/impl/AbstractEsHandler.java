package com.dr.archive.utilization.search.service.impl;

import com.dr.archive.utilization.search.service.ArchiveIndexNameMaker;
import com.dr.framework.common.form.core.service.FormDefinitionService;
import com.dr.framework.common.form.engine.model.core.FormModel;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * es 客户端抽象类
 *
 * @author dr
 */
public class AbstractEsHandler {
    @Autowired
    protected RestHighLevelClient client;
    @Autowired
    protected ArchiveIndexNameMaker nameMaker;
    @Autowired
    protected FormDefinitionService formDefinitionService;

    protected String indexName(FormModel formModel) {
        return nameMaker.makeIndexName(formModel);
    }

    protected String indexName(String formDefinitionId) {
        FormModel formModel = formDefinitionService.selectFormDefinitionById(formDefinitionId);
        return indexName(formModel);
    }

    /**
     * 获取网络请求配置
     *
     * @return
     */
    protected RequestOptions getRequestOptions() {
        return RequestOptions.DEFAULT;
    }

}
